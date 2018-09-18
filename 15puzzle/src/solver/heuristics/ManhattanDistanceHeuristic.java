package solver.heuristics;

import java.awt.Point;

import puzzle.Puzzle;

/**
 * 
 * Manhattan distance (taxicab geometry) heuristic implementation
 * 
 * Manhattan distance is metric in which the distance between two points (in this program two puzzle cells)
 *      is the sum of the absolute differences of their Cartesian coordinates
 * 
 * Source - https://en.wikipedia.org/wiki/Taxicab_geometry
 * 
 * @author Marius
 *
 */
public class ManhattanDistanceHeuristic implements Heuristic {

    public final static String HEURISTIC_NAME = "Manhattan distance";

    @Override
    public int heuristicValue(Puzzle puzzle) {
        int distance = 0;
        for (Point p : puzzle.getAllCellCoords()) {
            if (puzzle.getCell(p) > 0) {
                Point correct = Puzzle.SOLVED.getCoordsOfValue(puzzle.getCell(p));
                distance = distance + Math.abs(correct.x - p.x) + Math.abs(correct.y - p.y);
            }
        }
        return distance;
    }

    @Override
    public String getName() {
        return HEURISTIC_NAME;
    }

}
