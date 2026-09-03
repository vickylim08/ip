package luna.ui;

import java.util.List;
import java.util.Scanner;

import luna.task.Task;
import luna.task.TaskList;

/**
 * Handles all command-line interactions with the user.
 */
public class Ui {
    private static final String DIVIDER_LINE = "   _________________________________________________________________";
    private static final String WELCOME_TEXT = "Hello! I'm Luna.\nWhat can I do for you?";
    private static final String AVAILABLE_COMMANDS = "Available commands:\n"
            + "> todo <desc>: Adds a todo task with the given description\n"
            + "> deadline <desc> /by <yyyy-MM-dd>: Adds a deadline task with the given due date\n"
            + "> event <desc> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>: Adds a task that spans across a specific time\n"
            + "> list: Displays all currently saved items with index numbers\n"
            + "> find <keyword>: Displays tasks whose descriptions contain the keyword\n"
            + "> mark <index>: Marks the task at the specified index number as completed ([X]).\n"
            + "> unmark <index>: Marks the task at the specified index number as not done ([ ]).\n"
            + "> delete <index>: Removes the task at the specified index number from the list.\n"
            + "> bye: Exits the program";
    private static final String EXIT_TEXT = "Bye. Hope to see you again soon!";
    private final Scanner scanner;
    private final boolean shouldPrintToConsole;
    private String latestResponse;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this(true);
    }

    /**
     * Creates a UI that can optionally print responses to the console.
     *
     * @param shouldPrintToConsole Whether responses should be printed immediately.
     */
    public Ui(boolean shouldPrintToConsole) {
        this.shouldPrintToConsole = shouldPrintToConsole;
        this.scanner = shouldPrintToConsole ? new Scanner(System.in) : null;
        this.latestResponse = "";
    }

    /**
     * Shows the welcome banner and available command list.
     */
    public void showWelcome() {
        showMessage(getWelcomeMessage());
    }

    /**
     * Returns the welcome banner and available command list as one string.
     *
     * @return Welcome message shown when Luna starts.
     */
    public String getWelcomeMessage() {
        if (shouldPrintToConsole) {
            return DIVIDER_LINE + '\n'
                    + "   " + WELCOME_TEXT.replace("\n", "\n   ") + '\n'
                    + DIVIDER_LINE + '\n'
                    + AVAILABLE_COMMANDS + '\n';
        }

        return WELCOME_TEXT + "\n\n" + AVAILABLE_COMMANDS;
    }

    /**
     * Reads one line of user input.
     *
     * @return User input with leading and trailing whitespace removed.
     */
    public String readCommand() {
        if (scanner == null) {
            throw new IllegalStateException("This UI is not configured for command-line input.");
        }

        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    /**
     * Shows the exit message.
     */
    public void showExit() {
        showMessage(formatResponse("Bye. Hope to see you again soon!"));
    }

    /**
     * Shows the current list of tasks.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        StringBuilder message = new StringBuilder();
        message.append("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            message.append('\n').append(i + 1).append(". ").append(tasks.get(i));
        }
        showMessage(formatResponse(message.toString()));
    }

    /**
     * Shows the tasks whose descriptions match a search keyword.
     *
     * @param matchingTasks Tasks that matched the search keyword.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        StringBuilder message = new StringBuilder();
        message.append("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            message.append('\n').append(i + 1).append(". ").append(matchingTasks.get(i));
        }
        showMessage(formatResponse(message.toString()));
    }

    /**
     * Shows a confirmation for a task that was marked as done.
     *
     * @param task Task that was marked.
     */
    public void showMarkSuccess(Task task) {
        showMessage(formatResponse("Nice! I've marked this as done:\n" + task));
    }

    /**
     * Shows a confirmation for a task that was marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showUnmarkSuccess(Task task) {
        showMessage(formatResponse("OK, I've marked this task as not done yet:\n" + task));
    }

    /**
     * Shows a confirmation for a task that was added.
     *
     * @param task Added task.
     * @param taskCount Current number of tasks in the list.
     */
    public void showAddSuccess(Task task, int taskCount) {
        showMessage(formatResponse("Got it. I've added this task:\n"
                + task + '\n'
                + "Now you have " + taskCount + " tasks in the list."));
    }

    /**
     * Shows a confirmation for a task that was deleted.
     *
     * @param task Removed task.
     * @param taskCount Current number of tasks remaining in the list.
     */
    public void showDeleteSuccess(Task task, int taskCount) {
        showMessage(formatResponse("Noted. I've removed this task:\n"
                + task + '\n'
                + "Now you have " + taskCount + " tasks in the list."));
    }

    /**
     * Shows an error message to the user.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        showMessage(formatResponse("Oh no! " + message));
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
        if (scanner != null) {
            scanner.close();
        }
    }

    /**
     * Returns the most recent response and clears it from the buffer.
     *
     * @return Latest response text, or an empty string if none exists.
     */
    public String consumeLatestResponse() {
        String response = latestResponse;
        latestResponse = "";
        return response;
    }

    /**
     * Stores a response and optionally prints it to the console.
     *
     * @param message Response to show.
     */
    private void showMessage(String message) {
        latestResponse = message;
        if (shouldPrintToConsole) {
            System.out.print(message);
        }
    }

    /**
     * Formats a response for the active presentation mode.
     *
     * @param content Message content without CLI-specific decoration.
     * @return Formatted response text.
     */
    private String formatResponse(String content) {
        if (!shouldPrintToConsole) {
            return content;
        }

        String indentedContent = content.replace("\n", "\n      ");
        return DIVIDER_LINE + '\n'
                + "      " + indentedContent + '\n'
                + DIVIDER_LINE + '\n';
    }
}
