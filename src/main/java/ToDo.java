public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String encode() {
        return "T | " + (this.isDone ? "1" : "0") + " | " + clean(description);
    }

    @Override
    public String toString() {
        return String.format("[T] %s", super.toString());
    }
}
