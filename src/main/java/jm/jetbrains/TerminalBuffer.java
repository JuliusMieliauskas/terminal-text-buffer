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

        for (int i = 0; i < height; i++) {
            screen[i] = new Line(width, currentAttributes);
        }
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

    public void setCursorPosition(int row, int col) {
        int newRow = Math.max(0, Math.min(row, height - 1));
        int newCol = Math.max(0, Math.min(col, width - 1));
        this.cursorRow = newRow;
        this.cursorCol = newCol;
    }

    public void moveCursor(Direction direction, int n) {
        int newRow = cursorRow;
        int newCol = cursorCol;
        switch (direction) {
            case UP -> newRow -= n;
            case DOWN -> newRow += n;
            case LEFT -> newCol -= n;
            case RIGHT -> newCol += n; // TODO: Maybe we should wrap it to new line ?? maybe not
        }
        setCursorPosition(newRow, newCol);
    }

    // --- EDITING ---
    public void writeText(String text) {
        for (char c : text.toCharArray()) {
            Cell currentCell = screen[cursorRow].getCell(cursorCol);
            currentCell.setCharacter(c);
            currentCell.setAttributes(currentAttributes);

            cursorCol++;

            // Handle Wrapping
            if (cursorCol >= width) {
                cursorCol = 0;
                cursorRow++;
            }

            // Handle Scrolling
            if (cursorRow >= height) {
                insertEmptyLineAtBottom();

                cursorRow = height - 1;
            }
        }
    }

    public void insertText(String text) {
        // TODO: Implement insertion and wrapping logic
    }

    public void fillLine(char c) {
        screen[cursorRow].fillLineWithCharacters(c, currentAttributes);
    }

    public void insertEmptyLineAtBottom() {
        Line topRow = screen[0];

        scrollback.addLast(topRow);

        // Enforce the maximum scrollback size
        while (scrollback.size() > maxScrollbackSize) {
            scrollback.removeFirst(); // Drops the oldest line from history
        }

        // Shift the Screen Array Up
        System.arraycopy(screen, 1, screen, 0, height - 1);

        // Create the new bottom line
        screen[height - 1] = new Line(width, currentAttributes);
    }

    public void clearScreen() {
        // TODO: Clear active screen
    }

    public void clearScreenAndScrollback() {
        // TODO: Clear both
    }

    // --- CONTENT ACCESS ---
    public String getScreenContents() {
        StringBuilder sb = new StringBuilder();
        for (Line line : screen) {
            sb.append(line.getLineContentsAsString());
        }
        return sb.toString();
    }

}
