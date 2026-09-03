package luna;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import luna.storage.Storage;
import luna.task.Task;
import luna.task.TaskList;
import luna.ui.Ui;

/**
 * Tests for the GUI-facing command API in {@link Luna}.
 */
public class LunaTest {
    @Test
    public void getWelcomeMessage_newLuna_returnsGreetingAndCommandList() {
        Luna luna = new Luna(new Ui(false), new InMemoryStorage());

        String welcomeMessage = luna.getWelcomeMessage();

        assertTrue(welcomeMessage.contains("Hello! I'm Luna."));
        assertTrue(welcomeMessage.contains("Available commands:"));
        assertFalse(welcomeMessage.contains("___"));
    }

    @Test
    public void getResponse_todoCommand_returnsConfirmationAndPersistsTask() {
        InMemoryStorage storage = new InMemoryStorage();
        Luna luna = new Luna(new Ui(false), storage);

        String response = luna.getResponse("todo read book");

        assertTrue(response.contains("I've added this task"));
        assertTrue(response.contains("[T][ ] read book"));
        assertFalse(response.contains("___"));
        assertEquals(List.of("T | 0 | read book"), storage.getSavedTasks());
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Luna luna = new Luna(new Ui(false), new InMemoryStorage());

        String response = luna.getResponse("unknown");

        assertTrue(response.contains("Oh no! I don't know this command :("));
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

    /**
     * Test double that keeps task data in memory.
     */
    private static class InMemoryStorage extends Storage {
        private final List<Task> loadedTasks;
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

        /**
         * Returns the serialized tasks most recently saved.
         *
         * @return Saved task lines.
         */
        public List<String> getSavedTasks() {
            return new ArrayList<>(savedTasks);
        }
    }
}
