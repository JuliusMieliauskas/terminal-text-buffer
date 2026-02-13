package jm.jetbrains;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;

public class TerminalBuffer {

    // Configuration
    private final int width;
    private final int height;
    private final int maxScrollbackSize;

    // State
    private int cursorCol;
    private int cursorRow;
    private TextAttributes currentAttributes;

    private final Line[] screen;
    private final Deque<Line> scrollback;

    // --- SETUP ---
    public TerminalBuffer(int width, int height, int maxScrollbackSize) {
        this.width = width;
        this.height = height;
        this.maxScrollbackSize = maxScrollbackSize;
        this.cursorCol = 0;
        this.cursorRow = 0;

        this.currentAttributes = TextAttributes.createDefault();
        this.scrollback = new LinkedList<>();
        this.screen = new Line[height];

        // TODO: Initialize the screen array with empty Lines
    }

    // --- ATTRIBUTES ---
    public void setCurrentAttributes(TerminalColor fg, TerminalColor bg, Set<TextStyle> styles) {
        this.currentAttributes = new TextAttributes(fg, bg, styles);
    }

    // --- CURSOR ---
    public int getCursorCol() {
        return cursorCol;
    }

    public int getCursorRow() {
        return cursorRow;
    }

    public void setCursorPosition(int col, int row) {
        // TODO: Implement bounds checking
    }

    public void moveCursor(Direction direction, int n) {
        // TODO: Implement bounds-checked movement
    }

    // --- EDITING ---
    public void writeText(String text) {
        // TODO: Implement overwrite logic
    }

    public void insertText(String text) {
        // TODO: Implement insertion and wrapping logic
    }

    public void fillLine(char c) {
        // TODO: Implement line filling
    }

    public void insertEmptyLineAtBottom() {
        // TODO: Implement the "Shift Up" scroll logic
    }

    public void clearScreen() {
        // TODO: Clear active screen
    }

    public void clearScreenAndScrollback() {
        // TODO: Clear both
    }

    // --- CONTENT ACCESS ---
    // TODO
}
