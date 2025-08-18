import java.util.Scanner;

public class Morpheus {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input;
        String init = "____________________________________________________________\n" +
                " Hey there! My name is Morpheus\n" +
                " Yeah, like the one from the Matrix\n" +
                " What can I do for you today?\n" +
                "____________________________________________________________\n";
        System.out.println(init);
        input = sc.nextLine();
        while (!input.isBlank() && !input.equalsIgnoreCase("bye")) {
            String output = "____________________________________________________________\n" +
                    input + "\n" +
                    "____________________________________________________________\n";
            System.out.println(output);
            input = sc.nextLine();
        }

        System.out.println("Bye! Just hit run to boot me up again. See you soon!");
        sc.close();
    }
}
