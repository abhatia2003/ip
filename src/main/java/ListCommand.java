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
