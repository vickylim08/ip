package luna.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that takes place within a time range.
 */
public class Event extends Task {
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd MMM uuuu, h:mm a",
                    Locale.ENGLISH);

    /**
     * Creates an event task with the given description and time range.
     *
     * @param description Description of the event task.
     * @param fromDateTime Start time of the event.
     * @param toDateTime End time of the event.
     */
    public Event(String description, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        super(description);
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    /**
     * Returns the start time text of this event.
     *
     * @return Start time text.
     */
    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    /**
     * Returns the end time text of this event.
     *
     * @return End time text.
     */
    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    @Override
    public String toStorageString() {
        return "E | " + getStorageStatus() + " | " + getDescription()
                + " | " + fromDateTime + " | " + toDateTime;
    }

    @Override
    public String toString() {
        return "[E]" + getStatusIcon() + " "
                + getDescription()
                + " (from: " + fromDateTime.format(OUTPUT_FORMAT)
                + " to: " + toDateTime.format(OUTPUT_FORMAT) + ")";
    }
}
