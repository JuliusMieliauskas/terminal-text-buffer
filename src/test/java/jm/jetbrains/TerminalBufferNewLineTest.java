package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferNewLineTest {

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
    void testNewLineInsert() {
        // 1. Insert new line at the end
        // 2. Check that th contents were shifted up

        String initialText = "123456789";
        buffer.writeText(initialText);

        buffer.insertEmptyLineAtBottom();

        String contentsAfterWrite = buffer.getScreenContents();
        String expectedContents = "456789   ";
        assertEquals(expectedContents, contentsAfterWrite);
    }

}
