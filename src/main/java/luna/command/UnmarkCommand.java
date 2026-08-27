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

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LunaException {
        if (input.equalsIgnoreCase("unmark")) {
            throw new LunaException("Please provide a task number to mark as done.");
        }

        int index = Integer.parseInt(input.substring(7).trim()) - 1;
        Task task = tasks.unmark(index);
        saveTasks(storage, tasks);
        ui.showUnmarkSuccess(task);
    }
}
