package morpheus.utils;

import java.util.List;
import java.util.Scanner;

import morpheus.tasks.Task;

/**
 * Entry point for all user interaction in the Morpheus task manager application.
 * <p>
 * The {@code Ui} class handles input and output between the user and the system,
 * including reading commands, displaying task lists, confirmations, reminders,
 * and personality-rich feedback messages. It is responsible for formatting all
 * user-facing strings and ensuring a friendly, engaging experience.
 * </p>
 *
 * <ul>
 *   <li>Reads user input from the console</li>
 *   <li>Displays task lists, search results, and reminders</li>
 *   <li>Confirms actions such as adding, marking, unmarking, and deleting tasks</li>
 *   <li>Provides personality-rich feedback and guidance</li>
 * </ul>
 *
 * Example usage:
 * <pre>
 *     Ui ui = new Ui();
 *     String input = ui.readInput();
 *     System.out.println(ui.listMessage(taskList));
 * </pre>
 *
 * @author Aayush
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a new {@code Ui} instance with a scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of input from the user via the console.
     * @return the user's input as a String
     */
    public String readInput() {
        return scanner.nextLine();
    }

    /**
     * Closes the input scanner. Should be called before program termination to release resources.
     */
    public void closeScanner() {
        scanner.close();
    }

    /**
     * Displays the farewell message when the user exits the application.
     * @return a friendly goodbye message
     */
    public String byeMessage() {
        return "It’s time to unplug. Goodbye, friend! May your code be bug-free.";
    }

    /**
     * Displays the current task list, or a hint if it is empty.
     * @param taskList the list of tasks to display
     * @return a formatted string representing the task list or a hint if empty
     */
    public String listMessage(List<Task> taskList) {
        return formatTaskList(
                taskList,
                "Here’s your task list, Neo. Let’s see what reality you want to shape today.\n",
                "Your list is empty for now. Add one with 'todo', "
                        + "'deadline', or 'event', and I'll keep track for you."
        );
    }

    /**
     * Displays the results of a task search.
     * @param filteredTaskList the list of tasks matching the search
     * @return a formatted string of search results or a message if none found
     */
    public String findMessage(List<Task> filteredTaskList) {
        return formatTaskList(
                filteredTaskList,
                "Searching the Matrix for your request... Found it!",
                "I couldn’t find any tasks matching your search. You can add one using "
                        + "'todo', 'deadline', or 'event', and I'll track it for you."
        );
    }

    /**
     * Displays a message confirming a task has been marked as done.
     * @param task the task that was marked as done
     * @return a confirmation message
     */
    public String markMessage(String task) {
        return "Marked as complete! You’re one step closer to mastering the Matrix.\n" + task;
    }

    /**
     * Displays a message confirming a task has been marked as not done.
     * @param task the task that was unmarked
     * @return a confirmation message
     */
    public String unmarkMessage(String task) {

        return "Unmarked. Sometimes, even the One needs a break.\n" + task;
    }

    /**
     * Displays a message confirming a new task has been added.
     * @param taskList the list of tasks after addition
     * @return a confirmation message including the new task and total count
     */
    public String addTaskMessage(List<Task> taskList) {
        String taskInfo = String.format("A new ToDo? I like your style! Let’s add it and keep moving forward.\n%s",
                taskList.get(taskList.size() - 1));
        String taskCount = String.format("You now have %d task(s) on your list. Nice progress!", taskList.size());
        return taskInfo + "\n" + taskCount;
    }

    /**
     * Displays a reminder message for a specific task.
     * @param task the task to remind about
     * @return a formatted reminder message
     */
    public String reminderMessage(String task) {
        return String.format("Here are your reminders. Even Morpheus needs a nudge sometimes!\n%s\n", task);
    }

    /**
     * Displays a message confirming a task has been deleted.
     * @param task the task that was deleted
     * @param taskList the list of tasks after deletion
     * @return a confirmation message including the deleted task and total count
     */
    public String deleteTaskMessage(String task, List<Task> taskList) {
        String taskCount = String.format("You now have %d task(s) on your list. Great work!", taskList.size());
        return "Deleted! Sometimes you have to let go to move forward.\n" + task + "\n" + taskCount;
    }

    // Helper to format task lists (reduces duplication)
    private String formatTaskList(List<Task> tasks, String header, String emptyMessage) {
        if (tasks.isEmpty()) {
            return emptyMessage;
        }

        StringBuilder sb = new StringBuilder(header).append("\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d. %s%n", i + 1, tasks.get(i).toString()));
        }
        return sb.toString().trim();
    }
}
