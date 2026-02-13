package jm.jetbrains;

public class Line {
    private final Cell[] cells;

    public Line(int width, TextAttributes defaultAttributes) {
        cells = new Cell[width];
        // Pre-fill the line with empty spaces
        for (int i = 0; i < width; i++) {
            cells[i] = new Cell(' ', defaultAttributes);
        }
    }

    public Cell getCell(int index) {
        return cells[index];
    }

    public int getWidth() {
        return cells.length;
    }

}