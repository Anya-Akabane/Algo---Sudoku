package algorithms;

public class DLX {

    // =======================
    // Inner Classes
    // =======================

    static class Node {
        Node left, right, up, down;
        ColumnNode column;

        Node() {
            left = right = up = down = this;
        }

        Node linkDown(Node node) {
            node.down = down;
            node.down.up = node;
            node.up = this;
            this.down = node;
            return node;
        }

        Node linkRight(Node node) {
            node.right = right;
            node.right.left = node;
            node.left = this;
            this.right = node;
            return node;
        }
    }

    static class ColumnNode extends Node {
        int size;
        String name;

        ColumnNode(String name) {
            super();
            this.size = 0;
            this.name = name;
            this.column = this;
        }

        int nameIndex() {
            return parseColName(this.name);
        }
    }

    // =======================
    // Fields
    // =======================
    private ColumnNode header;
    private final Node[] solutionNodes = new Node[81];
    private final int[][] solvedBoard = new int[9][9];
    private final int[][] puzzle;

    private int steps = 0;            // 🔢 Step counter
    private long solveTimeMs = 0;     // ⏱ Time in milliseconds

    // =======================
    // Constructor
    // =======================
    public DLX(int[][] puzzle) {
        this.puzzle = puzzle;
        header = buildDLXBoard(puzzle);
    }

    // =======================
    // Solve Method
    // =======================
    public boolean solve() {
        steps = 0;
        long start = System.currentTimeMillis();

        boolean foundSolution = search(0);

        long end = System.currentTimeMillis();
        solveTimeMs = end - start;

        return foundSolution;
    }

    public int[][] getSolution() {
        return solvedBoard;
    }

    public int getStepCount() {
        return steps;
    }

    public long getSolveTimeMs() {
        return solveTimeMs;
    }

    // Simple manual formatting for solve time
    public String getSolveTimeFormatted() {
        long secs = solveTimeMs / 1000;
        long remainder = solveTimeMs % 1000;
        // Format to something like 0.123 s
        String millisStr = String.valueOf(remainder);
        // Zero-pad to 3 digits if needed
        if (remainder < 10) millisStr = "00" + millisStr;
        else if (remainder < 100) millisStr = "0" + millisStr;
        return secs + "." + millisStr + " s";
    }

