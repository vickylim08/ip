package luna.command;

import luna.LunaException;
import luna.storage.Storage;
import luna.task.Task;
import luna.task.TaskList;
import luna.ui.Ui;

/**
 * Represents the command that deletes a task.
 */
public class DeleteCommand extends Command {
    private final String input;

    /**
     * Creates a delete command from the full user input.
     *
     * @param input Full command entered by the user.
     */
    public DeleteCommand(String input) {
        this.input = input;
    }

    /**
     * Deletes the task at the requested index, saves the list, and shows the result.
     *
     * @param tasks Current tasks in memory.
     * @param ui User interface for showing results.
     * @param storage Storage used to persist task changes.
     * @throws LunaException If the input is incomplete or saving fails.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LunaException {
        if (input.equalsIgnoreCase("delete")) {
            throw new LunaException("Please provide a task number to delete.");
        }

        int index = Integer.parseInt(input.substring(7).trim()) - 1;
        Task task = tasks.remove(index);
        saveTasks(storage, tasks);
        ui.showDeleteSuccess(task, tasks.size());
    }
}
