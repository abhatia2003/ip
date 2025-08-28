package morpheus.tasks;

import morpheus.utils.CustomDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTaskTest {
    @Test
    public void encodeTest1() {
        Task event = new EventTask("Participate in Competition", true, new CustomDateTime("28/8/2025 1800"), new CustomDateTime("30/8/2025 11:00"));
        assertEquals("E | 1 | Participate in Competition | 28 Aug 2025, 6:00 PM | 30 Aug 2025, 11:00 AM", event.encode());
    }

    @Test
    public void encodeTest2() {
        Task event = new EventTask("Work on Journal Entry", new CustomDateTime("12/9/2025 11am"), new CustomDateTime("14/9/2025 2359"));
        assertEquals("E | 0 | Work on Journal Entry | 12 Sep 2025, 11:00 AM | 14 Sep 2025, 11:59 PM", event.encode());
    }

    @Test
    public void toStringTest1() {
        Task event = new EventTask("Participate in Competition", true, new CustomDateTime("28/8/2025 18:00"),  new CustomDateTime("30/8/2025 11:00"));
        assertEquals("[E] [X] Participate in Competition (from: 28 Aug 2025, 6:00 PM to: 30 Aug 2025, 11:00 AM)", event.toString());
    }

    @Test
    public void toStringTest2() {
        Task event = new EventTask("Hand in Homework", new CustomDateTime("12/9/2025 11:00 am"), new CustomDateTime("14/9/2025 23:59"));
        assertEquals("[E] [ ] Hand in Homework (from: 12 Sep 2025, 11:00 AM to: 14 Sep 2025, 11:59 PM)", event.toString());
    }
}
