package jm.jetbrains;

import java.util.EnumSet;
import java.util.Set;

public record TextAttributes(
        TerminalColor foreground,
        TerminalColor background,
        Set<TextStyle> styles
) {
    // Helper to create the default starting state
    public static TextAttributes createDefault() {
        return new TextAttributes(
                TerminalColor.DEFAULT,
                TerminalColor.DEFAULT,
                EnumSet.noneOf(TextStyle.class)
        );
    }
}