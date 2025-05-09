package solvers;
import algorithms.ConstraintPropagation;
import solver.SudokuSolver;

public class ConstraintPropagationSolver implements SudokuSolver {
    private int steps = 0; // Steps counter

    @Override
    public int[][] solve(int[][] puzzle) {
        ConstraintPropagation cpSolver = new ConstraintPropagation();
        int[][] solution = cpSolver.solve(puzzle);
        steps = cpSolver.getSteps(); // Retrieve the number of steps
        return solution;
    }

    public int getSteps() {
        return steps; // Return the updated steps
    }
}