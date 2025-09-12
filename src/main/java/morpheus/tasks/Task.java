package morpheus.tasks;

import java.util.Optional;

import morpheus.utils.CustomDateTime;


/**
 * Represents a generic task in the Morpheus task manager.
 * <p>
 * A {@code Task} stores a description, a completion status,
 * and an optional reminder. It is the base abstraction extended by
 * concrete subclasses such as:
 * <ul>
 *   <li>{@link ToDoTask}</li>
 *   <li>{@link DeadlineTask}</li>
 *   <li>{@link EventTask}</li>
 * </ul>
 * </p>
 *
 * Each subclass may override methods such as {@link #encode()} or
 * {@link #toString()} to provide additional details (e.g., deadlines or durations).
 *
 * Tasks are immutable in type (once created as a {@link ToDoTask}, it cannot
 * change into another type) but mutable in state (status, reminder, etc.).
 *
 * @author Aayush
 */
public abstract class Task {

    /** The description of the task (e.g., "Read book"). */
    protected String description;

    /** Whether the task is completed. */
    protected boolean isDone;

    /** An optional reminder for this task, represented as a {@link CustomDateTime}. */
    protected CustomDateTime reminder;

    /**
     * Constructs a new task with the given description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructs a new task with the given description and completion status.
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
     * @return {@code "X"} if the task is done, otherwise a single space {@code " "}
     */
    public String getStatusIcon() {
        return this.isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void mark() {
        this.isDone = true;
    }

    /** Marks this task as not completed. */
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
        if (s == null) {
            return "";
        }
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
     * Sets a reminder for this task.
     *
     * @param reminder the {@link CustomDateTime} reminder to attach
     */
    public void setReminder(CustomDateTime reminder) {
        this.reminder = reminder;
    }

    /**
     * Returns the reminder for this task, if present.
     *
     * @return an {@link Optional} containing the reminder, or empty if none is set
     */
    public Optional<CustomDateTime> getReminder() {
        return Optional.ofNullable(reminder);
    }

    /**
     * Returns the description of the task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Creates a deep copy of this task.
     * <p>
     * Subclasses must override this method to return a new instance of the
     * specific task type with identical field values. This ensures that
     * copies are independent of the original objects.
     * </p>
     *
     * @return a new {@link Task} instance that is a deep copy of this task
     */
    public abstract Task copy();

    /**
     * Returns a string representation of this task,
     * including its status, description, and reminder (if any).
     * <p>
     * Example without reminder:
     * <pre>[X] Read book</pre>
     * Example with reminder:
     * <pre>[ ] Meet friends ⏰ 12 Sep 2025, 3:00 PM</pre>
     * </p>
     *
     * @return a string representation of this task
     */
    @Override
    public String toString() {
        String base = String.format("[%s] %s", getStatusIcon(), description);
        return base;
    }
}
