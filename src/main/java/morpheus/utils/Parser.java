package morpheus.utils;

import morpheus.commands.AddCommand;
import morpheus.commands.ByeCommand;
import morpheus.commands.CheckRemindersCommand;
import morpheus.commands.Command;
import morpheus.commands.DeleteCommand;
import morpheus.commands.FindCommand;
import morpheus.commands.ListCommand;
import morpheus.commands.MarkCommand;
import morpheus.commands.RemindCommand;
import morpheus.commands.UnmarkCommand;
/**
 * Parses raw user input into corresponding {@link Command} objects.
 * <p>
 * The {@code Parser} is responsible for interpreting the first keyword
 * of a user's input and instantiating the appropriate {@link Command}
 * subclass. Any additional text after the keyword is preserved and
 * passed to the command for further handling.
 * </p>
 *
 * <h3>Supported commands:</h3>
 * <ul>
 *   <li><code>bye</code> → {@link ByeCommand}</li>
 *   <li><code>list</code> → {@link ListCommand}</li>
 *   <li><code>mark {taskNumber}</code> → {@link MarkCommand}</li>
 *   <li><code>unmark {taskNumber}</code> → {@link UnmarkCommand}</li>
 *   <li><code>delete {taskNumber}</code> → {@link DeleteCommand}</li>
 *   <li><code>todo {...}</code> → {@link AddCommand}</li>
 *   <li><code>deadline {...} /by {...}</code> → {@link AddCommand}</li>
 *   <li><code>event {...} /from {...} /to {...}</code> → {@link AddCommand}</li>
 * </ul>
 *
 * If the input does not match a recognized command, {@code null} is returned.
 *
 * @author Aayush
 */
public class Parser {

    /**
     * Parses a raw user input string and returns the corresponding {@link Command}.
     *
     * @param input the raw user input string
     * @return a {@link Command} instance if the input matches a known command,
     *         or {@code null} if the input is unrecognized
     */
    public static Command parse(String input) {
        String[] parts = input.trim().toLowerCase().split("\\s+", 2);
        String command = parts[0];

        switch (command) {
        case "bye": return new ByeCommand(input);
        case "list": return new ListCommand(input);
        case "find": return new FindCommand(input);
        case "unmark": return new UnmarkCommand(input);
        case "mark": return new MarkCommand(input);
        case "delete": return new DeleteCommand(input);
        case "remind": return new RemindCommand(input);
        case "reminders": return new CheckRemindersCommand(input);
        case "event":
        case "todo":
        case "deadline":
            return new AddCommand(input);
        default: return null;
        }
    }
}
