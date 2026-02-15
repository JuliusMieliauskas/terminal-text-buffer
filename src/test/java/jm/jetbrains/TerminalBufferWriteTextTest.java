package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferWriteTextTest {

    private TerminalBuffer buffer;
    private final int WIDTH = 4;
    private final int HEIGHT = 4;
    private final int MAX_SCROLLBACK = 10;

    @BeforeEach
    void setUp() {
        // initialize a fresh buffer for every test
        buffer = new TerminalBuffer(WIDTH, HEIGHT, MAX_SCROLLBACK);
    }

    @Test
    void testWriteFullScreenContents() {
        String initialText = "abcd1234efgh5678";
        buffer.writeText(initialText);

        String contentsAfterFirstWrite = buffer.getScreenContents();
        String expectedContents = "abcd1234efgh5678";
        assertEquals(expectedContents, contentsAfterFirstWrite);
    }

    @Test
    void testCorrectTextWrite() {
        // 1. Move cursor down one line, and right one column
        // 2. Write some text
        // 3. Assert contents
        // 4. Move cursor back
        // 5, Overwrite contents
        // 6. Assert again
        buffer.moveCursor(Direction.DOWN, 1);
        buffer.moveCursor(Direction.RIGHT, 1);

        String initialText = "abcd";
        buffer.writeText(initialText);

        String contentsAfterFirstWrite = buffer.getScreenContents();
        String expectedContents = "     abcd       ";
        assertEquals(expectedContents, contentsAfterFirstWrite);

        buffer.setCursorPosition(1, 3);
        buffer.writeText("ef");

        String contentsAfterSecondWrite = buffer.getScreenContents();
        expectedContents = "     abef       ";
        assertEquals(expectedContents, contentsAfterSecondWrite);
    }

    @Test
    void testWriteTextWhenScreenFull() {
        // 1. Move cursor down three lines, and right three columns
        // 2. Write some text which would trigger the new line
        // 3. Assert contents, we expect the excess text to be on new line
        buffer.moveCursor(Direction.DOWN, 3);
        buffer.moveCursor(Direction.RIGHT, 3);

        String initialText = "abcd";
        buffer.writeText(initialText);

        // Expected Contents
        // Line 1 |
        // Line 2 |
        // Line 3 |    a
        // Line 4 | bcd

        String contentsAfterFirstWrite = buffer.getScreenContents();
        String expectedContents = "           abcd ";
        assertEquals(expectedContents, contentsAfterFirstWrite);
    }

}
