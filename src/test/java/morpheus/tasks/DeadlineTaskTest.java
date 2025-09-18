package morpheus.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import morpheus.utils.CustomDateTime;

public class DeadlineTaskTest {

    @Test
    public void encodeTest1() {
        Task deadline = new DeadlineTask("Submit Project", true, new CustomDateTime("28/8/2025 1800"));
        assertEquals("D | 1 | Submit Project | 28 Aug 2025, 6:00 PM", deadline.encode());
    }

    @Test
    public void encodeTest2() {
        Task deadline = new DeadlineTask("Hand in Homework", new CustomDateTime("20/9/2025 11am"));
        assertEquals("D | 0 | Hand in Homework | 20 Sep 2025, 11:00 AM", deadline.encode());
    }

    @Test
    public void toStringTest1() {
        Task deadline = new DeadlineTask("Submit Project", true, new CustomDateTime("28/8/2025 18:00"));
        assertEquals("[D] [X] Submit Project (by: 28 Aug 2025, 6:00 PM)", deadline.toString());
    }

    @Test
    public void toStringTest2() {
        Task deadline = new DeadlineTask("Hand in Homework", new CustomDateTime("20/9/2025 11:00 am"));
        assertEquals("[D] [ ] Hand in Homework (by: 20 Sep 2025, 11:00 AM)", deadline.toString());
    }

    @Test
    public void emptyDescriptionTest() {
        Task deadline = new DeadlineTask("", new CustomDateTime("20/9/2025 11:00 am"));
        assertEquals("[D] [ ]  (by: 20 Sep 2025, 11:00 AM)", deadline.toString());
    }

    @Test
    public void invalidDateFormatTest() {
        Exception exception = assertThrows(Exception.class, () -> {
            new DeadlineTask("Test", new CustomDateTime("invalid-date"));
        });
        assertTrue(exception.getMessage().toLowerCase().contains("invalid"));
    }
}
