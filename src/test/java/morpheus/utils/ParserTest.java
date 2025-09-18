package morpheus.utils;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import morpheus.commands.AddCommand;
import morpheus.commands.ByeCommand;
import morpheus.commands.CheckRemindersCommand;
import morpheus.commands.DeleteCommand;
import morpheus.commands.FindCommand;
import morpheus.commands.ListCommand;
import morpheus.commands.MarkCommand;
import morpheus.commands.RemindCommand;
import morpheus.commands.UnmarkCommand;

public class ParserTest {
    @Test
    public void testParseValidCommands() {
        assertInstanceOf(ByeCommand.class, Parser.parse("bye"));
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(FindCommand.class, Parser.parse("find something"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(RemindCommand.class, Parser.parse("remind"));
        assertInstanceOf(CheckRemindersCommand.class, Parser.parse("reminders"));
        assertInstanceOf(AddCommand.class, Parser.parse("event project meeting /from 2025-09-18 /to 2025-09-19"));
        assertInstanceOf(AddCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline submit report /by 2025-09-18"));
    }

    @Test
    public void testParseInvalidCommand() {
        assertNull(Parser.parse("invalidcommand"));
        assertNull(Parser.parse("foobar123"));
    }

    @Test
    public void testParseEmptyInput() {
        assertNull(Parser.parse(""));
        assertNull(Parser.parse("   "));
    }
}
