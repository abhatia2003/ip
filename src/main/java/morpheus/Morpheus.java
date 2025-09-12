package morpheus;

import java.util.List;

import morpheus.commands.CheckRemindersCommand;
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
    private static final String INVALID_COMMAND_MSG =
            "Seems like you entered an invalid command. Please try again.";
    private static final String EXIT_COMMAND = "END PROGRAM";
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
        this.taskList = initializeTaskList();
    }

    private List<Task> initializeTaskList() {
        return storage.load();
    }

    /**
     * Returns the welcome message shown when the program starts.
     * Introduces the assistant persona to the user.
     */
    public String getWelcomeMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hey there! I'm Morpheus, like the one from The Matrix.\n")
                .append("How can I help you today?\n");

        // Run the reminders check
        String reminders = new CheckRemindersCommand("reminders")
                .execute(this.taskList, this.storage, this.ui);

        if (!reminders.isBlank()) {
            sb.append("\nBefore we dive in, here are a few things I thought you’d like to be reminded about:\n")
                    .append(reminders)
                    .append("\nLet’s get started \n");
        }

        return sb.toString();
    }

    /**
     * Processes a single user input and returns the response.
     *
     * @param input the raw user input string
     * @return the response message, or EXIT_COMMAND if program should terminate
     */
    public String getResponse(String input) {
        Command command = Parser.parse(input);

        if (command == null) {
            return INVALID_COMMAND_MSG;
        }

        if (command.isExit()) {
            return EXIT_COMMAND;
        }

        return command.execute(this.taskList, this.storage, this.ui);
    }
}
