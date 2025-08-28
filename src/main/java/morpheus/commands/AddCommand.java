package morpheus.commands;

import java.util.List;

import morpheus.tasks.DeadlineTask;
import morpheus.tasks.EventTask;
import morpheus.tasks.Task;
import morpheus.tasks.ToDoTask;
import morpheus.utils.CustomDateTime;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

public class AddCommand extends Command {
    public static final String TODO = "todo";
    public static final String DEADLINE = "deadline";
    public static final String EVENT = "event";
    public static final String INVALID_TYPE = "invalid";
    private String type;

    public AddCommand(String input) {
        super(input);
        this.type = parseType(input.trim());
    }

    private String parseType(String input) {
        if (input.toLowerCase().startsWith(TODO)) {
            return TODO;
        } else if (input.toLowerCase().startsWith(DEADLINE)) {
            return DEADLINE;
        } else if (input.toLowerCase().startsWith(EVENT)) {
            return EVENT;
        } else {
            return INVALID_TYPE;
        }
    }

    @Override
    public void execute(List<Task> taskList, Storage storage, Ui ui) {
        if (!this.input.isEmpty()) {
            try {
                switch (this.type) {
                case TODO:
                    String task = this.input.substring(TODO.length()).trim();
                    if (task.length() < 2) {
                        throw new IllegalArgumentException("your todo description looks a bit short. "
                                + "Try: todo <description>");
                    }
                    taskList.add(new ToDoTask(task));
                    break;
                case DEADLINE:
                    String[] deadlineParts = this.input.substring(DEADLINE.length()).trim().split("(?i)/by");
                    if (deadlineParts.length < 2) {
                        throw new IllegalArgumentException("I can only add a deadline once I have a due time. "
                                + "Try: deadline <task> /by <time>");
                    }
                    String deadlineContent = deadlineParts[0].trim();
                    CustomDateTime deadlineEndTime = new CustomDateTime(deadlineParts[1].trim());
                    taskList.add(new DeadlineTask(deadlineContent, deadlineEndTime));
                    break;
                case EVENT:
                    String[] eventParts = this.input.substring(EVENT.length()).trim().split("(?i)/from|/to");
                    if (eventParts.length < 3) {
                        throw new IllegalArgumentException("I can only add an event once I have both start and "
                                + "end times. Try: event <task> /from <start> /to <end>");
                    }
                    String eventContent = eventParts[0].trim();
                    CustomDateTime eventStartTime = new CustomDateTime(eventParts[1].trim());
                    CustomDateTime eventEndTime = new CustomDateTime(eventParts[2].trim());
                    if (eventEndTime.compareTo(eventStartTime) < -1) {
                        throw new IllegalArgumentException("the end time can only happen after the event has "
                                + "started. Please try again with a valid set of timings");
                    }

                    taskList.add(new EventTask(eventContent, eventStartTime, eventEndTime));
                    break;
                default:
                    throw new IllegalArgumentException("I didn't recognise that task type. Please start "
                            + "with 'todo', 'deadline', or 'event' and I'll take it from there.");
                }
                ui.addTaskMessage(taskList);
                storage.save(taskList);
            } catch (IllegalArgumentException e) {
                System.out.println("Sorry, " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Sorry, something unexpected happened while adding that task. "
                        + "Could I trouble you to add it in again?");
            }
        } else {
            System.out.println("Looks like that line was empty. Whenever you're ready, type a task and "
                    + "I'll add it for you.");
        }
    }
}
