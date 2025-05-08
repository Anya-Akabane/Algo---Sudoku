public interface SudokuSolver {
    /**
     * Solves the given Sudoku puzzle.
     * 
     * @param puzzle A 9x9 2D array representing the Sudoku puzzle.
     *               0 represents empty cells.
     * @return A solved 9x9 2D array, or throws an exception if no solution exists.
     */
    int[][] solve(int[][] puzzle);
}