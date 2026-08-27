/**
 * Represents the command that adds a todo task.
 */
public class TodoCommand extends Command {
    private final String input;

    /**
     * Creates a todo command from the full user input.
     *
     * @param input Full command entered by the user.
     */
    public TodoCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LunaException {
        if (input.equalsIgnoreCase("todo")) {
            throw new LunaException("Please provide the description for the todo task.");
        }

        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new LunaException("The description of a todo cannot be empty.");
        }

        Task task = new Todo(description);
        tasks.add(task);
        saveTasks(storage, tasks);
        ui.showAddSuccess(task, tasks.size());
    }
}
