import solver.SudokuSolver;
import solvers.BacktrackingSolver;
import solvers.ConstraintPropagationSolver;
import solvers.DLXSolver;
import solvers.HeuristicsSolver;

public class SudokuSolverFactory {
    public static SudokuSolver getSolver(String algorithm) {
        switch (algorithm.toLowerCase()) {
            case "backtracking":
                return new BacktrackingSolver();
            case "heuristics":
                return new HeuristicsSolver();
            // case "genetic":
            //     return new GeneticAlgorithmSolver();
            case "constraint":
                return new ConstraintPropagationSolver();
            case "dlx":
                return new DLXSolver();
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }
}