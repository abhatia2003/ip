package morpheus.utils;

import morpheus.commands.*;

public class Parser {
     public static Command parse(String input) {
         if (input.equalsIgnoreCase("bye")) {
             return new ByeCommand(input);
         } else if (input.equalsIgnoreCase("list")) {
             return new ListCommand(input);
         } else if (input.trim().toLowerCase().startsWith("mark")) {
             return new MarkCommand(input);
         } else if (input.trim().toLowerCase().startsWith("unmark")) {
             return new MarkCommand(input);
         } else if (input.trim().toLowerCase().startsWith("delete")) {
             return new DeleteCommand(input);
         } else if (input.trim().toLowerCase().startsWith("event")
                 || input.trim().toLowerCase().startsWith("todo")
                 || input.trim().toLowerCase().startsWith("deadline")) {
            return new AddCommand(input);
         } else {
             return null;
         }
     }
}
