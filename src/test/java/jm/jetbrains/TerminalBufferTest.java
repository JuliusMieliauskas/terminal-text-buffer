package jm.jetbrains;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalBufferTest {

    @Test
    void testConstructor() {
        // Remove in the future
        TerminalBuffer buffer = new TerminalBuffer(80, 24, 1000);
        assertNotNull(buffer);

        assertEquals(0, buffer.getCursorCol());
        assertEquals(0, buffer.getCursorRow());
    }
}