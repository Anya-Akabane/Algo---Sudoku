package solvers;
import algorithms.DLX;
import solver.SudokuSolver;

public class DLXSolver implements SudokuSolver {
    private int steps = 0; // Steps counter

    @Override
    public int[][] solve(int[][] puzzle) {
        DLX dlx = new DLX(puzzle);
        if (dlx.solve()) {
            steps = dlx.getStepCount(); // Retrieve the number of steps from Heuristics
            return dlx.getSolution();
        } else {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }
    }

    public int getSteps() {
        return steps; // Return the updated steps
    }
}