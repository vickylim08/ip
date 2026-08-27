package luna.task;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
