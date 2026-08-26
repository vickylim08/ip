import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading tasks from disk and saving tasks back to disk.
 */
public class Storage {
    private static final Path FILE_PATH = Path.of("./data/luna.txt");

    /**
     * Loads all saved tasks from the data file.
     *
     * @return List of tasks loaded from storage.
     * @throws IOException If reading the file fails.
     * @throws LunaException If the saved file format is invalid.
     */
    public List<Task> loadTasks() throws IOException, LunaException {
        if (!Files.exists(FILE_PATH)) {
            return new ArrayList<>();
        }

        List<String> lines = Files.readAllLines(FILE_PATH);
        List<Task> tasks = new ArrayList<>();

        for (String line : lines) {
            if (!line.isBlank()) {
                tasks.add(parseTask(line));
            }
        }

        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @throws IOException If writing the file fails.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        Files.createDirectories(FILE_PATH.getParent());
        List<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            lines.add(task.toStorageString());
        }

        Files.write(FILE_PATH, lines);
    }

    private Task parseTask(String line) throws LunaException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            throw new LunaException("Saved data file is corrupted.");
        }

        Task task = createTask(parts);
        if ("1".equals(parts[1])) {
            task.markAsDone();
        } else if (!"0".equals(parts[1])) {
            throw new LunaException("Saved task status is invalid.");
        }

        return task;
    }

    private Task createTask(String[] parts) throws LunaException {
        String taskType = parts[0];
        String description = parts[2];

        try {
            switch (taskType) {
                case "T":
                    return new Todo(description);
                case "D":
                    if (parts.length != 4) {
                        throw new LunaException("Saved deadline task is corrupted.");
                    }

                    LocalDate deadlineDate = LocalDate.parse(parts[3]);
                    return new Deadline(description, deadlineDate);
                case "E":
                    if (parts.length != 5) {
                        throw new LunaException("Saved event task is corrupted.");
                    }

                    LocalDateTime from = LocalDateTime.parse(parts[3]);
                    LocalDateTime to = LocalDateTime.parse(parts[4]);
                    return new Event(description, from, to);
                default:
                    throw new LunaException("Saved task type is invalid.");
            }
        } catch (DateTimeParseException e) {
            throw new LunaException("Saved date or time is invalid.");
        }
    }
}
