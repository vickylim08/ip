import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Represents the command that adds an event task.
 */
public class EventCommand extends Command {
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm");

    private final String input;

    /**
     * Creates an event command from the full user input.
     *
     * @param input Full command entered by the user.
     */
    public EventCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(List<Task> tasks, Ui ui, Storage storage) throws LunaException {
        if (input.equalsIgnoreCase("event")) {
            throw new LunaException("Please provide the description, start, and end time.");
        }

        String[] parts = input.substring(6).split(" /from | /to ");
        if (parts.length < 3) {
            throw new LunaException("Please provide the description, start, and end time.");
        }

        String description = parts[0].trim();

        try {
            LocalDateTime from = LocalDateTime.parse(parts[1].trim(), INPUT_FORMAT);
            LocalDateTime to = LocalDateTime.parse(parts[2].trim(), INPUT_FORMAT);
            Event event = new Event(description, from, to);
            tasks.add(event);
            saveTasks(storage, tasks);
            ui.showAddSuccess(event, tasks.size());
        } catch (DateTimeParseException e) {
            throw new LunaException("Please use the format yyyy-MM-dd HHmm.");
        }
    }
}
