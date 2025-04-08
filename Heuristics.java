public class Heuristics {

    static final int SIZE = 9; // Size of the Sudoku board (9x9)

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
            printBoard(board);
        } else {
            System.out.println("No solution exists.");
        }
    }

    // Print Sudoku board
    static void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("------+-------+------");

            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");
                System.out.print(board[i][j] == 0 ? ". " : board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Solve Sudoku using backtracking with heuristics
    static boolean solveSudoku(int[][] board) {
        int[] cell = findMostConstrainedCell(board);
        if (cell == null) return true; // Sudoku solved

        int row = cell[0], col = cell[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;

                // Recursively try to solve the rest of the board
                if (solveSudoku(board)) return true;

                // Backtrack if not possible
                board[row][col] = 0;
            }
        }

        return false;
    }

    // Find the most constrained cell (MCV)
    static int[] findMostConstrainedCell(int[][] board) {
        int minOptions = SIZE + 1;
        int[] bestCell = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    int options = countValidOptions(board, row, col);
                    if (options < minOptions) {
                        minOptions = options;
                        bestCell = new int[]{row, col};
                    }
                    if (minOptions == 1) return bestCell; // Optimize early
                }
            }
        }

        return bestCell;
    }

    // Count valid options for a specific cell
    static int countValidOptions(int[][] board, int row, int col) {
        int count = 0;
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) count++;
        }
        return count;
    }

    // Check if placing a number at (row, col) is valid
    static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        // Check 3x3 grid
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }

        return true;
    }
}