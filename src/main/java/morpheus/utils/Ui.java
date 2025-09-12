package morpheus.utils;

import java.util.List;
import java.util.Scanner;

import morpheus.tasks.Task;

/**
 * Handles all user interaction for the Morpheus task manager.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a new {@code Ui} instance with a scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Reads a line of input from the user. */
    public String readInput() {
        return scanner.nextLine();
    }

    /** Closes the input scanner. Should be called before program termination. */
    public void closeScanner() {
        scanner.close();
    }

    /** Displays the farewell message. */
    public String byeMessage() {
        return "Thanks for spending time with me today. "
                + "Press Run anytime to start me again. See you soon!";
    }

    /** Displays the current task list, or a hint if it is empty. */
    public String listMessage(List<Task> taskList) {
        return formatTaskList(
                taskList,
                "Here's a quick summary of your tasks:\n",
                "Your list is empty for now. Add one with 'todo', 'deadline', or 'event', and I'll keep track for you."
        );
    }

    /** Displays the results of a task search. */
    public String findMessage(List<Task> filteredTaskList) {
        return formatTaskList(
                filteredTaskList,
                "Here are the tasks I found that match your search:",
                "I couldn’t find any tasks matching your search. "
                        + "You can add one using 'todo', 'deadline', or 'event', and I'll track it for you."
        );
    }

    /** Displays a message confirming a task has been marked as done. */
    public String markMessage(String task) {
        return "Nice! I've marked this as completed:\n" + task;
    }

    /** Displays a message confirming a task has been marked as not done. */
    public String unmarkMessage(String task) {
        return "All set. I've marked this task as not done:\n" + task;
    }

    /** Displays a message confirming a new task has been added. */
    public String addTaskMessage(List<Task> taskList) {
        String taskInfo = String.format("Added this task:\n %s", taskList.get(taskList.size() - 1));
        String taskCount = String.format("You now have %d task(s) on your list. Nice progress!", taskList.size());
        return taskInfo + "\n" + taskCount;
    }

    public String reminderMessage(String task) {
        return String.format("Reminder set for:\n%s\n", task);
    }

    /** Displays a message confirming a task has been deleted. */
    public String deleteTaskMessage(String task, List<Task> taskList) {
        String taskCount = String.format("You now have %d task(s) on your list. Great work!", taskList.size());
        return "Got it! I've removed this task:\n" + task + "\n" + taskCount;
    }

    // 🔹 Helper to format task lists (reduces duplication)
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
