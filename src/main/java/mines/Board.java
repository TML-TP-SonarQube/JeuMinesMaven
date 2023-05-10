package mines;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * The Board class extends JPanel and represents the game board in Minesweeper.
 * It contains an array of cells, and manages the game logic.
 * This class initializes the game board, including setting up the cells and placing mines.
 * It also handles user input and updates the game state accordingly.
 */
public class Board extends JPanel {
    private static final long serialVersionUID = 6195235521361212179L;

    /**
     * The total number of mines in the game board.
     */
    private static final int MINES = 40;

    /**
     * The number of rows in the game board.
     */
    public static final int ROWS = 16;

    /**
     * The number of columns in the game board.
     */
    public static final int COLS = 16;

    /**
     * A pseudo-random number generator used to randomly place mines on the board.
     * The {@code Random} instance is initialized in the constructor with the
     * current time as the seed, so the sequence of numbers generated is likely
     * to be different each time the program is run.
     */
    private final Random random;

    /**
     * A list that contains all the cells of the game board.
     * The cells are stored in row-major order, i.e., each row is stored
     * consecutively in the list.
     * it is marked as {@code transient} to exclude it from serialization.
     */
    private transient List<Cell> boardCells;

    /**
     * The total number of images.
     */
    private static final int NUM_IMAGES = 13;

    /**

     An array of images used to represent the different cell states.
     The images are loaded once and stored in this array for easy access.
     The array is marked as {@code final} to ensure that its reference cannot be changed,
     and it is marked as {@code transient} to exclude it from serialization.
     */
    private final transient  Image[] img;

    /**
     * A boolean flag indicating whether the game is currently in progress or not.
     * If the value is true, the game is still in progress. If the value is false, the game
     * has either been won or lost and is no longer in progress.
     */
    private boolean inGame;

    /**
     * The number of mines left to be marked by the player.
     */
    private int minesLeft;

    /**
     * The total number of all cells .
     */
    private int allCells;

    /**
     * The status bar label used to display the current game status to the user.
     */
    private final JLabel statusbar;

    /**
     * The total number of checked cells .
     */
    private int checkedCells = 0;

    /**
     * The index of the mark image.
     */
    private static final int DRAW_MARK = 11;

    /**
     * The index of the wrong mark image.
     */
    private static final int DRAW_WRONG_MARK = 12;

    /**
     * The index of the cover image.
     */
    private static final int COVER_FOR_CELL = 10;


    /**
     * Constructs a new Board object.
     *
     * @param statusbar The status bar to display the game score.
     * @throws NoSuchAlgorithmException If the SecureRandom algorithm is not available.
     */
    public Board(JLabel statusbar) throws NoSuchAlgorithmException {

        random = SecureRandom.getInstanceStrong();

        this.statusbar = statusbar;

        img =  new Image[NUM_IMAGES];
        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = new ImageIcon("images/" + (i) + ".gif").getImage();
        }

