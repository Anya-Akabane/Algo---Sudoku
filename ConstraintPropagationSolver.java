public class ConstraintPropagationSolver implements SudokuSolver {
    @Override
    public int[][] solve(int[][] puzzle) {
        CPSudokuSolver cpSolver = new CPSudokuSolver();
        return cpSolver.solve(puzzle); // Assuming CPSudokuSolver has a `solve` method
    }
}