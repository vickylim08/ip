package luna.task;

/**
 * Represents a generic task tracked by Luna.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return Status icon showing whether the task is done.
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return Task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is done.
     *
     * @return {@code true} if the task is done, {@code false} otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the text format used to save this task to disk.
     *
     * @return Serialized representation of this task.
     */
    public String toStorageString() {
        return "T | " + getStorageStatus() + " | " + description;
    }

    /**
     * Returns the numeric status used in the save file.
     *
     * @return {@code 1} if done, otherwise {@code 0}.
     */
    protected String getStorageStatus() {
        return isDone ? "1" : "0";
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}
