package morpheus.commands;

import java.util.List;

import morpheus.tasks.Task;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents an abstract user command in the Morpheus task manager.
 * <p>
 * Each concrete subclass of {@code Command} corresponds to a specific user
 * action, such as adding a task, listing tasks, or marking a task as done.
 * </p>
 *
 * A command encapsulates:
 * <ul>
 *   <li>The raw user input string that triggered it</li>
 *   <li>An exit flag to indicate if the program should terminate after execution</li>
 * </ul>
 *
 * Subclasses must implement the {@link #execute(List, Storage, Ui)} method
 * to define their behavior.
 */
public abstract class Command {
    /**
     * The raw input string entered by the user that created this command.
     */
    protected final String input;

    /**
     * Indicates whether this command signals program termination.
     * Defaults to {@code false}, set to true only by exit-related commands.
     */
    private boolean isExit = false;

    /**
     * Constructs a new {@code Command}.
     *
     * @param input the raw user input that created this command
     */
    public Command(String input) {
        this.input = input;
    }

    /**
     * Executes the command, performing its intended action.
     *
     * @param taskList the current list of tasks
     * @param storage  the storage handler for saving tasks
     * @param ui       the user interface handler for displaying output
     */
    public abstract String execute(List<Task> taskList, Storage storage, Ui ui);

    /**
     * Marks this command as an exit command.
     */
    protected void setExit(boolean shouldExit) {
        this.isExit = shouldExit;
    }

    /**
     * Returns whether this command causes the program to terminate.
     *
     * @return {@code true} if this command should exit the program, {@code false} otherwise
     */
    public boolean isExit() {
        return isExit;
    }
}