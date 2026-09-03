package luna;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import luna.storage.Storage;
import luna.ui.DialogBox;
import luna.ui.Ui;

/**
 * JavaFX GUI for the Luna chatbot.
 */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 420.0;
    private static final double WINDOW_HEIGHT = 640.0;
    private static final double INPUT_HEIGHT = 44.0;
    private static final double SEND_BUTTON_WIDTH = 80.0;
    private static final double EDGE_PADDING = 8.0;

    private final Luna luna;
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;

    /**
     * Creates the JavaFX application and its chatbot backend.
     */
    public Main() {
        this.luna = new Luna(new Ui(false), new Storage());
    }

    /**
     * Builds and shows the main JavaFX window.
     *
     * @param stage Primary stage provided by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox(12);
        dialogContainer.setPadding(new Insets(12));
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        userInput.setPromptText("Enter a Luna command...");
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        Scene scene = new Scene(mainLayout, WINDOW_WIDTH, WINDOW_HEIGHT);
        configureStage(stage, mainLayout, scene);
        registerInputHandlers();
        showStartupMessages();

        stage.show();
    }

    /**
     * Configures the window layout and sizing.
     *
     * @param stage Application window.
     * @param mainLayout Root layout pane.
     * @param scene Scene displayed on the stage.
     */
    private void configureStage(Stage stage, AnchorPane mainLayout, Scene scene) {
        stage.setTitle("Luna");
        stage.setResizable(false);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setScene(scene);

        mainLayout.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        scrollPane.setPrefSize(WINDOW_WIDTH - (EDGE_PADDING * 2), WINDOW_HEIGHT - INPUT_HEIGHT - 24.0);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        userInput.setPrefHeight(INPUT_HEIGHT);
        userInput.setPrefWidth(WINDOW_WIDTH - SEND_BUTTON_WIDTH - (EDGE_PADDING * 3));

        sendButton.setPrefHeight(INPUT_HEIGHT);
        sendButton.setPrefWidth(SEND_BUTTON_WIDTH);

        AnchorPane.setTopAnchor(scrollPane, EDGE_PADDING);
        AnchorPane.setLeftAnchor(scrollPane, EDGE_PADDING);
        AnchorPane.setRightAnchor(scrollPane, EDGE_PADDING);
        AnchorPane.setBottomAnchor(scrollPane, INPUT_HEIGHT + (EDGE_PADDING * 2));

        AnchorPane.setLeftAnchor(userInput, EDGE_PADDING);
        AnchorPane.setBottomAnchor(userInput, EDGE_PADDING);

        AnchorPane.setRightAnchor(sendButton, EDGE_PADDING);
        AnchorPane.setBottomAnchor(sendButton, EDGE_PADDING);
    }

    /**
     * Registers the handlers for sending chat messages.
     */
    private void registerInputHandlers() {
        sendButton.setOnAction(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());
    }

    /**
     * Shows any startup notice and Luna's welcome message.
     */
    private void showStartupMessages() {
        String startupNotice = luna.consumePendingResponse();
        if (!startupNotice.isBlank()) {
            dialogContainer.getChildren().add(DialogBox.getLunaDialog(startupNotice));
        }

        dialogContainer.getChildren().add(DialogBox.getLunaDialog(luna.getWelcomeMessage()));
    }

    /**
     * Sends the current input to Luna and appends the conversation to the GUI.
     */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = luna.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getLunaDialog(response)
        );
        userInput.clear();

        if (luna.isExitRequested()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
