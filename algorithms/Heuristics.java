package algorithms;
public class Heuristics {
    private final int SIZE = 9;
    private int steps = 0;
 
    public static void main(String[] args) {
        int[][] board = {
            {0, 0, 0, 5, 0, 6, 3, 0, 0},
            {6, 0, 4, 0, 3, 0, 7, 0, 5},
            {5, 3, 2, 0, 0, 7, 4, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 5, 0, 0, 0, 0, 3, 4},
            {4, 0, 9, 3, 0, 8, 0, 2, 0},
            {0, 4, 0, 0, 0, 0, 0, 0, 0},
            {0, 2, 1, 0, 0, 5, 0, 7, 0},
            {7, 5, 0, 9, 0, 0, 2, 0, 0}
        };
 
        Heuristics heuristics = new Heuristics();
        long start = System.nanoTime();
        if (heuristics.solve(board)) {
            long end = System.nanoTime();
            heuristics.print(board);
            System.out.println("\nSteps: " + heuristics.steps);
            System.out.println("Time: " + ((end - start) / 1_000_000.0) + " ms");
        } else {
            System.out.println("No solution found.");
        }
    }

    // Print the Sudoku board
    // In bảng Sudoku
    public void print(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) System.out.println("------+-------+------");
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) System.out.print("| ");
                System.out.print((board[i][j] == 0 ? ". " : board[i][j] + " "));
            }
            System.out.println();
        }
    }
   
    public boolean solve(int[][] board) {
        // find the next best cell to fill using MRV
        int[] cell = findMRV(board);
        if (cell == null) return true;
 
        int row = cell[0];
        int col = cell[1];
 
        int[] values = getLCV(board, row, col);
        for (int i = 0; i < values.length; i++) {
            int val = values[i];
            if (val == 0) break;
            if (isValid(board, row, col, val)) {
                board[row][col] = val;
                steps++;
 
                // tries placing numbers, checks forward validity
                if (forwardCheck(board, row, col) && solve(board)) {
                    return true;
                }
                board[row][col] = 0; // backtrack if no solution found and tries next value
            }
        }
        return false;
    }
 
    public int[] findMRV(int[][] board) {
        int min = 10;
        int[] best = null;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    int count = countOptions(board, row, col);
                    if (count < min) {
                        min = count;
                        best = new int[]{row, col};
                        if (min == 1) return best;
                    }
                }
            }
        }
        return best;
    }

    // LCV: return the an array of valid values sorted by increasing conflicts
    // LCV: trả về mảng giá trị hợp lệ sắp xếp theo độ gây xung đột tăng dần
    public int[] getLCV(int[][] board, int row, int col) {
        int[] scores = new int[10];
        int[] result = new int[9];
        int idx = 0;
 
        for (int val = 1; val <= 9; val++) {
            if (isValid(board, row, col, val)) {
                scores[val] = countConstraints(board, row, col, val);
                result[idx++] = val;
            }
        }

        // Sắp xếp theo số lượng xung đột tăng dần
        // Sort by the number of conflicts in ascending order (Line 88)
        for (int i = 0; i < idx - 1; i++) {
            for (int j = i + 1; j < idx; j++) {
                if (scores[result[i]] > scores[result[j]]) {
                    int temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }
 
        return result;
    }

    // Forward checking: kiểm tra xem sau khi gán, các ô còn lại vẫn còn lựa chọn không
    // Forward checking: Check if after assigning, the remaining cells still have options (Line 101)
    public boolean forwardCheck(int[][] board, int row, int col) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 0 && countOptions(board, i, j) == 0)
                    return false;
            }
        }
        return true;
    }

    // Đếm số giá trị hợp lệ cho ô (row, col)
    // Count the number of valid values for the cell (row, col) (Line 110)
    public int countOptions(int[][] board, int row, int col) {
        int count = 0;
        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) count++;
        }
        return count;
    }

    // Tính số lượng ô bị ảnh hưởng nếu gán giá trị này (để dùng trong LCV)
    // Calculate the number of cells affected if this value is assigned (used in LCV) (Line 117)
    public int countConstraints(int[][] board, int row, int col, int num) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == 0 && isValid(board, row, i, num)) count++;
            if (board[i][col] == 0 && isValid(board, i, col, num)) count++;
        }
 
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == 0 && isValid(board, i, j, num)) count++;
            }
        }
 
        return count;
    }

    // Kiểm tra gán số có hợp lệ không
    // Check if assigning a number is valid (Line 134)
    public boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
 
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }
 
        return true;
    }

    public int getSteps() {
        return steps; // Return the updated steps
    }
}