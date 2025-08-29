package morpheus.tasks;

/**
 * Represents a generic task in the Morpheus task manager.
 * <p>
 * A {@code Task} stores a description and a completion status
 * (done or not done). This is an abstract representation that
 * is extended by concrete subclasses such as:
 * <ul>
 *   <li>{@link ToDoTask}</li>
 *   <li>{@link DeadlineTask}</li>
 *   <li>{@link EventTask}</li>
 * </ul>
 * </p>
 *
 * Each subclass may override methods such as {@link #encode()} or
 * {@link #toString()} to provide additional details (e.g., dates).
 *
 * @author Aayush
 */
public class Task {
    /**
     * The description of the task.
     */
    protected String description;

    /**
     * Whether the task is completed.
     */
    protected boolean isDone;

    /**
     * Constructs a new Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructs a new Task with the given description and completion status.
     *
     * @param description the description of the task
     * @param isDone      whether the task is completed
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns a status icon representing the completion state of the task.
     *
     * @return {@code "X"} if the task is done, otherwise a single space
     */
    public String getStatusIcon() {
        return (this.isDone ? "X" : " ");
    }

    /**
     * Marks this task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Cleans a string by removing carriage returns and newlines,
     * replacing them with spaces, and trimming leading/trailing whitespace.
     *
     * @param s the input string
     * @return the cleaned string, or an empty string if {@code s} is null
     */
    public static String clean(String s) {
        if (s == null) return "";
        return s.replace("\r", " ")
                .replace("\n", " ")
                .trim();
    }

    /**
     * Encodes this task into a storage-friendly string representation.
     * <p>
     * Subclasses should override this method to provide
     * their own serialization format.
     * </p>
     *
     * @return the encoded representation of this task, or {@code null} by default
     */
    public String encode() {
        return null;
    }

    /**
     * Returns a string representation of this task,
     * including its status and description.
     * <p>
     * Example: <code>[X] Read book</code>
     * </p>
     *
     * @return a string representation of this task
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}
