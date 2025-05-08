public class SudokuGA {
    static final int GRID_SIZE = 9;             // Size of the Sudoku grid (9x9)
    static final int POP_SIZE = 100;            // Population size
    static final int MAX_GENERATIONS = 10000;   // Maximum number of generations
    static final double MUTATION_RATE = 0.5;    // Mutation rate

    // Solve method to find a solution for the given Sudoku puzzle
    public int[][] solve(int[][] puzzle) {
        // Set the puzzle as the base for the genetic algorithm
        SudokuGA.puzzle = puzzle;

        // Initialize the population
        Population population = new Population();
        int generation = 0;

        // Run the genetic algorithm
        while (generation < MAX_GENERATIONS) {
            Individual fittest = population.getFittest();
            if (fittest.fitness == 0) {
                // Found a valid solution
                return fittest.grid;
            }
            population.evolve();
            generation++;
        }

        // If no solution is found within the maximum generations, throw an exception
        throw new IllegalArgumentException("No solution found within " + MAX_GENERATIONS + " generations.");
    }

    // Static fields and methods for the genetic algorithm remain unchanged
    static int[][] puzzle = new int[GRID_SIZE][GRID_SIZE];

    static class Individual {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        int fitness = 0;

        Individual() {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (puzzle[i][j] != 0) {
                        grid[i][j] = puzzle[i][j];
                    } else {
                        grid[i][j] = (int) (Math.random() * GRID_SIZE) + 1;
                    }
                }
            }
        }

        void calculateFitness() {
            fitness = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                fitness += countConflictsInRow(i);
                fitness += countConflictsInColumn(i);
                fitness += countConflictsInBox(i);
            }
        }

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

        void mutate() {
            int row = (int) (Math.random() * GRID_SIZE);
            int col = (int) (Math.random() * GRID_SIZE);
            if (puzzle[row][col] == 0) {
                grid[row][col] = (int) (Math.random() * GRID_SIZE) + 1;
            }
        }

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

    static class Population {
        Individual[] individuals = new Individual[POP_SIZE];

        Population() {
            for (int i = 0; i < POP_SIZE; i++) {
                individuals[i] = new Individual();
                individuals[i].calculateFitness();
            }
        }

        Individual getFittest() {
            Individual fittest = individuals[0];
            for (Individual individual : individuals) {
                if (individual.fitness < fittest.fitness) {
                    fittest = individual;
                }
            }
            return fittest;
        }

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

        Individual selectParent() {
            return getFittest();
        }
    }

    static void printBoard(int[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}