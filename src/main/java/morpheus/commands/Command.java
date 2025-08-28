package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

public abstract class Command {
    protected String input;
    protected boolean isExit = false;

    public Command(String input) {
        this.input = input;
    }

    public abstract void execute(List<Task> taskList, Storage storage, Ui ui);

    public boolean isExit() {
        return isExit;
    }
}
