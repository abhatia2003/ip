package morpheus.tasks;

public class ToDoTask extends Task {
    public ToDoTask(String description) {
        super(description);
    }

    public ToDoTask(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Creates a deep copy of this ToDo task.
     * <p>
     * The returned copy has the same description and completion status
     * as the original, but is an independent object.
     * </p>
     *
     * @return a new {@link ToDoTask} with the same values as this task
     */
    @Override
    public Task copy() {
        return new ToDoTask(this.description, this.isDone);
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
