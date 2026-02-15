## How to Run Tests

```bash
# Run all tests
mvn test
```

### 1. Setup & Configuration
- [X] **Configurable Dimensions**: Initialize buffer with specific width (columns) and height (rows).
- [X] **Scrollback Limit**: Configure the maximum number of lines preserved in the scrollback history.

### 2. Cell Attributes & Styling
- [X] **Attribute State**: Ability to set current foreground color, background color, and styles (Bold, Italic, Underline).
- [X] **Persistence**: Ensure new text operations inherit the currently set attributes.
- [X] **Data Structure**: Efficient storage of `Cell` objects containing character data and attributes.

### 3. Cursor Management
- [X] **Get Position**: Retrieve current cursor column and row.
- [X] **Set Position**: Move cursor to a specific (x, y) coordinate.
- [X] **Directional Movement**: Move cursor Up, Down, Left, Right by `N` cells.
- [X] **Bounds Checking**: strictly enforce that the cursor cannot move outside the visible screen dimensions.

### 4. Editing Operations (Cursor-Dependent)
- [X] **Write Text (Overwrite)**: Write text starting at the cursor position, overwriting existing content.
    - [X] Wraps to the next line if the text hits the right edge.
    - [X] Triggers scroll if the cursor hits the bottom-right edge.
- [X] **Insert Text (Ripple)**: Insert text at the cursor position, shifting existing content to the right.
    - [X] Handles line wrapping for shifted content.
    - [X] Moves the cursor to the end of the inserted text.
- [X] **Fill Line**: Replace the entire current line with a specific character (e.g., spaces or dashes) using current attributes.

### 5. Editing Operations (Global)
- [X] **Insert Empty Line**: Insert a new line at the bottom of the screen.
    - [X] Triggers "Shift Up": Top line moves to scrollback, all lines shift up, new line appears at bottom.
- [X] **Clear Screen**: Reset all visible cells to default (empty char, default colors).
- [X] **Clear All**: Reset the visible screen AND wipe the scrollback history.

### 6. Content Access (View Layer)
- [X] **Get Character**: Retrieve the `Cell` (char + style) at a specific (col, row).
    - [X] Supports negative indexing for Scrollback (e.g., row `-1` is the most recent history).
- [X] **Get Attributes**: Retrieve style information for a specific cell.
- [X] **Get Line String**: Return a specific row as a String (Screen or Scrollback).
- [X] **Get Screen String**: Return the entire visible screen as a single formatted String.
- [X] **Get Full History**: Return the entire Screen + Scrollback content as a String.

### 7. Bonus Features (Optional)
- [ ] **Wide Characters**: Support for CJK ideographs/Emoji (occupying 2 columns).
- [ ] **Resize Strategy**: Logic to handle resizing the screen dimensions (reflowing text or cropping).
