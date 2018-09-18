package puzzle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Puzzle helper service for various functions with puzzle logic
 * 
 * @author Marius
 *
 */
public class PuzzleService {

    /**
     * @return all possible moves of a given puzzle.
     * There can be max of 4 possible moves if empty cell is not near edge of puzzle
     */
    public List<Point> getValidMoves(Puzzle puzzle) {
        ArrayList<Point> moves = new ArrayList<Point>();
        Point emptyCell = puzzle.getEmptyCell();
        addIfValid(moves, new Point(emptyCell.x, emptyCell.y - 1)); // left
        addIfValid(moves, new Point(emptyCell.x, emptyCell.y + 1)); // right
        addIfValid(moves, new Point(emptyCell.x + 1, emptyCell.y)); // up
        addIfValid(moves, new Point(emptyCell.x - 1, emptyCell.y)); // down
        return moves;
    }

    public void addIfValid(ArrayList<Point> moves, Point cellToMove) {
        if (cellToMove.x < 0 || cellToMove.x >= Puzzle.EDGE || cellToMove.y < 0 || cellToMove.y >= Puzzle.EDGE) {
            return;
        }
        moves.add(cellToMove);
    }

    /**
     * @param puzzle - current puzzle
     * @param cellToMove - move to apply
     * @return returns a <b>new</b> puzzle with the move applied
     */
    public Puzzle clone(Puzzle puzzle, Point cellToMove) {
        Puzzle out = new Puzzle(puzzle);
        out.move(cellToMove);
        return out;
    }

    /**
     * @param puzzle - current puzzle
     * @return returns list of all possible puzzles, which can be made by applying one move to current puzzle
     */
    public List<Puzzle> getAdjacentPuzzles(Puzzle puzzle) {
        ArrayList<Puzzle> adjacentPuzzles = new ArrayList<Puzzle>();
        getValidMoves(puzzle).forEach(move -> adjacentPuzzles.add(clone(puzzle, move)));
        return adjacentPuzzles;
    }
    
    /**
     * @param list - 2d representation of puzzle, collected from left to right, top to bottom
     * @param emptyRow - row number of empty cell
     * @return logical value if puzzle is solvable
     */
    public boolean isSolvable(List<Integer> list, int emptyRow) {
        int parity = 0;

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j) && list.get(j) != 0) {
                    parity++;
                }
            }
        }

        if (emptyRow % 2 != 0) { // empty on even row
            return parity % 2 == 0;
        } else { // empty on odd row
            return parity % 2 != 0;
        }
    }
    
    /**
     * @param puzzle
     * @return 2d representation of puzzle, collected from left to right, top to bottom
     */
    public List<Integer> getValues2D(Puzzle puzzle) {
        List<Integer> values2D = new ArrayList<>();
        for (int x = 0; x < Puzzle.EDGE; x++) {
            for (int y = 0; y < Puzzle.EDGE; y++) {
                values2D.add(puzzle.getCell(x, y));
            }
        }
        return values2D;
    }
}
