package luna.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Tests the intentionally asymmetric structures used for user commands and Luna responses.
 */
public class DialogBoxTest {
    @BeforeAll
    public static void startJavaFxToolkit() {
        Platform.startup(() -> { });
    }

    @Test
    public void getUserDialog_messageProvided_createsCompactRightAlignedMessage() {
        DialogBox dialogBox = DialogBox.getUserDialog("list");
        Label messageLabel = assertInstanceOf(Label.class, dialogBox.getChildren().get(0));

        assertEquals(Pos.TOP_RIGHT, dialogBox.getAlignment());
        assertEquals(1, dialogBox.getChildren().size());
        assertEquals("user-message", messageLabel.getId());
        assertEquals("list", messageLabel.getText());
    }

    @Test
    public void getLunaDialog_messageProvided_createsWideLeftAlignedResponsePanel() {
        DialogBox dialogBox = DialogBox.getLunaDialog("Here are your tasks.");
        VBox responsePanel = assertInstanceOf(VBox.class, dialogBox.getChildren().get(0));
        Label headerLabel = assertInstanceOf(Label.class, responsePanel.getChildren().get(0));
        Label messageLabel = assertInstanceOf(Label.class, responsePanel.getChildren().get(1));

        assertEquals(Pos.TOP_LEFT, dialogBox.getAlignment());
        assertEquals("luna-response", responsePanel.getId());
        assertEquals("LUNA", headerLabel.getText());
        assertEquals("Here are your tasks.", messageLabel.getText());
    }
}
