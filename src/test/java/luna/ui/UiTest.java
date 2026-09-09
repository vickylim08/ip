package luna.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals("Here are the tasks in your list:\n1. [T][ ] read book", ui.consumeLatestResponse());
    }

    @Test
    public void showMatchingTasks_tasksProvided_formatsNumberedMatchingTasks() {
        Ui ui = new Ui(false);
        List<Task> matchingTasks = List.of(new Todo("read book"));

        ui.showMatchingTasks(matchingTasks);

        assertEquals("Here are the matching tasks in your list:\n1. [T][ ] read book",
                ui.consumeLatestResponse());
    }

    @Test
    public void showArchivedTasks_tasksProvided_formatsNumberedArchivedTasks() {
        Ui ui = new Ui(false);
        List<Task> archivedTasks = List.of(new Todo("read book"));

        ui.showArchivedTasks(archivedTasks);

        assertEquals("Here are your archived tasks:\n1. [T][ ] read book", ui.consumeLatestResponse());
    }

    @Test
    public void showArchivedTasks_emptyList_formatsEmptyArchiveMessage() {
        Ui ui = new Ui(false);

        ui.showArchivedTasks(List.of());

        assertEquals("There are no archived tasks.", ui.consumeLatestResponse());
    }

    @Test
    public void showAddSuccess_taskProvided_formatsTaskCountConfirmation() {
        Ui ui = new Ui(false);
        Task task = new Todo("read book");

        ui.showAddSuccess(task, 1);

        assertEquals("Got it. I've added this task:\n"
                + "[T][ ] read book\n"
                + "Now you have 1 tasks in the list.", ui.consumeLatestResponse());
    }

    @Test
    public void showDeleteSuccess_taskProvided_formatsTaskCountConfirmation() {
        Ui ui = new Ui(false);
        Task task = new Todo("read book");

        ui.showDeleteSuccess(task, 0);

        assertEquals("Noted. I've removed this task:\n"
                + "[T][ ] read book\n"
                + "Now you have 0 tasks in the list.", ui.consumeLatestResponse());
    }

    @Test
    public void showArchiveSuccess_oneTask_formatsArchiveConfirmation() {
        Ui ui = new Ui(false);

        ui.showArchiveSuccess(1, 1);

        assertEquals("Archived 1 task to data/archive.txt.\n"
                + "Now you have 1 task in the list.", ui.consumeLatestResponse());
    }

    @Test
    public void showNoTasksToArchive_formatsNoOpMessage() {
        Ui ui = new Ui(false);

        ui.showNoTasksToArchive();

        assertEquals("There are no tasks to archive.", ui.consumeLatestResponse());
    }
}
