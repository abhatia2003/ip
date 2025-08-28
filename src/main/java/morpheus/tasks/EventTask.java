package morpheus.tasks;

import morpheus.utils.CustomDateTime;

/**
 * Represents an Event task, which has a description, a start date/time,
 * and an end date/time.
 * <p>
 * An Event task is created when a user enters a command in the format:
 * <code>event &lt;description&gt; /from &lt;startDateTime&gt; /to &lt;endDateTime&gt;</code>
 * </p>
 *
 * This class supports encoding for storage and generating a user-friendly
 * string representation for display.
 *
 * @author Aayush
 */
public class EventTask extends Task {
    private final CustomDateTime startDateTime;
    private final CustomDateTime endDateTime;

    /**
     * Creates a new Event task with a description, start date/time, and end date/time.
     * The task is initially marked as not done.
     *
     * @param description the description of the event
     * @param startDateTime the starting date and time of the event
     * @param endDateTime the ending date and time of the event
     */
    public EventTask(String description, CustomDateTime startDateTime, CustomDateTime endDateTime) {
        super(description);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Creates a new Event task with a description, completion status,
     * start date/time, and end date/time.
     *
     * @param description the description of the event
     * @param isDone whether the task is completed
     * @param startDateTime the starting date and time of the event
     * @param endDateTime the ending date and time of the event
     */
    public EventTask(String description, boolean isDone, CustomDateTime startDateTime, CustomDateTime endDateTime) {
        super(description, isDone);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Encodes the Event task into a string format suitable for storage.
     * The format is: <code>E | &lt;isDone&gt; | &lt;description&gt; | &lt;startDateTime&gt; | &lt;endDateTime&gt;</code>.
     *
     * @return the encoded string representation of this Event task
     */
    @Override
    public String encode() {
        return "E | " + (this.isDone ? "1" : "0") + " | "
                + clean(description) + " | "
                + this.startDateTime.toString() + " | " + this.endDateTime.toString();
    }

    /**
     * Returns a user-friendly string representation of the Event task.
     * Example: <code>[E] [ ] Meeting (from: 24 Apr 2025, 1:00 PM to: 24 Apr 2025, 3:00 PM)</code>
     *
     * @return a string representation of this Event task
     */
    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)",
                super.toString(),
                this.startDateTime.toString(),
                this.endDateTime.toString());
    }
}
