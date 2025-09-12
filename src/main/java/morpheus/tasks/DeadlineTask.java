package morpheus.tasks;

import morpheus.utils.CustomDateTime;

/**
 * Represents a Deadline task, which has a description and an end date/time.
 * <p>
 * A Deadline task is created when a user enters a command in the format:
 * <code>deadline &lt;description&gt; /by &lt;endDateTime&gt;</code>
 * </p>
 *
 * This class supports encoding for storage and generating a user-friendly
 * string representation for display.
 *
 * @author Aayush
 */
public class DeadlineTask extends Task {
    private final CustomDateTime endDateTime;

    /**
     * Creates a new Deadline task with a description and end date/time.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     * @param endDateTime the deadline date and time
     */
    public DeadlineTask(String description, CustomDateTime endDateTime) {
        super(description);
        this.endDateTime = endDateTime;
    }

    /**
     * Creates a new Deadline task with a description, completion status,
     * and end date/time.
     *
     * @param description the description of the task
     * @param isDone whether the task is completed
     * @param endDateTime the deadline date and time
     */
    public DeadlineTask(String description, boolean isDone, CustomDateTime endDateTime) {
        super(description, isDone);
        this.endDateTime = endDateTime;
    }

    /**
     * Creates a deep copy of this Deadline task.
     * <p>
     * The returned copy has the same description, completion status,
     * and deadline date-time as the original, but is an independent object.
     * </p>
     *
     * @return a new {@link DeadlineTask} with the same values as this task
     */
    @Override
    public Task copy() {
        return new DeadlineTask(
                this.description,
                this.isDone,
                this.endDateTime
        );
    }

    /**
     * Encodes the Deadline task into a string format suitable for storage.
     * The format is: <code>D | &lt;isDone&gt; | &lt;description&gt; | &lt;endDateTime&gt;</code>.
     *
     * @return the encoded string representation of this Deadline task
     */
    @Override
    public String encode() {
        String base = "D | " + (this.isDone ? "1" : "0") + " | "
                + clean(description) + " | " + this.endDateTime.toString();
        if (reminder != null) {
            base += " | REMINDER: " + reminder.toString();
        }
        return base;
    }

    /**
     * Returns a user-friendly string representation of the Deadline task.
     * Example: <code>[D] [ ] Submit project (by: 28 Aug 2025, 8:00 PM)</code>
     *
     * @return a string representation of this Deadline task
     */
    @Override
    public String toString() {
        String base = String.format("[D] %s (by: %s)", super.toString(), this.endDateTime.toString());
        if (reminder != null) {
            base += String.format(" ⏰ %s", reminder.toString());
        }
        return base;
    }
}
