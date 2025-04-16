public class SudokuGA {
    static final int GRID_SIZE = 9;             // Kích thước của lưới Sudoku 9x9
    static final int POP_SIZE = 100;            // Kích thước quần thể
    static final int MAX_GENERATIONS = 10000;    // Số thế hệ tối đa
    static final double MUTATION_RATE = 0.5;   // Tỷ lệ đột biến

    // Bảng Sudoku gốc với 0 là các ô trống
    static int[][] puzzle = {
        {5, 3, 4, 6, 7, 8, 9, 1, 2},
        {6, 7, 2, 1, 0, 5, 3, 4, 8}, // ← zero at [1][4]
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 0, 9, 7, 6, 1, 4, 2, 3}, // ← zero at [3][1]
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 0, 6}, // ← zero at [5][7]
        {9, 6, 1, 5, 3, 7, 0, 8, 4}, // ← zero at [6][6]
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 0}
    };

    // Đại diện của một cá thể trong quần thể (một giải pháp Sudoku)
    static class Individual {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];  // Lưới Sudoku
        int fitness = 0;                               // Đánh giá mức độ thích ứng (số lượng các vi phạm)

        // Khởi tạo cá thể với Sudoku hợp lệ
        Individual() {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    // Sao chép các giá trị của bài Sudoku từ puzzle (với giá trị 0 là trống)
                    if (puzzle[i][j] != 0) {
                        grid[i][j] = puzzle[i][j]; //dán đề bài
                    } else {
                        grid[i][j] = (int) (Math.random() * GRID_SIZE) + 1; // gán giá trị ngẫu nhiên 
                    }
                }
            }
        }

        // Tính toán độ thích ứng (fitness) của cá thể
        void calculateFitness() {
            fitness = 0;
            // Kiểm tra mỗi hàng, mỗi cột và mỗi box 3x3
            for (int i = 0; i < GRID_SIZE; i++) {
                fitness += countConflictsInRow(i);
                fitness += countConflictsInColumn(i);
                fitness += countConflictsInBox(i);
            }
        }

        // Đếm số lượng vi phạm trong hàng
        int countConflictsInRow(int row) {
            boolean[] seen = new boolean[GRID_SIZE + 1];
            int conflicts = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                int value = grid[row][i];
                if (value != 0 && seen[value]) {
                    conflicts++;
                } else {
                    seen[value] = true;
                }
            }
            return conflicts;
        }

        // Đếm số lượng vi phạm trong cột
        int countConflictsInColumn(int col) {
            boolean[] seen = new boolean[GRID_SIZE + 1];
            int conflicts = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                int value = grid[i][col];
                if (value != 0 && seen[value]) {
                    conflicts++;
                } else {
                    seen[value] = true;
                }
            }
            return conflicts;
        }

        // Đếm số lượng vi phạm trong box 3x3
        int countConflictsInBox(int box) {
            boolean[] seen = new boolean[GRID_SIZE + 1];
            int conflicts = 0;
            int rowStart = (box / 3) * 3;
            int colStart = (box % 3) * 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int value = grid[rowStart + i][colStart + j];
                    if (value != 0 && seen[value]) {
                        conflicts++;
                    } else {
                        seen[value] = true;
                    }
                }
            }
            return conflicts;
        }

        // Đột biến (thay đổi giá trị của một ô ngẫu nhiên)
        void mutate() {
            int row = (int) (Math.random() * GRID_SIZE);
            int col = (int) (Math.random() * GRID_SIZE);
            if (puzzle[row][col] == 0) {
                grid[row][col] = (int) (Math.random() * GRID_SIZE) + 1;
            }
        }

        // Giao phối với một cá thể khác để tạo cá thể con
        Individual crossover(Individual other) {
            Individual child = new Individual();
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (Math.random() < 0.5) {
                        child.grid[i][j] = this.grid[i][j];
                    } else {
                        child.grid[i][j] = other.grid[i][j];
                    }
                }
            }
            return child;
        }
    }

    // Quần thể cá thể
    static class Population {
        Individual[] individuals = new Individual[POP_SIZE];

        // Khởi tạo quần thể
        Population() {
            for (int i = 0; i < POP_SIZE; i++) {
                individuals[i] = new Individual();
                individuals[i].calculateFitness();
            }
        }

        // Lọc ra những cá thể tốt nhất
        Individual getFittest() {
            Individual fittest = individuals[0];
            for (Individual individual : individuals) {
                if (individual.fitness < fittest.fitness) {
                    fittest = individual;
                }
            }
            return fittest;
        }

        // Tạo thế hệ mới
        void evolve() {
            Individual[] newGeneration = new Individual[POP_SIZE];
            for (int i = 0; i < POP_SIZE; i++) {
                Individual parent1 = selectParent();
                Individual parent2 = selectParent();
                Individual child = parent1.crossover(parent2);
                if (Math.random() < MUTATION_RATE) {
                    child.mutate();
                }
                child.calculateFitness();
                newGeneration[i] = child;
            }
            individuals = newGeneration;
        }

        // Chọn cha mẹ (chọn cá thể tốt nhất)
        Individual selectParent() {
            return getFittest(); // Sử dụng chọn lựa cá thể tốt nhất trong quần thể
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();  // Bắt đầu tính thời gian
        Population population = new Population();
        int generation = 0;
        int step = 0; // Biến đếm số bước (step)

        while (generation < MAX_GENERATIONS) {
            Individual fittest = population.getFittest();
            step++;  // Tăng bước sau mỗi vòng lặp
            System.out.println("Step: " + step + " | Generation: " + generation + " | Fitness: " + fittest.fitness);
            if (fittest.fitness == 0) {
                System.out.println("Đã tìm thấy giải pháp hợp lệ sau " + generation + " thế hệ:");
                printBoard(fittest.grid);
                long endTime = System.currentTimeMillis();  // Kết thúc tính thời gian
                System.out.println("Thời gian thực thi: " + (endTime - startTime) + " ms");
                return;
            }
            population.evolve();
            generation++;
        }

        System.out.println("Không thể giải Sudoku trong " + MAX_GENERATIONS + " thế hệ.");
        long endTime = System.currentTimeMillis();  // Kết thúc tính thời gian
        System.out.println("Thời gian thực thi: " + (endTime - startTime) + " ms");
    }

    // In lưới Sudoku
    static void printBoard(int[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}