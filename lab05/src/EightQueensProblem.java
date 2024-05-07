import java.util.Scanner;

public class EightQueensProblem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of rows and columns (usually 8 for a standard chessboard): ");
        int rows = scanner.nextInt();
        int cols = rows; // Assuming a standard square chessboard

        System.out.print("Enter the number of queens to place: ");
        int queens = scanner.nextInt();

        int[][] board = new int[rows][cols];

        if (solveQueens(board, 0, queens)) {
            System.out.println("Solution found:");
            printBoard(board);
        } else {
            System.out.println("No solution exists.");
        }

        scanner.close();
    }

    private static boolean solveQueens(int[][] board, int row, int totalQueens) {
        if (totalQueens == 0) {
            // All queens have been placed
            return true;
        }

        for (int col = 0; col < board.length; col++) {
            if (isSafe(board, row, col)) {
                // Place queen at this position
                board[row][col] = 1;

                // Recursive call for next row
                if (solveQueens(board, row + 1, totalQueens - 1)) {
                    return true;
                }

                // Backtrack if placing queen at this position does not lead to a solution
                board[row][col] = 0;
            }
        }

        // No solution found for this row
        return false;
    }

    private static boolean isSafe(int[][] board, int row, int col) {
        // Check for same column
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 1) {
                return false;
            }
        }

        // Check top-left to bottom-right diagonal
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        // Check top-right to bottom-left diagonal
        for (int i = row, j = col; i >= 0 && j < board.length; i--, j++) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
