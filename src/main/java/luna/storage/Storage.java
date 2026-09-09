package luna.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import luna.LunaException;
import luna.task.Deadline;
import luna.task.Event;
import luna.task.Task;
import luna.task.TaskList;
import luna.task.Todo;

/**
 * Handles loading tasks from disk and saving tasks back to disk.
 */
public class Storage {
    private static final Path DEFAULT_FILE_PATH = Path.of("./data/luna.txt");
    private static final String FIELD_SEPARATOR = " | ";
    private final Path filePath;

    /**
     * Creates storage backed by Luna's default data file.
     */
    public Storage() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates storage backed by the given data file.
     *
     * @param filePath File used to save and load tasks.
     */
    Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all saved tasks from the data file.
     *
     * @return List of tasks loaded from storage.
     * @throws IOException If reading the file fails.
     * @throws LunaException If the saved file format is invalid.
     */
    public List<Task> loadTasks() throws IOException, LunaException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<String> lines = Files.readAllLines(filePath);
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
    public void saveTasks(TaskList tasks) throws IOException {
        Files.createDirectories(filePath.getParent());
        List<String> lines = new ArrayList<>();

        for (Task task : tasks.asList()) {
            lines.add(task.toStorageString());
        }

        assert lines.size() == tasks.size() : "Every task must produce exactly one storage line";
        Files.write(filePath, lines);
    }

    /**
     * Parses one saved task line into an in-memory task object.
     *
     * @param line One non-blank line from the save file.
     * @return Parsed task represented by the line.
     * @throws LunaException If the saved line has an invalid format or status value.
     */
    private Task parseTask(String line) throws LunaException {
        assert line != null && !line.isBlank() : "parseTask expects a non-blank storage line";
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            throw new LunaException("Saved data file is corrupted.");
        }

        assert parts.length >= 3 : "createTask expects the validated base storage fields";
        Task task = createTask(parts);
        assert task != null : "A valid storage record must create a task";
        if ("1".equals(parts[1])) {
            task.markAsDone();
        } else if (!"0".equals(parts[1])) {
            throw new LunaException("Saved task status is invalid.");
        }

        return task;
    }

    /**
     * Creates a task instance from the split save-file fields.
     *
     * @param parts Fields extracted from one saved task line.
     * @return Task instance matching the saved task type.
     * @throws LunaException If the task type, field count, or date values are invalid.
     */
    private Task createTask(String[] parts) throws LunaException {
        assert parts != null && parts.length >= 3
                : "createTask expects a non-null record with base fields";
        String taskType = parts[0];

        try {
            switch (taskType) {
                case "T":
                    return new Todo(joinDescription(parts, 0));
                case "D":
                    return createDeadline(parts);
                case "E":
                    return createEvent(parts);
                default:
                    throw new LunaException("Saved task type is invalid.");
            }
        } catch (DateTimeParseException e) {
            throw new LunaException("Saved date or time is invalid.");
        }
    }

    /**
     * Creates a deadline from validated storage fields.
     *
     * @param parts Fields extracted from a saved deadline.
     * @return Deadline represented by the fields.
     * @throws LunaException If the field count is invalid.
     */
    private Deadline createDeadline(String[] parts) throws LunaException {
        if (parts.length < 4) {
            throw new LunaException("Saved deadline task is corrupted.");
        }

        LocalDate deadlineDate = LocalDate.parse(parts[parts.length - 1]);
        return new Deadline(joinDescription(parts, 1), deadlineDate);
    }

    /**
     * Creates an event from validated storage fields.
     *
     * @param parts Fields extracted from a saved event.
     * @return Event represented by the fields.
     * @throws LunaException If the field count is invalid.
     */
    private Event createEvent(String[] parts) throws LunaException {
        if (parts.length < 5) {
            throw new LunaException("Saved event task is corrupted.");
        }

        LocalDateTime from = LocalDateTime.parse(parts[parts.length - 2]);
        LocalDateTime to = LocalDateTime.parse(parts[parts.length - 1]);
        return new Event(joinDescription(parts, 2), from, to);
    }

    /**
     * Reconstructs a description while preserving separator text inside it.
     *
     * @param parts Fields extracted from a saved task line.
     * @param trailingFieldCount Number of fields after the description.
     * @return Reconstructed task description.
     */
    private String joinDescription(String[] parts, int trailingFieldCount) {
        int descriptionEnd = parts.length - trailingFieldCount;
        return String.join(FIELD_SEPARATOR, Arrays.copyOfRange(parts, 2, descriptionEnd));
    }
}
