package luna.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Represents one message in the JavaFX conversation view.
 *
 * <p>User commands and Luna responses deliberately use different structures: commands appear as compact chips,
 * while responses appear as wider app output panels.</p>
 */
public class DialogBox extends HBox {
    private static final double USER_MESSAGE_MAX_WIDTH = 260.0;
    private static final String USER_MESSAGE_STYLE = """
            -fx-background-color: #2563eb;
            -fx-background-radius: 16 16 4 16;
            -fx-padding: 9 14 9 14;
            -fx-font-size: 13px;
            -fx-text-fill: white;
            """;
    private static final String LUNA_PANEL_STYLE = """
            -fx-background-color: #f8fafc;
            -fx-background-radius: 0 10 10 0;
            -fx-border-color: transparent transparent transparent #6366f1;
            -fx-border-width: 0 0 0 3;
            -fx-padding: 10 14 12 14;
            """;
    private static final String LUNA_HEADER_STYLE = """
            -fx-font-size: 10px;
            -fx-font-weight: bold;
            -fx-text-fill: #4f46e5;
            """;
    private static final String LUNA_MESSAGE_STYLE = """
            -fx-font-size: 13px;
            -fx-text-fill: #1f2937;
            """;
    private static final String ERROR_PANEL_STYLE = """
            -fx-background-color: #fff1f2;
            -fx-background-radius: 0 10 10 0;
            -fx-border-color: #fecdd3 #fecdd3 #fecdd3 #dc2626;
            -fx-border-width: 1 1 1 4;
            -fx-border-radius: 0 10 10 0;
            -fx-padding: 10 14 12 14;
            """;
    private static final String ERROR_HEADER_STYLE = """
            -fx-font-size: 11px;
            -fx-font-weight: bold;
            -fx-text-fill: #b91c1c;
            """;
    private static final String ERROR_MESSAGE_STYLE = """
            -fx-font-size: 13px;
            -fx-text-fill: #7f1d1d;
            """;
    private static final String WELCOME_MESSAGE_STYLE = LUNA_MESSAGE_STYLE + """
            -fx-font-family: "Monospaced";
            """;

    private DialogBox() {
        setPadding(new Insets(2, 4, 2, 4));
        setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Returns a compact, right-aligned chip representing user input.
     *
     * @param message User message text.
     * @return Dialog box aligned for the user.
     */
    public static DialogBox getUserDialog(String message) {
        DialogBox dialogBox = new DialogBox();
        Label messageLabel = createMessageLabel(message, USER_MESSAGE_STYLE);

        messageLabel.setId("user-message");
        messageLabel.setMaxWidth(USER_MESSAGE_MAX_WIDTH);
        dialogBox.setAlignment(Pos.TOP_RIGHT);
        dialogBox.getChildren().add(messageLabel);
        return dialogBox;
    }

    /**
     * Returns a wide response panel representing Luna's output.
     *
     * @param message Luna response text.
     * @return Dialog box aligned for Luna.
     */
    public static DialogBox getLunaDialog(String message) {
        return createLunaDialog(message, "LUNA", LUNA_PANEL_STYLE, LUNA_HEADER_STYLE, LUNA_MESSAGE_STYLE);
    }

    /**
     * Returns a visually prominent panel representing an error from Luna.
     *
     * @param message Error message text.
     * @return Dialog box styled to draw attention to the error.
     */
    public static DialogBox getErrorDialog(String message) {
        return createLunaDialog(message, "ERROR — CHECK YOUR COMMAND", ERROR_PANEL_STYLE,
                ERROR_HEADER_STYLE, ERROR_MESSAGE_STYLE);
    }

    /**
     * Returns a monospaced response panel for Luna's ASCII-art welcome message.
     *
     * @param message Welcome message containing the ASCII-art banner.
     * @return Monospaced dialog box aligned for Luna.
     */
    public static DialogBox getWelcomeDialog(String message) {
        return createLunaDialog(message, "LUNA", LUNA_PANEL_STYLE, LUNA_HEADER_STYLE, WELCOME_MESSAGE_STYLE);
    }

    /**
     * Builds Luna's app-output presentation with a header and a full-width body.
     *
     * @param message Luna response text.
     * @param header Header describing the response type.
     * @param panelStyle Inline CSS applied to the response panel.
     * @param headerStyle Inline CSS applied to the response header.
     * @param messageStyle Inline CSS applied to the response body.
     * @return Dialog box containing the response panel.
     */
    private static DialogBox createLunaDialog(String message, String header, String panelStyle,
            String headerStyle, String messageStyle) {
        DialogBox dialogBox = new DialogBox();
        VBox responsePanel = new VBox(5);
        Label headerLabel = new Label(header);
        Label messageLabel = createMessageLabel(message, messageStyle);

        responsePanel.setId("luna-response");
        responsePanel.setStyle(panelStyle);
        responsePanel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(responsePanel, Priority.ALWAYS);

        headerLabel.setId("luna-header");
        headerLabel.setStyle(headerStyle);
        messageLabel.setId("luna-message");
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        responsePanel.getChildren().addAll(headerLabel, messageLabel);
        dialogBox.setAlignment(Pos.TOP_LEFT);
        dialogBox.getChildren().add(responsePanel);
        return dialogBox;
    }

    /**
     * Creates a wrapping label shared by the two message presentations.
     *
     * @param message Text to display.
     * @param style Inline CSS applied to the label.
     * @return Configured message label.
     */
    private static Label createMessageLabel(String message, String style) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle(style);
        return messageLabel;
    }
}
