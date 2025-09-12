package morpheus.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import morpheus.tasks.DeadlineTask;
import morpheus.tasks.EventTask;
import morpheus.tasks.Task;
import morpheus.tasks.ToDoTask;


/**
 * Handles reading and writing of task data to a persistent file.
 */
public class Storage {

    private static final String TODO_CODE = "T";
    private static final String DEADLINE_CODE = "D";
    private static final String EVENT_CODE = "E";
    private static final String DONE_CODE = "1";

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

    private final Path file;

    /**
     * Creates a new {@code Storage} object that manages persistence of tasks
     * in the specified file path.
     * <p>
     * The constructor ensures that the underlying file and its parent directories
     * exist by calling {@link #checkFile(Path)}. If initialization fails due to
     * I/O issues, a warning message is printed to the error stream, but the
     * {@code Storage} object is still created with the provided path.
     * </p>
     *
     * @param filePath the path to the save file where tasks will be stored
     */
    public Storage(String filePath) {
        Path p = toPath(filePath);
        try {
            checkFile(p);
        } catch (IOException e) {
            System.err.println("[WARN] Could not initialize file: " + e.getMessage());
        }
        this.file = p;
    }

    private static Path toPath(String filePath) {
        String[] parts = filePath.split("/");
        return Paths.get(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
    }

    /**
     * Loads tasks from the save file into memory.
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
     * Ensures that the given file and its parent directories exist
     * so that tasks can always be persisted between sessions.
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
     */
    private static Optional<Task> decodeTask(String line) {
        try {
            String[] parts = Arrays.stream(line.split("\\|"))
                    .map(String::trim)
                    .toArray(String[]::new);

            // check if last part is a reminder
            String reminderRaw = null;
            if (parts[parts.length - 1].startsWith("REMINDER:")) {
                reminderRaw = parts[parts.length - 1].substring("REMINDER:".length()).trim();
            }

            String type = parts[0];
            boolean isDone = DONE_CODE.equals(parts[1]);

            Task task;
            switch (type) {
            case TODO_CODE:
                task = decodeToDo(parts, isDone);
                break;
            case DEADLINE_CODE:
                task = decodeDeadline(parts, isDone);
                break;
            case EVENT_CODE:
                task = decodeEvent(parts, isDone);
                break;
            default:
                System.err.println("[WARN] Unknown type: " + type + " in line: " + line);
                return Optional.empty();
            }

            // attach reminder if found
            if (reminderRaw != null && !reminderRaw.isEmpty()) {
                task.setReminder(new CustomDateTime(reminderRaw));
            }

            return Optional.of(task);

        } catch (Exception e) {
            System.err.println("[WARN] Corrupted line: " + line);
            return Optional.empty();
        }
    }

    private static Task decodeToDo(String[] parts, boolean isDone) {
        return new ToDoTask(parts[2], isDone);
    }

    private static Task decodeDeadline(String[] parts, boolean isDone) {
        String end = decodeTime(parts[3]);
        return new DeadlineTask(parts[2], isDone, new CustomDateTime(end));
    }

    private static Task decodeEvent(String[] parts, boolean isDone) {
        String start = decodeTime(parts[3]);
        String end = decodeTime(parts[4]);
        return new EventTask(parts[2], isDone, new CustomDateTime(start), new CustomDateTime(end));
    }

    /**
     * Converts a formatted date-time string from storage into a normalized format.
     */
    public static String decodeTime(String input) {
        try {
            // Try "12 Sep 2025, 3:00 PM"
            LocalDateTime dateTime = LocalDateTime.parse(input, INPUT_FORMATTER);
            return dateTime.format(OUTPUT_FORMATTER);
        } catch (Exception e) {
            try {
                // Already normalized (e.g. 12/9/2025 1500)
                CustomDateTime cdt = new CustomDateTime(input);
                return cdt.toLocalDateTime().format(OUTPUT_FORMATTER);
            } catch (Exception inner) {
                throw new IllegalArgumentException("Unsupported date format: " + input);
            }
        }
    }
}
