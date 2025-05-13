// import java.util.Scanner;

// import solver.SudokuSolver;
// import solvers.BacktrackingSolver;
// import solvers.ConstraintPropagationSolver;
// import solvers.DLXSolver;
// import solvers.HeuristicsSolver;

// public class RMIT_Sudoku_Solver {
//     private SudokuSolver solver; // Store the actual solver instance

//     public int[][] solve(int[][] puzzle) { 
//         // Use the factory to get the default solver (e.g., "backtracking")
//         // backtracking | dlx | heuristics | constraint
//         // solver = SudokuSolverFactory.getSolver("heuristics");

//         // Solve the puzzle using the selected algorithm
//         return solver.solve(puzzle);
//     }

//     public static void main(String[] args) {
//         // Create an instance of the SudokuGenerator
//         SudokuGenerator generator = new SudokuGenerator();

//         // Generate a puzzle with 30 clues
//         int clues = 30; // You can adjust this number to control the difficulty
//         int[][] puzzle = generator.generatePuzzle(clues);

//         // Print the generated puzzle
//         System.out.println("Generated Puzzle:");
//         printGrid(puzzle);

//         System.out.println("\nGenerated Puzzle (2D Array Format):");
//         printGridAsArray(puzzle);

//         // Prompt the user to select a solving algorithm
//         Scanner scanner = new Scanner(System.in);
//         System.out.println("\nSelect a solving algorithm:");
//         System.out.println("1 - Backtracking");
//         System.out.println("2 - Heuristics");
//         System.out.println("3 - Constraint Propagation");
//         System.out.println("4 - DLX");
//         System.out.println("5 - (3000 puzzles) Backtracking");
//         System.out.println("6 - (3000 puzzles) Heuristics");
//         System.out.println("7 - (3000 puzzles) Constraint Propagation");
//         System.out.println("8 - (3000 puzzles) DLX");
//         System.out.println("9 - (3000 puzzles) All");
//         System.out.print("Enter your choice (1-4): ");

//         int choice = scanner.nextInt();
//         scanner.close();

//         RMIT_Sudoku_Solver solverInstance = new RMIT_Sudoku_Solver();

//         // Set the solver based on the user's choice
//         switch (choice) {
//             case 1:
//                 solverInstance.solver = new BacktrackingSolver();
//                 break;
//             case 2:
//                 solverInstance.solver = new HeuristicsSolver();
//                 break;
//             case 3:
//                 solverInstance.solver = new ConstraintPropagationSolver();
//                 break;
//             case 4:
//                 solverInstance.solver = new DLXSolver();
//                 break;
//             default:
//                 System.out.println("Invalid choice. Defaulting to Backtracking.");
//                 solverInstance.solver = new BacktrackingSolver();
//         }

//         // Start the timer
//         long startTime = System.nanoTime();

//         // Solve the puzzle using the default algorithm
//         int[][] solution = solverInstance.solve(puzzle);

//         // End the timer
//         long endTime = System.nanoTime();

//         // Calculate the elapsed time in milliseconds
//         double elapsedTime = (endTime - startTime) / 1e6;

//         // Print the solved puzzle
//         System.out.println("\nSolved Sudoku:");
//         printGrid(solution);

//         // Retrieve and print the number of steps
//         if (solverInstance.solver instanceof BacktrackingSolver) {
//             int steps = ((BacktrackingSolver) solverInstance.solver).getSteps();
//             System.out.println("Steps taken: " + steps);
//         } else if (solverInstance.solver instanceof HeuristicsSolver) {
//             int steps = ((HeuristicsSolver) solverInstance.solver).getSteps();
//             System.out.println("Steps taken: " + steps);
//         } else if (solverInstance.solver instanceof ConstraintPropagationSolver) {
//             int steps = ((ConstraintPropagationSolver) solverInstance.solver).getSteps();
//             System.out.println("Steps taken: " + steps);
//         } else {
//             int steps = ((DLXSolver) solverInstance.solver).getSteps();
//             System.out.println("Steps taken: " + steps);
//         }

//         // Print the time taken
//         System.out.printf("Time taken to solve: %.3f ms%n", elapsedTime);
//     }

//     // Helper method to print a Sudoku grid
//     private static void printGrid(int[][] grid) {
//         for (int[] row : grid) {
//             for (int cell : row) {
//                 System.out.print((cell == 0 ? "." : cell) + " ");
//             }
//             System.out.println();
//         }
//     }

//     // Helper method to print a Sudoku grid in 2D array format
//     private static void printGridAsArray(int[][] grid) {
//         System.out.println("{");
//         for (int i = 0; i < grid.length; i++) {
//             System.out.print("    {");
//             for (int j = 0; j < grid[i].length; j++) {
//                 System.out.print(grid[i][j]);
//                 if (j < grid[i].length - 1) {
//                     System.out.print(", ");
//                 }
//             }
//             System.out.print("}");
//             if (i < grid.length - 1) {
//                 System.out.println(",");
//             } else {
//                 System.out.println();
//             }
//         }
//         System.out.println("};");
//     }
// }

import java.util.Scanner;

import solver.SudokuSolver;
import solvers.BacktrackingSolver;
import solvers.ConstraintPropagationSolver;
import solvers.DLXSolver;
import solvers.HeuristicsSolver;

public class RMIT_Sudoku_Solver {
    private SudokuSolver solver; // Store the actual solver instance

