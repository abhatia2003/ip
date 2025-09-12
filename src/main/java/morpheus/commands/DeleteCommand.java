package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that deletes a task from the task list.
 */
public class DeleteCommand extends Command {

    private static final String COMMAND_WORD = "delete";
    private static final String INVALID_INDEX_MSG =
            "I couldn't find that task number. Try 'list' to see what's available, then pick a number from there.";
    private static final String MISSING_NUMBER_MSG =
            "It seems I couldn't spot a task number after 'delete'. You can try something like: delete 2";

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
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = parseTaskIndex();
            Task task = taskList.remove(id);
            storage.save(taskList);
            return ui.deleteTaskMessage(task.toString(), taskList);
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
