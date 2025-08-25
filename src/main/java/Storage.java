// Storage.java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Storage {
    private final Path file;

    public Storage() {
        this(Paths.get("data", "morpheus.txt"));
    }

    public Storage(Path file) {
        this.file = file;
    }

    public List<Task> load() {
        List<Task> taskList = new ArrayList<>();
        try {
            checkFile();
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
            checkFile();
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

    private void checkFile() throws IOException {
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
                    return Optional.of(new ToDo(parts[2], completionStatus));
                case "D":
                    return Optional.of(new Deadline(parts[2], completionStatus, parts[3]));
                case "E":
                    return Optional.of(new Event(parts[2], completionStatus, parts[3], parts[4]));
                default:
                    System.err.println("[SKIP] Unknown type: " + type + " in line: " + line);
                    return Optional.empty();
            }
        } catch (Exception e) {
            System.err.println("[SKIP] Corrupted line: " + line);
            return Optional.empty();
        }
    }
}