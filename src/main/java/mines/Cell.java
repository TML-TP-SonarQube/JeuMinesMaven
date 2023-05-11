package mines;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * The Cell class implements ICell interface and represents a cell on the game board.
 * It contains information about the cell's state and contents, as well as its location
 * on the board and the indices of its corners.
 */
public class Cell implements ICell {

    /**
     * The width of the cell.
     */
    static final int WIDTH = 25;

    /**
     * The height of the cell.
     */
    static final int HEIGHT = 25;

    /**
     * The index of the cell.
     */
    private final int index;

    /**
     * The state of the cell, represented as an instance of the CellState enum.
     */
    private CellState state;

    /**
     * The boolean that represents if the cell is mined.
     */
    private Boolean isCellMined;

    /**
     * A map containing the indices of each of the cell's corners.
     */
    private final Map<Corner, Integer> corners;

    /**
     * The total number of mined corner cells surrounding this cell.
     */
    private int cornersMineCell;

    /**
     * The content of the cell, represented as an integer that corresponds to a specific image.
     */
    private int cellContent;

    /**
     * The image of the cell.
     */
    private Image cellImage;

    /**
     * The row number of the cell.
     */
    private final int row;

    /**
     * The column number of the cell.
     */
    private final int column;

    /**
     * The index of the mine image.
     */
    private static final int MINE_CELL = 9;


    /**
     * Creates a new Cell instance with the given index and image.
     *
     * @param index the index of the cell
     * @param img   the image to be displayed on the cell
     */
    public Cell(int index, Image img){

        this.index = index;
        corners = new EnumMap<>(Corner.class);
        row = index / Board.COLS;
        column = index % Board.COLS;
        initCell(img);
        setCellCorners();
    }

    /**
     * Initializes the cell
     * @param img the image to be displayed on the cell
     */
    public void initCell(Image img){
        state = CellState.UNCHECKED;
        isCellMined = false;
        cornersMineCell = 0;
        cellContent = 0;
        cellImage = img;
    }

    /**
     * Sets the indices of all the corners of the cell in a map.
     */
    private void setCellCorners(){

        // check for existing a top row
        if(row > 0 ) {
            corners.put(Corner.TOP, (index - Board.COLS));

            // check for existing a left column
            if(column > 0)
                corners.put(Corner.TOP_LEFT, (index - Board.COLS - 1));

            // check for existing a right column
            if(column < Board.COLS - 1 )
                corners.put(Corner.TOP_RIGHT, (index - Board.COLS + 1));

        }

        // check for existing a left column
        if(column > 0)
            corners.put(Corner.LEFT, (index - 1));

        // check for existing a right column
        if(column < Board.COLS - 1 )
            corners.put(Corner.RIGHT, (index + 1));

        // check for existing a bottom row
        if(row < Board.ROWS - 1){
            corners.put(Corner.BOTTOM, (index + Board.COLS));

            // check for existing a left column
            if(column > 0)
                corners.put(Corner.BOTTOM_LEFT, (index + Board.COLS - 1));

            // check for existing a right column
            if(column < Board.COLS - 1)
                corners.put(Corner.BOTTOM_RIGHT, (index + Board.COLS + 1));

        }


    }

    /**
     * Increments the number of mined corner cells surrounding this cell.
     */
    public void addCornerMineCell(){
        cornersMineCell ++;
        setCellContent(cornersMineCell);
    }

    /**
     * Gets the row number of the cell.
     *
     * @return the row number of the cell.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column number of the cell.
     *
     * @return the column number of the cell.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the content of the cell to the given integer value.
     *
     * @param cellCorners the integer value to set as the cell's content
     */
    private void setCellContent(int cellCorners){
        boolean notMinedCell = !isCellMined;
        if (notMinedCell)
            cellContent = cellCorners;
    }

    /**
     * Returns the content of the cell, represented as an integer value.
     *
     * @return the cell's content as an integer value
     */
    public int getCellContent(){
        return cellContent;
    }

    /**
     * Returns a map containing the indices of each of the cell's corners.
     *
     * @return a map containing the indices of each of the cell's corners
     */
    public Map<Corner, Integer> getCellCorners(){
        return corners;
    }

    /**
     * Mines the cell.
     */
    public void mineTheCell(){
        isCellMined = true;
        cellContent = MINE_CELL;
    }

    /**
     * Checks whether this cell is mined.
     *
     * @return true if the cell is mined, false otherwise
     */
    public boolean isMined(){
        return isCellMined;
    }

    /**
     * Checks whether this cell has no mined corner cells surrounding it.
     *
     * @return true if the cell has no mined corner cells surrounding it, false otherwise
     */
    public boolean hasNoMineCellCorners(){
        return cornersMineCell == 0 && !isCellMined;
    }

    /**
     * Sets the state of the cell to the given state, and sets the image to be displayed on the cell.
     *
     * @param state the state to set for the cell
     * @param img   the image to be displayed on the cell
     */
    private void setCellState(CellState state, Image img){
        this.state = state;
        cellImage = img;
    }

    /**
     * Gets the current state of the cell.
     *
     * @return the current state of the cell.
     */
    public CellState getCellState(){
        return state;
    }

    /**
     * Sets the cell state to CHECKED.
     *
     * @param img the image to display for the cell.
     */
    public void checkCell(Image img){
        setCellState(CellState.CHECKED, img);
    }

    /**
     * Sets the cell state to MARKED.
     *
     * @param img the image to display for the cell.
     */
    public void markCell(Image img){
        setCellState(CellState.MARKED, img);
    }

    /**
     * Sets the cell state to UNCHECKED.
     *
     * @param img the image to display for the cell.
     */
    public void unMarkCell(Image img){
        setCellState(CellState.UNCHECKED, img);
    }

    /**
     * Returns the current image of the cell.
     *
     * @return the current image of the cell.
     */
    public Image getCellImage(){
        return cellImage;
    }
}
