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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Parser#parse(String)}.
 */
public class ParserTest {
    @Test
    public void parse_listCommand_returnsListCommand() throws LunaException {
        Command command = Parser.parse("list");

        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_byeCommand_returnsExitCommand() throws LunaException {
        Command command = Parser.parse("bye");

        assertInstanceOf(ExitCommand.class, command);
    }

    @Test
    public void parse_markCommand_returnsMarkCommand() throws LunaException {
        Command command = Parser.parse("mark 1");

        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_unmarkCommand_returnsUnmarkCommand() throws LunaException {
        Command command = Parser.parse("unmark 1");

        assertInstanceOf(UnmarkCommand.class, command);
    }

    @Test
    public void parse_todoCommand_returnsTodoCommand() throws LunaException {
        Command command = Parser.parse("todo read book");

        assertInstanceOf(TodoCommand.class, command);
    }

    @Test
    public void parse_deadlineCommand_returnsDeadlineCommand() throws LunaException {
        Command command = Parser.parse("deadline submit report /by 2026-08-27");

        assertInstanceOf(DeadlineCommand.class, command);
    }

    @Test
    public void parse_eventCommand_returnsEventCommand() throws LunaException {
        Command command = Parser.parse("event meeting /from 2026-08-27T09:00 /to 2026-08-27T10:00");

        assertInstanceOf(EventCommand.class, command);
    }

    @Test
    public void parse_deleteCommand_returnsDeleteCommand() throws LunaException {
        Command command = Parser.parse("delete 1");

        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    public void parse_mixedCaseCommandWord_returnsCorrectCommand() throws LunaException {
        Command command = Parser.parse("ToDo read book");

        assertInstanceOf(TodoCommand.class, command);
    }

    @Test
    public void parse_unknownCommand_throwsLunaException() {
        assertThrows(LunaException.class, () -> Parser.parse("unknown"));
    }
}
