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

    public void fillLineWithCharacters(char c, TextAttributes textAttributes) {
        for(Cell cell : cells) {
            cell.setCharacter(c);
            cell.setAttributes(textAttributes);
        }
    }

    public Cell getCell(int index) {
        return cells[index];
    }

    public void setCell(int index, Cell newCell) {
        cells[index] = newCell;
    }

    public int getWidth() {
        return cells.length;
    }

    public String getLineContentsAsString() {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : cells) {
            sb.append(cell.getCharacter());
        }
        return sb.toString();
    }

}