package solvers;
import algorithms.Heuristics;
import solver.SudokuSolver;

public class HeuristicsSolver implements SudokuSolver {
    private int steps = 0; // Steps counter

    @Override
    public int[][] solve(int[][] puzzle) {
        Heuristics heuristics = new Heuristics();
        if (heuristics.solve(puzzle)) {
            steps = heuristics.getSteps(); // Retrieve the number of steps from Heuristics
            return puzzle;
        } else {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }
    }

    public int getSteps() {
        return steps; // Return the updated steps
    }
}