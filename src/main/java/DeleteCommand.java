import java.util.List;

public class DeleteCommand extends Command {

    public DeleteCommand(String input) {
        super(input);
    }

    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = Integer.valueOf(input.substring(6).trim()) - 1;
            Task task = taskList.remove(id);
            ui.deleteTaskMessage(task.toString(), taskList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("I couldn't find that task number. Try 'list' to see what's available, then pick a number from there.");
        } catch (NumberFormatException e) {
            System.out.println("It seems I couldn't spot a task number after 'delete'. You can try something like: delete 2");
        }
    }
}
