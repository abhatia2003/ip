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

public class Storage {
    private final Path file;

    public Storage(String filePath) {
        Path p = Storage.toPath(filePath);
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

    private void checkFile(Path file) throws IOException {
        if (Files.notExists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }
        if (Files.notExists(file)) {
            Files.createFile(file);
        }
    }

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
                return Optional.of(new DeadlineTask(parts[2],
                        completionStatus,
                        new CustomDateTime(deadlineEndTime)));
            case "E":
                String eventStartTime = decodeTime(parts[3]);
                String eventEndTime = decodeTime(parts[4]);
                return Optional.of(new EventTask(parts[2],
                        completionStatus,
                        new CustomDateTime(eventStartTime),
                        new CustomDateTime(eventEndTime)));
            default:
                System.err.println("[SKIP] Unknown type: " + type + " in line: " + line);
                return Optional.empty();
            }
        } catch (Exception e) {
            System.err.println("[SKIP] Corrupted line: " + line);
            return Optional.empty();
        }
    }

    public static String decodeTime(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter
                .ofPattern("d MMM yyyy, h:mm a", Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");

        return dateTime.format(outputFormatter);
    }
}
