package morpheus.tasks;

import morpheus.utils.CustomDateTime;

public class DeadlineTask extends Task {
    private final CustomDateTime endDateTime;

    public DeadlineTask(String description, CustomDateTime endDateTime) {
        super(description);
        this.endDateTime = endDateTime;
    }

    public DeadlineTask(String description, boolean isDone, CustomDateTime endDateTime) {
        super (description, isDone);
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

    @Override
    public String encode() {
        return "D | " + (this.isDone ? "1" : "0") + " | "
                + clean(description) + " | " + this.endDateTime.toString();
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), this.endDateTime.toString());
    }
}
