import java.util.List;

public class ByeCommand extends Command {

    public ByeCommand(String input) {
        super(input);
        this.isExit = true;
    }

    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        ui.byeMessage();
        storage.save(taskList);
    }
}
