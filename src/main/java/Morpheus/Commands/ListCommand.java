package Morpheus.Commands;

import Morpheus.Utils.Storage;
import Morpheus.Tasks.Task;
import Morpheus.Utils.Ui;

import java.util.List;

public class ListCommand extends Command {
    public ListCommand(String input) {
        super(input);
    }

    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        ui.listMessage(taskList);
    }
}
