package mines;

import java.awt.*;
import java.util.Map;

/**
 * Represents a cell on a game board.
 */
public interface ICell {

    /**
     * Initializes the cell
     * @param img the image to be displayed on the cell
     */
    void initCell(Image img);

    /**
     * Increments the number of mined corner cells surrounding this cell.
     */
    void addCornerMineCell();

    /**
     * Gets the row number of the cell.
     *
     * @return the row number of the cell.
     */
    int getRow();

    /**
     * Gets the column number of the cell.
     *
     * @return the column number of the cell.
     */
    int getColumn();

    /**
     * Gets the cell's content.
     *
     * @return the cell's content.
     */
    int getCellContent();

    /**
     * Gets the corners of the cell.
     *
     * @return a map of the corners of the cell.
     */
    Map<Corner, Integer> getCellCorners();

    /**
     * Mines the cell.
     */
    void mineTheCell();

    /**
     * Checks whether this cell is mined.
     *
     * @return true if the cell is mined, false otherwise
     */
    boolean isMined();

    /**
     * Checks whether this cell has no mined corner cells surrounding it.
     *
     * @return true if the cell has no mined corner cells surrounding it, false otherwise
     */
    boolean hasNoMineCellCorners();

    /**
     * Gets the state of the cell.
     *
     * @return the state of the cell.
     */
    CellState getCellState();

    /**
     * Marks the cell as checked.
     *
     * @param img the image to display on the cell.
     */
    void checkCell(Image img);

    /**
     * Marks the cell as marked.
     *
     * @param img the image to display on the cell.
     */
    void markCell(Image img);

    /**
     * Sets the cell state to UNCHECKED.
     *
     * @param img the image to display on the cell.
     */
    void unMarkCell(Image img);

    /**
     * Gets the current image of the cell.
     *
     * @return the current image of the cell.
     */
    Image getCellImage();
}