    // =======================
    // Build DLX Matrix
    // =======================
    private ColumnNode buildDLXBoard(int[][] grid) {
        int numColumns = 324;
        ColumnNode headerNode = new ColumnNode("root");

        // Create an array for the 324 ColumnNodes
        ColumnNode[] columnNodes = new ColumnNode[numColumns];

        // Link them horizontally off the header
        ColumnNode prev = headerNode;
        for (int i = 0; i < numColumns; i++) {
            ColumnNode col = new ColumnNode("C" + i);
            columnNodes[i] = col;
            prev = (ColumnNode) prev.linkRight(col);
        }

        // Build the rows
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                for (int digit = 1; digit <= 9; digit++) {
                    // If puzzle has a fixed digit that isn't 'digit', skip
                    if (grid[row][col] != 0 && grid[row][col] != digit) {
                        continue;
                    }
                    int box = (row / 3) * 3 + (col / 3);

                    int colIndexRowCol = row * 9 + col;
                    int colIndexRowDig = 81 + row * 9 + (digit - 1);
                    int colIndexColDig = 81 * 2 + col * 9 + (digit - 1);
                    int colIndexBoxDig = 81 * 3 + box * 9 + (digit - 1);

                    ColumnNode c1 = columnNodes[colIndexRowCol];
                    ColumnNode c2 = columnNodes[colIndexRowDig];
                    ColumnNode c3 = columnNodes[colIndexColDig];
                    ColumnNode c4 = columnNodes[colIndexBoxDig];

                    Node r1 = new Node(); r1.column = c1;
                    Node r2 = new Node(); r2.column = c2;
                    Node r3 = new Node(); r3.column = c3;
                    Node r4 = new Node(); r4.column = c4;

                    // Link the four nodes in a row
                    r1.linkRight(r2).linkRight(r3).linkRight(r4);

                    // Link them down into the column
                    c1.up.linkDown(r1); c1.size++;
                    c2.up.linkDown(r2); c2.size++;
                    c3.up.linkDown(r3); c3.size++;
                    c4.up.linkDown(r4); c4.size++;
                }
            }
        }

        return headerNode;
    }

    // =======================
    // DLX Search Algorithm
    // =======================
    private boolean search(int k) {
        steps++;

        // If no columns remain, we have a complete solution
        if (header.right == header) {
            fillSolution(k);
            return true;
        }

        ColumnNode col = chooseColumnNodeHeuristic();
        if (col == null || col.size == 0) {
            return false;
        }

        cover(col);

        for (Node row = col.down; row != col; row = row.down) {
            solutionNodes[k] = row;

            // Cover all columns in this row
            for (Node nodeRight = row.right; nodeRight != row; nodeRight = nodeRight.right) {
                cover(nodeRight.column);
            }

            if (search(k + 1)) {
                return true;
            }

            // Backtrack: Uncover all columns in this row
            for (Node nodeLeft = row.left; nodeLeft != row; nodeLeft = nodeLeft.left) {
                uncover(nodeLeft.column);
            }
        }

        uncover(col);
        return false;
    }

    private ColumnNode chooseColumnNodeHeuristic() {
        int minSize = Integer.MAX_VALUE;
        ColumnNode chosen = null;

        for (Node c = header.right; c != header; c = c.right) {
            ColumnNode col = (ColumnNode) c;
            if (col.size < minSize) {
                minSize = col.size;
                chosen = col;
            }
        }
        return chosen;
    }

    private void cover(ColumnNode col) {
        col.right.left = col.left;
        col.left.right = col.right;

        for (Node row = col.down; row != col; row = row.down) {
            for (Node nodeRight = row.right; nodeRight != row; nodeRight = nodeRight.right) {
                nodeRight.down.up = nodeRight.up;
                nodeRight.up.down = nodeRight.down;
                ((ColumnNode) nodeRight.column).size--;
            }
        }
    }

    private void uncover(ColumnNode col) {
        for (Node row = col.up; row != col; row = row.up) {
            for (Node nodeLeft = row.left; nodeLeft != row; nodeLeft = nodeLeft.left) {
                ((ColumnNode) nodeLeft.column).size++;
                nodeLeft.down.up = nodeLeft;
                nodeLeft.up.down = nodeLeft;
            }
        }
        col.right.left = col;
        col.left.right = col;
    }

    // =======================
    // Decode Final Solution
    // =======================
    private void fillSolution(int k) {
        for (int i = 0; i < k; i++) {
            Node rowNode = solutionNodes[i];
            Node start = rowNode;
            int minColIdx = ((ColumnNode) rowNode.column).nameIndex();

            // Find the node whose column index is smallest,
            // so we can decode row/col/digit from it properly.
            for (Node n = rowNode.right; n != rowNode; n = n.right) {
                int colIdx = ((ColumnNode) n.column).nameIndex();
                if (colIdx < minColIdx) {
                    minColIdx = colIdx;
                    start = n;
                }
            }

            // Collect up to 4 column indices from this row.
            int[] colIndices = new int[4];
            int count = 0;

            // Add the "start" node's column index
            colIndices[count++] = ((ColumnNode) start.column).nameIndex();

            // Add the rest of the nodes' column indices
            for (Node n = start.right; n != start; n = n.right) {
                colIndices[count++] = ((ColumnNode) n.column).nameIndex();
            }

            decodeAndFill(colIndices, count);
        }
    }

    private void decodeAndFill(int[] colIdxList, int count) {
        int row = -1, col = -1, digit = -1;

        for (int i = 0; i < count; i++) {
            int idx = colIdxList[i];
            if (idx < 81) {
                row = idx / 9;
                col = idx % 9;
            } else if (idx < 162) {
                int off = idx - 81;
                row = off / 9;
                digit = (off % 9) + 1;
            } else if (idx < 243) {
                int off = idx - 162;
                col = off / 9;
                digit = (off % 9) + 1;
            } else {
                int off = idx - 243;
                digit = (off % 9) + 1;
            }
        }

        solvedBoard[row][col] = digit;
    }

    // Custom parseColName() with no library calls
    private static int parseColName(String colName) {
        // Skip the first character 'C'; parse the rest as an integer
        int result = 0;
        for (int i = 1; i < colName.length(); i++) {
            char c = colName.charAt(i);
            result = result * 10 + (c - '0');
        }
        return result;
    }

    // =======================
    // Main: Demo
    // =======================
    public static void main(String[] args) {
        int[][] puzzle = {
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

        DLX solver = new DLX(puzzle);

        if (solver.solve()) {
            // Print solution
            System.out.println("Solution found:");
            int[][] solution = solver.getSolution();
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    System.out.print(solution[r][c] + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("No solution found.");
        }

        System.out.println("Steps taken: " + solver.getStepCount());
        System.out.println("Time taken: " + solver.getSolveTimeFormatted());
    }
}