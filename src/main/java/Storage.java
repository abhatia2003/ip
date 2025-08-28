import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

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
                    return Optional.of(new DeadlineTask(parts[2], completionStatus, new CustomDateTime(parts[3])));
                case "E":
                    return Optional.of(new EventTask(parts[2], completionStatus, new CustomDateTime(parts[3]), new CustomDateTime(parts[4])));
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