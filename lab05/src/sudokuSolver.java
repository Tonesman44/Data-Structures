public class sudokuSolver {
        private static final int BOARD_SIZE = 9;
        private static final int EMPTY_CELL = 0;

        public static void main(String[] args) {
            int[][] board = {
                    {5, 3, 0, 0, 7, 0, 0, 0, 0},
                    {6, 0, 0, 1, 9, 5, 0, 0, 0},
                    {0, 9, 8, 0, 0, 0, 0, 6, 0},
                    {8, 0, 0, 0, 6, 0, 0, 0, 3},
                    {4, 0, 0, 8, 0, 3, 0, 0, 1},
                    {7, 0, 0, 0, 2, 0, 0, 0, 6},
                    {0, 6, 0, 0, 0, 0, 2, 8, 0},
                    {0, 0, 0, 4, 1, 9, 0, 0, 5},
                    {0, 0, 0, 0, 8, 0, 0, 7, 9}
            };

            if (solveSudoku(board)) {
                System.out.println("Sudoku solved successfully:");
                printBoard(board);
            } else {
                System.out.println("No solution exists for this Sudoku puzzle.");
            }
        }

        private static boolean solveSudoku(int[][] board) {
            int row, col;

            // If there is no unassigned location, we are done
            if (!findEmptyCell(board)) {
                return true;
            }

            // Consider digits 1 to 9
            for (int num = 1; num <= 9; num++) {
                row = findEmptyCellRow(board);
                col = findEmptyCellColumn(board);

                // Check if num is safe for the current cell
                if (isSafe(board, row, col, num)) {
                    board[row][col] = num;

                    // If the assignment is successful, return true
                    if (solveSudoku(board)) {
                        return true;
                    }

                    // If the assignment leads to an invalid solution, backtrack
                    board[row][col] = EMPTY_CELL;
                }
            }
            // If no digit can be placed, return false
            return false;
        }

        private static boolean findEmptyCell(int[][] board) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    if (board[row][col] == EMPTY_CELL) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static int findEmptyCellRow(int[][] board) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    if (board[row][col] == EMPTY_CELL) {
                        return row;
                    }
                }
            }
            return -1; // No empty cell found
        }

        private static int findEmptyCellColumn(int[][] board) {
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    if (board[row][col] == EMPTY_CELL) {
                        return col;
                    }
                }
            }
            return -1; // No empty cell found
        }

        private static boolean isSafe(int[][] board, int row, int col, int num) {
            // Check if num is not already in the current row
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[row][c] == num) {
                    return false;
                }
            }

            // Check if num is not already in the current column
            for (int r = 0; r < BOARD_SIZE; r++) {
                if (board[r][col] == num) {
                    return false;
                }
            }

            // Check if num is not already in the current 3x3 box
            int boxStartRow = row - row % 3;
            int boxStartCol = col - col % 3;
            for (int r = boxStartRow; r < boxStartRow + 3; r++) {
                for (int c = boxStartCol; c < boxStartCol + 3; c++) {
                    if (board[r][c] == num) {
                        return false;
                    }
                }
            }

            return true;
        }

        private static void printBoard(int[][] board) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
        }
    }