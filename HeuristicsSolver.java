public class HeuristicsSolver implements SudokuSolver {
    @Override
    public int[][] solve(int[][] puzzle) {
        if (solveSudoku(puzzle)) {
            return puzzle;
        } else {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }
    }

    private boolean solveSudoku(int[][] board) {
        int[] cell = findMostConstrainedCell(board);
        if (cell == null) return true;

        int row = cell[0], col = cell[1];
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;
                if (solveSudoku(board)) return true;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private int[] findMostConstrainedCell(int[][] board) {
        int minOptions = 10;
        int[] bestCell = null;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    int options = countValidOptions(board, row, col);
                    if (options < minOptions) {
                        minOptions = options;
                        bestCell = new int[]{row, col};
                    }
                }
            }
        }
        return bestCell;
    }

    private int countValidOptions(int[][] board, int row, int col) {
        int count = 0;
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) count++;
        }
        return count;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }
        return true;
    }
}