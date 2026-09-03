package luna;

import java.io.IOException;

import luna.command.Command;
import luna.parser.Parser;
import luna.storage.Storage;
import luna.task.TaskList;
import luna.ui.Ui;

/**
 * Runs Luna, a simple command-line task manager chatbot.
 */
public class Luna {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private boolean isExitRequested;

    /**
     * Creates a Luna application with its UI, storage, and task list.
     */
    public Luna() {
        this(new Ui(), new Storage());
    }

    /**
     * Creates a Luna application with the given collaborators.
     *
     * @param ui User interface used to format and display responses.
     * @param storage Storage used to load and save tasks.
     */
    public Luna(Ui ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
        this.tasks = loadTasks();
        this.isExitRequested = false;
    }

    /**
     * Runs the main command loop of the application.
     */
    public void run() {
        ui.showWelcome();

        while (!isExitRequested) {
            String input = ui.readCommand();

            if (input.isEmpty()) {
                continue;
            }
            getResponse(input);
        }
        ui.close();
    }

    /**
     * Returns the welcome message shown when the application starts.
     *
     * @return Welcome message with the command summary.
     */
    public String getWelcomeMessage() {
        return ui.getWelcomeMessage();
    }

    /**
     * Processes one user command and returns Luna's response text.
     *
     * @param input Raw user command.
     * @return Response generated for the command.
     */
    public String getResponse(String input) {
        String trimmedInput = input == null ? "" : input.trim();
        if (trimmedInput.isEmpty()) {
            return "";
        }

        try {
            Command command = Parser.parse(trimmedInput);
            command.execute(tasks, ui, storage);
            isExitRequested = command.isExit();
        } catch (LunaException e) {
            ui.showError(e.getMessage());
        } catch (NumberFormatException e) {
            ui.showError("Please provide a valid integer for task number.");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("That task number does not exist in your list.");
        }

        return ui.consumeLatestResponse();
    }

    /**
     * Returns whether Luna has already received an exit command.
     *
     * @return {@code true} if the application should stop accepting commands.
     */
    public boolean isExitRequested() {
        return isExitRequested;
    }

    /**
     * Returns any pending startup message that has not been consumed yet.
     *
     * @return Pending response text, or an empty string if none exists.
     */
    public String consumePendingResponse() {
        return ui.consumeLatestResponse();
    }

    /**
     * Starts the Luna application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Luna().run();
    }

    /**
     * Loads tasks from storage and falls back to an empty list if loading fails.
     *
     * @return Task list initialized from saved data when available.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException | LunaException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }
}
