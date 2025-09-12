package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that lists all tasks in the current task list.
 * <p>
 * This command is triggered when the user enters the keyword:
 * <code>list</code>.
 * </p>
 *
 * The list includes details for each task such as:
 * <ul>
 *   <li>Task type (ToDo, Event, or Deadline)</li>
 *   <li>Completion status</li>
 *   <li>Description</li>
 *   <li>Start time (if applicable)</li>
 *   <li>End time (if applicable)</li>
 * </ul>
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    /**
     * Creates a new ListCommand.
     *
     * @param input the raw user input that triggered this command
     */
    public ListCommand(String input) {
        super(input);
    }

    /**
     * Executes the list command.
     * Delegates to the {@link Ui} to generate a formatted view of all tasks.
     * Useful for users to see the current state of their task list.
     *
     * @param taskList the list of tasks to display
     * @param storage  the storage handler (unused in this command but part of the interface)
     * @param ui       the user interface handler responsible for displaying the message
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        return ui.listMessage(taskList);
    }
}
