package morpheus.commands;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import morpheus.utils.Parser;



public class AddCommandTest {
    @Test
    public void testAddValidToDo() {
        assertInstanceOf(AddCommand.class, Parser.parse("todo read book"));
    }

    @Test
    public void testAddValidDeadline() {
        assertInstanceOf(AddCommand.class, Parser.parse("deadline submit report /by 2025-09-18"));
    }

    @Test
    public void testAddValidEvent() {
        assertInstanceOf(AddCommand.class, Parser.parse("event project meeting /from 2025-09-18 /to 2025-09-19"));
    }

    @Test
    public void testAddInvalidCommand() {
        var cmd = Parser.parse("todo");
        assertInstanceOf(AddCommand.class, cmd);
        // Prepare minimal context for execute
        java.util.List<morpheus.tasks.Task> taskList = new java.util.ArrayList<>();
        var storage = new morpheus.utils.Storage("data/morpheus.txt");
        var ui = new morpheus.utils.Ui();
        String result = ((AddCommand) cmd).execute(taskList, storage, ui);
        assertTrue(result.toLowerCase().contains("short") || result.toLowerCase().contains("description"));
    }

    @Test
    public void testAddLongDescription() {
        String longDesc = "todo " + "a".repeat(1000);
        assertInstanceOf(AddCommand.class, Parser.parse(longDesc));
    }

    @Test
    public void testAddSpecialCharacters() {
        assertInstanceOf(AddCommand.class, Parser.parse("todo !@#$%^&*()"));
    }
}
