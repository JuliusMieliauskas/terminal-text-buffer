package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalBufferCursorTest {

    private TerminalBuffer buffer;
    private final int WIDTH = 80;
    private final int HEIGHT = 24;
    private final int MAX_SCROLLBACK = 1000;

    @BeforeEach
    void setUp() {
        // initialize a fresh buffer for every test
        buffer = new TerminalBuffer(WIDTH, HEIGHT, MAX_SCROLLBACK);
    }

    @Test
    void testMoveUpClampsToTop() {
        // place cursor a few rows down, attempt to move past top
        buffer.setCursorPosition(10, 5);
        buffer.moveCursor(Direction.UP, 10); // would go to -5, should clamp to 0
        assertEquals(0, buffer.getCursorRow());
        assertEquals(10, buffer.getCursorCol()); // col unchanged
    }

    @Test
    void testMoveLeftClampsToZero() {
        buffer.setCursorPosition(5, 10);
        buffer.moveCursor(Direction.LEFT, 10); // would go to -5, should clamp to 0
        assertEquals(0, buffer.getCursorCol());
        assertEquals(10, buffer.getCursorRow()); // row unchanged
    }

    @Test
    void testMoveRightClampsToWidthMinusOne() {
        buffer.setCursorPosition(70, 10);
        buffer.moveCursor(Direction.RIGHT, 20); // would go beyond width, should clamp to WIDTH-1
        assertEquals(WIDTH - 1, buffer.getCursorCol());
        assertEquals(10, buffer.getCursorRow());
    }

    @Test
    void testMoveDownClampsToHeightMinusOne() {
        buffer.setCursorPosition(10, 20);
        buffer.moveCursor(Direction.DOWN, 10); // would go beyond height, should clamp to HEIGHT-1
        assertEquals(HEIGHT - 1, buffer.getCursorRow());
        assertEquals(10, buffer.getCursorCol());
    }

    @Test
    void testMoveWithinBounds() {
        buffer.setCursorPosition(10, 10);
        buffer.moveCursor(Direction.RIGHT, 5);
        buffer.moveCursor(Direction.DOWN, 3);
        assertEquals(15, buffer.getCursorCol());
        assertEquals(13, buffer.getCursorRow());
    }
}

