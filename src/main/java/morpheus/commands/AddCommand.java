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
 */
public class AddCommand extends Command {
    public static final String TODO = "todo";
    public static final String DEADLINE = "deadline";
    public static final String EVENT = "event";
    public static final String INVALID_TYPE = "invalid";

    private static final String EMPTY_INPUT_MSG =
            "Looks like that line was empty. Whenever you're ready, type a task and I'll add it for you.";
    private static final String UNEXPECTED_ERROR_MSG =
            "Sorry, something unexpected happened while adding that task. Could I trouble you to add it in again?";
    private static final String INVALID_TYPE_MSG =
            "I didn't recognise that task type. "
                    + "Please start with 'todo', 'deadline', or 'event' and I'll take it from there.";
    private static final String TODO_DESC_ERROR =
            "Your todo description looks a bit short. Try: todo <description>";
    private static final String DEADLINE_FORMAT_ERROR =
            "I can only add a deadline once I have a due time. Try: deadline <task> /by <time>";
    private static final String EVENT_FORMAT_ERROR =
            "I can only add an event once I have both start and end times. Try: event <task> /from <start> /to <end>";
    private static final String EVENT_TIME_ERROR =
            "The end time can only happen after the event has started. Please try again with a valid set of timings.";

    private final String type;

    /**
     * Constructs a new {@code AddCommand} using the given raw user input.
     * <p>
     * This constructor ensures the input is not {@code null}, parses the task
     * type (e.g., {@code todo}, {@code deadline}, {@code event}), and stores it
     * for later use when executing the command.
     * </p>
     *
     * @param input the raw user input string that specifies the task to add;
     *              must not be {@code null}
     * @throws AssertionError if {@code input} is {@code null} or the parsed type is {@code null}
     */
    public AddCommand(String input) {
        super(input);
        assert input != null : "Input should not be null";
        this.type = parseType(input.trim());
        assert this.type != null : "Parsed type should not be null";
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
    public String execute(List<Task> taskList, Storage storage, Ui ui) {
        if (this.input.isEmpty()) {
            return EMPTY_INPUT_MSG;
        }

        try {
            switch (this.type) {
            case TODO:
                handleTodoTask(taskList);
                break;
            case DEADLINE:
                handleDeadlineTask(taskList);
                break;
            case EVENT:
                handleEventTask(taskList);
                break;
            default:
                assert this.type.equals(INVALID_TYPE) : "Unexpected type in AddCommand";
                throw new IllegalArgumentException(INVALID_TYPE_MSG);
            }
            storage.save(taskList);
            return ui.addTaskMessage(taskList);
        } catch (IllegalArgumentException e) {
            return "Sorry, " + e.getMessage();
        } catch (Exception e) {
            return UNEXPECTED_ERROR_MSG;
        }
    }

    private void handleTodoTask(List<Task> taskList) {
        String task = this.input.substring(TODO.length()).trim();
        assert task != null : "TODO description should not be null";
        if (task.length() < 2) {
            throw new IllegalArgumentException(TODO_DESC_ERROR);
        }
        taskList.add(new ToDoTask(task));
    }

    private void handleDeadlineTask(List<Task> taskList) {
        String[] deadlineParts = this.input.substring(DEADLINE.length()).trim().split("(?i)/by");
        assert deadlineParts.length >= 2 : "Deadline should include a due date";
        if (deadlineParts.length < 2) {
            throw new IllegalArgumentException(DEADLINE_FORMAT_ERROR);
        }
        String deadlineContent = deadlineParts[0].trim();
        CustomDateTime deadlineEndTime = new CustomDateTime(deadlineParts[1].trim());
        taskList.add(new DeadlineTask(deadlineContent, deadlineEndTime));
    }

    private void handleEventTask(List<Task> taskList) {
        String[] eventParts = this.input.substring(EVENT.length()).trim().split("(?i)/from|/to");
        assert eventParts.length >= 3 : "Event should include a start date and an end date";
        if (eventParts.length < 3) {
            throw new IllegalArgumentException(EVENT_FORMAT_ERROR);
        }
        String eventContent = eventParts[0].trim();
        CustomDateTime eventStartTime = new CustomDateTime(eventParts[1].trim());
        CustomDateTime eventEndTime = new CustomDateTime(eventParts[2].trim());
        assert eventStartTime != null && eventEndTime != null : "Event times should not be null";
        if (eventEndTime.compareTo(eventStartTime) < -1) {
            throw new IllegalArgumentException(EVENT_TIME_ERROR);
        }
        taskList.add(new EventTask(eventContent, eventStartTime, eventEndTime));
    }
}
