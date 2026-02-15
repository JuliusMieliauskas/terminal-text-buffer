package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferClearTest {

    private TerminalBuffer buffer;
    private final int WIDTH = 2;
    private final int HEIGHT = 2;
    private final int MAX_SCROLLBACK = 10;

    @BeforeEach
    void setUp() {
        // initialize a fresh buffer for every test
        buffer = new TerminalBuffer(WIDTH, HEIGHT, MAX_SCROLLBACK);
    }

    @Test
    void testClearScreenOnly() {
        String initialText = "ab12cd34";
        buffer.writeText(initialText);

        buffer.clearScreen();

        String expectedContents = "ab12";
        assertEquals(expectedContents, buffer.getScrollbackAndScreenContents());
    }

    @Test
    void clearEverythingTest() {
        String initialText = "ab12cd34";
        buffer.writeText(initialText);

        buffer.clearScreenAndScrollback();

        String expectedContents = "    ";
        assertEquals(expectedContents, buffer.getScrollbackAndScreenContents());
    }
}
