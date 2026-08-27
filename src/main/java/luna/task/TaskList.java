package luna.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents the in-memory list of tasks managed by Luna.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from an existing collection of tasks.
     *
     * @param tasks Existing tasks to copy into this task list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the given zero-based index.
     *
     * @param index Zero-based position of the task.
     * @return Task at the given index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param index Zero-based position of the task.
     * @return Removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given zero-based index as done.
     *
     * @param index Zero-based position of the task.
     * @return Updated task.
     */
    public Task mark(int index) {
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the given zero-based index as not done.
     *
     * @param index Zero-based position of the task.
     * @return Updated task.
     */
    public Task unmark(int index) {
        Task task = tasks.get(index);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a copy of the current tasks for read-only style access.
     *
     * @return Copy of the task list contents.
     */
    public List<Task> asList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Returns the tasks whose descriptions contain the given keyword.
     *
     * @param keyword Keyword to search for in task descriptions.
     * @return Matching tasks in their original order.
     */
    public List<Task> findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase(Locale.ENGLISH);

        for (Task task : tasks) {
            String normalizedDescription = task.getDescription().toLowerCase(Locale.ENGLISH);
            if (normalizedDescription.contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }

        return matchingTasks;
    }
}
