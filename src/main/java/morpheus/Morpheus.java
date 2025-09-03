package morpheus;

import java.util.List;

import morpheus.commands.Command;
import morpheus.tasks.Task;
import morpheus.utils.Parser;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Entry point for the Morpheus task manager application.
 * <p>
 * Morpheus allows users to manage tasks of different types (ToDo, Deadline, Event)
 * through a command-line interface. Users can add, list, mark, unmark, and delete tasks.
 * </p>
 *
 * This class initializes the core components:
 * <ul>
 *   <li>{@link Ui} for user interaction</li>
 *   <li>{@link Storage} for saving and loading tasks</li>
 *   <li>{@link List}&lt;{@link Task}&gt; for storing the current tasks</li>
 * </ul>
 *
 * The main program flow is handled by the {@link #getResponse(String)} method, which continually
 * reads user input, parses it into a {@link Command}, and executes it until the
 * program is instructed to exit.
 *
 * @author Aayush
 */
public class Morpheus {
    private final Ui ui;
    private final Storage storage;
    private final List<Task> taskList;

    /**
     * Constructs a new instance of Morpheus.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public Morpheus(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.taskList = storage.load();
    }

    /**
     * Displays the welcome banner and introduction message.
     * Made up of ASCII art for aesthetic purposes.
     */
    public String getWelcomeMessage() {
        String init =
                "Hey there! I'm Morpheus, like the one from The Matrix.\n"
                + "How can I help you today?\n";
        return init;
    }


    /**
     * Runs the main program loop.
     * <p>
     * The loop continues until an exit command is issued. On each iteration:
     * <ol>
     *   <li>Prompts the user for input via {@link Ui}</li>
     *   <li>Parses the input into a {@link Command} using {@link Parser}</li>
     *   <li>Executes the command, modifying the task list and storage as needed</li>
     *   <li>Exits the loop if the command signals termination</li>
     * </ol>
     * </p>
     */
    public String getResponse(String input) {
        Command command = Parser.parse(input);
        if (command == null) {
            return "Seems like you entered an invalid command. Please try again.";
        } else if (command.isExit()) {
            return "END PROGRAM";
        } else {
            return command.execute(this.taskList, this.storage, this.ui);
        }
    }
}
