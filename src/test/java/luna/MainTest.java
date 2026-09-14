package luna;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Tests resources required by the JavaFX application.
 */
public class MainTest {
    @Test
    public void resources_applicationStarts_areAvailable() {
        assertNotNull(MainTest.class.getResource("/luna.css"));
        assertNotNull(MainTest.class.getResource("/images/user-avatar.png"));
    }
}
