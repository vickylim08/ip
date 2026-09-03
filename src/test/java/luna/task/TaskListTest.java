package luna.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link TaskList#mark(int)} method.
 */
public class TaskListTest {
    @Test
    public void mark_taskAtValidIndex_marksTaskDoneAndReturnsSameTask() {
        Todo todo = new Todo("submit quiz");
        TaskList taskList = new TaskList(List.of(todo));

        Task markedTask = taskList.mark(0);

        assertSame(todo, markedTask);
        assertTrue(markedTask.isDone());
        assertEquals("[X]", markedTask.getStatusIcon());
    }

    @Test
    public void mark_taskAlreadyDone_keepsTaskDoneAndReturnsSameTask() {
        Todo todo = new Todo("submit quiz");
        todo.markAsDone();
        TaskList taskList = new TaskList(List.of(todo));

        Task markedTask = taskList.mark(0);

        assertSame(todo, markedTask);
        assertTrue(markedTask.isDone());
        assertEquals("[X]", markedTask.getStatusIcon());
    }

    @Test
    public void mark_negativeIndex_throwsIndexOutOfBoundsException() {
        TaskList taskList = new TaskList(List.of(new Todo("submit quiz")));

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.mark(-1));
    }

    @Test
    public void mark_indexEqualToSize_throwsIndexOutOfBoundsException() {
        TaskList taskList = new TaskList(List.of(new Todo("submit quiz")));

        assertThrows(IndexOutOfBoundsException.class, () -> taskList.mark(1));
    }

    @Test
    public void findTasks_keywordMatchesMultipleTasks_returnsMatchingTasksInOrder() {
        Todo firstMatch = new Todo("read book");
        Todo nonMatch = new Todo("submit quiz");
        Todo secondMatch = new Todo("return book");
        TaskList taskList = new TaskList(List.of(firstMatch, nonMatch, secondMatch));

        List<Task> matchingTasks = taskList.findTasks("book");

        assertIterableEquals(List.of(firstMatch, secondMatch), matchingTasks);
    }

    @Test
    public void findTasks_keywordUsesCaseInsensitiveMatching_returnsMatchingTasks() {
        Todo match = new Todo("Read Book");
        Todo nonMatch = new Todo("write summary");
        TaskList taskList = new TaskList(List.of(match, nonMatch));

        List<Task> matchingTasks = taskList.findTasks("book");

        assertIterableEquals(List.of(match), matchingTasks);
    }

    @Test
    public void findTasks_keywordMatchesNoTasks_returnsEmptyList() {
        TaskList taskList = new TaskList(List.of(new Todo("read book"), new Todo("submit quiz")));

        List<Task> matchingTasks = taskList.findTasks("meeting");

        assertTrue(matchingTasks.isEmpty());
    }
}
