package morpheus.commands;

import java.util.List;

import morpheus.tasks.DeadlineTask;
import morpheus.tasks.EventTask;
import morpheus.tasks.Task;
import morpheus.tasks.ToDoTask;
import morpheus.utils.CustomDateTime;
import morpheus.utils.Storage;
import morpheus.utils.Ui;

/**
 * Represents a command that adds a new task to the task list.
 * <p>
 * This command is triggered when the user enters one of the following formats:
 * <ul>
 *   <li><code>todo &lt;description&gt;</code></li>
 *   <li><code>deadline &lt;description&gt; /by &lt;endTime&gt;</code></li>
 *   <li><code>event &lt;description&gt; /from &lt;startTime&gt; /to &lt;endTime&gt;</code></li>
 * </ul>
 * </p>
 *
 * Depending on the input, a {@link ToDoTask}, {@link DeadlineTask}, or {@link EventTask}
 * is created and added to the provided task list. The {@link Ui} displays a confirmation
 * message and the updated list is saved to {@link Storage}.
 *
 * @author Aayush
 */
public class AddCommand extends Command {
    public static final String TODO = "todo";
    public static final String DEADLINE = "deadline";
    public static final String EVENT = "event";
    public static final String INVALID_TYPE = "invalid";
    private String type;

    /**
     * Creates a new AddCommand based on the user's raw input.
     *
     * @param input the raw user input string that specifies the task type and details
     */
    public AddCommand(String input) {
        super(input);
        assert input != null : "Input should not be null";
        this.type = parseType(input.trim());
        assert this.type != null : "Parsed type should not be null";
    }

    /**
     * Determines the type of task based on the user's input.
     *
     * @param input the trimmed, lowercase user input
     * @return one of {@link #TODO}, {@link #DEADLINE}, {@link #EVENT}, or {@link #INVALID_TYPE}
     */
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

    /**
     * Executes the add command by creating the appropriate task based on
     * the parsed type and adding it to the provided task list.
     * <p>
     * Supported behaviors:
     * <ul>
     *   <li><b>ToDo:</b> requires only a description</li>
     *   <li><b>Deadline:</b> requires a description and a due time introduced with <code>/by</code></li>
     *   <li><b>Event:</b> requires a description, a start time introduced with <code>/from</code>,
     *       and an end time introduced with <code>/to</code></li>
     * </ul>
     * </p>
     *
     * After successfully adding the task:
     * <ul>
     *   <li>The {@link Ui} displays a confirmation message</li>
     *   <li>The {@link Storage} persists the updated task list</li>
     * </ul>
     *
     * Error handling:
     * <ul>
     *   <li>Invalid or missing description → {@link IllegalArgumentException}</li>
     *   <li>Missing or malformed date/time arguments → {@link IllegalArgumentException}</li>
     *   <li>Other unexpected failures are caught and reported gracefully</li>
     * </ul>
     *
     * @param taskList the list of tasks to which the new task is added
     * @param storage the storage handler used to save the updated task list
     * @param ui the user interface handler responsible for displaying messages
     */
    @Override
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        if (!this.input.isEmpty()) {
            try {
                switch (this.type) {
                case TODO:
                    String task = this.input.substring(TODO.length()).trim();
                    assert task != null : "TODO description should not be null";
                    if (task.length() < 2) {
                        throw new IllegalArgumentException("your todo description looks a bit short. "
                                + "Try: todo <description>");
                    }
                    taskList.add(new ToDoTask(task));
                    break;
                case DEADLINE:
                    String[] deadlineParts = this.input.substring(DEADLINE.length()).trim().split("(?i)/by");
                    assert deadlineParts.length >= 2 : "Deadline should include a due date";
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
                    assert eventParts.length >= 3 : "Event should include a start date and an end date";
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
                    assert this.type.equals(INVALID_TYPE) : "Unexpected type in AddCommand";
                    throw new IllegalArgumentException("I didn't recognise that task type. Please start "
                            + "with 'todo', 'deadline', or 'event' and I'll take it from there.");
                }
                storage.save(taskList);
                return ui.addTaskMessage(taskList);
            } catch (IllegalArgumentException e) {
                return "Sorry, " + e.getMessage();
            } catch (Exception e) {
                return "Sorry, something unexpected happened while adding that task. "
                        + "Could I trouble you to add it in again?";
            }
        } else {
            return "Looks like that line was empty. Whenever you're ready, type a task and "
                    + "I'll add it for you.";
        }
    }
}
