package jm.jetbrains;

public class Cell {
    private char character;
    private TextAttributes attributes;

    public Cell(char character, TextAttributes attributes) {
        this.character = character;
        this.attributes = attributes;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public TextAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(TextAttributes attributes) {
        this.attributes = attributes;
    }
}
