package mines;

import java.awt.BorderLayout;

import javax.swing.*;

// This is a refactored version of the source: https://github.com/janbodnar/Java-Minesweeper-Game

/**
 * The Mines class is the entry point for the Minesweeper game application.
 * It contains the main method that creates an instance of the game and starts it.
 */
public class Mines extends JFrame {
    private static final long serialVersionUID = 4772165125287256837L;

    /**
     * The padding in pixels added around the board in the game window.
     */
    private static final int BOARD_PADDING = 20;

    /**
     * The height of the status bar label.
     */
    private static final int SCORE_PANEL_HEIGHT = 50;

    /**
     * The width of the game window in pixels.
     */
    static final int WIDTH = Cell.WIDTH * Board.COLS + BOARD_PADDING;

    /**
     * The height of the game window in pixels.
     */
    static final int HEIGHT = Cell.HEIGHT * Board.ROWS + SCORE_PANEL_HEIGHT + BOARD_PADDING;



    /**
     * Constructs a Mines object and initializes the game frame with a set size and layout.
     * It also initializes an instance of the Board class, which generates the game board.
     */
    public Mines() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Minesweeper");

        JPanel scorePanel = new JPanel(new BorderLayout());

        // a label to display the marks left title
        JLabel marksLeftTitleLabel = new JLabel("Marks Left: ");

        JLabel marksLeftLabel = new JLabel("");

        scorePanel.add(marksLeftTitleLabel, BorderLayout.WEST);
        scorePanel.add(marksLeftLabel,BorderLayout.CENTER);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        scorePanel.setSize(WIDTH, SCORE_PANEL_HEIGHT);
        add(scorePanel, BorderLayout.SOUTH);

        JPanel boardPanel = new JPanel(new BorderLayout());
        int boardSidePadding = BOARD_PADDING / 2;
        boardPanel.setBorder(BorderFactory.createEmptyBorder(boardSidePadding, boardSidePadding, boardSidePadding, boardSidePadding));
        boardPanel.add(new Board(marksLeftLabel), BorderLayout.CENTER);

        add(boardPanel);
        setResizable(false);
        setVisible(true);
    }


    /**
     * The main method that creates an instance of the Mines game and starts it.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Mines();
    }
}