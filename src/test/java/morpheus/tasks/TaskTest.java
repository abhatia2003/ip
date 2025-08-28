package morpheus.tasks;

import morpheus.utils.CustomDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    @Test
    public void getStatusIconTest1() {
        Task toDo = new ToDoTask("Exercise", true);
        assertEquals("X", toDo.getStatusIcon());
    }

    @Test
    public void getStatusIconTest2() {
        Task toDo = new ToDoTask("Laundry");
        assertEquals(" ", toDo.getStatusIcon());
    }

    @Test
    public void markTest() {
        Task deadline = new DeadlineTask("Assignment", new CustomDateTime("28/8/2025 1300"));
        deadline.mark();
        assertEquals("X", deadline.getStatusIcon());
    }

    @Test
    public void unmarkTest() {
        Task deadline = new DeadlineTask("Assignment", true, new CustomDateTime("28/8/2025 1300"));
        deadline.unmark();
        assertEquals(" ", deadline.getStatusIcon());
    }

    @Test
    public void cleanTest1() {
        String input = " hello ";
        assertEquals("hello", Task.clean(input));
    }

    @Test
    public void cleanTest2() {
        String input = "Sleep\n\n/by\r24/4/2025 10pm\n";
        assertEquals("Sleep  /by 24/4/2025 10pm", Task.clean(input));
    }

    @Test
    public void cleanTest3() {
        String input = "Meet friends\n/from 28/8/2025 1pm\r\n/to 28/8/2025 4pm\n";
        assertEquals("Meet friends /from 28/8/2025 1pm  /to 28/8/2025 4pm", Task.clean(input));
    }
}
