import java.util.List;

/**
 * Represents the command that exits the application.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) {
        ui.showExit();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
