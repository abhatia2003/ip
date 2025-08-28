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

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)",
                super.toString(),
                this.startDateTime.toString(),
                this.endDateTime.toString());
    }
}
