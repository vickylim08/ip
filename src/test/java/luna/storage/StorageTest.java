package luna.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import luna.LunaException;
import luna.task.Deadline;
import luna.task.Event;
import luna.task.Task;
import luna.task.TaskList;
import luna.task.Todo;

/**
 * Tests for task persistence in {@link Storage}.
 */
public class StorageTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void saveAndLoad_tasksContainSeparator_preservesTaskData()
            throws IOException, LunaException {
        Storage storage = new Storage(temporaryDirectory.resolve("luna.txt"));
        Deadline deadline = new Deadline("submit | report", LocalDate.of(2026, 9, 10));
        Event event = new Event("team | meeting", LocalDateTime.of(2026, 9, 10, 9, 0),
                LocalDateTime.of(2026, 9, 10, 10, 0));
        TaskList tasks = new TaskList(new Todo("buy | milk"), deadline, event);
        tasks.mark(1);

        storage.saveTasks(tasks);

        List<Task> loadedTasks = storage.loadTasks();

        assertEquals(List.of("buy | milk", "submit | report", "team | meeting"),
                loadedTasks.stream().map(Task::getDescription).toList());
        assertTrue(loadedTasks.get(1).isDone());
        assertEquals(deadline.getByDate(), ((Deadline) loadedTasks.get(1)).getByDate());
        assertEquals(event.getFromDateTime(), ((Event) loadedTasks.get(2)).getFromDateTime());
        assertEquals(event.getToDateTime(), ((Event) loadedTasks.get(2)).getToDateTime());
    }

    @Test
    public void loadTasks_invalidDeadlineDate_throwsLunaException() throws IOException {
        Path dataFile = temporaryDirectory.resolve("luna.txt");
        Files.writeString(dataFile, "D | 0 | submit report | invalid-date");
        Storage storage = new Storage(dataFile);

        assertThrows(LunaException.class, storage::loadTasks);
    }
}
