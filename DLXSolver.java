public class DLXSolver implements SudokuSolver {
    @Override
    public int[][] solve(int[][] puzzle) {
        DLX dlx = new DLX(puzzle);
        if (dlx.solve()) {
            return dlx.getSolution();
        } else {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }
    }
}