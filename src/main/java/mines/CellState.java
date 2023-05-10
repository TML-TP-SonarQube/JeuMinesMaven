package mines;

/**

 The {@code CellState} enum represents the possible states of a cell in a Minesweeper game.
 <p>Possible values are:</p>
 <ul>
 <li>{@code CHECKED}: the cell has been checked and its content is visible.</li>
 <li>{@code MARKED}: the cell has been marked as potentially containing a mine.</li>
 <li>{@code UNCHECKED}: the cell has not been checked or marked.</li>
 </ul>
 */
public enum CellState {
    /**
     * the cell has been checked and its content is visible.
     */
    CHECKED,

    /**
     * the cell has been marked as potentially containing a mine.
     */
    MARKED,

    /**
     * the cell has not been checked or marked.
     */
    UNCHECKED
}
