package solvers;
import algorithms.ConstraintPropagation;
import solver.SudokuSolver;

public class ConstraintPropagationSolver implements SudokuSolver {
    private int steps = 0; // Steps counter

    @Override
    public int[][] solve(int[][] puzzle) {
        ConstraintPropagation cpSolver = new ConstraintPropagation();
        final int[][] solution = new int[9][9]; // Placeholder for the solution

        // Create a thread to run the constraint propagation algorithm
        Thread solverThread = new Thread(() -> {
            // // Simulate no solution found within 2 minutes
            // try {
            //     // Simulate a long-running process by sleeping for 2 minutes and 30 seconds
            //     Thread.sleep(150_000); // 150,000 milliseconds = 2 minutes and 30 seconds
            // } catch (InterruptedException e) {
            //     // Handle thread interruption
            //     System.out.println("Solver thread was interrupted.");
            // }

            int[][] result = cpSolver.solve(puzzle);
            if (result != null) {
                // Copy the solved puzzle to the solution array
                for (int i = 0; i < 9; i++) {
                    System.arraycopy(result[i], 0, solution[i], 0, 9);
                }
                steps = cpSolver.getSteps(); // Retrieve the number of steps
            }
        });

        // Start the thread
        solverThread.start();

        try {
            // Wait for the thread to complete within 2 minutes (120,000 milliseconds)
            solverThread.join(120_000);

            // Check if the thread is still running after the timeout
            if (solverThread.isAlive()) {
                // Interrupt the thread and throw a timeout exception
                solverThread.interrupt();
                throw new IllegalStateException("No solution found within 2 minutes.");
            }
        } catch (InterruptedException e) {
            // Handle thread interruption
            throw new IllegalStateException("Solver was interrupted.", e);
        }

        // Check if the solution array is still empty (no solution found)
        if (isEmpty(solution)) {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }

        return solution;
    }

    public int getSteps() {
        return steps; // Return the updated steps
    }

    // Helper method to check if the solution array is empty
    private boolean isEmpty(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}