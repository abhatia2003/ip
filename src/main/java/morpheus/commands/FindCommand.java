package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that searches for tasks containing a given keyword
 * in their description.
 */
public class FindCommand extends Command {

    private static final String COMMAND_WORD = "find";
    private static final String EMPTY_KEYWORD_MSG =
            "It seems like you did not finish your find request. Could you please try again?";

    public FindCommand(String input) {
        super(input);
    }

    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        String target = parseTargetKeyword();
        if (target.isEmpty()) {
            return EMPTY_KEYWORD_MSG;
        }

        List<Task> filteredTasks = filterTasks(taskList, target);
        return ui.findMessage(filteredTasks);
    }

    private String parseTargetKeyword() {
        return this.input.substring(COMMAND_WORD.length()).trim();
    }

    private List<Task> filterTasks(List<Task> taskList, String target) {
        return taskList.stream()
                .map(Task::copy)
                .filter(task -> task.getDescription().toLowerCase().contains(target.toLowerCase()))
                .toList();
    }
}
