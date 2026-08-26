/**
 * Represents a task that takes place within a time range.
 */
public class Event extends Task {
    private final String fromDateTime;
    private final String toDateTime;

    /**
     * Creates an event task with the given description and time range.
     *
     * @param description Description of the event task.
     * @param fromDateTime Start time of the event.
     * @param toDateTime End time of the event.
     */
    public Event(String description, String fromDateTime, String toDateTime) {
        super(description);
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    /**
     * Returns the start time text of this event.
     *
     * @return Start time text.
     */
    public String getFromDateTime() {
        return fromDateTime;
    }

    /**
     * Returns the end time text of this event.
     *
     * @return End time text.
     */
    public String getToDateTime() {
        return toDateTime;
    }

    @Override
    public String toStorageString() {
        return "E | " + getStorageStatus() + " | " + getDescription()
                + " | " + fromDateTime + " | " + toDateTime;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + fromDateTime + " to: " + toDateTime + " )";
    }
}
