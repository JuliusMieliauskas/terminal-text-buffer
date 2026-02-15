package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferScrollbackTest {

    private TerminalBuffer buffer;
    private final int WIDTH = 2;
    private final int HEIGHT = 2;
    private final int MAX_SCROLLBACK = 2;

    @BeforeEach
    void setUp() {
        // initialize a fresh buffer for every test
        buffer = new TerminalBuffer(WIDTH, HEIGHT, MAX_SCROLLBACK);
    }

    @Test
    void testSimpleScrollbackText() {
        String initialText = "ab12cd34";
        buffer.writeText(initialText);

        String expectedScreenContents = "cd34";

        String screenContents = buffer.getScreenContents();
        assertEquals(expectedScreenContents, screenContents);

        String mostRecentScrollbackLine = buffer.getLineContents(-1);
        String leastRecentScrollbackLine = buffer.getLineContents(-2);
        assertEquals("12", mostRecentScrollbackLine);
        assertEquals("ab", leastRecentScrollbackLine);
    }
}
