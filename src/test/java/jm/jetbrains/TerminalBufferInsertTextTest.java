package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferInsertTextTest {

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
    void testInsertTextWithLinePropagation() {
        String initialText = "abcd1234efgh5678";
        buffer.writeText(initialText);

        buffer.setCursorPosition(1, 1);

        buffer.insertText("XX");

        // We expect the text to shift and create a new line
        String expectedContents = "1XX234efgh5678  ";

        String contentsAfterFirstWrite = buffer.getScreenContents();
        assertEquals(expectedContents, contentsAfterFirstWrite);

        // Assert Cursor is at the very end of the inserted text
        assertEquals(1, buffer.getCursorRow());
        assertEquals(3, buffer.getCursorCol());
    }

}
