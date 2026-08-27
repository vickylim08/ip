import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Represents the command that adds a deadline task.
 */
public class DeadlineCommand extends Command {
    private final String input;

    /**
     * Creates a deadline command from the full user input.
     *
     * @param input Full command entered by the user.
     */
    public DeadlineCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws LunaException {
        if (input.equalsIgnoreCase("deadline")) {
            throw new LunaException("Please provide the description and the deadline.");
        }

        String[] parts = input.substring(9).split(" /by ", 2);
        if (parts.length < 2) {
            throw new LunaException("Please provide the description and the deadline.");
        }

        String description = parts[0].trim();
        String dateText = parts[1].trim();

        try {
            LocalDate date = LocalDate.parse(dateText);
            Deadline deadline = new Deadline(description, date);
            tasks.add(deadline);
            saveTasks(storage, tasks);
            ui.showAddSuccess(deadline, tasks.size());
        } catch (DateTimeParseException e) {
            throw new LunaException("Please use the date format yyyy-MM-dd.");
        }
    }
}
