import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TicTacToeGUI extends JFrame implements ActionListener {

    private static final char HUMAN_PLAYER = 'X';
    private static final char AI_PLAYER = 'O';
    private static final char EMPTY = '_';

    private JButton[][] buttons = new JButton[3][3];
    private char[][] board = {
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY}
    };

    public TicTacToeGUI() {
        setTitle("Tic-Tac-Toe");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        initializeButtons();

        setVisible(true);
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("-");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttonClicked == buttons[i][j] && board[i][j] == EMPTY) {
                    board[i][j] = HUMAN_PLAYER;
                    buttons[i][j].setText(String.valueOf(HUMAN_PLAYER));
                    if (checkWin(HUMAN_PLAYER)) {
                        JOptionPane.showMessageDialog(this, "You win!");
                        resetBoard();
                        return;
                    }
                    if (isBoardFull()) {
                        JOptionPane.showMessageDialog(this, "It's a draw!");
                        resetBoard();
                        return;
                    }

                    Move bestMove = findBestMove();
                    board[bestMove.row][bestMove.col] = AI_PLAYER;
                    buttons[bestMove.row][bestMove.col].setText(String.valueOf(AI_PLAYER));

                    if (checkWin(AI_PLAYER)) {
                        JOptionPane.showMessageDialog(this, "AI wins!");
                        resetBoard();
                    }
                    if (isBoardFull()) {
                        JOptionPane.showMessageDialog(this, "It's a draw!");
                        resetBoard();
                    }
                }
            }
        }
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
                buttons[i][j].setText("-");
            }
        }
    }

    private boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) return false;
            }
        }
        return true;
    }

    private Move findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(-1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = AI_PLAYER;
                    int moveScore = minimax(0, false);
                    board[i][j] = EMPTY;

                    if (moveScore > bestScore) {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestScore = moveScore;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkWin(AI_PLAYER)) return 10 - depth;
        if (checkWin(HUMAN_PLAYER)) return depth - 10;
        if (isBoardFull()) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = AI_PLAYER;
                        int score = minimax(depth + 1, false);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = HUMAN_PLAYER;
                        int score = minimax(depth + 1, true);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private static class Move {
        int row, col;

        Move(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}