public class Deadline extends Task{
    private String endTime;

    public Deadline(String description, String endTime) {
        super(description);
        this.endTime = endTime;
    }

    public Deadline(String description, boolean isDone, String endTime) {
        super (description, isDone);
        this.endTime = endTime;
    }

    @Override
    public String encode() {
        return "D | " + (this.isDone ? "1" : "0") + " | "
                + clean(description) + " | " + clean(this.endTime);
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), this.endTime);
    }
}
