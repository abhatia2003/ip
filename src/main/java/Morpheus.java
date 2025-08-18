import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Morpheus {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input;
        List<String> tasklist = new ArrayList<>();
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
        while (true) {
            input = sc.nextLine();
            if (input.isEmpty() || input.equalsIgnoreCase("bye")) {
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println("------------------------------------------------------------");
                for (int i = 0; i < tasklist.size(); i++) {
                    String item = String.format("%d. %s", i + 1, tasklist.get(i));
                    System.out.println(item);
                }
                System.out.println("------------------------------------------------------------");
            } else {
                tasklist.add(input);
                String output = String.format("Added Task: %s", input);
                String printMessage = "------------------------------------------------------------\n" +
                        output + "\n" +
                        "------------------------------------------------------------\n";
                System.out.println(printMessage);
            }
        }
        String byeMessage = "------------------------------------------------------------\n" +
                "Bye! Just hit run to boot me up again. See you soon!\n" +
                "------------------------------------------------------------\n";
        System.out.println(byeMessage);
        sc.close();
    }
}
