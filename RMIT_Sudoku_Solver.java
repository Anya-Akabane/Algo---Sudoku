public class RMIT_Sudoku_Solver {
    public int[][] solve(int[][] puzzle) { 
        // Use the factory to get the default solver (e.g., "backtracking")
        // backtracking | dlx | heuristics | genetic | constraint
        SudokuSolver solver = SudokuSolverFactory.getSolver("backtracking");

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

        RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver();

        // Start the timer
        long startTime = System.nanoTime();

        // Solve the puzzle using the default algorithm
        int[][] solution = solver.solve(puzzle);

        // End the timer
        long endTime = System.nanoTime();

        // Calculate the elapsed time in milliseconds
        double elapsedTime = (endTime - startTime) / 1e6;

        // Print the solved puzzle
        System.out.println("\nSolved Sudoku:");
        printGrid(solution);

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
}
