import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Morpheus {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input;
        List<Task> tasklist = new ArrayList<>();
        welcomeMessage();
        while (true) {
            input = sc.nextLine();
            if (input.isEmpty() || input.equalsIgnoreCase("bye")) {
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
        String init = "------------------------------------------------------------\n" +
                " Hey there! My name is Morpheus\n" +
                " Like the guy from the Matrix\n" +
                " What can I do for you today?\n" +
                "------------------------------------------------------------\n";
        System.out.println(banner);
        System.out.println(init);
    }

    private static void byeMessage() {
        String output = "------------------------------------------------------------\n" +
                "Bye! Just hit run to boot me up again. See you soon!\n" +
                "------------------------------------------------------------\n";
        System.out.println(output);
    }

    private static void listMessage(List<Task> tasklist) {
        String upper = "------------------------------------------------------------\n" +
                "Here is a summary of your tasks: ";
        System.out.println(upper);
        for (int i = 0; i < tasklist.size(); i++) {
            String item = String.format("%d. %s", i + 1, tasklist.get(i).toString());
            System.out.println(item);
        }
        System.out.println("------------------------------------------------------------");
    }

    private static void markMessage(String input, List<Task> tasklist) {
        try {
            int id = Integer.valueOf(input.substring(4).trim()) - 1;
            Task task = tasklist.get(id);
            task.mark();
            String output = "--------------------------------------------------------------\n" +
                    "Nicely Done! I've marked this task as completed: \n" +
                    task.toString() + "\n" +
                    "--------------------------------------------------------------\n";
            System.out.println(output);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This list item does not exist, please try again with an index that is present.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number after 'mark', please try again.");
        }
    }

    private static void unmarkMessage(String input, List<Task> tasklist) {
        try {
            int id = Integer.valueOf(input.substring(6).trim()) - 1;
            Task task = tasklist.get(id);
            task.unmark();
            String output = "--------------------------------------------------------------\n" +
                    "Okay, I've unmarked this task and it is now incomplete: \n" +
                    task.toString() + "\n" +
                    "--------------------------------------------------------------\n";
            System.out.println(output);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This list item does not exist, please try again with an index that is present.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number after 'unmark', please try again.");
        }
    }

    private static void addTask(String input, List<Task> tasklist) {
        tasklist.add(new Task(input));
        String output = String.format("Added Task: %s", input);
        String printMessage = "------------------------------------------------------------\n" +
                output + "\n" +
                "------------------------------------------------------------\n";
        System.out.println(printMessage);
    }
}
