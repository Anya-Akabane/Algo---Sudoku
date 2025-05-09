import solver.SudokuSolver;
import solvers.BacktrackingSolver;
import solvers.ConstraintPropagationSolver;
import solvers.DLXSolver;
import solvers.HeuristicsSolver;

public class RMIT_Sudoku_Solver {
    private SudokuSolver solver; // Store the actual solver instance

    public int[][] solve(int[][] puzzle) { 
        // Use the factory to get the default solver (e.g., "backtracking")
        // backtracking | dlx | heuristics | constraint
        solver = SudokuSolverFactory.getSolver("heuristics");

        // Solve the puzzle using the selected algorithm
        return solver.solve(puzzle);
    }

    public static void main(String[] args) {
        // Create an instance of the SudokuGenerator
        SudokuGenerator generator = new SudokuGenerator();

        // Generate a puzzle with 30 clues
        int clues = 30; // You can adjust this number to control the difficulty
        int[][] puzzle = generator.generatePuzzle(clues);

        // Print the generated puzzle
        System.out.println("Generated Puzzle:");
        printGrid(puzzle);

        System.out.println("\nGenerated Puzzle (2D Array Format):");
        printGridAsArray(puzzle);

        RMIT_Sudoku_Solver solverInstance = new RMIT_Sudoku_Solver();

        // Start the timer
        long startTime = System.nanoTime();

        // Solve the puzzle using the default algorithm
        int[][] solution = solverInstance.solve(puzzle);

        // End the timer
        long endTime = System.nanoTime();

        // Calculate the elapsed time in milliseconds
        double elapsedTime = (endTime - startTime) / 1e6;

        // Print the solved puzzle
        System.out.println("\nSolved Sudoku:");
        printGrid(solution);

        // Retrieve and print the number of steps
        if (solverInstance.solver instanceof BacktrackingSolver) {
            int steps = ((BacktrackingSolver) solverInstance.solver).getSteps();
            System.out.println("Steps taken: " + steps);
        } else if (solverInstance.solver instanceof HeuristicsSolver) {
            int steps = ((HeuristicsSolver) solverInstance.solver).getSteps();
            System.out.println("Steps taken: " + steps);
        } else if (solverInstance.solver instanceof ConstraintPropagationSolver) {
            int steps = ((ConstraintPropagationSolver) solverInstance.solver).getSteps();
            System.out.println("Steps taken: " + steps);
        } else {
            int steps = ((DLXSolver) solverInstance.solver).getSteps();
            System.out.println("Steps taken: " + steps);
        }

        // Print the time taken
        System.out.printf("Time taken to solve: %.3f ms%n", elapsedTime);
    }

    // Helper method to print a Sudoku grid
    private static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print((cell == 0 ? "." : cell) + " ");
            }
            System.out.println();
        }
    }

    // Helper method to print a Sudoku grid in 2D array format
    private static void printGridAsArray(int[][] grid) {
        System.out.println("{");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("    {");
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
                if (j < grid[i].length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("}");
            if (i < grid.length - 1) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }
        System.out.println("};");
    }
}
