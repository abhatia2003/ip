package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that ends the program.
 * <p>
 * This command is triggered when the user enters the keyword:
 * <code>bye</code>.
 * </p>
 *
 * Upon execution, a farewell message is displayed to the user and
 * the current task list is saved to storage before termination.
 *
 * Example usage:
 * <pre>
 * bye
 * </pre>
 * This terminates the program after saving tasks.
 */
public class ByeCommand extends Command {

    /**
     * Creates a new ByeCommand and sets the exit flag to {@code true}.
     *
     * @param input the raw user input that triggered this command
     */
    public ByeCommand(String input) {
        super(input);
        this.setExit(true);
    }

    /**
     * Executes the bye command by displaying a farewell message
     * and saving the current task list to storage.
     *
     * @param taskList the list of tasks
     * @param storage  the storage handler used to save tasks
     * @param ui       the user interface handler responsible for displaying messages
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            storage.save(taskList);
        } catch (Exception e) {
            System.err.println("Failed to save tasks before exiting.");
            e.printStackTrace();
        }
        return ui.byeMessage();
    }
}