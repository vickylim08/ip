import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs Luna, a simple command-line task manager chatbot.
 */
public class Luna {
    private static final DateTimeFormatter EVENT_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm");
    private static final String DIVIDER_LINE = "   _________________________________________________________________\n";
    private static final String WELCOME_BANNER = DIVIDER_LINE
            + "   Hello! I'm Luna.\n"
            + "   What can I do for you?\n"
            + DIVIDER_LINE;
    private static final String AVAILABLE_COMMANDS = "Available Commands:\n"
            + "> todo <desc>: Adds a simple task with no date/time attached\n"
            + "> deadline <desc> /by <date/time>: Adds a task that must be done before a specific time\n"
            + "> event <desc> /from <start> /to <end>: Adds a task that spans across a specific time\n"
            + "> list: displays all currently saved items with index numbers\n"
            + "> mark <index>: Marks the task at the specified index number as completed ([X]).\n"
            + "> unmark <index>: Marks the task at the specified index number as not done ([ ]).\n"
            + "> delete <index>: Removes the task at the specified index number from the list.\n"
            + "> bye: Exits the program\n";
    private static final String EXIT_MESSAGE = DIVIDER_LINE
            + "       Bye. Hope to see you again soon!\n"
            + DIVIDER_LINE;

    /**
     * Starts the Luna application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        System.out.println(WELCOME_BANNER);
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage();
        List<Task> tasks = loadTasks(storage);

        System.out.println(AVAILABLE_COMMANDS);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }
            try {
                Command command = getCommand(input);
                if (command == Command.BYE) {
                    System.out.println(EXIT_MESSAGE);
                    break;
                }
                processCommand(command, input, tasks, storage);
            } catch (LunaException e) {
                printError(e.getMessage());
            } catch (NumberFormatException e) {
                printError("Please provide a valid integer for task number.");
            } catch (IndexOutOfBoundsException e) {
                printError("That task number does not exist in your list.");
            }
        }
        scanner.close();
    }

    private static List<Task> loadTasks(Storage storage) {
        try {
            return storage.loadTasks();
        } catch (IOException | LunaException e) {
            printError("I could not load your saved tasks. Starting with an empty list.");
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

    private static void processCommand(Command command, String input, List<Task> tasks, Storage storage)
            throws LunaException {
        switch (command) {
        case LIST:
            System.out.print(DIVIDER_LINE);
            System.out.print("      Here are the tasks in your list\n");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("      " + (i + 1) + ". " + tasks.get(i));
            }
            System.out.println(DIVIDER_LINE);
            break;
        case MARK:
            if (input.equalsIgnoreCase("mark")) {
                throw new LunaException("Please provide a task number to mark as done.");
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5).trim()) - 1;
                Task task = tasks.get(index); // throws IndexOutOfBoundsException if invalid
                task.markAsDone();
                saveTasks(storage, tasks);
                System.out.print(DIVIDER_LINE);
                System.out.print("      Nice! I've marked this as done:\n");
                System.out.println("      " + task);
                System.out.print(DIVIDER_LINE);
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
                System.out.print(DIVIDER_LINE);
                System.out.print("      OK, I've marked this task as not done yet:\n");
                System.out.println("      " + task);
                System.out.print(DIVIDER_LINE);
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
                System.out.print(DIVIDER_LINE);
                System.out.print("      Got it. I've added this task:\n");
                System.out.println("          " + task);
                System.out.print("      Now you have " + tasks.size() + " tasks in the list.\n");
                System.out.println(DIVIDER_LINE);
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

                    System.out.print(DIVIDER_LINE);
                    System.out.print("      Got it. I've added this task:\n");
                    System.out.println("          " + deadline);
                    System.out.print("      Now you have " + tasks.size() + " tasks in the list.\n");
                    System.out.println(DIVIDER_LINE);
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

                    System.out.print(DIVIDER_LINE);
                    System.out.print("      Got it. I've added this task:\n");
                    System.out.println("          " + event);
                    System.out.println("      Now you have "
                            + tasks.size() + " tasks in the list.");
                    System.out.println(DIVIDER_LINE);
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
                System.out.print(DIVIDER_LINE);
                System.out.print("      Noted. I've removed this task:\n");
                System.out.println("          " + task);
                System.out.print("      Now you have " + tasks.size() + " tasks in the list.\n");
                System.out.println(DIVIDER_LINE);
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

    private static void printError(String message) {
        System.out.print(DIVIDER_LINE);
        System.out.println("        Oh no! " + message);
        System.out.print(DIVIDER_LINE);
    }
}
