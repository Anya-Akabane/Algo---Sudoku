import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private Random random = new Random();

    // Generate a complete solution grid
    public int[][] generateSolution() {
        int[][] grid = new int[SIZE][SIZE];
        fillGrid(grid);
        return grid;
    }

    // Generate a puzzle with the specified number of clues
    public int[][] generatePuzzle(int clues) {
        int[][] solution = generateSolution();
        int[][] puzzle = copyGrid(solution);

        int cellsToRemove = SIZE * SIZE - clues;
        while (cellsToRemove > 0) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            if (puzzle[row][col] != 0) {
                int backup = puzzle[row][col];
                puzzle[row][col] = 0;

                if (!isUniquelySolvable(puzzle)) {
                    puzzle[row][col] = backup; // Restore the number
                } else {
                    cellsToRemove--;
                }
            }
        }
        return puzzle;
    }

    // Helper method to fill the grid with a valid solution
    private boolean fillGrid(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    ArrayList<Integer> candidates = getRandomCandidates();
                    for (int num : candidates) {
                        if (isSafe(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (fillGrid(grid)) {
                                return true;
                            }
                            grid[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // No valid number found
                }
            }
        }
        return true; // Grid is fully filled
    }

    // Check if a number can be safely placed in a cell
    private boolean isSafe(int[][] grid, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (grid[row][x] == num || grid[x][col] == num) {
                return false;
            }
        }
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check if the puzzle is uniquely solvable
    private boolean isUniquelySolvable(int[][] puzzle) {
        SudokuSolver solver = new BacktrackingSolver(); // Use your BacktrackingSolver
        int[][] solution1 = solver.solve(copyGrid(puzzle));
        int[][] solution2 = solver.solve(copyGrid(puzzle));

        return areGridsEqual(solution1, solution2);
    }

    // Compare two grids for equality
    private boolean areGridsEqual(int[][] grid1, int[][] grid2) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid1[row][col] != grid2[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Get a randomized list of candidate numbers
    private ArrayList<Integer> getRandomCandidates() {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            candidates.add(i);
        }
        Collections.shuffle(candidates); // Randomize the order
        return candidates;
    }

    // Create a deep copy of a grid
    private int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(grid[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }
}