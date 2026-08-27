package luna.command;

import luna.LunaException;
import luna.storage.Storage;
import luna.task.Task;
import luna.task.TaskList;
import luna.ui.Ui;

/**
 * Represents the command that marks a task as not done.
 */
public class UnmarkCommand extends Command {
    private final String input;

    /**
     * Creates an unmark command from the full user input.
     *
     * @param input Full command entered by the user.
     */
    public UnmarkCommand(String input) {
        this.input = input;
    }

    /**
     * Marks the task at the requested index as not done, saves the list, and shows the result.
     *
     * @param tasks Current tasks in memory.
     * @param ui User interface for showing results.
     * @param storage Storage used to persist task changes.
     * @throws LunaException If the input is incomplete or saving fails.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LunaException {
        if (input.equalsIgnoreCase("unmark")) {
            throw new LunaException("Please provide a task number to mark as not done.");
        }

        int index = Integer.parseInt(input.substring(7).trim()) - 1;
        Task task = tasks.unmark(index);
        saveTasks(storage, tasks);
        ui.showUnmarkSuccess(task);
    }
}
