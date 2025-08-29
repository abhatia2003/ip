package morpheus.utils;

import java.util.List;
import java.util.Scanner;

import morpheus.tasks.Task;

/**
 * Handles all user interaction for the Morpheus task manager.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Reading user input from the command line</li>
 *   <li>Displaying banners, prompts, and messages to the user</li>
 *   <li>Presenting task-related updates (add, list, mark, unmark, delete)</li>
 * </ul>
 * </p>
 *
 * All output is printed to the standard console.
 *
 * @author Aayush
 */
public class Ui {
    private static final String HORIZONTAL_LINE = "------------------------------------------------------------\n";
    private static final String TODO = "todo";
    private static final String DEADLINE = "deadline";
    private static final String EVENT = "event";
    private Scanner scanner;

    /**
     * Constructs a new {@code Ui} instance with a scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the input string entered by the user
     */
    public String readInput() {
        return scanner.nextLine();
    }

    /**
     * Closes the input scanner. Should be called before program termination.
     */
    public void closeScanner() {
        scanner.close();
    }

    /**
     * Displays the welcome banner and introduction message.
     * Made up of ASCII art for aesthetic purposes.
     */
    public void welcomeMessage() {
        String welcome = """
     _    _      _
    | |  | |    | |
    | |  | | ___| | ___ ___  _ __ ___   ___
    | |/\\| |/ _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\
    \\  /\\  /  __/ | (_| (_) | | | | | |  __/
     \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|
            """;

        String to = """
     _
    | |
    | |_ ___
    | __/ _ \\
    | || (_) |
     \\__\\___/
            """;

        String morpheus = """
    ___  ___                 _
    |  \\/  |                | |
    | .  . | ___  _ __ _ __ | |__   ___ _   _ ___
    | |\\/| |/ _ \\| '__| '_ \\| '_ \\ / _ \\ | | / __|
    | |  | | (_) | |  | |_) | | | |  __/ |_| \\__ \\
    \\_|  |_/\\___/|_|  | .__/|_| |_|\\___|\\__,_|___/
                      | |
                      |_|
            """;

        String banner = welcome + "\n" + to + "\n" + morpheus;

        String init = HORIZONTAL_LINE
                + " Hey there! I'm Morpheus, like the one from The Matrix.\n"
                + " How can I help you today?\n"
                + HORIZONTAL_LINE;

        System.out.print(banner);
        System.out.print(init);
    }

    /**
     * Displays the farewell message and closing banner.
     */
    public void byeMessage() {
        System.out.println(
                HORIZONTAL_LINE
                        + "Thanks for spending time with me today. "
                        + "Press Run anytime to start me again. See you soon!\n"
                        + HORIZONTAL_LINE
        );
    }

    /**
     * Displays the current task list.
     * <p>
     * If the list is empty, shows a message prompting the user to add tasks.
     * Otherwise, enumerates each task with its index and details.
     * </p>
     *
     * @param tasklist the list of tasks to display
     */
    public void listMessage(List<Task> tasklist) {
        if (tasklist.size() == 0) {
            System.out.println(HORIZONTAL_LINE
                    + "Your list is empty for now. Add one with 'todo', 'deadline', or 'event', "
                    + "and I'll keep track for you.\n"
                    + HORIZONTAL_LINE
            );
            return;
        }
        String upperMessage = HORIZONTAL_LINE + "Here's a quick summary of your tasks:";
        System.out.println(upperMessage);
        for (int i = 0; i < tasklist.size(); i++) {
            String item = String.format("%d. %s", i + 1, tasklist.get(i).toString());
            System.out.println(item);
        }
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays the results of a task search to the user.
     * <p>
     * If no tasks match the search, a message is shown informing the user
     * that no tasks were found and suggesting how to add new ones.
     * Otherwise, this method prints a numbered list of matching tasks,
     * surrounded by horizontal separators for readability.
     * </p>
     *
     * @param filteredTasklist the list of tasks that match the user's search query
     */
    public void findMessage(List<Task> filteredTasklist) {
        if (filteredTasklist.isEmpty()) {
            System.out.println(HORIZONTAL_LINE
                    + "I couldn’t find any tasks matching your search. "
                    + "You can add one using 'todo', 'deadline', or 'event', and I'll track it for you.\n"
                    + HORIZONTAL_LINE
            );
            return;
        }

        String header = HORIZONTAL_LINE
                + "Here are the tasks I found that match your search:";
        System.out.println(header);

        for (int i = 0; i < filteredTasklist.size(); i++) {
            String item = String.format("%d. %s", i + 1, filteredTasklist.get(i).toString());
            System.out.println(item);
        }

        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a message confirming a task has been marked as done.
     *
     * @param task the task that was marked
     */
    public void markMessage(String task) {
        String output = HORIZONTAL_LINE
                + "Nice! I've marked this as completed:\n"
                + task + "\n"
                + HORIZONTAL_LINE;
        System.out.println(output);
    }

    /**
     * Displays a message confirming a task has been marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void unmarkMessage(String task) {
        String output = HORIZONTAL_LINE
                + "All set. I've marked this task as not done:\n"
                + task + "\n"
                + HORIZONTAL_LINE;
        System.out.println(output);
    }

    /**
     * Displays a message confirming a new task has been added, along with the
     * updated number of tasks.
     *
     * @param taskList the current list of tasks (the most recently added task is shown)
     */
    public void addTaskMessage(List<Task> taskList) {
        String output = String.format("Added this task:\n %s", taskList.get(taskList.size() - 1));
        String taskLength = String.format(
                "You now have %d task(s) on your list. Nice progress!",
                taskList.size());
        String printMessage = HORIZONTAL_LINE
                + output + "\n" + taskLength + "\n"
                + HORIZONTAL_LINE;
        System.out.println(printMessage);
    }

    /**
     * Displays a message confirming a task has been deleted, along with the
     * updated number of tasks.
     *
     * @param task     the task that was deleted
     * @param taskList the updated list of tasks
     */
    public void deleteTaskMessage(String task, List<Task> taskList) {
        String taskLength = String.format("You now have %d task(s) on your list. Great work!", taskList.size());
        String output = HORIZONTAL_LINE
                + "Got it! I've removed this task:\n"
                + task + "\n" + taskLength + "\n"
                + HORIZONTAL_LINE;
        System.out.println(output);
    }
}
