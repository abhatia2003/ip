package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that marks a task as completed.
 */
public class MarkCommand extends Command {

    private static final String COMMAND_WORD = "mark";
    private static final String INVALID_INDEX_MSG =
            "I couldn't find that task number. Try 'list' to see what's available, then pick a number from there.";
    private static final String MISSING_NUMBER_MSG =
            "It seems I couldn't spot a task number after 'mark'. You can try something like: mark 2";

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
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = parseTaskIndex();
            Task task = taskList.get(id);
            task.mark();
            storage.save(taskList);
            return ui.markMessage(task.toString());
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