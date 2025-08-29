package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that searches for tasks containing a given keyword
 * in their description.
 * <p>
 * This command is triggered when the user enters the keyword:
 * <code>find {searchTerm}</code>.
 * </p>
 *
 * Upon execution, all tasks whose descriptions contain the search term
 * (case-insensitive) are displayed to the user. A deep copy of the task list
 * is used to ensure the original data remains unaffected.
 *
 * Example usage:
 * <pre>
 * find book
 * </pre>
 * This will list all tasks with descriptions containing "book".
 *
 * @author Aayush
 */
public class FindCommand extends Command {

    /**
     * Creates a new FindCommand.
     *
     * @param input the raw user input that triggered this command
     */
    public FindCommand(String input) {
        super(input);
    }

    /**
     * Executes the find command by filtering tasks whose descriptions
     * contain the target keyword (case-insensitive).
     * <p>
     * The {@link Ui} displays the filtered results to the user.
     * </p>
     *
     * @param taskList the list of tasks
     * @param storage  the storage handler (unused here, but part of the interface)
     * @param ui       the user interface handler responsible for displaying results
     */
    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        String target = this.input.substring("find".length()).trim();
        if (target.isEmpty()) {
            System.out.println("It seems like you did not finish your find request. Could you please try again?");
            return;
        }
        List<Task> deepCopy = taskList.stream()
                .map(Task::copy)
                .toList();
        List<Task> filteredDeepCopy = deepCopy.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(target.toLowerCase()))
                .toList();
        ui.findMessage(filteredDeepCopy);
    }
}
