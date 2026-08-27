import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs Luna, a simple command-line task manager chatbot.
 */
public class Luna {
    /**
     * Starts the Luna application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();
        List<Task> tasks = loadTasks(storage, ui);

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

    private static List<Task> loadTasks(Storage storage, Ui ui) {
        try {
            return storage.loadTasks();
        } catch (IOException | LunaException e) {
            ui.showLoadingError();
            return new ArrayList<>();
        }
    }
}
