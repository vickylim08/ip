package luna.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Deadline}.
 */
public class DeadlineTest {
    @Test
    public void getByDate_deadlineCreated_returnsStoredDate() {
        LocalDate deadlineDate = LocalDate.of(2026, 8, 27);
        Deadline deadline = new Deadline("submit report", deadlineDate);

        assertEquals(deadlineDate, deadline.getByDate());
    }
}
