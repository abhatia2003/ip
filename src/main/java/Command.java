import java.util.List;

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
