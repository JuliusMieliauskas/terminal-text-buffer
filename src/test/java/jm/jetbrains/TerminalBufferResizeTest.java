package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferResizeTest {

    private TerminalBuffer buffer;
    private final int WIDTH = 3;
    private final int HEIGHT = 3;
    private final int MAX_SCROLLBACK = 10;

    @BeforeEach
    void setUp() {
        // initialize a fresh buffer for every test
        buffer = new TerminalBuffer(WIDTH, HEIGHT, MAX_SCROLLBACK);
    }

    @Test
    void testColumnShrinkResize() {
        String initialText = "ab12cd3";
        buffer.writeText(initialText);

        // Shrink the buffer width to 2
        buffer.setMaxColumns(2);

        // After shrinkage the buffer should be full with latest text, and any overflow should be moved to scrollback
        String screenContents = buffer.getScreenContents();
        String scrollbackContents = buffer.getScrollbackContents().trim();

        assertEquals("b12cd3", screenContents);
        assertEquals("a", scrollbackContents);
    }

    // TODO: Implement more sophisticated tests when scrollback size is full and similar
}
