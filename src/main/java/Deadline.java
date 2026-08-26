import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a deadline.
 */
public class Deadline extends Task {
    private final LocalDate byDateTime;
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.ENGLISH);

    /**
     * Creates a deadline task with the given description and deadline.
     *
     * @param description Description of the deadline task.
     * @param byDateTime Deadline of the task.
     */
    public Deadline(String description, LocalDate byDateTime) {
        super(description);
        this.byDateTime = byDateTime;
    }

    /**
     * Returns the deadline text of this task.
     *
     * @return Deadline text.
     */
    public LocalDate getByDateTime() {
        return byDateTime;
    }

    @Override
    public String toStorageString() {
        return "D | " + getStorageStatus() + " | " + getDescription() + " | " + byDateTime;
    }

    @Override
    public String toString() {
        return "[D]" + getStatusIcon() + " "
                + getDescription()
                + " (by: " + byDateTime.format(OUTPUT_FORMAT) + ")";
    }
}
