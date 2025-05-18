# ALgorithms and Analysis - COSC2469|COSC2722


## Project Description
-------------------
Algo---Sudoku is a Java-based application for generating and solving Sudoku puzzles using multiple algorithms. It supports single-puzzle and batch (3000 puzzles) solving, and reports performance metrics such as steps, memory usage, and time.

## Features
--------
- Generate random Sudoku puzzles with a specified number of clues.
- Solve puzzles using five algorithms: Backtracking, Heuristics, Constraint Propagation, DLX, and AStar.
- Benchmark algorithms by solving 3000 puzzles in batch mode.
- Display puzzles and solutions in a readable format.
- Reports steps, average memory usage, and time taken for each algorithm.

## Github Repository
[Group9 github](https://github.com/Anya-Akabane/Algo---Sudoku.git)



## Video Presentation and Demo
YouTube Link: 

GG Drive Link: [Group9 folder](https://drive.google.com/drive/folders/1UG1U2yrI8_dbzzYAz-c4Ai2swfAGa1Tg?usp=sharing)

## Contribution 
-------------------
Each member's score reflects their relative contribution to the project, including code, documentation, testing, and project management.

## Group 09 Members

| Name                   | Student ID  | Contribution Score |
|------------------------|-------------|:-------------------:|
| Nguyen Thi Hong Hanh   | s4020232    | 5                   |
| Nguyen Tuan Minh Khoi  | s3995060    | 5                   |
| Le Quang Thao          | s4028673    | 5                   |
| Tran Anh Thu           | s4052233    | 5                   |


### Thao
- **Responsibilities**:
  - Led **high-level system design** and overall architecture.
  - Developed critical **data structures** used across solvers.
  - Implemented the **DLX (Dancing Links)** algorithm for efficient solving.
  - Contributed to **testing & evaluation** phases.
  - Assisted in **coordination** and project integration.

### Khoi
- **Responsibilities**:
  - Designed **core system structure** and data abstractions.
  - Implemented the **Backtracking** solver.
  - Conducted **complexity analysis** and optimization.
  - Supported **team coordination** and review of architectural decisions.

### Hanh
- **Responsibilities**:
  - Focused on **complexity analysis** and algorithm evaluation.
  - Implemented the **Heuristics-based solver** (MRV, LCV, FC) and **A\* Search** algorithm.
  - Led **report writing**, and created the **presentation**.
  - Played a key role in **team communication and coordination**.

### Thu
- **Responsibilities**:
  - Led **testing and evaluation** for solver accuracy and performance.
  - Implemented **Constraint Propagation** and **Genetic Algorithms**.
  - Authored significant parts of the **final report** and **presentation materials**.
  - Collaborated closely in **coordination** and document finalization.


Prerequisites
-------------
- Java JDK 8 or higher

## How to Run
----------
**  Option 1: Run by terminal
1. Open a terminal (Command Prompt or PowerShell) in the project directory:
   ```
   cd d:\Algorithms\Algo-code\Algo---Sudoku
   ```

2. Compile all Java files:
   ```
   javac -d . *.java algorithms/*.java solver/*.java solvers/*.java
   ```

3. Run the main program:
   ```
   java RMIT_Sudoku_Solver
   ```
**  Option 2: Run by Run Java file
- Locate to RMIT_Sudoku_Solver.java file
- Click Run Java button

## Step-by-Step Running Program
----------------------------
1. When prompted, select a solving algorithm or batch mode:
   ```
   1 - Backtracking
   2 - Heuristics
   3 - Constraint Propagation
   4 - DLX
   5 - AStar
   6 - (3000 puzzles) Backtracking
   7 - (3000 puzzles) Heuristics
   8 - (3000 puzzles) Constraint Propagation
   9 - (3000 puzzles) DLX
   10 - (3000 puzzles) AStar
   11 - (3000 puzzles) All
   ```
2. Enter your choice (1-11) and press Enter.

3. For single puzzle mode (options 1-5):
   - The program generates a random Sudoku puzzle and solves it using the selected algorithm.
   - It displays the puzzle, solution, steps taken, average memory used, and time taken.

4. For batch mode (options 6-11):
   - The program solves 3000 puzzles using the selected algorithm(s) and reports average statistics.

5. If you encounter errors, ensure all `.java` files are present and your Java version is correct.

--------------------------
## Delete untracked files
To see all untracked files before deleting, run:

```
git clean -n
```

To delete all untracked files in the repository:

```
git clean -f
```

--------------------------
## Project Structure and File Responsibilities
    
    ```
    Algo---Sudoku/
    │   RMIT_Sudoku_Solver.java
    │   SudokuGenerator.java
    │   SudokuSolverFactory.java
    │   ...
    ├── algorithms/
    │     AStar.java
    │     Backtracking.java
    │     ConstraintPropagation.java
    │     DLX.java
    │     Heuristics.java
    │     MemoryTracker.java
    ├── solver/
    │     SudokuSolver.java
    ├── solvers/
          AStarSolver.java
          BacktrackingSolver.java
          ConstraintPropagationSolver.java
          DLXSolver.java
          HeuristicsSolver.java
    
    ---
    
    This modular structure allows easy extension and maintenance. Each algorithm is encapsulated in its own file, and the main program interacts with them through a common interface.
```