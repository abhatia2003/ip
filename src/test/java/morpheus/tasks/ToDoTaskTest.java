package morpheus.tasks;

import morpheus.utils.CustomDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTaskTest {
    @Test
    public void encodeTest1() {
        Task toDo = new ToDoTask("Meet John");
        assertEquals("T | 0 | Meet John", toDo.encode());
    }

    @Test
    public void encodeTest2() {
        Task toDo = new ToDoTask("Go to the Gym", true);
        assertEquals("T | 1 | Go to the Gym", toDo.encode());
    }

    @Test
    public void toStringTest1() {
        Task toDo = new ToDoTask("Meet Jane");
        assertEquals("[T] [ ] Meet Jane", toDo.toString());
    }

    @Test
    public void toStringTest2() {
        Task toDo = new ToDoTask("Visit the Zoo", true);
        assertEquals("[T] [X] Visit the Zoo", toDo.toString());
    }
}
