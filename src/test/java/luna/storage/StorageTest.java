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

    @Test
    public void loadTasks_eventEndsBeforeStart_throwsLunaException() throws IOException {
        Path dataFile = temporaryDirectory.resolve("luna.txt");
        Files.writeString(dataFile,
                "E | 0 | meeting | 2026-09-10T10:00 | 2026-09-10T09:00");
        Storage storage = new Storage(dataFile);

        assertThrows(LunaException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_blankDescription_throwsLunaException() throws IOException {
        Path dataFile = temporaryDirectory.resolve("luna.txt");
        Files.writeString(dataFile, "T | 0 | ");
        Storage storage = new Storage(dataFile);

        assertThrows(LunaException.class, storage::loadTasks);
    }

    @Test
    public void loadTasks_duplicateActiveTask_throwsLunaException() throws IOException {
        Path dataFile = temporaryDirectory.resolve("luna.txt");
        Files.write(dataFile, List.of("T | 0 | read book", "T | 1 | READ BOOK"));
        Storage storage = new Storage(dataFile);

        LunaException exception = assertThrows(LunaException.class, storage::loadTasks);

        assertTrue(exception.getMessage().contains("duplicate task at line 2"));
    }

    @Test
    public void archiveTasks_existingArchiveIsCorrupted_doesNotModifyFile() throws IOException {
        Path archiveFile = temporaryDirectory.resolve("archive.txt");
        Files.writeString(archiveFile, "corrupted record");
        Storage storage = new Storage(temporaryDirectory.resolve("luna.txt"), archiveFile);

        assertThrows(LunaException.class, () -> storage.archiveTasks(List.of(new Todo("read book"))));
        assertEquals("corrupted record", Files.readString(archiveFile));
    }

    @Test
    public void archiveAndLoad_multipleBatches_appendsTasksInOrder()
            throws IOException, LunaException {
        Path activeFile = temporaryDirectory.resolve("luna.txt");
        Path archiveFile = temporaryDirectory.resolve("archive.txt");
        Storage storage = new Storage(activeFile, archiveFile);
        Todo firstTask = new Todo("read | book");
        Deadline secondTask = new Deadline("submit report", LocalDate.of(2026, 9, 12));
        Event thirdTask = new Event(
                "team meeting",
                LocalDateTime.of(2026, 9, 12, 9, 0),
                LocalDateTime.of(2026, 9, 12, 10, 0));
        secondTask.markAsDone();

        storage.archiveTasks(List.of(firstTask));
        storage.archiveTasks(List.of(secondTask, thirdTask, firstTask));

        List<Task> archivedTasks = storage.loadArchivedTasks();
        Event archivedEvent = (Event) archivedTasks.get(2);
        assertEquals(List.of("read | book", "submit report", "team meeting", "read | book"),
                archivedTasks.stream().map(Task::getDescription).toList());
        assertTrue(archivedTasks.get(1).isDone());
        assertEquals(LocalDate.of(2026, 9, 12), ((Deadline) archivedTasks.get(1)).getByDate());
        assertEquals(LocalDateTime.of(2026, 9, 12, 9, 0), archivedEvent.getFromDateTime());
        assertEquals(List.of(
                "T | 0 | read | book",
                "D | 1 | submit report | 2026-09-12",
                "E | 0 | team meeting | 2026-09-12T09:00 | 2026-09-12T10:00",
                "T | 0 | read | book"), Files.readAllLines(archiveFile));
    }

    @Test
    public void loadArchivedTasks_missingArchive_returnsEmptyList()
            throws IOException, LunaException {
        Storage storage = new Storage(
                temporaryDirectory.resolve("luna.txt"),
                temporaryDirectory.resolve("archive.txt"));

        List<Task> archivedTasks = storage.loadArchivedTasks();

        assertTrue(archivedTasks.isEmpty());
    }

    @Test
    public void loadArchivedTasks_corruptedArchive_throwsLunaException() throws IOException {
        Path archiveFile = temporaryDirectory.resolve("archive.txt");
        Files.writeString(archiveFile, "invalid archive record");
        Storage storage = new Storage(temporaryDirectory.resolve("luna.txt"), archiveFile);

        assertThrows(LunaException.class, storage::loadArchivedTasks);
    }
}
