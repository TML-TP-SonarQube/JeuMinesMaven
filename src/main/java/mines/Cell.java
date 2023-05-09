package mines;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class Cell{

    private final int index;
    private CellState state;
    private Boolean isCellMined;
    private final Map<Corner, Integer> corners;
    private int cornersMineCell;
    private int cellContent;
    static final int WIDTH = 25;
    static final int HEIGHT = 25;
    private Image cellImage;
    int row;
    int column;
    private static final int MINE_CELL = 9;


    public Cell(int index, Image img){

        this.index = index;
        state = CellState.UNCHECKED;
        isCellMined = false;
        corners = new EnumMap<>(Corner.class);
        cornersMineCell = 0;
        cellContent = 0;
        cellImage = img;
        row = index / Board.COLS;
        column = index % Board.COLS;
        setCellCorners();
    }

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

    public void addCornerMineCell(){
        cornersMineCell ++;
        setCellContent(cornersMineCell);
    }

    private void setCellContent(int cellCorners){
        boolean notMinedCell = !isCellMined;
        if (notMinedCell)
            cellContent = cellCorners;
    }

    public int getCellContent(){
        return cellContent;
    }

    public Map<Corner, Integer> getCellCorners(){
        return corners;
    }

    public void mineTheCell(){
        isCellMined = true;
        cellContent = MINE_CELL;
    }

    public boolean isMined(){
        return isCellMined;
    }

    public boolean hasNoMineCellCorners(){
        return cornersMineCell == 0 && !isCellMined;
    }

    private void setCellState(CellState state, Image img){
        this.state = state;
        cellImage = img;
    }

    public CellState getCellState(){
        return state;
    }

    public void checkCell(Image img){
        setCellState(CellState.CHECKED, img);
    }

    public void markCell(Image img){
        setCellState(CellState.MARKED, img);
    }

    public void unMarkCell(Image img){
        setCellState(CellState.UNCHECKED, img);
    }

    public Image getCellImage(){
        return cellImage;
    }
}
