package Morpheus.Commands;

import Morpheus.Utils.Storage;
import Morpheus.Tasks.Task;
import Morpheus.Utils.Ui;

import java.util.List;

public class MarkCommand extends Command {

    public MarkCommand(String input) {
        super(input);
    }

    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        try {
            int id = Integer.valueOf(this.input.substring(4).trim()) - 1;
            Task task = taskList.get(id);
            task.mark();
            ui.markMessage(task.toString());
            storage.save(taskList);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("I couldn't find that task number. Try 'list' to see what's available, " +
                    "then pick a number from there.");
        } catch (NumberFormatException e) {
            System.out.println("It seems I couldn't spot a task number after 'mark'. " +
                    "You can try something like: mark 2");
        }
    }
}
