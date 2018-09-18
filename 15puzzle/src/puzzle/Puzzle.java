package puzzle;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Class for storing puzzle board state and making moves
 * 
 * @author Marius
 *
 */
public class Puzzle {
    public static final List<Integer> solvedArray = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0);
    public static final Puzzle SOLVED = new Puzzle(solvedArray);
    public static final int EDGE = 4;
    
    private ArrayList<Point> cellCoords = new ArrayList<Point>();
    private int[][] cells;
    private Point emptyCell;

    public Puzzle() {
        this(solvedArray);
    }

    public Puzzle(List<Integer> values) {
        Iterator<Integer> iterator = values.iterator();
        cells = new int[EDGE][EDGE];
        for (int i = 0; i < EDGE; i++) {
            for (int j = 0; j < EDGE; j++) {
                cellCoords.add(new Point(i, j));
                int num = iterator.next();
                if (num == 0) {
                    emptyCell = new Point(i, j);
                }
                cells[i][j] = num;
            }
        }
    }

    public Puzzle(Puzzle clone) {
        this();
        cellCoords.forEach(coord -> cells[coord.x][coord.y] = clone.getCell(coord));
        emptyCell = clone.getEmptyCell();
    }

    /**
     * Makes a move with given cell (swaps given with empty)
     * @param cellToMove - cell to put in empty space
     */
    public void move(Point cellToMove) {
        cells[emptyCell.x][emptyCell.y] = cells[cellToMove.x][cellToMove.y];
        cells[cellToMove.x][cellToMove.y] = 0;
        emptyCell = cellToMove;
    }

    /**
     * @param value - cell value
     * @return returns coordinates of given value in this puzzle
     */
    public Point getCoordsOfValue(int value) {
        for (Point coord : cellCoords) {
            if (getCell(coord) == value) {
                return new Point(coord.x, coord.y);
            }
        }
        return null;
    }

    public int getCell(Point p) {
        return cells[p.x][p.y];
    }

    public int getCell(int x, int y) {
        return cells[x][y];
    }

    public Point getEmptyCell() {
        return emptyCell;
    }
    
    public List<Point> getAllCellCoords() {
        return this.cellCoords;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Puzzle) {
            for (Point p : cellCoords) {
                if (this.getCell(p) != ((Puzzle) o).getCell(p)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        int index = 1;
        for (Point p : cellCoords) {
            hash = (hash + this.getCell(p)) * index;
            index++;
        }
        return hash;
    }
}
