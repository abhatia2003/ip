public class Event extends Task {
    private String startTime;
    private String endTime;

    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Event(String description, boolean isDone, String startTime, String endTime) {
        super(description, isDone);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String encode() {
        return "E | " + (this.isDone ? "1" : "0") + " | "
                + clean(description) + " | "
                + clean(this.startTime) + " | " + clean(this.endTime);
    }

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)", super.toString(), this.startTime, this.endTime);
    }
}
