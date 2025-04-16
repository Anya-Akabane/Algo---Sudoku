import java.util.Arrays;

public class Backtracking {
    static int steps = 0; // Counter for steps

    public static void display(int[][] board) {
        for(int[] arr : board) {
            System.out.println(Arrays.toString(arr));
        }
    }

    public static boolean isSafe(int[][] board, int row, int col, int i) {
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

    public static boolean sudoku(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int current = board[i][j];
                if (current == 0) {
                    for (int checkedValue = 1; checkedValue <= 9; checkedValue++) {
                        if (isSafe(board, i, j, checkedValue)) {
                            board[i][j] = checkedValue;
                            if (sudoku(board)) {
                                return true;
                            }
                            board[i][j] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String args[]) {
        int[][] board= {
            {0, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 6, 0, 0, 0, 0, 3},
            {0, 7, 4, 0, 8, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 0, 2},
            {0, 8, 0, 0, 4, 0, 0, 1, 0},
            {6, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 7, 8, 0},
            {5, 0, 0, 0, 0, 9, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 4, 0}
        };

        long startTime = System.nanoTime(); // Start
        sudoku(board);
        long endTime = System.nanoTime(); // End

        display(board);

        double timeTaken = (endTime - startTime) / 1e9;

        System.out.println("Steps taken: " + steps);
        System.out.println("Time taken: " + timeTaken + " s");
    }
}