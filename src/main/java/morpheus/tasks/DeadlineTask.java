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
