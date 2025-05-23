package solvers;
import algorithms.Backtracking;
import solver.SudokuSolver;

public class BacktrackingSolver implements SudokuSolver {
    private int steps = 0;
    private double avgMemoryUsage;

    @Override
    public int[][] solve(int[][] puzzle) {
        Backtracking backtracking = new Backtracking();
        final int[][] solution = new int[9][9]; // Placeholder for the solution

        // Create a thread to run the backtracking algorithm
        Thread solverThread = new Thread(() -> {
            // Simulate no solution found within 2 minutes
            // try {
            //     // Simulate a long-running process by sleeping for 2 minutes and 30 seconds
            //     Thread.sleep(150_000); // 150,000 milliseconds = 2 minutes and 30 seconds
            // } catch (InterruptedException e) {
            //     // Handle thread interruption
            //     System.out.println("Solver thread was interrupted.");
            // }

            if (backtracking.solve(puzzle)) {
                // Copy the solved puzzle to the solution array
                for (int i = 0; i < 9; i++) {
                    System.arraycopy(puzzle[i], 0, solution[i], 0, 9);
                }
                steps = backtracking.getSteps(); // Update steps from Backtracking instance
                avgMemoryUsage = backtracking.getAvgMemoryUsage();
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

    public double getAvgMemoryUsage() {
        return avgMemoryUsage;
    }

    public int getSteps() {
        return steps;
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