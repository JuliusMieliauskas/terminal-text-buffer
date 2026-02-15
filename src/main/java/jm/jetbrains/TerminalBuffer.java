package jm.jetbrains;

import java.util.*;

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

            Cell currentCell = screen[cursorRow].getCell(cursorCol);
            currentCell.setCharacter(c);
            currentCell.setAttributes(currentAttributes);

            cursorCol++;
        }
    }

    public void insertText(String text) {
        // Logic: maintain "carry" array which represents the characters which need to be inserted
        // Update this array with characters which do not fit into current line and insert them to start of next line.
        // Propagate those lines until we have fully processed all the text and added new lines if required
        List<Cell> carry = new ArrayList<>();
        for (char c : text.toCharArray()) {
            carry.add(new Cell(c, currentAttributes));
        }

        // Start the cascade at the current cursor row
        int currentRow = cursorRow;
        int insertCol = cursorCol;

        // Loop until there is no more carry or we run out of screen
        while (!carry.isEmpty() && currentRow < height) {
            // Perform the insertion on the specific line
            List<Cell> newOverflow = insertCellsIntoRow(currentRow, insertCol, carry);

            carry = newOverflow;
            currentRow++;
            insertCol = 0; // All subsequent wraps insert at the START of the line
        }

        // If we still have overflow after reaching the bottom, we must scroll
        while (!carry.isEmpty()) {
            insertEmptyLineAtBottom();

            List<Cell> newOverflow = insertCellsIntoRow(height - 1, 0, carry);
            carry = newOverflow;
        }

        // Update cursor positions
        int totalAdvance = cursorCol + text.length();
        int newRow = cursorRow + (totalAdvance / width);
        int newCol = totalAdvance % width;
        setCursorPosition(newRow, newCol);
    }

    private List<Cell> insertCellsIntoRow(int row, int startCol, List<Cell> toInsert) {
        Line line = screen[row];
        int insertCount = toInsert.size();

        List<Cell> overflow = new ArrayList<>();

        Cell[] tempBuffer = new Cell[width + insertCount];

        // Copy part 1: Start of line up to insert position
        for (int i = 0; i < startCol; i++) {
            tempBuffer[i] = line.getCell(i);
        }

        // Copy part 2: The new text
        for (int i = 0; i < insertCount; i++) {
            tempBuffer[startCol + i] = toInsert.get(i);
        }

        // Copy part 3: The existing text that gets shifted
        for (int i = startCol; i < width; i++) {
            tempBuffer[i + insertCount] = line.getCell(i);
        }

        for (int i = 0; i < width; i++) {
            line.setCell(i, new Cell(tempBuffer[i].getCharacter(), tempBuffer[i].getAttributes()));
        }

        // Collect the rest into overflow list, trimming trailing empty cells
        int lastNonEmptyIndex = -1;
        for (int i = width; i < tempBuffer.length; i++) {
            if (tempBuffer[i].getCharacter() != ' ') {
                lastNonEmptyIndex = i;
            }
        }
        if (lastNonEmptyIndex == -1) {
            return overflow;
        }
        for (int i = width; i <= lastNonEmptyIndex; i++) {
            overflow.add(tempBuffer[i]);
        }

        return overflow;
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

    public String getScrollbackAndScreenContents() {
        StringBuilder sb = new StringBuilder();
        for (Line line : scrollback) {
            sb.append(line.getLineContentsAsString());
        }
        for (Line line : screen) {
            sb.append(line.getLineContentsAsString());
        }
        return sb.toString();
    }

    public char getCellCharacter(int row, int col) {
        // If row is negative get from scrollback
        // -1 is the first row (most recent) row in scrollback, -2 is the second most recent, etc.
        if (row < 0) {
            int scrollbackIndex = scrollback.size() + row;
            if (scrollbackIndex < 0) {
                throw new IndexOutOfBoundsException("Scrollback does not have that many lines");
            }
            Line scrollbackLine = ((LinkedList<Line>) scrollback).get(scrollbackIndex);
            return scrollbackLine.getCell(col).getCharacter();
        } else {
            if (row >= height) {
                throw new IndexOutOfBoundsException("Row exceeds screen height");
            }
            return screen[row].getCell(col).getCharacter();
        }
    }

    public TextAttributes getCellAttributes(int row, int col) {
        if (row < 0) {
            int scrollbackIndex = scrollback.size() + row;
            if (scrollbackIndex < 0) {
                throw new IndexOutOfBoundsException("Scrollback does not have that many lines");
            }
            Line scrollbackLine = ((LinkedList<Line>) scrollback).get(scrollbackIndex);
            return scrollbackLine.getCell(col).getAttributes();
        } else {
            if (row >= height) {
                throw new IndexOutOfBoundsException("Row exceeds screen height");
            }
            return screen[row].getCell(col).getAttributes();
        }
    }

    public String getLineContents(int row) {
        if (row < 0) {
            int scrollbackIndex = scrollback.size() + row;
            if (scrollbackIndex < 0) {
                throw new IndexOutOfBoundsException("Scrollback does not have that many lines");
            }
            Line scrollbackLine = ((LinkedList<Line>) scrollback).get(scrollbackIndex);
            return scrollbackLine.getLineContentsAsString();
        } else {
            if (row >= height) {
                throw new IndexOutOfBoundsException("Row exceeds screen height");
            }
            return screen[row].getLineContentsAsString();
        }
    }

}
