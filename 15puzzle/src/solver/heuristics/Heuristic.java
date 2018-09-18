package solver.heuristics;

import puzzle.Puzzle;

public interface Heuristic {

    public int heuristicValue(Puzzle p);

    public String getName();

}
