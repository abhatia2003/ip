package morpheus.tasks;

/**
 * Represents a ToDo task, which is a simple task with only a description
 * and no start or end date/time.
 * <p>
 * A ToDo task is created when a user enters a command in the format:
 * <code>todo &lt;description&gt;</code>
 * </p>
 *
 * This class supports encoding for storage and generating a string
 * representation for display to the user.
 *
 * @author Aayush
 */
public class ToDoTask extends Task {

    /**
     * Creates a new ToDo task with a description.
     *
     * @param description the title or description of the task
     */
    public ToDoTask(String description) {
        super(description);
    }

    /**
     * Creates a new ToDo task with a description and a completion status.
     *
     * @param description the title or description of the task
     * @param isDone whether the task is completed
     */
    public ToDoTask(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Creates a deep copy of this ToDo task.
     * <p>
     * The returned copy has the same description and completion status
     * as the original, but is an independent object.
     * </p>
     *
     * @return a new {@link ToDoTask} with the same values as this task
     */
    @Override
    public Task copy() {
        return new ToDoTask(this.description, this.isDone);
    }

    /**
     * Encodes the ToDo task into a string format suitable for storage.
     * The format is: <code>T | &lt;isDone&gt; | &lt;description&gt;</code>.
     *
     * @return the encoded string representation of this ToDo task
     */
    @Override
    public String encode() {

        String base = "T | " + (this.isDone ? "1" : "0") + " | " + clean(description);
        if (reminder != null) {
            base += " | REMINDER: " + reminder.toString();
        }
        return base;
    }

    /**
     * Returns a user-friendly string representation of the ToDo task.
     * Example: <code>[T] [ ] Finish homework</code>
     *
     * @return a string representation of this ToDo task
     */
    @Override
    public String toString() {
        String base = String.format("[T] %s", super.toString());
        if (reminder != null) {
            base += String.format(" ⏰ %s", reminder.toString());
        }
        return base;
    }
}