        addMouseListener(new MinesAdapter());
        initGame();
    }

    /**
     * Initializes the game board by creating all the cells
     */
    public void initGame(){
        allCells = ROWS * COLS;
        boardCells = new ArrayList<>(allCells);

        for (int i = 0; i < allCells; i++) {
            int cellIndex = i;
            Cell newCell = new Cell(cellIndex, img[COVER_FOR_CELL]);
            boardCells.add(newCell);
        }
        repaint();
        newGame();

        // Set the layout manager of the panel to BorderLayout
        setLayout(new BorderLayout());

    }

    /**
     * Starts a new game by setting up the mines.
     */
    public void newGame() {

        inGame = true;
        minesLeft = MINES;

        statusbar.setText(Integer.toString(minesLeft));

        int position;
        int i = 0;
        while (i < MINES) {

            position = random.nextInt(allCells);

            if(!boardCells.get(position).isMined()){
                boardCells.get(position).mineTheCell();
                i++;
                boardCells.get(position).getCellCorners()
                        .forEach((corner, integer) ->
                                boardCells.get(integer).addCornerMineCell()
                        );
            }
        }

    }

    /**
     * Finds and uncovers all empty cells adjacent to the specified cell index.
     *
     * @param cellIndex The index of the cell to find empty cells around.
     */
    public void findEmptyCells(int cellIndex) {

        boardCells.get(cellIndex).getCellCorners()
                .values()
                .forEach(cellCorner -> {
                    if(boardCells.get(cellCorner).getCellState() != CellState.CHECKED) {
                        boardCells.get(cellCorner).checkCell(img[boardCells.get(cellCorner).getCellContent()]);
                        checkedCells++;
                        if (boardCells.get(cellCorner).hasNoMineCellCorners())
                            findEmptyCells(cellCorner);
                    }
                });

        repaint();

    }


    /**
     * Resets the board by covering all the cells
     * and setting up the mines by calling {@code  newGame()}
     */
    public void resetBoard() {
        boardCells.forEach(cell-> cell.initCell(img[COVER_FOR_CELL]));
        newGame();
        repaint();
    }

    /**
     * Checks if the player has won the game.
     * If all mine cells are marked and the rest are checked,
     * updates the statusbar to announce the win.
     */
    public void checkWining(){
        if(checkedCells == (allCells - MINES) && minesLeft == 0){
            inGame = false;
            statusbar.setText("Game Won");
        }
    }

    /**
     * Reveal the board and shows the final result by uncovering all cells.
     * Updates the statusbar to announce the loss.
     */
    public void revealBoard() {

        boardCells.forEach(cell  -> {
            if(cell.getCellState() == CellState.MARKED) {
                if (cell.isMined())
                    cell.markCell(img[DRAW_MARK]);
                else
                    cell.markCell(img[DRAW_WRONG_MARK]);

            }else {
                cell.checkCell(img[cell.getCellContent()]);
            }
        });
        setSize(Cell.WIDTH * COLS, Cell.HEIGHT * ROWS);
        repaint();
        statusbar.setText("Game Lost");
    }

    /**
     * Overrides the paint method to draw the cells on the game board.
     *
     * @param g The graphics object to paint on.
     */
    @Override
    public void paint(Graphics g) {
        boardCells.forEach(cell -> g.drawImage(cell.getCellImage(), cell.column * Cell.WIDTH,
                cell.row * Cell.HEIGHT,Cell.WIDTH, Cell.HEIGHT, null));

    }

    /**
     * Handles the mouse events for the game board panel.
     * This class extends MouseAdapter
     * and implements the logic for handling left and right mouse clicks on the game board.
     */
    private class MinesAdapter extends MouseAdapter {

        /**
         * Overrides the mousePressed method of MouseAdapter class to handle mouse press events on the panel.
         * Gets the clicked cell column and row from the MouseEvent object.
         * Checks  whether it is a left or right click and calls the appropriate method to handle the click
         * @param e the MouseEvent object representing the mouse press event
         */
        @Override
        public void mousePressed(MouseEvent e) {

            int cellColumn = e.getX() / Cell.WIDTH;
            int cellRow = e.getY() / Cell.HEIGHT;

            int cellIndex = cellRow * COLS + cellColumn;

            if (e.getButton() == MouseEvent.BUTTON1) {
                // Left mouse button clicked
                cellLabelLeftClicked(cellIndex);
            } else{
                // Right mouse button clicked
                cellLabelRightClicked(cellIndex);
            }

            checkWining();
        }

        /**
         * Handles a left mouse click event on a cell in the game panel.
         * Uncovers the clicked cell and updates the game panel.
         * @param cellIndex the index of the clicked cell
         */
        private void cellLabelLeftClicked(int cellIndex) {

            if (!inGame) {
                resetBoard();
                return;
            }

            if(boardCells.get(cellIndex).getCellState() == CellState.UNCHECKED){

                if (boardCells.get(cellIndex).isMined()) {
                    inGame = false;
                    revealBoard();
                }
                else {
                    boardCells.get(cellIndex).checkCell(img[boardCells.get(cellIndex).getCellContent()]);
                    checkedCells++;
                    repaint();
                    if (boardCells.get(cellIndex).hasNoMineCellCorners())
                        findEmptyCells(cellIndex);

                }
            }

        }

        /**
         * Handles a right mouse click event on a cell in the game panel.
         * Marks the clicked cell with a flag to indicate it might have a mine
         * and updates the statusbar to show the remaining mines.
         * @param cellIndex the index of the clicked cell
         */
        private void cellLabelRightClicked(int cellIndex){

            if (!inGame) {
                resetBoard();
                return;
            }

            if(boardCells.get(cellIndex).getCellState() == CellState.UNCHECKED){
                if(minesLeft > 0){
                    boardCells.get(cellIndex).markCell(img[DRAW_MARK]);
                    minesLeft--;
                    repaint();

                    if(minesLeft == 0)
                        statusbar.setText("No marks left");
                    else
                        statusbar.setText(Integer.toString(minesLeft));
                }

            }else
            if(boardCells.get(cellIndex).getCellState() == CellState.MARKED){
                boardCells.get(cellIndex).unMarkCell(img[COVER_FOR_CELL]);
                minesLeft++;
                repaint();
                statusbar.setText(Integer.toString(minesLeft));
            }
        }

    }
}
