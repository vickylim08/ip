/**
 * Represents the command that marks a task as done.
 */
public class MarkCommand extends Command {
    private final String input;

    /**
     * Creates a mark command from the full user input.
     *
     * @param input Full command entered by the user.
     */
    public MarkCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LunaException {
        if (input.equalsIgnoreCase("mark")) {
            throw new LunaException("Please provide a task number to mark as done.");
        }

        int index = Integer.parseInt(input.substring(5).trim()) - 1;
        Task task = tasks.mark(index);
        saveTasks(storage, tasks);
        ui.showMarkSuccess(task);
    }
}
