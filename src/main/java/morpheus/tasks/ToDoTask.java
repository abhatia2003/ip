package morpheus.tasks;

public class ToDoTask extends Task {
    public ToDoTask(String description) {
        super(description);
    }

    public ToDoTask(String description, boolean isDone) {
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
