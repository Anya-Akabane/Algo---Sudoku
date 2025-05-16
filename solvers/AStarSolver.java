package solvers;
import algorithms.AStar;
import solver.SudokuSolver;

public class AStarSolver implements SudokuSolver {
    private int steps = 0;
    private double avgMemoryUsage = 0;

    @Override
    public int[][] solve(int[][] puzzle) {
        AStar astar = new AStar(puzzle);
        final int[][] solution = new int[9][9];

        Thread solverThread = new Thread(() -> {
            // Simulate no solution found within 2 minutes
            // try {
            //     // Simulate a long-running process by sleeping for 2 minutes and 30 seconds
            //     Thread.sleep(150_000); // 150,000 milliseconds = 2 minutes and 30 seconds
            // } catch (InterruptedException e) {
            //     // Handle thread interruption
            //     System.out.println("Solver thread was interrupted.");
            // }

            if (astar.solve()) {
                int[][] result = astar.getSolution();
                for (int i = 0; i < 9; i++) {
                    System.arraycopy(result[i], 0, solution[i], 0, 9);
                }
                steps = astar.getSteps();
                avgMemoryUsage = astar.getAvgMemoryUsage();
            }
        });

        solverThread.start();

        try {
            solverThread.join(120_000);
            if (solverThread.isAlive()) {
                solverThread.interrupt();
                throw new IllegalStateException("No solution found within 2 minutes.");
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException("Solver was interrupted.", e);
        }

        if (isEmpty(solution)) {
            throw new IllegalArgumentException("No solution exists for the given Sudoku puzzle.");
        }

        return solution;
    }

    public int getSteps() {
        return steps;
    }

    public double getAvgMemoryUsage() {
        return avgMemoryUsage;
    }

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