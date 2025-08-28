package morpheus.commands;

import morpheus.tasks.Task;

import morpheus.utils.Storage;
import morpheus.utils.Ui;

import java.util.List;

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
 *
 * @author Aayush
 */
public class ListCommand extends Command {

    /**
     * Creates a new ListCommand.
     *
     * @param input the raw user input that triggered this command
     */
    public ListCommand(String input) {
        super(input);
    }

    /**
     * Executes the list command, instructing the {@link Ui} to display
     * all tasks currently in the provided task list.
     *
     * @param taskList the list of tasks to display
     * @param storage the storage handler (unused in this command but part of the interface)
     * @param ui the user interface handler responsible for displaying the message
     */
    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        ui.listMessage(taskList);
    }
}
