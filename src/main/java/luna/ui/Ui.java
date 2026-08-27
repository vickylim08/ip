package luna.ui;

import java.util.Scanner;

import luna.task.Task;
import luna.task.TaskList;

/**
 * Handles all command-line interactions with the user.
 */
public class Ui {
    private static final String DIVIDER_LINE = "   _________________________________________________________________\n";
    private static final String WELCOME_BANNER = DIVIDER_LINE
            + "   Hello! I'm Luna.\n"
            + "   What can I do for you?\n"
            + DIVIDER_LINE;
    private static final String AVAILABLE_COMMANDS = "Available Commands:\n"
            + "> todo <desc>: Adds a todo task with the given description\n"
            + "> deadline <desc> /by <yyyy-MM-dd>: Adds a deadline task with the given due date\n"
            + "> event <desc> /from <start> /to <end>: Adds a task that spans across a specific time\n"
            + "> list: displays all currently saved items with index numbers\n"
            + "> mark <index>: Marks the task at the specified index number as completed ([X]).\n"
            + "> unmark <index>: Marks the task at the specified index number as not done ([ ]).\n"
            + "> delete <index>: Removes the task at the specified index number from the list.\n"
            + "> bye: Exits the program\n";
    private static final String EXIT_MESSAGE = DIVIDER_LINE
            + "       Bye. Hope to see you again soon!\n"
            + DIVIDER_LINE;
    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the welcome banner and available command list.
     */
    public void showWelcome() {
        System.out.println(WELCOME_BANNER);
        System.out.println(AVAILABLE_COMMANDS);
    }

    /**
     * Reads one line of user input.
     *
     * @return User input with leading and trailing whitespace removed.
     */
    public String readCommand() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    /**
     * Shows the exit message.
     */
    public void showExit() {
        System.out.println(EXIT_MESSAGE);
    }

    /**
     * Shows the current list of tasks.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.print(DIVIDER_LINE);
        System.out.print("      Here are the tasks in your list\n");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("      " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(DIVIDER_LINE);
    }

    /**
     * Shows a confirmation for a task that was marked as done.
     *
     * @param task Task that was marked.
     */
    public void showMarkSuccess(Task task) {
        System.out.print(DIVIDER_LINE);
        System.out.print("      Nice! I've marked this as done:\n");
        System.out.println("      " + task);
        System.out.print(DIVIDER_LINE);
    }

    /**
     * Shows a confirmation for a task that was marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showUnmarkSuccess(Task task) {
        System.out.print(DIVIDER_LINE);
        System.out.print("      OK, I've marked this task as not done yet:\n");
        System.out.println("      " + task);
        System.out.print(DIVIDER_LINE);
    }

    /**
     * Shows a confirmation for a task that was added.
     *
     * @param task Added task.
     * @param taskCount Current number of tasks in the list.
     */
    public void showAddSuccess(Task task, int taskCount) {
        System.out.print(DIVIDER_LINE);
        System.out.print("      Got it. I've added this task:\n");
        System.out.println("          " + task);
        System.out.print("      Now you have " + taskCount + " tasks in the list.\n");
        System.out.println(DIVIDER_LINE);
    }

    /**
     * Shows a confirmation for a task that was deleted.
     *
     * @param task Removed task.
     * @param taskCount Current number of tasks remaining in the list.
     */
    public void showDeleteSuccess(Task task, int taskCount) {
        System.out.print(DIVIDER_LINE);
        System.out.print("      Noted. I've removed this task:\n");
        System.out.println("          " + task);
        System.out.print("      Now you have " + taskCount + " tasks in the list.\n");
        System.out.println(DIVIDER_LINE);
    }

    /**
     * Shows an error message to the user.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.print(DIVIDER_LINE);
        System.out.println("        Oh no! " + message);
        System.out.print(DIVIDER_LINE);
    }

    /**
     * Shows a loading error when the saved task file cannot be read.
     */
    public void showLoadingError() {
        showError("I could not load your saved tasks. Starting with an empty list.");
    }

    /**
     * Closes the UI input resource.
     */
    public void close() {
        scanner.close();
    }
}
