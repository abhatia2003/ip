package morpheus;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MorpheusTest {
    @Test
    public void testRun() {

    }

    @Test
    public void testWelcomeMessage() {
        Morpheus m = new Morpheus("data/morpheus.txt");
        String welcome = m.getWelcomeMessage();
        assertNotNull(welcome);
        assertFalse(welcome.isEmpty());
    }

    @Test
    public void testExitCommand() {
        Morpheus m = new Morpheus("data/morpheus.txt");
        String response = m.getResponse("bye");
        assertTrue(response.equalsIgnoreCase("END PROGRAM") || response.toLowerCase().contains("bye"));
    }

    @Test
    public void testInvalidInput() {
        Morpheus m = new Morpheus("data/morpheus.txt");
        String response = m.getResponse("invalidcommand");
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    public void testEmptyInput() {
        Morpheus m = new Morpheus("data/morpheus.txt");
        String response = m.getResponse("");
        assertTrue(!response.isEmpty());
    }
}
