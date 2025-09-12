package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that marks a task as not completed.
 */
public class UnmarkCommand extends Command {

    private static final String COMMAND_WORD = "unmark";
    private static final String INVALID_INDEX_MSG =
            "I couldn't find that task number. Try 'list' to see what's available, then pick a number from there.";
    private static final String MISSING_NUMBER_MSG =
            "It seems I couldn't spot a task number after 'unmark'. You can try something like: unmark 2";

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
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = parseTaskIndex();
            Task task = taskList.get(id);
            task.unmark();
            storage.save(taskList);
            return ui.unmarkMessage(task.toString());
        } catch (IndexOutOfBoundsException e) {
            return INVALID_INDEX_MSG;
        } catch (NumberFormatException e) {
            return MISSING_NUMBER_MSG;
        }
    }

    private int parseTaskIndex() {
        String numberPart = input.substring(COMMAND_WORD.length()).trim();
        return Integer.parseInt(numberPart) - 1; // zero-based index
    }
}