    public int[][] solve(int[][] puzzle) { 
        // Solve the puzzle using the selected algorithm
        return solver.solve(puzzle);
    }

    public static void main(String[] args) {
        // Create an instance of the SudokuGenerator
        SudokuGenerator generator = new SudokuGenerator();

        // Prompt the user to select a solving algorithm
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nSelect a solving algorithm:");
        System.out.println("1 - Backtracking");
        System.out.println("2 - Heuristics");
        System.out.println("3 - Constraint Propagation");
        System.out.println("4 - DLX");
        System.out.println("5 - (3000 puzzles) Backtracking");
        System.out.println("6 - (3000 puzzles) Heuristics");
        System.out.println("7 - (3000 puzzles) Constraint Propagation");
        System.out.println("8 - (3000 puzzles) DLX");
        System.out.println("9 - (3000 puzzles) All");
        System.out.print("Enter your choice (1-9): ");

        int choice = scanner.nextInt();
        scanner.close();

        RMIT_Sudoku_Solver solverInstance = new RMIT_Sudoku_Solver();

        // Handle options 1-4 (single puzzle solving)
        if (choice >= 1 && choice <= 4) {
            solverInstance.solver = getSolverByChoice(choice);
            solveSinglePuzzle(generator, solverInstance);
        } 
        // Handle options 5-8 (3000 puzzles with a single algorithm)
        else if (choice >= 5 && choice <= 8) {
            SudokuSolver solver = getSolverByChoice(choice - 4);
            solveMultiplePuzzles(generator, solver, 3000);
        } 
        // Handle option 9 (3000 puzzles with all algorithms)
        else if (choice == 9) {
            solveAllAlgorithms(generator, 3000);
        } 
        // Invalid choice
        else {
            System.out.println("Invalid choice. Exiting.");
        }
    }

    // Helper method to get the solver instance based on user choice
    private static SudokuSolver getSolverByChoice(int choice) {
        switch (choice) {
            case 1:
                return new BacktrackingSolver();
            case 2:
                return new HeuristicsSolver();
            case 3:
                return new ConstraintPropagationSolver();
            case 4:
                return new DLXSolver();
            default:
                throw new IllegalArgumentException("Invalid solver choice.");
        }
    }

    // Solve a single puzzle
    private static void solveSinglePuzzle(SudokuGenerator generator, RMIT_Sudoku_Solver solverInstance) {
        int clues = 30; // You can adjust this number to control the difficulty
        int[][] puzzle = generator.generatePuzzle(clues);

        // Print the generated puzzle
        System.out.println("Generated Puzzle:");
        printGrid(puzzle);
        printGridAsArray(puzzle);

        // Start the timer
        long startTime = System.nanoTime();

        // Solve the puzzle
        int[][] solution = solverInstance.solve(puzzle);

        // End the timer
        long endTime = System.nanoTime();

        // Calculate the elapsed time in milliseconds
        double elapsedTime = (endTime - startTime) / 1e6;

        // Print the solved puzzle
        System.out.println("\nSolved Sudoku:");
        printGrid(solution);

        // Retrieve and print the number of steps
        printSteps(solverInstance.solver);

        // Print the time taken
        System.out.printf("Time taken to solve: %.3f ms%n", elapsedTime);
    }

    // Solve multiple puzzles with a single algorithm
    private static void solveMultiplePuzzles(SudokuGenerator generator, SudokuSolver solver, int puzzleCount) {
        long totalSteps = 0;
        long totalTime = 0;

        for (int i = 0; i < puzzleCount; i++) {
            int[][] puzzle = generator.generatePuzzle(30); // Generate a puzzle with 30 clues

            long startTime = System.nanoTime();
            solver.solve(puzzle);
            long endTime = System.nanoTime();

            totalTime += (endTime - startTime);
            totalSteps += getStepsFromSolver(solver);
        }

        System.out.println("\nResults for " + puzzleCount + " puzzles:");
        System.out.println("Total time taken: " + (totalTime / 1e6) + " ms");
        System.out.println("Average time per puzzle: " + (totalTime / puzzleCount / 1e6) + " ms");
        System.out.println("Average steps per puzzle: " + (totalSteps / puzzleCount));
    }

    // Solve multiple puzzles with all algorithms
    private static void solveAllAlgorithms(SudokuGenerator generator, int puzzleCount) {
        System.out.println("\nSolving " + puzzleCount + " puzzles with all algorithms...");

        for (int i = 1; i <= 4; i++) {
            SudokuSolver solver = getSolverByChoice(i);
            System.out.println("\nAlgorithm: " + solver.getClass().getSimpleName());
            solveMultiplePuzzles(generator, solver, puzzleCount);
        }
    }

    // Helper method to print the number of steps
    private static void printSteps(SudokuSolver solver) {
        int steps = getStepsFromSolver(solver);
        System.out.println("Steps taken: " + steps);
    }

    // Helper method to get steps from a solver
    private static int getStepsFromSolver(SudokuSolver solver) {
        if (solver instanceof BacktrackingSolver) {
            return ((BacktrackingSolver) solver).getSteps();
        } else if (solver instanceof HeuristicsSolver) {
            return ((HeuristicsSolver) solver).getSteps();
        } else if (solver instanceof ConstraintPropagationSolver) {
            return ((ConstraintPropagationSolver) solver).getSteps();
        } else if (solver instanceof DLXSolver) {
            return ((DLXSolver) solver).getSteps();
        }
        return 0;
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