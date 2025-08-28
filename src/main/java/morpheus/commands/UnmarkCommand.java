package morpheus.commands;

import morpheus.utils.Storage;
import morpheus.utils.Ui;
import morpheus.tasks.Task;

import java.util.List;

/**
 * Represents a command that marks a task as not completed.
 * <p>
 * This command is triggered when the user enters the keyword:
 * <code>unmark {taskNumber}</code>.
 * </p>
 *
 * Upon execution, the specified task is updated to an incomplete state,
 * the change is displayed to the user, and the task list is saved
 * back to storage.
 *
 * Example usage:
 * <pre>
 * unmark 2
 * </pre>
 * This marks the second task in the list as incomplete.
 *
 * @author Aayush
 */
public class UnmarkCommand extends Command {

    /**
     * Creates a new UnmarkCommand.
     *
     * @param input the raw user input that triggered this command
     */
    public UnmarkCommand(String input) {
        super(input);
    }

    /**
     * Executes the unmark command by updating the completion status of
     * the specified task in the task list to "not done".
     * <p>
     * The {@link Ui} is used to display a confirmation message, and
     * the {@link Storage} is updated with the modified task list.
     * </p>
     *
     * @param taskList the list of tasks
     * @param storage  the storage handler responsible for saving changes
     * @param ui       the user interface handler responsible for displaying messages
     */
    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = Integer.valueOf(this.input.substring(6).trim()) - 1;
            Task task = taskList.get(id);
            task.unmark();
            ui.unmarkMessage(task.toString());
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("I couldn't find that task number. Try 'list' to see what's available, then pick a number from there.");
        } catch (NumberFormatException e) {
            System.out.println("It seems I couldn't spot a task number after 'unmark'. You can try something like: unmark 2");
        }
    }
}
