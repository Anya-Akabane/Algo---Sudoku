public class SudokuSolverFactory {
    public static SudokuSolver getSolver(String algorithm) {
        switch (algorithm.toLowerCase()) {
            case "backtracking":
                return new BacktrackingSolver();
            case "dlx":
                return new DLXSolver();
            case "heuristics":
                return new HeuristicsSolver();
            case "genetic":
                return new GeneticAlgorithmSolver();
            case "constraint":
                return new ConstraintPropagationSolver();
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }
}