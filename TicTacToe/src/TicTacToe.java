import java.util.InputMismatchException;
import java.util.Scanner;

public class TicTacToe {

    private static final char HUMAN_PLAYER = 'X';
    private static final char AI_PLAYER = 'O';
    private static final char EMPTY = '_';
    
    private char[][] board = {
        { EMPTY, EMPTY, EMPTY },
        { EMPTY, EMPTY, EMPTY },
        { EMPTY, EMPTY, EMPTY }
    };

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.playGame();
    }

    private void playGame() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to Tic-Tac-Toe! You are 'X', AI is 'O'.");
    printBoard();
    
    while (true) {
        int row = -1, col = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("Enter your move (row[0-2] and column[0-2]): ");
                row = scanner.nextInt();
                col = scanner.nextInt();

                if (row >= 0 && row <= 2 && col >= 0 && col <= 2 && board[row][col] == EMPTY) {
                    validInput = true;
                } else {
                    System.out.println("Invalid move. Please enter a row and column between 0 and 2 that is not already taken.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter integers only.");
                scanner.next();
            }
        }

        makeMove(row, col, HUMAN_PLAYER);
        printBoard();

        if (checkWin(HUMAN_PLAYER)) {
            System.out.println("You win!");
            break;
        }

        if (isBoardFull()) {
            System.out.println("It's a draw!");
            break;
        }

        System.out.println("AI is making its move...");
        Move bestMove = findBestMove();
        makeMove(bestMove.row, bestMove.col, AI_PLAYER);
        printBoard();

        if (checkWin(AI_PLAYER)) {
            System.out.println("AI wins!");
            break;
        }

        if (isBoardFull()) {
            System.out.println("It's a draw!");
            break;
        }
    }

    scanner.close();
}

    private void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean makeMove(int row, int col, char player) {
        if (board[row][col] == EMPTY) {
            board[row][col] = player;
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWin(char player) {

        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        return false;
    }

    private int minimax(int depth, boolean isMaximizing) {
        if (checkWin(AI_PLAYER)) {
            return 10 - depth;
        }
        if (checkWin(HUMAN_PLAYER)) {
            return depth - 10;
        }
        if (isBoardFull()) {
            return 0;
        }

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

    private static class Move {
        int row, col;

        Move(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}