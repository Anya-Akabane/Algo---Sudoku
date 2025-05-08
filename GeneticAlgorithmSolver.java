public class GeneticAlgorithmSolver implements SudokuSolver {
    @Override
    public int[][] solve(int[][] puzzle) {
        SudokuGA ga = new SudokuGA();
        return ga.solve(puzzle); // Assuming SudokuGA has a `solve` method
    }
}