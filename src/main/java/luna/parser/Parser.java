package luna.parser;

import luna.LunaException;
import luna.command.Command;
import luna.command.DeadlineCommand;
import luna.command.DeleteCommand;
import luna.command.EventCommand;
import luna.command.ExitCommand;
import luna.command.ListCommand;
import luna.command.MarkCommand;
import luna.command.TodoCommand;
import luna.command.UnmarkCommand;

/**
 * Converts raw user input into executable commands.
 */
public class Parser {
    /**
     * Parses raw user input into a command object.
     *
     * @param input Full command entered by the user.
     * @return Parsed command object.
     * @throws LunaException If the command word is invalid.
     */
    public static Command parse(String input) throws LunaException {
        String commandWord = input.split(" ", 2)[0].toLowerCase();

        switch (commandWord) {
        case "list":
            return new ListCommand();
        case "bye":
            return new ExitCommand();
        case "mark":
            return new MarkCommand(input);
        case "unmark":
            return new UnmarkCommand(input);
        case "todo":
            return new TodoCommand(input);
        case "deadline":
            return new DeadlineCommand(input);
        case "event":
            return new EventCommand(input);
        case "delete":
            return new DeleteCommand(input);
        default:
            throw new LunaException("I don't know this command :(");
        }
    }
}
