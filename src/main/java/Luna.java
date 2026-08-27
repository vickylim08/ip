import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs Luna, a simple command-line task manager chatbot.
 */
public class Luna {
    private static final DateTimeFormatter EVENT_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm");

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
                Command command = getCommand(input);
                if (command == Command.BYE) {
                    ui.showExit();
                    break;
                }
                processCommand(command, input, tasks, storage, ui);
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

    private static Command getCommand(String input) throws LunaException {
        String commandWord = input.split(" ", 2)[0];
        try {
            return Command.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LunaException("I don't know this command :(");
        }
    }

    private static void processCommand(Command command, String input, List<Task> tasks,
                                       Storage storage, Ui ui) throws LunaException {
        switch (command) {
        case LIST:
            ui.showTaskList(tasks);
            break;
        case MARK:
            if (input.equalsIgnoreCase("mark")) {
                throw new LunaException("Please provide a task number to mark as done.");
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                Task task = tasks.get(index); // throws IndexOutOfBoundsException if invalid
                task.markAsDone();
                saveTasks(storage, tasks);
                ui.showMarkSuccess(task);
            }
            break;
        case UNMARK:
            if (input.equalsIgnoreCase("unmark")) {
                throw new LunaException("Please provide a task number to mark as done.");
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                Task task = tasks.get(index);
                task.markAsNotDone();
                saveTasks(storage, tasks);
                ui.showUnmarkSuccess(task);
            }
            break;
        case TODO:
            if (input.equalsIgnoreCase("todo")) {
                throw new LunaException("Please provide the description for the todo task.");
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5).trim();
                Task task = new Todo(description);
                if (description.isEmpty()) {
                    throw new LunaException("The description of a todo cannot be empty.");
                }
                tasks.add(task);
                saveTasks(storage, tasks);
                ui.showAddSuccess(task, tasks.size());
            }
            break;
        case DEADLINE:
            if (input.equalsIgnoreCase("deadline")) {
                throw new LunaException("Please provide the description and the deadline.");
            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ", 2);
                if (parts.length < 2) {
                    throw new LunaException("Please provide the description and the deadline.");
                }

                String description = parts[0].trim();
                String dateText = parts[1].trim();

                try {
                    LocalDate date = LocalDate.parse(dateText);
                    Deadline deadline = new Deadline(description, date);

                    tasks.add(deadline);
                    saveTasks(storage, tasks);
                    ui.showAddSuccess(deadline, tasks.size());
                } catch (DateTimeParseException e) {
                    throw new LunaException(
                            "Please use the date format yyyy-MM-dd.");
                }
            }
            break;
        case EVENT:
            if (input.equalsIgnoreCase("event")) {
                throw new LunaException("Please provide the description, start, and end time.");
            } else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split(" /from | /to ");
                if (parts.length < 3) {
                    throw new LunaException("Please provide the description, start, and end time.");
                }

                try {
                    String description = parts[0].trim();

                    LocalDateTime from = LocalDateTime.parse(
                            parts[1].trim(), EVENT_INPUT_FORMAT);

                    LocalDateTime to = LocalDateTime.parse(
                            parts[2].trim(), EVENT_INPUT_FORMAT);

                    Event event = new Event(description, from, to);

                    tasks.add(event);
                    saveTasks(storage, tasks);
                    ui.showAddSuccess(event, tasks.size());
                } catch (DateTimeParseException e) {
                    throw new LunaException(
                            "Please use the format yyyy-MM-dd HHmm.");
                }
            }
            break;
        case DELETE:
            if (input.equalsIgnoreCase("delete")) {
                throw new LunaException("Please provide a task number to delete.");
            } else if (input.startsWith("delete ")) {
                int index = Integer.parseInt(input.substring(7).trim()) - 1;
                Task task = tasks.get(index);
                tasks.remove(task);
                saveTasks(storage, tasks);
                ui.showDeleteSuccess(task, tasks.size());
            }
            break;
        case BYE:
            break;
        }
    }

    private static void saveTasks(Storage storage, List<Task> tasks) throws LunaException {
        try {
            storage.saveTasks(tasks);
        } catch (IOException e) {
            throw new LunaException("I could not save your tasks to disk.");
        }
    }
}
