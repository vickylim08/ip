import java.io.IOException;

/**
 * Represents an executable user command.
 */
public abstract class Command {
    /**
     * Executes this command against the current task list.
     *
     * @param tasks Current tasks in memory.
     * @param ui User interface for showing results.
     * @param storage Storage used to persist task changes.
     * @throws LunaException If the command cannot be executed.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws LunaException;

    /**
     * Returns whether this command should terminate the application.
     *
     * @return {@code true} if the application should exit.
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves the current task list to storage.
     *
     * @param storage Storage used to persist tasks.
     * @param tasks Tasks to save.
     * @throws LunaException If saving fails.
     */
    protected void saveTasks(Storage storage, TaskList tasks) throws LunaException {
        try {
            storage.saveTasks(tasks);
        } catch (IOException e) {
            throw new LunaException("I could not save your tasks to disk.");
        }
    }
}
