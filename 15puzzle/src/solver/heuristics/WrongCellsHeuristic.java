package solver.heuristics;

import puzzle.Puzzle;

/**
 * 
 * Amount of cells in wrong place heuristic implementation.
 * 
 * Heuristic simply count how many cells are misplaced (not including empty cell) 
 *         comparing current cell position with correct(solved) puzzle
 * 
 * @author Marius
 *
 */
public class WrongCellsHeuristic implements Heuristic {

    public final static String HEURISTIC_NAME = "Amount of cells in wrong place";

    @Override
    public int heuristicValue(Puzzle puzzle) {
        int wrongAmount = 0;
        for (int i = 0; i < Puzzle.EDGE; i++) {
            for (int j = 0; j < Puzzle.EDGE; j++) {
                if ((puzzle.getCell(i, j) > 0) && (puzzle.getCell(i, j) != Puzzle.SOLVED.getCell(i, j))) {
                    wrongAmount++;
                }
            }
        }
        return wrongAmount;
    }

    @Override
    public String getName() {
        return HEURISTIC_NAME;
    }

}
