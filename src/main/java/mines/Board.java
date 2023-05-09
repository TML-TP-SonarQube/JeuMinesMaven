package mines;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class Board extends JPanel {
    private static final long serialVersionUID = 6195235521361212179L;


    private final Random random;
    private transient List<Cell> boardCells;
    private static final int NUM_IMAGES = 13;
    private boolean inGame;
    private int minesLeft;
    private final transient  Image[] img;
    private static final int MINES = 40;
    public static final int ROWS = 16;
    public static final int COLS = 16;
    private int allCells;
    private final JLabel statusbar;
    private int checkedCells = 0;
    private static final int DRAW_MARK = 11;
    private static final int DRAW_WRONG_MARK = 12;
    private static final int COVER_FOR_CELL = 10;

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

    @Override
    public void paint(Graphics g) {
        boardCells.forEach(cell -> g.drawImage(cell.getCellImage(), cell.column * Cell.WIDTH,
                cell.row * Cell.HEIGHT,Cell.WIDTH, Cell.HEIGHT, null));

    }

    private class MinesAdapter extends MouseAdapter {

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


        private void cellLabelLeftClicked(int cellIndex) {

            if (!inGame) {
                initGame();
                return;
            }

            if(boardCells.get(cellIndex).getCellState() == CellState.UNCHECKED){

                if (boardCells.get(cellIndex).isMined()) {
                    inGame = false;
                    gameOver();
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

        private void cellLabelRightClicked(int cellIndex){

            if (!inGame) {
                initGame();
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

        private void checkWining(){
            if(checkedCells == (allCells - MINES) && minesLeft == 0){
                inGame = false;
                statusbar.setText("Game Won");
            }
        }

        private void gameOver() {

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

    }


}
