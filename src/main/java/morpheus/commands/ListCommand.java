package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

public class ListCommand extends Command {
    public ListCommand(String input) {
        super(input);
    }

    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        ui.listMessage(taskList);
    }
}
