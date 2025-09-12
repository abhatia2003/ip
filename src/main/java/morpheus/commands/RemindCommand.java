package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.CustomDateTime;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that sets a reminder on an existing task.
 * <p>
 * Usage:
 * <pre>
 *     remind &lt;taskNumber&gt; &lt;date/time&gt;
 * </pre>
 * Example:
 * <pre>
 *     remind 2 12 Sep 2025, 3:00 PM
 * </pre>
 * </p>
 * A reminder is stored with the task and will be shown when checking reminders.
 */
public class RemindCommand extends Command {

    private static final String USAGE_MESSAGE =
            "Usage: remind <taskNumber> <date/time>";

    /**
     * Creates a new {@code RemindCommand}.
     *
     * @param input the raw user input that triggered this command
     */
    public RemindCommand(String input) {
        super(input);
    }

    /**
     * Executes the remind command by setting a reminder on the specified task.
     * <p>
     * If the input is invalid (missing task number, invalid index, or invalid date/time),
     * a usage message will be returned instead.
     * </p>
     *
     * @param taskList the list of tasks to update
     * @param storage  the storage handler, used to persist reminders
     * @param ui       the user interface handler for feedback messages
     * @return a confirmation message if successful, or a usage message if input is invalid
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        String[] parts = input.trim().split("\\s+", 3);

        if (parts.length < 3) {
            return USAGE_MESSAGE;
        }

        Integer index = parseIndex(parts[1], taskList.size());
        if (index == null) {
            return USAGE_MESSAGE;
        }

        CustomDateTime reminderTime;
        try {
            reminderTime = new CustomDateTime(parts[2]);
        } catch (IllegalArgumentException e) {
            return USAGE_MESSAGE;
        }

        Task task = taskList.get(index);
        task.setReminder(reminderTime);

        storage.save(taskList);
        return ui.reminderMessage(task.toString());
    }

    /**
     * Parses and validates the task index from the user input.
     *
     * @param indexStr  the raw index string from input
     * @param taskCount the total number of tasks
     * @return the zero-based task index if valid, or {@code null} if invalid
     */
    private Integer parseIndex(String indexStr, int taskCount) {
        try {
            int idx = Integer.parseInt(indexStr) - 1; // convert to zero-based index
            if (idx < 0 || idx >= taskCount) {
                return null;
            }
            return idx;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
