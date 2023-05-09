package mines;

import java.awt.BorderLayout;
import java.security.NoSuchAlgorithmException;

import javax.swing.*;

// This is a refactored version of the source: https://github.com/janbodnar/Java-Minesweeper-Game

public class Mines extends JFrame {
    private static final long serialVersionUID = 4772165125287256837L;

    private static final int BOARD_PADDING = 20;
    private static final int SCORE_PANEL_HEIGHT = 50;
    private static final int WIDTH = Cell.WIDTH * Board.COLS + BOARD_PADDING;
    private static final int HEIGHT = Cell.HEIGHT * Board.ROWS + SCORE_PANEL_HEIGHT + BOARD_PADDING;




    public Mines() throws NoSuchAlgorithmException {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Minesweeper");

        JPanel scorePanel = new JPanel(new BorderLayout());

        // Create a label to display the score
        JLabel scoreTitleLabel = new JLabel("Marks Left: ");

        JLabel statusbar = new JLabel("");

        scorePanel.add(scoreTitleLabel, BorderLayout.WEST);
        scorePanel.add(statusbar,BorderLayout.CENTER);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        scorePanel.setSize(WIDTH, SCORE_PANEL_HEIGHT);
        add(scorePanel, BorderLayout.SOUTH);

        JPanel boardPanel = new JPanel(new BorderLayout());
        int boardSidePadding = BOARD_PADDING / 2;
        boardPanel.setBorder(BorderFactory.createEmptyBorder(boardSidePadding, boardSidePadding, boardSidePadding, boardSidePadding));
        boardPanel.add(new Board(statusbar), BorderLayout.CENTER);

        add(boardPanel);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new Mines();
    }
}