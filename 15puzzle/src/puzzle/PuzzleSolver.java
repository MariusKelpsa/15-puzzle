package puzzle;

import java.awt.Point;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import solver.AStarSolver;
import solver.heuristics.Heuristic;
import solver.heuristics.ManhattanDistanceHeuristic;
import solver.heuristics.WrongCellsHeuristic;
/**
 * 
 * Main class for starting 15-puzzle solver and controlling CLI.
 * 
 * @author Marius Kelpša
 *
 */
public class PuzzleSolver {

    private final PuzzleService puzzleService = new PuzzleService();
    private final Heuristic wrongCellsHeuristic = new WrongCellsHeuristic();
    private final Heuristic manhattanDistanceHeuristic = new ManhattanDistanceHeuristic();
    private AStarSolver aStarSolver;

    public PuzzleSolver() {
        this.aStarSolver = new AStarSolver(puzzleService, manhattanDistanceHeuristic);
    }
    
    public void printPuzzle(Puzzle p) {
        StringBuilder result = new StringBuilder();
        result.append("---------------------\n");
        for (int i = 0; i < Puzzle.EDGE; i++) {
            result.append("| ");
            for (int j = 0; j < Puzzle.EDGE; j++) {
                int n = p.getCell(i, j);
                if (n > 9) {
                    result.append(Integer.toString(n));
                } else if (n > 0) {
                    result.append(" " + Integer.toString(n));
                } else {
                    result.append("__");
                }
                result.append(" | ");
            }
            result.append("\n---------------------\n");
        }
        result.append("\n");
        System.out.print(result);
    }

    public void showSolution(List<Puzzle> solution) {
        if (solution != null) {
            System.out.printf("Here is puzzle solution in %d moves:\n", solution.size());
            solution.forEach(puzzle -> printPuzzle(puzzle));
        } else {
            System.out.println("Puzzle is unsolvable.");
        }
    }

    public void shuffle(Puzzle puzzle, int movesAmount) {
        for (int i = 0; i < movesAmount; i++) {
            List<Point> validMoves = puzzleService.getValidMoves(puzzle);
            int moveNo = (int) (Math.random() * validMoves.size());
            puzzle.move(validMoves.get(moveNo));
        }
    }

    public void solve(Puzzle puzzle) {
        System.out.println("Puzzle to solve:");
        printPuzzle(puzzle);
        System.out.printf("Solving puzzle using \"%s\" heuristic\n", getHeuristicName());
        boolean solvable = puzzleService.isSolvable(puzzleService.getValues2D(puzzle), puzzle.getEmptyCell().x);
        showSolution(solvable ? aStarSolver.solve(puzzle) : null);
    }
    
    public void printMenu(Scanner scanner) {
        System.out.println("Menu Options:");
        System.out.println("1. Enter new puzzle");
        System.out.println("2. Generate random puzzle");
        System.out.println("3. Change heuristic");
        System.out.println("4. Exit");
        System.out.println("");

        int option = 0;
        try {
            option = scanner.nextInt();
        } catch (InputMismatchException ex) { // controlable situation
            scanner.next(); // clear invalid input
        }
        switch (option) {
            case 1: {
                printSubmenu1(scanner);
                break;
            }
            case 2: {
                Puzzle puzzle = new Puzzle();
                shuffle(puzzle, 100);
                solve(puzzle);
                break;
            }
            case 3: {
                printSubmenu3(scanner);
                break;
            }
            case 4: {
                System.exit(1);
            }
            default: {
                System.out.println("Invalid input, choose option from 1 to 4");
                printMenu(scanner);
            }
        }
    }
    
    private void printSubmenu1(Scanner scanner) {
        System.out.println("Insert puzzle in one line (left to right, top to bottom), where 0 is empty cell.");
        System.out.println("Example: 5 3 0 4 6 1 7 8 2 10 11 12 9 13 14 15");
        System.out.println("makes following puzzle:");
        System.out.println("---------------------");
        System.out.println("|  5 |  3 | __ |  4 | ");
        System.out.println("---------------------");
        System.out.println("|  6 |  1 |  7 |  8 | ");
        System.out.println("---------------------");
        System.out.println("|  2 | 10 | 11 | 12 | ");
        System.out.println("---------------------");
        System.out.println("|  9 | 13 | 14 | 15 | ");
        System.out.println("---------------------");
        System.out.println("");
        String line = scanner.next();
        List<Integer> values = new LinkedList<>();
        List<String> cells = Arrays.asList(line.split(" "));
        try {
            cells.forEach(cell -> values.add(Integer.parseInt(cell)));
            if (!validateValues(values)) {
                printSubmenu1(scanner); // repaint submenu
            } else {
                Puzzle puzzle = new Puzzle(values);
                solve(puzzle);
            }
        } catch (NumberFormatException ex) { // controlable situation
            System.out.println("Invalid input. All values must be numbers separated by spaces");
            printSubmenu1(scanner); // repaint submenu
        }
    }
    
    private boolean validateValues(List<Integer> values) {
        if (values.size() != Puzzle.EDGE * Puzzle.EDGE) {
            System.out.println("Invalid amount of numbers, must be: " + Puzzle.EDGE * Puzzle.EDGE);
            return false;
        }
        if (!values.containsAll(Puzzle.solvedArray)) {
            System.out.println("Invalid values, must be unique value set from 0 to 15");
            return false;
        }
        return true;
    }
    
    private void printSubmenu3(Scanner scanner) {
        System.out.println("Pick heuristic:");
        System.out.println("1. " + WrongCellsHeuristic.HEURISTIC_NAME);
        System.out.println("2. " + ManhattanDistanceHeuristic.HEURISTIC_NAME);
        System.out.println("");
        int hOption = 0;
        try {
            hOption = scanner.nextInt();
        } catch (InputMismatchException ex) { // controlable situation
            scanner.next(); // clear invalid input
        }
        switch (hOption) {
            case 1: {
                changeHeuristic(wrongCellsHeuristic);
                printMenu(scanner);
                break;
            }
            case 2: {
                changeHeuristic(manhattanDistanceHeuristic);
                printMenu(scanner);
                break;
            }
            default: {
                System.out.println("Invalid input, choose 1 or 2");
                printSubmenu3(scanner);
            }
        }
    }

    private void changeHeuristic(Heuristic heuristic) {
        aStarSolver.setHeuristic(heuristic);
        System.out.printf("Using \"%s\" heuristic\n", heuristic.getName());
    }

    private String getHeuristicName() {
        return aStarSolver.getHeuristic().getName();
    }
    
    public static void main(String[] args) {
        System.out.println("15-puzzle solver, using A* algorithm");
        System.out.printf("Algorithm currenly uses \"%s\" heuristic (changeable)\n", ManhattanDistanceHeuristic.HEURISTIC_NAME);
        System.out.println();
        PuzzleSolver p = new PuzzleSolver();
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(System.getProperty("line.separator"));
        p.printMenu(scanner);
    }
}