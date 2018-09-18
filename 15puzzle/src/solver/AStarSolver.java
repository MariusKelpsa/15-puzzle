package solver;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import puzzle.Puzzle;
import puzzle.PuzzleService;
import solver.heuristics.Heuristic;

/**
 * 
 * A* search algorithm for finding 15-puzzle solution
 * 
 * @author Marius
 *
 */
public class AStarSolver {

    private PuzzleService puzzleService;
    private Heuristic heuristic;

    public AStarSolver(PuzzleService puzzleService, Heuristic heuristic) {
        this.puzzleService = puzzleService;
        this.heuristic = heuristic;
    }

    /**
     * @param puzzle - puzzle to solve
     * 
     * @return list of moves to solve given puzzle, or <code>null</code> if it couldn't solve it
     */
    public List<Puzzle> solve(Puzzle puzzle) {
        HashMap<Puzzle, Puzzle> puzzleMoves = new HashMap<Puzzle, Puzzle>(); // map of puzzles: <currentPuzzle,puzzleAfterMove>
        HashMap<Puzzle, Integer> solutionTreeDepth = new HashMap<Puzzle, Integer>(); // puzzles with values of how many moves were made
        HashMap<Puzzle, Integer> heuristicScores = new HashMap<Puzzle, Integer>(); // puzzles with values of heuristic score
        
        /**
         * Comparator is used in priority queue to determine which puzzle are closer to being solved
         */
        Comparator<Puzzle> comparator = new Comparator<Puzzle>() {
            @Override
            public int compare(Puzzle a, Puzzle b) {
                return heuristicScores.get(a) - heuristicScores.get(b);
            }
        };
        
        PriorityQueue<Puzzle> puzzlesToSolve = new PriorityQueue<Puzzle>(25000, comparator); // queue of original puzzle copies for solving

        // initialization of required structures with given puzzle
        puzzleMoves.put(puzzle, null);
        solutionTreeDepth.put(puzzle, 0);
        heuristicScores.put(puzzle, getHeuristicValue(puzzle));
        puzzlesToSolve.add(puzzle);
        int index = 0;
        
        // A* algorithm core: makes all possible moves from given puzzle and adds them to priority queue
        // priority queue is sorted by heuristic value, which basically calculates how close puzzle is to solution
        // at every iteration algorithm takes puzzle with best possibility to be solved earliest (because it's first in queue)
        while (puzzlesToSolve.size() > 0) {
            Puzzle bestCandidate = puzzlesToSolve.remove();
            index++;
            if (index % 25000 == 0) {
                System.out.printf("Solving... Considered %,d moves.\n", index);
            }
            if (isSolved(bestCandidate)) {
                System.out.printf("Solution considered %d moves\n", index);
                return buildResult(puzzleMoves, bestCandidate);
            }
            // adding to queue all valid unique moves from current puzzle and updating all other required structures 
            for (Puzzle moveAfter : puzzleService.getAdjacentPuzzles(bestCandidate)) {
                if (!puzzleMoves.containsKey(moveAfter)) {
                    puzzleMoves.put(moveAfter, bestCandidate);
                    solutionTreeDepth.put(moveAfter, solutionTreeDepth.get(bestCandidate) + 1);
                    int heuristicValue = getHeuristicValue(moveAfter);
                    heuristicScores.put(moveAfter, solutionTreeDepth.get(moveAfter) + heuristicValue);
                    puzzlesToSolve.add(moveAfter);
                }
            }
        }
        return null;
    }
    
    /**
     * Method backtraces moves of how puzzle was solved and returns it as LinkedList
     */
    private List<Puzzle> buildResult(HashMap<Puzzle, Puzzle> puzzleMoves, Puzzle finalMove) {
        LinkedList<Puzzle> result = new LinkedList<Puzzle>();
        Puzzle moveBefore = finalMove;
        while (moveBefore != null) {
            result.addFirst(moveBefore);
            moveBefore = puzzleMoves.get(moveBefore);
        }
        return result;
    }

    public boolean isSolved(Puzzle p) {
        return Puzzle.SOLVED.equals(p);
    }

    public int getHeuristicValue(Puzzle p) {
        return heuristic.heuristicValue(p);
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

}
