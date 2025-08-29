package morpheus.tasks;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getStatusIcon() {
        return (this.isDone ? "X" : " ");
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public static String clean(String s) {
        if (s == null) return "";
        return s.replace("\r", " ")
                .replace("\n", " ")
                .trim();
    }

    public String encode() {
        return null;
    }

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

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}