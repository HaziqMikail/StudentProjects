import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Portfolio {
    private JFrame frame;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private int playerScore = 0;
    private int aiScore = 0;
    private boolean playerTurn = true;

    public Portfolio() {
        frame = new JFrame("Tic Tac Toe - Minimax AI");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top panel contains the scoreboard and current turn label
        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        JLabel title = new JLabel("SCORE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        scoreLabel = new JLabel("Player: 0    | AI: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        

        statusLabel = new JLabel("Turn: Player", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setForeground(Color.BLACK); // Turn label is black

        topPanel.add(title);
        topPanel.add(scoreLabel);
        topPanel.add(statusLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Center panel contains the game board
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.BOLD, 40));
                final int row = i;
                final int col = j;
                button.addActionListener(e -> handlePlayerMove(row, col));
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        // Bottom panel contains control buttons
        JPanel bottomPanel = new JPanel();
        JButton playAgainButton = new JButton("Play Again");
        JButton exitButton = new JButton("End Game");

        playAgainButton.addActionListener(e -> resetBoard());
        exitButton.addActionListener(e -> System.exit(0));

        bottomPanel.add(playAgainButton);
        bottomPanel.add(exitButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void handlePlayerMove(int row, int col) {
        JButton button = buttons[row][col];
        if (!button.getText().equals("") || !playerTurn) return;

        button.setText("O");
        button.setForeground(Color.BLUE); // Player is blue and uses O
        statusLabel.setText("Turn: AI");
        playerTurn = false;

        if (checkWinner("O")) {
            playerScore++;
            scoreLabel.setText("Player: " + playerScore + "                       | AI: " + aiScore);
            JOptionPane.showMessageDialog(frame, "Player wins the round!");
            resetBoard();
            return;
        }

        if (isFull()) {
            JOptionPane.showMessageDialog(frame, "It's a draw!");
            resetBoard();
            return;
        }

        // Short delay for AI move to simulate thinking
        Timer timer = new Timer(200, e -> handleAIMove());
        timer.setRepeats(false);
        timer.start();
    }

    private void handleAIMove() {
        Move bestMove = findBestMove();
        buttons[bestMove.row][bestMove.col].setText("X");
        buttons[bestMove.row][bestMove.col].setForeground(Color.RED); // AI is red and uses X

        if (checkWinner("X")) {
            aiScore++;
            scoreLabel.setText("Player: " + playerScore + "                       | AI: " + aiScore);
            JOptionPane.showMessageDialog(frame, " AI wins the round!");
            resetBoard();
            return;
        }

        if (isFull()) {
            JOptionPane.showMessageDialog(frame, "🤝 It's a draw!");
            resetBoard();
            return;
        }

        statusLabel.setText("Turn: Player");
        playerTurn = true;
    }

    private boolean checkWinner(String player) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(player) &&
                buttons[i][1].getText().equals(player) &&
                buttons[i][2].getText().equals(player)) return true;
            if (buttons[0][i].getText().equals(player) &&
                buttons[1][i].getText().equals(player) &&
                buttons[2][i].getText().equals(player)) return true;
        }
        if (buttons[0][0].getText().equals(player) &&
            buttons[1][1].getText().equals(player) &&
            buttons[2][2].getText().equals(player)) return true;
        if (buttons[0][2].getText().equals(player) &&
            buttons[1][1].getText().equals(player) &&
            buttons[2][0].getText().equals(player)) return true;
        return false;
    }

    private boolean isFull() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                if (btn.getText().equals("")) return false;
            }
        }
        return true;
    }

    private void resetBoard() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                btn.setText("");
                btn.setForeground(Color.BLACK);
            }
        }
        playerTurn = true;
        statusLabel.setText("Turn: Player");
    }

    private class Move {
        int row, col;
        Move(int r, int c) {
            row = r;
            col = c;
        }
    }

    private Move findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(0, 0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText("X");
                    int score = minimax(0, false);
                    buttons[i][j].setText("");

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(i, j);
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkWinner("X")) return 10 - depth;
        if (checkWinner("O")) return depth - 10;
        if (isFull()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().equals("")) {
                        buttons[i][j].setText("X");
                        int score = minimax(depth + 1, false);
                        buttons[i][j].setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().equals("")) {
                        buttons[i][j].setText("O");
                        int score = minimax(depth + 1, true);
                        buttons[i][j].setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Portfolio::new);
    }
}
