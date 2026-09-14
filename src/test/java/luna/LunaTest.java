package luna;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import luna.storage.Storage;
import luna.task.Task;
import luna.task.TaskList;
import luna.task.Todo;
import luna.ui.Ui;

/**
 * Tests for the GUI-facing command API in {@link Luna}.
 */
public class LunaTest {
    @Test
    public void getWelcomeMessage_newLuna_returnsGreetingAndCommandList() {
        Luna luna = new Luna(new Ui(false), new InMemoryStorage());

        String welcomeMessage = luna.getWelcomeMessage();

        assertTrue(welcomeMessage.startsWith(" _\n"
                + "| |    _   _ _ __   __ _\n"
                + "| |   | | | | '_ \\ / _` |\n"
                + "| |___| |_| | | | | (_| |\n"
                + "|_____|\\__,_|_| |_|\\__,_|\n\n"
                + "Hi, I'm Luna."));
        assertTrue(welcomeMessage.contains("Available commands:"));
        assertTrue(welcomeMessage.contains("> event <desc> /from <yyyy-MM-dd HHmm> "
                + "/to <yyyy-MM-dd HHmm>: Adds a task that spans across a specific time"));
        assertFalse(welcomeMessage.contains("_______________________________________________________________"));
    }

    @Test
    public void getResponse_todoCommand_returnsConfirmationAndPersistsTask() {
        InMemoryStorage storage = new InMemoryStorage();
        Luna luna = new Luna(new Ui(false), storage);

        String response = luna.getResponse("todo read book");

        assertTrue(response.contains("I've added this task"));
        assertTrue(response.contains("[T][ ] read book"));
        assertFalse(response.contains("___"));
        assertFalse(luna.isLatestResponseError());
        assertEquals(List.of("T | 0 | read book"), storage.getSavedTasks());
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Luna luna = new Luna(new Ui(false), new InMemoryStorage());

        String response = luna.getResponse("unknown");

        assertTrue(response.contains("Oh no! I don't know this command :("));
        assertTrue(luna.isLatestResponseError());
    }

    @Test
    public void getResponse_byeCommand_returnsFarewellAndMarksExitRequested() {
        Luna luna = new Luna(new Ui(false), new InMemoryStorage());

        String response = luna.getResponse("bye");

        assertTrue(response.contains("Bye. Hope to see you again soon!"));
        assertTrue(luna.isExitRequested());
    }

    @Test
    public void getResponse_blankInput_returnsEmptyStringAndKeepsRunning() {
        Luna luna = new Luna(new Ui(false), new InMemoryStorage());

        String response = luna.getResponse("   ");

        assertEquals("", response);
        assertFalse(luna.isExitRequested());
    }

    @Test
    public void getResponse_archiveIndex_archivesSelectedTaskAndSavesRemainingTasks() {
        InMemoryStorage storage = new InMemoryStorage(List.of(
                new Todo("read book"),
                new Todo("submit quiz")));
        Luna luna = new Luna(new Ui(false), storage);

        String response = luna.getResponse("archive 1");

        assertEquals("Archived 1 task to data/archive.txt.\n"
                + "Now you have 1 task in the list.", response);
        assertEquals(List.of("T | 0 | read book"), storage.getArchivedTasks());
        assertEquals(List.of("T | 0 | submit quiz"), storage.getSavedTasks());
    }

    @Test
    public void getResponse_archiveAll_archivesEveryTaskAndClearsActiveList() {
        InMemoryStorage storage = new InMemoryStorage(List.of(
                new Todo("read book"),
                new Todo("submit quiz")));
        Luna luna = new Luna(new Ui(false), storage);

        String response = luna.getResponse("  ARCHIVE   ALL  ");

        assertEquals("Archived 2 tasks to data/archive.txt.\n"
                + "Now you have 0 tasks in the list.", response);
        assertEquals(List.of("T | 0 | read book", "T | 0 | submit quiz"), storage.getArchivedTasks());
        assertTrue(storage.getSavedTasks().isEmpty());
    }

    @Test
    public void getResponse_archiveAllWithEmptyList_returnsNoOpMessage() {
        InMemoryStorage storage = new InMemoryStorage();
        Luna luna = new Luna(new Ui(false), storage);

        String response = luna.getResponse("archive all");

        assertEquals("There are no tasks to archive.", response);
        assertTrue(storage.getArchivedTasks().isEmpty());
    }

    @Test
    public void getResponse_listArchived_displaysPersistedArchivedTasks() {
        InMemoryStorage storage = new InMemoryStorage();
        storage.archiveTasks(List.of(new Todo("read book")));
        Luna luna = new Luna(new Ui(false), storage);

        String response = luna.getResponse("list archived");

        assertEquals("Here are your archived tasks:\n1. [T][ ] read book", response);
    }

    @Test
    public void getResponse_archiveWithInvalidArgument_returnsUsageErrorWithoutArchiving() {
        InMemoryStorage storage = new InMemoryStorage(List.of(new Todo("read book")));
        Luna luna = new Luna(new Ui(false), storage);

        String response = luna.getResponse("archive book");

        assertEquals("Oh no! Please use archive <index> or archive all.", response);
        assertTrue(storage.getArchivedTasks().isEmpty());
    }

    @Test
    public void constructor_storageReturnsNull_assertsStorageContract() {
        assertThrows(AssertionError.class, () -> new Luna(new Ui(false), new NullStorage()));
    }

    /**
     * Test double that keeps task data in memory.
     */
    private static class InMemoryStorage extends Storage {
        private final List<Task> loadedTasks;
        private final List<Task> archivedTasks;
        private final List<String> savedTasks;

        /**
         * Creates empty in-memory storage.
         */
        InMemoryStorage() {
            this(List.of());
        }

        /**
         * Creates in-memory storage with predefined tasks.
         *
         * @param loadedTasks Tasks returned when Luna loads saved data.
         */
        InMemoryStorage(List<Task> loadedTasks) {
            this.loadedTasks = new ArrayList<>(loadedTasks);
            this.archivedTasks = new ArrayList<>();
            this.savedTasks = new ArrayList<>();
        }

        @Override
        public List<Task> loadTasks() {
            return new ArrayList<>(loadedTasks);
        }

        @Override
        public void saveTasks(TaskList tasks) {
            savedTasks.clear();
            for (Task task : tasks.asList()) {
                savedTasks.add(task.toStorageString());
            }
        }

        @Override
        public void archiveTasks(List<Task> tasks) {
            archivedTasks.addAll(tasks);
        }

        @Override
        public List<Task> loadArchivedTasks() {
            return new ArrayList<>(archivedTasks);
        }

        /**
         * Returns the serialized tasks most recently saved.
         *
         * @return Saved task lines.
         */
        public List<String> getSavedTasks() {
            return new ArrayList<>(savedTasks);
        }

        /**
         * Returns the serialized tasks currently kept in the test archive.
         *
         * @return Archived task lines.
         */
        public List<String> getArchivedTasks() {
            return archivedTasks.stream()
                    .map(Task::toStorageString)
                    .toList();
        }
    }

    /**
     * Test double that violates the storage loading contract.
     */
    private static class NullStorage extends Storage {
        @Override
        public List<Task> loadTasks() {
            return null;
        }
    }
}
