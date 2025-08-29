package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that marks a task as completed.
 * <p>
 * This command is triggered when the user enters the keyword:
 * <code>mark &lt;taskNumber&gt;</code>.
 * </p>
 *
 * Upon execution, the specified task is updated to a completed state,
 * the change is displayed to the user, and the task list is saved
 * back to storage.
 *
 * Example usage:
 * <pre>
 * mark 2
 * </pre>
 * This marks the second task in the list as completed.
 *
 * @author Aayush
 */
public class MarkCommand extends Command {

    /**
     * Creates a new MarkCommand.
     *
     * @param input the raw user input that triggered this command
     */
    public MarkCommand(String input) {
        super(input);
    }

    /**
     * Executes the mark command by updating the completion status of
     * the specified task in the task list to "done".
     * <p>
     * The {@link Ui} is used to display a confirmation message, and
     * the {@link Storage} is updated with the modified task list.
     * </p>
     *
     * @param taskList the list of tasks
     * @param storage the storage handler responsible for saving changes
     * @param ui the user interface handler responsible for displaying messages
     */
    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = Integer.valueOf(this.input.substring(4).trim()) - 1;
            Task task = taskList.get(id);
            task.mark();
            ui.markMessage(task.toString());
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("I couldn't find that task number. Try 'list' to see what's available, "
                    + "then pick a number from there.");
        } catch (NumberFormatException e) {
            System.out.println("It seems I couldn't spot a task number after 'mark'. "
                    + "You can try something like: mark 2");
        }
    }
}
