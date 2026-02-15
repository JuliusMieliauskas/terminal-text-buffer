package jm.jetbrains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminalBufferAttributeTest {

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
    void testSimpleAttributeChange() {
        String initialText = "abc";
        buffer.setCurrentAttributes(TerminalColor.BRIGHT_BLACK, TerminalColor.BRIGHT_BLACK, Set.of(TextStyle.UNDERLINE));
        buffer.writeText(initialText);

        buffer.setCurrentAttributes(TerminalColor.BRIGHT_CYAN, TerminalColor.BRIGHT_CYAN, Set.of(TextStyle.BOLD));
        buffer.writeText("def");

        // Verify the first character has correct attributes
        jm.jetbrains.TextAttributes textAttributes = buffer.getCellAttributes(0, 0);
        assertEquals(TerminalColor.BRIGHT_BLACK, textAttributes.foreground());
        assertEquals(TerminalColor.BRIGHT_BLACK, textAttributes.background());
        assertEquals(Set.of(TextStyle.UNDERLINE), textAttributes.styles());

        // Verify the second character has correct attributes
        textAttributes = buffer.getCellAttributes(1, 0);
        assertEquals(TerminalColor.BRIGHT_CYAN, textAttributes.foreground());
        assertEquals(TerminalColor.BRIGHT_CYAN, textAttributes.background());
        assertEquals(Set.of(TextStyle.BOLD), textAttributes.styles());

        // Verify the characters in the scrollback have correct attributes
        textAttributes = buffer.getCellAttributes(-1, 0);
        assertEquals(TerminalColor.BRIGHT_BLACK, textAttributes.foreground());
        assertEquals(TerminalColor.BRIGHT_BLACK, textAttributes.background());
        assertEquals(Set.of(TextStyle.UNDERLINE), textAttributes.styles());
    }
}
