package mines;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
    private static Board board;

    @BeforeAll
    static void initializeBoardTest(){
        board = new Board(new JLabel());
        assertEquals(Board.MINES, board.getBoardCells().stream().filter(ICell::isMined).count());
    }

    @Test
    void initGameTest() {

        // The constructor calls initGame method

        //checking allCells is initialized correctly
        assertEquals(Board.ROWS*Board.COLS, board.getAllCells());


        // checking the boardCells is not null
        assertNotNull(board.getBoardCells());

        // checking boardCells has the size of allCells
        assertEquals(Board.ROWS*Board.COLS, board.getBoardCells().size());

    }

    @Test
    void cellTotalMinedCornersTest(){
        // checking corners of mined cells are set correctly
        board.getBoardCells().stream()
                .filter( cell -> !cell.isMined())
                .forEach( cell -> {
                    long minedCorners = cell.getCellCorners()
                            .values().stream()
                            .filter(index -> board.getBoardCells().get(index).isMined())
                            .count();
                    assertEquals(cell.getCellContent(), minedCorners);
                });
    }
    @Test
    void newGameTest() {


        //checking inGame is set to true
        assertTrue(board.isInGame());

        // checking the board checked cells is reset to zero
        assertEquals(0, board.getCheckedCells());

        //checking minesLeft is reset to total mines
        assertEquals(Board.MINES, board.getMinesLeft());

        //checking the board has 40 mines
        long totalMinesInBoard = board.getBoardCells().stream().filter(ICell::isMined).count();
        assertEquals(Board.MINES, totalMinesInBoard);
    }

    @Test
    void findEmptyCellsTest() {

        // search randomly for an empty cell to check its corners
        ICell emptyCell = board.getBoardCells().stream()
                .filter(ICell::hasNoMineCellCorners)
                .findAny()
                .orElse(null);

        // check for no empty cell exist
        assertNotNull(emptyCell);

        // setting the cell to CHECKED state
        emptyCell.checkCell(new ImageIcon().getImage());

        int emptyCellIndex = emptyCell.getColumn() * emptyCell.getRow();
        board.findEmptyCells(emptyCellIndex);

        // checking all the corners are in state CHECKED
        board.getBoardCells().get(emptyCellIndex).getCellCorners().values()
                .forEach(index ->
                        assertEquals(CellState.CHECKED, board.getBoardCells().get(index).getCellState())
                );


    }

    @Test
    void mouseRightClickTest(){
        Board board = new Board(new JLabel());
        ICell randomCell = board.getBoardCells()
                .stream()
                .findAny().orElse(null);

        // checking that at least a cell exist
        assertNotNull(randomCell);

        // the position of the random cell in the board
        int cellIndex = randomCell.getRow() * Board.COLS + randomCell.getColumn() ;

        // simulating a user click event
        board.getMouseAdapter().cellRightClicked(cellIndex);

        // check that the cell state is MARKED
        assertEquals(CellState.MARKED, randomCell.getCellState());

        // checks that minesLeft decremented by one
        assertEquals(Board.MINES - 1, board.getMinesLeft());

    }

    @Test
    void mouseRightClickNoMarksLeftTest(){
        Board board = new Board(new JLabel());
        ICell randomCell = board.getBoardCells()
                .stream()
                .findAny().orElse(null);

        // checking that at least a cell exist
        assertNotNull(randomCell);
        // setting the mines left to one last mark
        board.setMinesLeft(1);

        // the position of the random cell in the board
        int cellIndex = randomCell.getRow() * Board.COLS + randomCell.getColumn();

        // simulating a user click event
        board.getMouseAdapter().cellRightClicked(cellIndex);

        // check that the status bar displays that no marks left
        assertTrue(board.getStatusbar().getText().contains("No marks left"));

        randomCell = board.getBoardCells()
                .stream()
                .filter( cell -> cell.getCellState() == CellState.UNCHECKED && !cell.isMined())
                .findAny().orElse(null);

        assertNotNull(randomCell);
        cellIndex = randomCell.getRow() * Board.COLS + randomCell.getColumn();
        // clicking when no marks left
        board.getMouseAdapter().cellRightClicked(cellIndex);

        assertEquals(0,board.getMinesLeft());


    }

    @Test
    void mouseRightClickCheckedCellTest(){
        Board board = new Board(new JLabel());
        ICell randomCell = board.getBoardCells()
                .stream()
                .filter(cell -> !cell.isMined())
                .findAny().orElse(null);

        // checking that at least a cell exist
        assertNotNull(randomCell);

        // the position of the random cell in the board
        int cellIndex = randomCell.getRow() * Board.COLS + randomCell.getColumn();

        // checking the cell
        board.getMouseAdapter().cellLeftClicked(cellIndex);
        // checking is in state CHECKED
        assertSame(CellState.CHECKED, randomCell.getCellState());

        // simulating a user click event
        board.getMouseAdapter().cellRightClicked(cellIndex);

        assertEquals(Board.MINES, board.getMinesLeft());


    }


    @Test
    void clickingToUnMarkAlreadyMarkedCellTest(){
        Board board = new Board(new JLabel());
        ICell randomCell = board.getBoardCells()
                .stream()
                .findAny().orElse(null);

        // checking that at least a cell exist
        assertNotNull(randomCell);

        // the position of the random cell in the board
        int cellIndex = randomCell.getRow() * Board.COLS + randomCell.getColumn();

        // simulating a user click event
        board.getMouseAdapter().cellRightClicked(cellIndex);

        // check that the cell state is MARKED
        assertEquals(CellState.MARKED, randomCell.getCellState());

        // clicking a second time to unMark a cell
        board.getMouseAdapter().cellRightClicked(cellIndex);

        // check that the cell state is UNCHECKED
        assertEquals(CellState.UNCHECKED, randomCell.getCellState());

    }

    @Test
    void mouseLeftClickTest(){
        Board board = new Board(new JLabel());
        ICell randomCell = board.getBoardCells().stream()
                .filter(cell -> !cell.isMined())
                .findAny().orElse(null);

        // checking that at least a non mined cell exist
        assertNotNull(randomCell);


        // simulating a user click event

        // the position of the random cell in the screen
        int cellIndex = randomCell.getRow() * Board.COLS + randomCell.getColumn() ;



        board.getMouseAdapter().cellLeftClicked(cellIndex);
        // check that the cell state is CHECKED
        assertEquals(CellState.CHECKED, randomCell.getCellState());

        // checks that at least one cell is checked (maybe more if the cell is empty)
        assertTrue( board.getCheckedCells() > 0);
    }

    @Test
    void leftMousePressedTest(){
        Board board = new Board(new JLabel());

        ICell randomCell = board.getBoardCells().stream()
                .filter(cell -> !cell.isMined())
                .findAny().orElse(null);

        // checking that at least a non mined cell exist
        assertNotNull(randomCell);
        int cellX = randomCell.getColumn() * Cell.WIDTH + (Cell.WIDTH/2);
        int cellY = randomCell.getRow() * Cell.HEIGHT + (Cell.HEIGHT/2);
        MouseEvent e = new MouseEvent(board, 0, 0,0,cellX,cellY,1,false,MouseEvent.BUTTON1);
        board.getMouseAdapter().mousePressed(e);

        // check that the cell state is CHECKED
        assertEquals(CellState.CHECKED, randomCell.getCellState());

        // checks that at least one cell is checked (maybe more if the cell is empty)
        assertTrue( board.getCheckedCells() > 0);
    }

    @Test
    void leftMousePressedEmptyCellTest(){
        Board board = new Board(new JLabel());

        ICell randomCell = board.getBoardCells().stream()
                .filter(ICell::hasNoMineCellCorners)
                .findAny().orElse(null);

        // checking that at least a non mined cell exist
        assertNotNull(randomCell);
        int cellX = randomCell.getColumn() * Cell.WIDTH + (Cell.WIDTH/2);
        int cellY = randomCell.getRow() * Cell.HEIGHT + (Cell.HEIGHT/2);
        MouseEvent e = new MouseEvent(board, 0, 0,0,cellX,cellY,1,false,MouseEvent.BUTTON1);
        board.getMouseAdapter().mousePressed(e);

        // check that the cell state is CHECKED
        assertEquals(CellState.CHECKED, randomCell.getCellState());

        // checks that at least one cell is checked (maybe more if the cell is empty)
        assertTrue( board.getCheckedCells() > 0);
    }

    @Test
    void leftMousePressedCheckedCellTest(){
        Board board = new Board(new JLabel());

        ICell randomCell = board.getBoardCells().stream()
                .filter(cell -> !cell.isMined())
                .findAny().orElse(null);

        // checking that at least a non mined cell exist
        assertNotNull(randomCell);
        int cellX = randomCell.getColumn() * Cell.WIDTH + (Cell.WIDTH/2);
        int cellY = randomCell.getRow() * Cell.HEIGHT + (Cell.HEIGHT/2);
        MouseEvent e = new MouseEvent(board, 0, 0,0,cellX,cellY,1,false,MouseEvent.BUTTON1);
        board.getMouseAdapter().mousePressed(e);

        // check that the cell state is CHECKED
        assertEquals(CellState.CHECKED, randomCell.getCellState());

        // checks that at least one cell is checked (maybe more if the cell is empty)
        assertTrue( board.getCheckedCells() > 0);

        // clicking in a checked cell
        board.getMouseAdapter().mousePressed(e);

        // check that the cell state is CHECKED
        assertEquals(CellState.CHECKED, randomCell.getCellState());

    }


    @Test
    void rightMousePressedTest(){
        Board board = new Board(new JLabel());
        ICell randomCell = board.getBoardCells()
                .stream()
                .findAny().orElse(null);

        // checking that at least a cell exist
        assertNotNull(randomCell);

        int cellX = randomCell.getColumn() * Cell.WIDTH + (Cell.WIDTH/2);
        int cellY = randomCell.getRow() * Cell.HEIGHT + (Cell.HEIGHT/2);
        MouseEvent e = new MouseEvent(board, 0, 0,0,cellX,cellY,1,false,MouseEvent.BUTTON3);
        board.getMouseAdapter().mousePressed(e);

        // check that the cell state is MARKED
        assertEquals(CellState.MARKED, randomCell.getCellState());

        // checks that minesLeft decremented by one
        assertEquals(Board.MINES - 1, board.getMinesLeft());
    }

    @Test
    void otherMousePressedTest(){
        Board board = new Board(new JLabel());
        ICell randomCell = board.getBoardCells()
                .stream()
                .findAny().orElse(null);

        // checking that at least a cell exist
        assertNotNull(randomCell);

        int cellX = randomCell.getColumn() * Cell.WIDTH + (Cell.WIDTH/2);
        int cellY = randomCell.getRow() * Cell.HEIGHT + (Cell.HEIGHT/2);
        MouseEvent e = new MouseEvent(board, 0, 0,0,cellX,cellY,1,false,MouseEvent.BUTTON2);
        board.getMouseAdapter().mousePressed(e);

        // check that the cell state is CHECKED
        assertNotEquals(CellState.CHECKED, randomCell.getCellState());

        // checks that at least one cell is checked (maybe more if the cell is empty)
        assertFalse( board.getCheckedCells() > 0);

        // check that the cell state is MARKED
        assertNotEquals(CellState.MARKED, randomCell.getCellState());

        // checks that minesLeft decremented by one
        assertNotEquals(Board.MINES - 1, board.getMinesLeft());
    }
    @AfterEach
    @Test
    void resetBoardTest() {
        board.resetBoard();
        board.getBoardCells().forEach(cell -> assertSame(CellState.UNCHECKED, cell.getCellState()));
    }

    @Test
    void checkWiningTest() {

        // simulating a winning scenario

        // marking all the mined cells
        board.getBoardCells()
                .stream()
                .filter(ICell::isMined)
                .forEach(cell -> cell.markCell(new ImageIcon().getImage()));

        board.setMinesLeft(0);


        // checking all non mined cells
        board.getBoardCells()
                .stream()
                .filter(cell -> !cell.isMined())
                .forEach(cell -> cell.checkCell(new ImageIcon().getImage()));

        board.setCheckedCells(board.getAllCells() - Board.MINES);


        board.checkWining();

        assertFalse(board.isInGame());

        assertTrue(board.getStatusbar().getText().contains("Game Won"));


    }

    @Test
    void checkFalseWiningTest() {

        // simulating a winning scenario

        // marking all the mined cells
        board.getBoardCells()
                .stream()
                .filter(ICell::isMined)
                .forEach(cell -> cell.markCell(new ImageIcon().getImage()));

        board.setMinesLeft(1);


        // checking all non mined cells
        board.getBoardCells()
                .stream()
                .filter(cell -> !cell.isMined())
                .forEach(cell -> cell.checkCell(new ImageIcon().getImage()));

        board.setCheckedCells(board.getAllCells() - Board.MINES);


        board.checkWining();

        assertTrue(board.isInGame());

        assertFalse(board.getStatusbar().getText().contains("Game Won"));


    }

    @Test
    void lostGameTest(){
        // simulating a lost scenario
        ICell randomMinedCell = board.getBoardCells().stream().filter(ICell::isMined).findAny().orElse(null);
        assertNotNull(randomMinedCell);

        // the position of the random cell in the screen
        int cellIndex = randomMinedCell.getRow() * Board.COLS + randomMinedCell.getColumn() ;
        board.getMouseAdapter().cellLeftClicked(cellIndex);

        assertFalse(board.isInGame());

        assertTrue(board.getStatusbar().getText().contains("Game Lost"));

    }
    
    @Test
    void revealBoardTest() {

        // mark a mined cell
        ICell randomMinedCell = board.getBoardCells().stream().filter(ICell::isMined).findAny().orElse(null);
        assertNotNull(randomMinedCell);
        randomMinedCell.markCell(new ImageIcon().getImage());

        // mark a non mined cell
        randomMinedCell = board.getBoardCells().stream().filter(cell -> !cell.isMined()).findAny().orElse(null);
        assertNotNull(randomMinedCell);
        randomMinedCell.markCell(new ImageIcon().getImage());

        board.revealBoard();
        board.getBoardCells()
                .forEach(cell ->
                        assertNotEquals(CellState.UNCHECKED, cell.getCellState())
                );



        assertTrue(board.getStatusbar().getText().contains("Game Lost"));
    }

    @Test
    void clickingLeftForNewGameTest(){
        // simulating a lost scenario
        ICell randomMinedCell = board.getBoardCells().stream().filter(ICell::isMined).findAny().orElse(null);
        assertNotNull(randomMinedCell);

        // the position of the random cell in the screen
        int cellIndex = randomMinedCell.getRow() * Board.COLS + randomMinedCell.getColumn() ;
        board.getMouseAdapter().cellLeftClicked(cellIndex);
        assertFalse(board.isInGame());

        // clicking for newGame
        board.getMouseAdapter().cellLeftClicked(cellIndex);
        assertTrue(board.isInGame());

    }

    @Test
    void clickingRightForNewGameTest(){
        // simulating a lost scenario
        ICell randomMinedCell = board.getBoardCells().stream().filter(ICell::isMined).findAny().orElse(null);
        assertNotNull(randomMinedCell);

        // the position of the random cell in the screen
        int cellIndex = randomMinedCell.getRow() * Board.COLS + randomMinedCell.getColumn() ;
        board.getMouseAdapter().cellLeftClicked(cellIndex);
        assertFalse(board.isInGame());

        // clicking for newGame
        board.getMouseAdapter().cellRightClicked(cellIndex);
        assertTrue(board.isInGame());

    }
}