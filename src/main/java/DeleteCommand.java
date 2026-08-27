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
