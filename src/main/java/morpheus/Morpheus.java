package morpheus;

import java.util.List;

import morpheus.commands.Command;
import morpheus.tasks.Task;
import morpheus.utils.Parser;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

public class Morpheus {
    private final Ui ui;
    private final Storage storage;
    private final List<Task> taskList;

    public Morpheus(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.taskList = storage.load();
    }

    public static void main(String[] args) {
        new Morpheus("data/morpheus.txt").run();
    }

    public void run() {
        boolean isExit = false;
        String input;
        this.ui.welcomeMessage();
        while (!isExit) {
            input = this.ui.readInput();
            if (input.isEmpty()) {
                System.out.println("Looks like that line was empty. Try adding a task with 'todo', "
                        + "'deadline', or 'event'. I'm ready when you are!");
                continue;
            }
            Command command = Parser.parse(input);
            if (command != null) {
                command.execute(this.taskList, this.storage, this.ui);
                isExit = command.isExit();
            } else {
                System.out.println("Seems like you entered an invalid command. Please try again");
            }
        }
        ui.closeScanner();
    }
}
