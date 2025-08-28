import java.util.List;

public class UnmarkCommand extends Command{

    public UnmarkCommand(String input) {
        super(input);
    }

    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = Integer.valueOf(this.input.substring(6).trim()) - 1;
            Task task = taskList.get(id);
            task.unmark();
            ui.unmarkMessage(task.toString());
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("I couldn't find that task number. Try 'list' to see what's available, then pick a number from there.");
        } catch (NumberFormatException e) {
            System.out.println("It seems I couldn't spot a task number after 'unmark'. You can try something like: unmark 2");
        }
    }
}