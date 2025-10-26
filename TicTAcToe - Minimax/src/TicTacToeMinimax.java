import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * TicTacToeMinimax
 * A simple Tic Tac Toe game using Java Swing and Minimax algorithm for unbeatable AI.
 * Tracks scores and alternates turns between player and AI.
 */
public class TicTacToeMinimax {
    private JFrame frame;
    private JButton[][] boardButtons; // 3x3 grid buttons
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private int playerScore = 0;
    private int aiScore = 0;
    private boolean isPlayerTurn = true;

    /**
     * Constructor - Sets up the game window, layout, and UI components.
     */
    public TicTacToeMinimax() {
        // Setup the main window
        frame = new JFrame("Tic Tac Toe - Minimax AI");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // --- Top panel: Displays score and turn information ---
        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        JLabel titleLabel = new JLabel("SCOREBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        scoreLabel = new JLabel("Player: 0   |   AI: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        statusLabel = new JLabel("Turn: Player", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        topPanel.add(titleLabel);
        topPanel.add(scoreLabel);
        topPanel.add(statusLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // --- Center panel: 3x3 Tic Tac Toe board ---
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardButtons = new JButton[3][3];

        // Initialize each button in the grid
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

        // --- Bottom panel: Play Again and End Game buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton resetButton = new JButton("Play Again");
        JButton exitButton = new JButton("End Game");

        resetButton.addActionListener(e -> resetBoard());
        exitButton.addActionListener(e -> System.exit(0));

        bottomPanel.add(resetButton);
        bottomPanel.add(exitButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true); // Show the window
    }

    /**
     * Handles player's move when a button is clicked.
     */
    private void handlePlayerMove(int row, int col) {
        JButton btn = boardButtons[row][col];

        // Ignore if cell is already taken or not player's turn
        if (!btn.getText().equals("") || !isPlayerTurn) return;

        btn.setText("O"); // Player uses "O"
        btn.setForeground(Color.BLUE);
        statusLabel.setText("Turn: AI");
        isPlayerTurn = false;

        // Check if the player has won
        if (checkWinner("O")) {
            playerScore++;
            updateScoreboard();
            JOptionPane.showMessageDialog(frame, "Player wins the round!");
            resetBoard();
            return;
        }

        // If it's a draw (board is full), reset
        if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetBoard();
            return;
        }

        // Delay AI move slightly to simulate thinking
        Timer timer = new Timer(300, e -> handleAIMove());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * AI plays using Minimax algorithm to find the best move.
     */
    private void handleAIMove() {
        Move best = getBestMove(); // Calculate best move
        boardButtons[best.row][best.col].setText("X"); // AI uses "X"
        boardButtons[best.row][best.col].setForeground(Color.RED);

        // Check if AI has won
        if (checkWinner("X")) {
            aiScore++;
            updateScoreboard();
            JOptionPane.showMessageDialog(frame, "AI wins the round!");
            resetBoard();
            return;
        }

        // If it's a draw
        if (isBoardFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetBoard();
            return;
        }

        // Switch turn to player
        statusLabel.setText("Turn: Player");
        isPlayerTurn = true;
    }

    /**
     * Update the scoreboard label with current scores.
     */
    private void updateScoreboard() {
        scoreLabel.setText("Player: " + playerScore + "   |   AI: " + aiScore);
    }

    /**
     * Check if a given symbol ("X" or "O") has won.
     */
    private boolean checkWinner(String mark) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (
                boardButtons[i][0].getText().equals(mark) &&
                boardButtons[i][1].getText().equals(mark) &&
                boardButtons[i][2].getText().equals(mark)
            ) return true;

            if (
                boardButtons[0][i].getText().equals(mark) &&
                boardButtons[1][i].getText().equals(mark) &&
                boardButtons[2][i].getText().equals(mark)
            ) return true;
        }

        // Check diagonals
        if (
            boardButtons[0][0].getText().equals(mark) &&
            boardButtons[1][1].getText().equals(mark) &&
            boardButtons[2][2].getText().equals(mark)
        ) return true;

        if (
            boardButtons[0][2].getText().equals(mark) &&
            boardButtons[1][1].getText().equals(mark) &&
            boardButtons[2][0].getText().equals(mark)
        ) return true;

        return false;
    }

    /**
     * Check if the board is full (no empty cells left).
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
     * Clear the board and reset turn to player.
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
     * Simple helper class to store a move's row and column.
     */
    private class Move {
        int row, col;
        Move(int r, int c) {
            row = r;
            col = c;
        }
    }

    /**
     * Finds the best move for the AI using the Minimax algorithm.
     */
    private Move getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(0, 0);

        // Try every empty cell and simulate outcome
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (boardButtons[row][col].getText().equals("")) {
                    boardButtons[row][col].setText("X"); // AI tries this move
                    int score = minimax(0, false);       // Simulate player's response
                    boardButtons[row][col].setText("");  // Undo move

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
     * This function helps the AI choose the best move.
     * It checks all possible moves until the game ends (win, lose, or draw),
     * then picks the best one based on the result.
     *
     * @param depth How deep the checking goes (how many moves ahead)
     * @param isAITurn true = AI's turn, false = Player's turn
     * @return A score: 1 = AI win, -1 = Player win, 0 = Draw
     */

    private int minimax(int depth, boolean isAITurn) {
        // Base conditions
        if (checkWinner("X")) return 10 - depth;  // AI wins
        if (checkWinner("O")) return depth - 10;  // Player wins
        if (isBoardFull()) return 0;              // Draw

        if (isAITurn) {
            // AI's turn: maximize score
            int best = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (boardButtons[row][col].getText().equals("")) {
                        boardButtons[row][col].setText("X");
                        int score = minimax(depth + 1, false);
                        boardButtons[row][col].setText("");
                        best = Math.max(best, score);
                    }
                }
            }
            return best;
        } else {
            // Player's turn: minimize AI's score
            int best = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (boardButtons[row][col].getText().equals("")) {
                        boardButtons[row][col].setText("O");
                        int score = minimax(depth + 1, true);
                        boardButtons[row][col].setText("");
                        best = Math.min(best, score);
                    }
                }
            }
            return best;
        }
    }

    /**
     * Main entry point - launches the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeMinimax::new);
    }
}
