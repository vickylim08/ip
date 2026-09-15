package luna.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import luna.task.Task;
import luna.task.TaskList;
import luna.task.Todo;

/**
 * Tests for task-display methods in {@link Ui}.
 */
public class UiTest {
    @Test
    public void showTaskList_tasksProvided_formatsNumberedTaskList() {
        Ui ui = new Ui(false);
        TaskList tasks = new TaskList(new Todo("read book"));

        ui.showTaskList(tasks);

        assertEquals("Here's what's on your radar:\n1. [T][ ] read book", ui.consumeLatestResponse());
    }

    @Test
    public void showMatchingTasks_tasksProvided_formatsNumberedMatchingTasks() {
        Ui ui = new Ui(false);
        List<Task> matchingTasks = List.of(new Todo("read book"));

        ui.showMatchingTasks(matchingTasks);

        assertEquals("These tasks came into view:\n1. [T][ ] read book",
                ui.consumeLatestResponse());
    }

    @Test
    public void showArchivedTasks_tasksProvided_formatsNumberedArchivedTasks() {
        Ui ui = new Ui(false);
        List<Task> archivedTasks = List.of(new Todo("read book"));

        ui.showArchivedTasks(archivedTasks);

        assertEquals("These tasks are resting in your archive:\n1. [T][ ] read book",
                ui.consumeLatestResponse());
    }

    @Test
    public void showArchivedTasks_emptyList_formatsEmptyArchiveMessage() {
        Ui ui = new Ui(false);

        ui.showArchivedTasks(List.of());

        assertEquals("Your archive is quiet - nothing is resting there yet.", ui.consumeLatestResponse());
    }

    @Test
    public void showAddSuccess_taskProvided_formatsTaskCountConfirmation() {
        Ui ui = new Ui(false);
        Task task = new Todo("read book");

        ui.showAddSuccess(task, 1);

        assertEquals("It's on your radar:\n"
                + "[T][ ] read book\n"
                + "You now have 1 task on your radar.", ui.consumeLatestResponse());
    }

    @Test
    public void showDeleteSuccess_taskProvided_formatsTaskCountConfirmation() {
        Ui ui = new Ui(false);
        Task task = new Todo("read book");

        ui.showDeleteSuccess(task, 0);

        assertEquals("Cleared from your path:\n"
                + "[T][ ] read book\n"
                + "You now have 0 tasks on your radar.", ui.consumeLatestResponse());
    }

    @Test
    public void showArchiveSuccess_oneTask_formatsArchiveConfirmation() {
        Ui ui = new Ui(false);

        ui.showArchiveSuccess(1, 1);

        assertEquals("Tucked away 1 task to data/archive.txt.\n"
                + "You have 1 task still on your radar.", ui.consumeLatestResponse());
    }

    @Test
    public void showNoTasksToArchive_formatsNoOpMessage() {
        Ui ui = new Ui(false);

        ui.showNoTasksToArchive();

        assertEquals("Your active list is already clear - there is nothing to archive.",
                ui.consumeLatestResponse());
    }

    @Test
    public void showHelp_formatsCompleteCommandReference() {
        Ui ui = new Ui(false);

        ui.showHelp();

        String response = ui.consumeLatestResponse();
        assertTrue(response.startsWith("Available commands:"));
        assertTrue(response.contains("> deadline <desc> /by <yyyy-MM-dd>:"));
        assertTrue(response.contains("> help: Displays this command guide."));
    }
}
