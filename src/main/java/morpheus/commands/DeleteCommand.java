package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that deletes a task from the task list.
 * <p>
 * This command is triggered when the user enters the keyword:
 * <code>delete {taskNumber}</code>.
 * </p>
 *
 * Upon execution, the specified task is removed from the list,
 * a confirmation message is displayed to the user, and the updated
 * task list is shown.
 *
 * Example usage:
 * <pre>
 * delete 2
 * </pre>
 * This removes the second task in the list.
 *
 * @author Aayush
 */
public class DeleteCommand extends Command {

    /**
     * Creates a new DeleteCommand.
     *
     * @param input the raw user input that triggered this command
     */
    public DeleteCommand(String input) {
        super(input);
    }

    /**
     * Executes the delete command by removing the specified task
     * from the task list.
     * <p>
     * The {@link Ui} is used to display a confirmation message with
     * the deleted task and the updated task count.
     * </p>
     *
     * @param taskList the list of tasks
     * @param storage  the storage handler (unused here, but part of the interface)
     * @param ui       the user interface handler responsible for displaying messages
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = Integer.valueOf(input.substring(6).trim()) - 1;
            Task task = taskList.remove(id);
            return ui.deleteTaskMessage(task.toString(), taskList);
        } catch (IndexOutOfBoundsException e) {
            return "I couldn't find that task number. "
                    + "Try 'list' to see what's available, then pick a number from there.";
        } catch (NumberFormatException e) {
            return "It seems I couldn't spot a task number after 'delete'. "
                    + "You can try something like: delete 2";
        }
    }
}
