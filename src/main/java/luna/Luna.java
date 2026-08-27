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

    /**
     * Creates a Luna application with its UI, storage, and task list.
     */
    public Luna() {
        this.ui = new Ui();
        this.storage = new Storage();
        this.tasks = loadTasks();
    }

    /**
     * Runs the main command loop of the application.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            if (input.isEmpty()) {
                continue;
            }
            try {
                Command command = Parser.parse(input);
                command.execute(tasks, ui, storage);
                if (command.isExit()) {
                    break;
                }
            } catch (LunaException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Please provide a valid integer for task number.");
            } catch (IndexOutOfBoundsException e) {
                ui.showError("That task number does not exist in your list.");
            }
        }
        ui.close();
    }

    /**
     * Starts the Luna application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Luna().run();
    }

    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException | LunaException e) {
            ui.showLoadingError();
            return new TaskList();
        }
    }
}
