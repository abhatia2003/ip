import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Morpheus {
    public static final String HORIZONTAL_LINE = "------------------------------------------------------------\n";
    public static final String TODO = "todo";
    public static final String DEADLINE = "deadline";
    public static final String EVENT = "event";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input;
        List<Task> tasklist = new ArrayList<>();
        welcomeMessage();
        while (true) {
            input = sc.nextLine();
            if (input.isEmpty()) {
                System.out.println("Looks like that line was empty. Try adding a task with 'todo', 'deadline', or 'event'. I’m ready when you are!");
                continue; // or print a gentle prompt
            }
            if (input.equalsIgnoreCase("bye")) {
                byeMessage();
                break;
            } else if (input.equalsIgnoreCase("list")) {
                listMessage(tasklist);
            } else if (input.trim().toLowerCase().startsWith("mark")) {
                markMessage(input, tasklist);
            } else if (input.trim().toLowerCase().startsWith("unmark")) {
                unmarkMessage(input, tasklist);
            } else {
                addTask(input, tasklist);
            }
        }
        sc.close();
    }

    private static void welcomeMessage() {
        String welcome = " _    _      _                          \n" +
                "| |  | |    | |                         \n" +
                "| |  | | ___| | ___ ___  _ __ ___   ___ \n" +
                "| |/\\| |/ _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\\n" +
                "\\  /\\  /  __/ | (_| (_) | | | | | |  __/\n" +
                " \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|";
        String to = " _        \n" +
                "| |       \n" +
                "| |_ ___  \n" +
                "| __/ _ \\ \n" +
                "| || (_) |\n" +
                " \\__\\___/ ";
        String morpheus = "\n" +
                "___  ___                 _                    \n" +
                "|  \\/  |                | |                   \n" +
                "| .  . | ___  _ __ _ __ | |__   ___ _   _ ___ \n" +
                "| |\\/| |/ _ \\| '__| '_ \\| '_ \\ / _ \\ | | / __|\n" +
                "| |  | | (_) | |  | |_) | | | |  __/ |_| \\__ \\\n" +
                "\\_|  |_/\\___/|_|  | .__/|_| |_|\\___|\\__,_|___/\n" +
                "                  | |                         \n" +
                "                  |_|                         \n";
        String banner = welcome + "\n" + to + "\n" + morpheus;
        String init = HORIZONTAL_LINE +
                " Hey there! I'm Morpheus, like the one from The Matrix.\n" +
                " How can I help you today?\n" +
                HORIZONTAL_LINE;
        System.out.println(banner);
        System.out.println(init);
    }

    private static void byeMessage() {
        System.out.println(
                HORIZONTAL_LINE +
                        "Thanks for spending time with me today. Press Run anytime to start me again. See you soon!\n" +
                        HORIZONTAL_LINE
        );
    }

    private static void listMessage(List<Task> tasklist) {
        if (tasklist.size() == 0) {
            System.out.println(
                    "Your list is empty for now. Add one with 'todo', 'deadline', or 'event', and I’ll keep track for you."
            );
            return;
        }
        String upperMessage = HORIZONTAL_LINE + "Here’s a quick summary of your tasks:";
        System.out.println(upperMessage);
        for (int i = 0; i < tasklist.size(); i++) {
            String item = String.format("%d. %s", i + 1, tasklist.get(i).toString());
            System.out.println(item);
        }
        System.out.println(HORIZONTAL_LINE);
    }

    private static void markMessage(String input, List<Task> tasklist) {
        try {
            int id = Integer.valueOf(input.substring(4).trim()) - 1;
            Task task = tasklist.get(id);
            task.mark();
            String output = HORIZONTAL_LINE +
                    "Nice! I’ve marked this as completed:\n" +
                    task.toString() + "\n" +
                    HORIZONTAL_LINE;
            System.out.println(output);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("I couldn’t find that task number just yet. Try 'list' to see what’s available, then pick a number from there.");
        } catch (NumberFormatException e) {
            System.out.println("It seems I couldn’t spot a task number after 'mark'. You can try something like: mark 2");
        }
    }

    private static void unmarkMessage(String input, List<Task> tasklist) {
        try {
            int id = Integer.valueOf(input.substring(6).trim()) - 1;
            Task task = tasklist.get(id);
            task.unmark();
            String output = HORIZONTAL_LINE +
                    "All set. I’ve marked this task as not done:\n" +
                    task.toString() + "\n" +
                    HORIZONTAL_LINE;
            System.out.println(output);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("I couldn’t find that task number just yet. Try 'list' to see what’s available, then pick a number from there.");
        } catch (NumberFormatException e) {
            System.out.println("It seems I couldn’t spot a task number after 'unmark'. You can try something like: unmark 2");
        }
    }

    private static void addTask(String input, List<Task> tasklist) {
        if (!input.isEmpty()) {
            try {
                input = input.trim();
                if (input.toLowerCase().startsWith(TODO)) {
                    String task = input.substring(TODO.length()).trim();
                    if (task.length() < 2) {
                        throw new IllegalArgumentException("your todo description looks a bit short. Try: todo <description>");
                    }
                    tasklist.add(new ToDo(task));
                } else if (input.toLowerCase().startsWith(DEADLINE)) {
                    input = input.substring(DEADLINE.length()).trim();
                    String[] parts = input.split("(?i)/by");

                    if (parts.length < 2) {
                        throw new IllegalArgumentException("I can only add a deadline once I have a due time. Try: deadline <task> /by <time>");
                    }

                    String content = parts[0].trim();
                    String endTime = parts[1].trim();
                    tasklist.add(new Deadline(content, endTime));

                } else if (input.toLowerCase().startsWith(EVENT)) {
                    input = input.substring(EVENT.length()).trim();
                    String[] parts = input.split("(?i)/from|/to");

                    if (parts.length < 3) {
                        throw new IllegalArgumentException("I can only add an event once I have both start and end times. Try: event <task> /from <start> /to <end>");
                    }

                    String content = parts[0].trim();
                    String startTime = parts[1].trim();
                    String endTime = parts[2].trim();
                    tasklist.add(new Event(content, startTime, endTime));

                } else {
                    throw new IllegalArgumentException("I didn’t recognise that task type. Please start with 'todo', 'deadline', or 'event' and I’ll take it from there.");
                }

                // Success message
                String output = String.format("Added this task:\n %s", tasklist.get(tasklist.size() - 1));
                String taskLength = String.format("You now have %d task(s) on your list. Nice progress!", tasklist.size());
                String printMessage = HORIZONTAL_LINE +
                        output + "\n" + taskLength + "\n" +
                        HORIZONTAL_LINE;
                System.out.println(printMessage);

            } catch (IllegalArgumentException e) {
                System.out.println("Sorry, " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Sorry, something unexpected happened while adding that task. Could I trouble you to add it in again?");
            }
        } else {
            System.out.println("Looks like that line was empty. Whenever you’re ready, type a task and I’ll add it for you.");
        }
    }
}