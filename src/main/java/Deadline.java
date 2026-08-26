/**
 * Represents a task that must be completed by a deadline.
 */
public class Deadline extends Task {
    private final String byDateTime;

    /**
     * Creates a deadline task with the given description and deadline.
     *
     * @param description Description of the deadline task.
     * @param byDateTime Deadline of the task.
     */
    public Deadline(String description, String byDateTime) {
        super(description);
        this.byDateTime = byDateTime;
    }

    /**
     * Returns the deadline text of this task.
     *
     * @return Deadline text.
     */
    public String getByDateTime() {
        return byDateTime;
    }

    @Override
    public String toStorageString() {
        return "D | " + getStorageStatus() + " | " + getDescription() + " | " + byDateTime;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + byDateTime + ")";
    }
}
