import java.util.List;

/**
 * Represents the command that displays all current tasks.
 */
public class ListCommand extends Command {
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
