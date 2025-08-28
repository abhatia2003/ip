package morpheus.utils;

import morpheus.tasks.Task;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private static final String HORIZONTAL_LINE = "------------------------------------------------------------\n";
    private static final String TODO = "todo";
    private static final String DEADLINE = "deadline";
    private static final String EVENT = "event";
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readInput() {
        return scanner.nextLine();
    }

    public void closeScanner() {
        scanner.close();
    }

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

        String init = HORIZONTAL_LINE +
                " Hey there! I'm Morpheus, like the one from The Matrix.\n" +
                " How can I help you today?\n" +
                HORIZONTAL_LINE;

        System.out.print(banner);   // keep as print; banner already ends with newlines
        System.out.print(init);
    }

    public void byeMessage() {
        System.out.println(
                HORIZONTAL_LINE +
                        "Thanks for spending time with me today. Press Run anytime to start me again. See you soon!\n" +
                        HORIZONTAL_LINE
        );
    }

    public void listMessage(List<Task> tasklist) {
        if (tasklist.size() == 0) {
            System.out.println(
                    "Your list is empty for now. Add one with 'todo', 'deadline', or 'event', " +
                            "and I'll keep track for you."
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

    public void markMessage(String task) {
        String output = HORIZONTAL_LINE +
                "Nice! I've marked this as completed:\n" +
                task + "\n" +
                HORIZONTAL_LINE;
        System.out.println(output);
    }

    public void unmarkMessage(String task) {
        String output = HORIZONTAL_LINE +
                "All set. I've marked this task as not done:\n" +
                task + "\n" +
                HORIZONTAL_LINE;
        System.out.println(output);
    }

    public void addTaskMessage(List<Task> taskList) {
        String output = String.format("Added this task:\n %s", taskList.get(taskList.size() - 1));
        String taskLength = String.format(
                "You now have %d task(s) on your list. Nice progress!",
                taskList.size());
        String printMessage = HORIZONTAL_LINE +
                output + "\n" + taskLength + "\n" +
                HORIZONTAL_LINE;
        System.out.println(printMessage);
    }

    public void deleteTaskMessage(String task, List<Task> taskList) {
        String taskLength = String.format("You now have %d task(s) on your list. Great work!", taskList.size());
        String output = HORIZONTAL_LINE +
                "Got it! I've removed this task:\n" +
                task + "\n" + taskLength + "\n" +
                HORIZONTAL_LINE;
        System.out.println(output);
    }
}
