package morpheus.commands;

import morpheus.utils.Storage;
import morpheus.tasks.Task;
import morpheus.utils.Ui;

import java.util.List;

public abstract class Command {
    public String input;
    public boolean isExit = false;

    public Command(String input) {
        this.input = input;
    }

    public abstract void execute(List<Task> taskList, Storage storage, Ui ui);

    public boolean isExit() {
        return isExit;
    }
}
