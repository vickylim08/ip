package luna.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Represents one chat message in the JavaFX conversation view.
 */
public class DialogBox extends HBox {
    private static final String USER_BUBBLE_STYLE = """
            -fx-background-color: #d9fdd3;
            -fx-background-radius: 16;
            -fx-padding: 10 14 10 14;
            -fx-font-size: 13px;
            """;
    private static final String LUNA_BUBBLE_STYLE = """
            -fx-background-color: #f3f4f6;
            -fx-background-radius: 16;
            -fx-padding: 10 14 10 14;
            -fx-font-size: 13px;
            """;
    private static final String USER_TAG_STYLE = """
            -fx-background-color: #2563eb;
            -fx-background-radius: 999;
            -fx-padding: 6 10 6 10;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            """;
    private static final String LUNA_TAG_STYLE = """
            -fx-background-color: #111827;
            -fx-background-radius: 999;
            -fx-padding: 6 10 6 10;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            """;

    private final Label text;
    private final Label speakerTag;

    /**
     * Creates a dialog box with a message bubble and speaker tag.
     *
     * @param message Message text to display.
     * @param speaker Speaker label shown beside the bubble.
     * @param bubbleStyle Inline CSS applied to the message bubble.
     * @param speakerStyle Inline CSS applied to the speaker tag.
     */
    private DialogBox(String message, String speaker, String bubbleStyle, String speakerStyle) {
        this.text = new Label(message);
        this.speakerTag = new Label(speaker);

        text.setWrapText(true);
        text.setMaxWidth(280);
        text.setStyle(bubbleStyle);

        speakerTag.setStyle(speakerStyle);
        speakerTag.setMinWidth(Label.USE_PREF_SIZE);

        setAlignment(Pos.TOP_RIGHT);
        setSpacing(10);
        setPadding(new Insets(2, 4, 2, 4));
        getChildren().addAll(text, speakerTag);
    }

    /**
     * Returns a dialog box representing user input.
     *
     * @param message User message text.
     * @return Dialog box aligned for the user.
     */
    public static DialogBox getUserDialog(String message) {
        return new DialogBox(message, "You", USER_BUBBLE_STYLE, USER_TAG_STYLE);
    }

    /**
     * Returns a dialog box representing Luna's response.
     *
     * @param message Luna response text.
     * @return Dialog box aligned for Luna.
     */
    public static DialogBox getLunaDialog(String message) {
        DialogBox dialogBox = new DialogBox(message, "Luna", LUNA_BUBBLE_STYLE, LUNA_TAG_STYLE);
        dialogBox.flip();
        return dialogBox;
    }

    /**
     * Flips the dialog box so the speaker tag appears on the left.
     */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(children);
        getChildren().setAll(children);
    }
}
