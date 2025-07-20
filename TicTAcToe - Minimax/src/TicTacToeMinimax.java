import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TicTacToeMinimax
 * A GUI-based Tic Tac Toe game using Minimax algorithm for unbeatable AI.
 * Features score tracking, AI logic, and simple UI using Java Swing.
 */
public class TicTacToeMinimax {
    private JFrame frame;
    private JButton[][] boardButtons;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private int playerScore = 0;
    private int aiScore = 0;
    private boolean isPlayerTurn = true;

    /**
     * Constructor - Initializes the game window and UI components.
     */
    public TicTacToeMinimax() {
        // Main game frame setup
        frame = new JFrame("Tic Tac Toe - Minimax AI");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // --- Top Panel: Scoreboard and turn status ---
        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        JLabel titleLabel = new JLabel("SCOREBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        scoreLabel = new JLabel("Player: 0   |   AI: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        statusLabel = new JLabel("Turn: Player", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setForeground(Color.BLACK);

        topPanel.add(titleLabel);
        topPanel.add(scoreLabel);
        topPanel.add(statusLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // --- Center Panel: Game board (3x3 grid) ---
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardButtons = new JButton[3][3];

        // Create 3x3 grid buttons and attach event listeners
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.BOLD, 40));
                final int r = row;
                final int c = col;
                button.addActionListener(e -> handlePlayerMove(r, c));
                boardButtons[row][col] = button;
                boardPanel.add(button);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        // --- Bottom Panel: Play Again and End Game buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton resetButton = new JButton("Play Again");
        JButton exitButton = new JButton("End Game");

        resetButton.addActionListener(e -> resetBoard());
        exitButton.addActionListener(e -> System.exit(0));

        bottomPanel.add(resetButton);
        bottomPanel.add(exitButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true); // Show the frame
    }

    /**
     * Handles player's move when they click a cell.
     */
    private void handlePlayerMove(int row, int col) {
        JButton btn = boardButtons[row][col];
        if (!btn.getText().equals("") || !isPlayerTurn) return; // Invalid move

        btn.setText("O");
        btn.setForeground(Color.BLUE); // Player uses "O"
        statusLabel.setText("Turn: AI");
        isPlayerTurn = false;

        // Check if player won
        if (checkWinner("O")) {
            playerScore++;
            updateScoreboard();
            JOptionPane.showMessageDialog(frame, "Player wins the round!");
            resetBoard();
            return;
        }

        // Check for draw
        if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetBoard();
            return;
        }

        // Small delay to simulate AI thinking
        Timer timer = new Timer(300, e -> handleAIMove());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Handles AI move using the Minimax algorithm.
     */
    private void handleAIMove() {
        Move best = getBestMove(); // Get best move from AI
        boardButtons[best.row][best.col].setText("X");
        boardButtons[best.row][best.col].setForeground(Color.RED); // AI uses X

        // Check if AI won
        if (checkWinner("X")) {
            aiScore++;
            updateScoreboard();
            JOptionPane.showMessageDialog(frame, "AI wins the round!");
            resetBoard();
            return;
        }

        // Check for draw
        if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetBoard();
            return;
        }

        // Switch turn back to player
        statusLabel.setText("Turn: Player");
        isPlayerTurn = true;
    }

    /**
     * Updates the scoreboard label.
     */
    private void updateScoreboard() {
        scoreLabel.setText("Player: " + playerScore + "   |   AI: " + aiScore);
    }

    /**
     * Checks if a player has won with the given symbol ("X" or "O").
     */
    private boolean checkWinner(String mark) {
        for (int i = 0; i < 3; i++) {
            if (boardButtons[i][0].getText().equals(mark) &&
                boardButtons[i][1].getText().equals(mark) &&
                boardButtons[i][2].getText().equals(mark)) return true;

            if (boardButtons[0][i].getText().equals(mark) &&
                boardButtons[1][i].getText().equals(mark) &&
                boardButtons[2][i].getText().equals(mark)) return true;
        }

        if (boardButtons[0][0].getText().equals(mark) &&
            boardButtons[1][1].getText().equals(mark) &&
            boardButtons[2][2].getText().equals(mark)) return true;

        if (boardButtons[0][2].getText().equals(mark) &&
            boardButtons[1][1].getText().equals(mark) &&
            boardButtons[2][0].getText().equals(mark)) return true;

        return false;
    }

    /**
     * Checks if the board is full (no more moves).
     */
    private boolean isBoardFull() {
        for (JButton[] row : boardButtons) {
            for (JButton btn : row) {
                if (btn.getText().equals("")) return false;
            }
        }
        return true;
    }

    /**
     * Clears the board and resets for the next round.
     */
    private void resetBoard() {
        for (JButton[] row : boardButtons) {
            for (JButton btn : row) {
                btn.setText("");
                btn.setForeground(Color.BLACK);
            }
        }
        isPlayerTurn = true;
        statusLabel.setText("Turn: Player");
    }

    /**
     * Helper class to represent a board move.
     */
    private class Move {
        int row, col;
        Move(int r, int c) {
            row = r;
            col = c;
        }
    }

    /**
     * Gets the best move for the AI using the Minimax algorithm.
     */
    private Move getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(0, 0);

        // Try all empty cells
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (boardButtons[row][col].getText().equals("")) {
                    boardButtons[row][col].setText("X"); // Try move
                    int score = minimax(0, false); // Minimize opponent's move
                    boardButtons[row][col].setText(""); // Undo move

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(row, col);
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Minimax Algorithm:
     * Simulates all possible game outcomes to choose the best move.
     * @param depth    current depth in the game tree
     * @param isAITurn true if it's AI's turn, false for player
     * @return score representing the outcome (+10 win, -10 lose, 0 draw)
     */
    private int minimax(int depth, boolean isAITurn) {
        if (checkWinner("X")) return 10 - depth;     // AI wins
        if (checkWinner("O")) return depth - 10;     // Player wins
        if (isBoardFull()) return 0;                 // Draw

        if (isAITurn) {
            int best = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (boardButtons[row][col].getText().equals("")) {
                        boardButtons[row][col].setText("X");
                        int score = minimax(depth + 1, false);
                        boardButtons[row][col].setText("");
                        best = Math.max(score, best);
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (boardButtons[row][col].getText().equals("")) {
                        boardButtons[row][col].setText("O");
                        int score = minimax(depth + 1, true);
                        boardButtons[row][col].setText("");
                        best = Math.min(score, best);
                    }
                }
            }
            return best;
        }
    }

    /**
     * Main method to start the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeMinimax::new);
    }
}
