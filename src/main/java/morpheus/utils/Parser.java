package morpheus.utils;

import morpheus.commands.*;

public class Parser {
     public static Command parse(String input) {
         String[] parts = input.trim().toLowerCase().split("\\s+", 2);
         String command = parts[0];

         switch (command) {
             case "bye": return new ByeCommand(input);
             case "list": return new ListCommand(input);
             case "find": return new FindCommand(input);
             case "unmark": return new UnmarkCommand(input);
             case "mark": return new MarkCommand(input);
             case "delete": return new DeleteCommand(input);
             case "event":
             case "todo":
             case "deadline": return new AddCommand(input);
             default: return null;
         }
     }
}
