package morpheus.utils;

import morpheus.tasks.DeadlineTask;
import morpheus.tasks.EventTask;
import morpheus.tasks.ToDoTask;
import morpheus.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Handles reading and writing of task data to a persistent file.
 * <p>
 * The {@code Storage} class ensures that tasks are saved and restored between
 * sessions of the Morpheus task manager. It manages the creation of the save
 * file and its parent directories if they do not exist.
 * </p>
 *
 * Tasks are serialized using the {@link Task#encode()} format and deserialized
 * via the internal {@link #decodeTask(String)} method.
 *
 * Example file line formats:
 * <ul>
 *   <li>ToDo: <code>T | 0 | Read book</code></li>
 *   <li>Deadline: <code>D | 1 | Submit report | 28/8/2025 1800</code></li>
 *   <li>Event: <code>E | 0 | Project meeting | 24/4/2025 1300 | 24/4/2025 1500</code></li>
 * </ul>
 *
 * @author Aayush
 */
public class Storage {
    private final Path file;

    /**
     * Constructs a new {@code Storage} instance with the given file path.
     * Ensures the file and its parent directories exist.
     *
     * @param filePath the path to the save file
     */
    public Storage(String filePath) {
        Path p = Storage.toPath(filePath);
        try {
            checkFile(p);
        } catch (IOException e) {
            System.err.println("[WARN] Could not initialize file: " + e.getMessage());
        }
        this.file = p;
    }

    /**
     * Converts a string file path into a {@link Path} object.
     *
     * @param filePath the string path
     * @return the corresponding Path object
     */
    private static Path toPath(String filePath) {
        String[] parts = filePath.split("/");
        return Paths.get(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
    }

    /**
     * Loads tasks from the save file into memory.
     *
     * @return a list of tasks decoded from the save file; returns an empty list if
     *         the file is unreadable or corrupted
     */
    public List<Task> load() {
        List<Task> taskList = new ArrayList<>();
        try {
            checkFile(file);
            try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                br.lines()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Storage::decodeTask)
                        .flatMap(Optional::stream)
                        .forEach(taskList::add);
            }
        } catch (IOException e) {
            System.err.println("[WARN] Could not read save file: " + e.getMessage());
        }
        return taskList;
    }

    /**
     * Saves the given list of tasks to the save file.
     *
     * @param tasks the tasks to persist
     */
    public void save(List<Task> tasks) {
        try {
            checkFile(file);
            try (BufferedWriter bw = Files.newBufferedWriter(
                    file, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {

                for (Task t : tasks) {
                    bw.write(t.encode());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("[WARN] Could not write save file: " + e.getMessage());
        }
    }

    /**
     * Ensures that the given file and its parent directories exist.
     *
     * @param file the path to check
     * @throws IOException if the file or directories cannot be created
     */
    private void checkFile(Path file) throws IOException {
        if (Files.notExists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }
        if (Files.notExists(file)) {
            Files.createFile(file);
        }
    }

    /**
     * Decodes a line of text from the save file into a {@link Task}.
     *
     * @param line the encoded line
     * @return an {@link Optional} containing the decoded task, or empty if decoding fails
     */
    private static Optional<Task> decodeTask(String line) {
        try {
            String[] parts = Arrays.stream(line.split("\\|"))
                    .map(String::trim).toArray(String[]::new);
            String type = parts[0];
            boolean completionStatus = "1".equals(parts[1]);

            switch (type) {
                case "T":
                    return Optional.of(new ToDoTask(parts[2], completionStatus));
                case "D":
                    String deadlineEndTime = decodeTime(parts[3]);
                    return Optional.of(new DeadlineTask(parts[2], completionStatus, new CustomDateTime(deadlineEndTime)));
                case "E":
                    String eventStartTime = decodeTime(parts[3]);
                    String eventEndTime = decodeTime(parts[4]);
                    return Optional.of(new EventTask(parts[2], completionStatus, new CustomDateTime(eventStartTime), new CustomDateTime(eventEndTime)));
                default:
                    System.err.println("[SKIP] Unknown type: " + type + " in line: " + line);
                    return Optional.empty();
            }
        } catch (Exception e) {
            System.err.println("[SKIP] Corrupted line: " + line);
            return Optional.empty();
        }
    }

    /**
     * Converts a formatted date-time string from storage into a normalized format.
     * <p>
     * Input format: {@code d MMM yyyy, h:mm a} (e.g., {@code 28 Aug 2025, 6:00 PM})
     * </p>
     * Output format: {@code d/M/yyyy HHmm} (e.g., {@code 28/8/2025 1800})
     *
     * @param input the date-time string
     * @return the reformatted string suitable for {@link CustomDateTime}
     */
    public static String decodeTime(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        return dateTime.format(outputFormatter);
    }
}