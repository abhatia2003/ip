public class Task {
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

    public String getDescription() {
        return this.description;
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

    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }
}