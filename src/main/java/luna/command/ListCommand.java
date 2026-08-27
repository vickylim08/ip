package luna.command;

import luna.storage.Storage;
import luna.task.TaskList;
import luna.ui.Ui;

/**
 * Represents the command that displays all current tasks.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
