package morpheus.tasks;

import morpheus.utils.CustomDateTime;

public class EventTask extends Task {
    private final CustomDateTime startDateTime;
    private final CustomDateTime endDateTime;

    public EventTask(String description, CustomDateTime startDateTime, CustomDateTime endDateTime) {
        super(description);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public EventTask(String description, boolean isDone, CustomDateTime startDateTime, CustomDateTime endDateTime) {
        super(description, isDone);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public String encode() {
        return "E | " + (this.isDone ? "1" : "0") + " | "
                + clean(description) + " | "
                + this.startDateTime.toString() + " | " + this.endDateTime.toString();
    }

    /**
     * Creates a deep copy of this Event task.
     * <p>
     * The returned copy has the same description, completion status,
     * start time, and end time as the original, but is an independent object.
     * </p>
     *
     * @return a new {@link EventTask} with the same values as this task
     */
    @Override
    public Task copy() {
        return new EventTask(
                this.description,
                this.isDone,
                this.startDateTime,
                this.endDateTime
        );
    }

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)", super.toString(), this.startDateTime.toString(), this.endDateTime.toString());
    }
}
