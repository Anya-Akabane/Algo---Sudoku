package algorithms;
import java.util.Arrays;

public class Backtracking {
    private int steps = 0; // Counter for steps
    private long[] memoryUsages = new long[1000]; // Adjust size as needed
    private int memoryUsageCount = 0;
    private double avgMemoryUsage;
    private MemoryTracker.State memoryState = new MemoryTracker.State(0, 0);

    public void display(int[][] board) {
        for(int[] arr : board) {
            System.out.println(Arrays.toString(arr));
        }
    }

    public boolean isSafe(int[][] board, int row, int col, int i) {
        // Increment step counter
        steps++;

        //check row
        for(int a = 0; a < board.length; a++) {
            if(board[a][col] == i) {
                return false;
            }
        }

        //check col
        for(int b = 0; b < board[row].length; b++) {
            if(board[row][b] == i) {
                return false;
            }
        }

        //check cell
        int strow = row - (row % 3);
        int stcol = col - (col % 3);
        
        for(int x = strow; x < strow + 3; x++) {
            for(int y = stcol; y < stcol + 3; y++) {
                if(board[x][y] == i) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean solve(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int current = board[i][j];
                if (current == 0) {
                    for (int checkedValue = 1; checkedValue <= 9; checkedValue++) {
                        if (isSafe(board, i, j, checkedValue)) {
                            board[i][j] = checkedValue;

                            MemoryTracker.trackMemoryUsage(steps, memoryState, memoryUsages);
                            
                            if (solve(board)) {
                                return true;
                            }
                            board[i][j] = 0;
                        }
                    }
                    return false;
                }
            }
        }

        memoryUsageCount = memoryState.memoryUsageCount;
        // Calculate and print average memory usage
        long total = 0;
        for (int i = 0; i < memoryUsageCount; i++) {
            total += memoryUsages[i];
        }
        if (memoryUsageCount > 0) {
            avgMemoryUsage = total / (double) memoryUsageCount / 1024; // in KB
            // System.out.println("Average memory used: " + NumberFormat.getInstance().format(avgMemoryUsage) + " KB");
        }

        return true;
    }

    public static void main(String args[]) {
        int[][] board= {
            {1, 0, 3, 9, 4, 0, 0, 8, 6},
            {4, 0, 0, 0, 2, 5, 0, 0, 0},
            {0, 0, 0, 8, 0, 0, 0, 0, 0},
            {0, 0, 4, 0, 0, 0, 0, 6, 0},
            {0, 0, 2, 0, 0, 0, 0, 0, 0},
            {3, 0, 6, 0, 0, 1, 2, 0, 0},
            {8, 3, 9, 0, 0, 0, 6, 5, 0},
            {0, 0, 1, 0, 0, 0, 0, 7, 9},
            {2, 7, 0, 0, 9, 0, 3, 4, 0}
        };

        Backtracking backtracking = new Backtracking();
        long startTime = System.nanoTime(); // Start
        // Solve the sudoku puzzle
        if (backtracking.solve(board)) {
            // End the timer
            long endTime = System.nanoTime();

            // Display the solved board
            backtracking.display(board);

            // Calculate and display the time taken
            double timeTaken = (endTime - startTime) / 1e9;
            System.out.println("Steps taken: " + backtracking.steps);
            System.out.println("Time taken: " + timeTaken + " s");
        } else {
            System.out.println("No solution exists for the given Sudoku puzzle.");
        }
    }
    
    public int getSteps() {
        return steps;
    }
    
    public long[] getMemoryUsages() {
        return memoryUsages;
    }

    public int getMemoryUsageCount() {
        return memoryUsageCount;
    }

    public double getAvgMemoryUsage() {
        return avgMemoryUsage;
    }
}