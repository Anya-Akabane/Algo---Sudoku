package solvers;
import algorithms.Backtracking;
import solver.SudokuSolver;

public class BacktrackingSolver implements SudokuSolver {
    private int steps = 0;

    @Override
    public int[][] solve(int[][] puzzle) {
        Backtracking backtracking = new Backtracking();
        if (backtracking.solve(puzzle)) {
            steps = backtracking.getSteps(); // Update steps from Backtracking instance
            return puzzle;
        } else {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }
    }

    public int getSteps() {
        return steps;
    }
}