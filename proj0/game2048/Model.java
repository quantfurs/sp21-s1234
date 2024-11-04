package game2048;

import java.util.Formatter;
import java.util.Observable;
import java.util.List;
import java.util.ArrayList;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */

    public static int getValue(Board board, int row, int col) {
        Tile tile = board.tile(col, row);
        return tile != null ? tile.value() : 0;
    }

    public static int[] getColumn(Board board, int colIndex) {
        int size = board.size();
        int[] column = new int[size];
        for (int row = 0; row < size; row++) {
            Tile tile = board.tile(colIndex, row);
            column[row] = tile != null ? tile.value() : 0;
        }
        return column;
    }


    private boolean moveAndMergeColumn(int col, int[] mergedValues) {
        boolean changed = false;
        int size = board.size();
        int targetRow = size - 1; // Start from the top row
        boolean[] tileUsed = new boolean[size]; // Keep track of used tiles

        int valueIndex = 0; // Index for mergedValues
        for (int row = size - 1; row >= 0; row--) {
            if (valueIndex < mergedValues.length) {
                int expectedValue = mergedValues[valueIndex];
                Tile tile = findTileWithValueInColumn(col, expectedValue, tileUsed);

                if (tile != null) {
                    if (tile.row() != targetRow) {
                        board.move(col, targetRow, tile);
                        changed = true;
                    }
                    tileUsed[tile.row()] = true; // Mark tile as used
                }
                valueIndex++;
                targetRow--;
            } else {
                // Clear any remaining tiles
                Tile tile = board.tile(col, row);
                if (tile != null && !tileUsed[row]) {
                    // Remove the tile by moving it off the board or to the same position
                    // Since we can't remove tiles, we can move them to the same position
                    board.move(col, row, tile);
                    changed = true;
                }
            }
        }

        return changed;
    }

    private Tile findTileWithValueInColumn(int col, int value, boolean[] tileUsed) {
        int size = board.size();
        for (int row = 0; row < size; row++) {
            Tile tile = board.tile(col, row);
            if (tile != null && tile.value() == value && !tileUsed[row]) {
                return tile;
            }
        }
        return null;
    }

    public static class MergeResult {
        public int[] mergedTiles;
        public int score;

        public MergeResult(int[] mergedTiles, int score) {
            this.mergedTiles = mergedTiles;
            this.score = score;
        }
    }

    public static MergeResult mergeTiles(int[] tiles) {
        List<Integer> mergedTilesList = new ArrayList<>();
        int score = 0;
        int idx = 0;

        while (idx < tiles.length) {
            int currentValue = tiles[idx];
            if (idx + 1 < tiles.length && currentValue == tiles[idx + 1]) {
                int newValue = currentValue * 2;
                mergedTilesList.add(newValue);
                score += newValue;
                idx += 2; // Skip the next tile since it's merged
            } else {
                mergedTilesList.add(currentValue);
                idx += 1;
            }
        }

        int[] mergedTiles = mergedTilesList.stream().mapToInt(i -> i).toArray();
        return new MergeResult(mergedTiles, score);
    }

    public static class TiltResult {
        public boolean changed;
        public int score;

        public TiltResult(boolean changed, int score) {
            this.changed = changed;
            this.score = score;
        }
    }

    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        int size = board.size();
        board.setViewingPerspective(side);

        for (int col = 0; col < size; col++) {
            int[] mergedPositions = new int[size]; // Keep track of merged positions
            for (int row = size - 2; row >= 0; row--) {
                Tile tile = board.tile(col, row);
                if (tile != null) {
                    int destRow = row;
                    int nextRow = row + 1;
                    while (nextRow < size) {
                        Tile nextTile = board.tile(col, nextRow);
                        if (nextTile == null) {
                            // Move to the empty spot
                            destRow = nextRow;
                        } else if (nextTile.value() == tile.value() && mergedPositions[nextRow] == 0) {
                            // Merge with the tile
                            destRow = nextRow;
                            mergedPositions[destRow] = 1; // Mark as merged
                            break;
                        } else {
                            // Can't move further
                            break;
                        }
                        nextRow++;
                    }
                    if (destRow != row) {
                        boolean merged = board.move(col, destRow, tile);
                        if (merged) {
                            score += board.tile(col, destRow).value();
                            mergedPositions[destRow] = 1; // Mark as merged
                        }
                        changed = true;
                    }
                }
            }
        }

        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size(); // Assuming Board has a size() method

        for (int i = 0; i < size; i++) { // Iterate through rows
            for (int j = 0; j < size; j++) { // Iterate through columns
                Tile currentTile = b.tile(i, j);
                if (currentTile == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        int size = b.size(); // Assuming Board has a size() method

        for (int i = 0; i < size; i++) { // Iterate through rows
            for (int j = 0; j < size; j++) { // Iterate through columns
                Tile currentTile = b.tile(i, j);
                if (currentTile != null && currentTile.value() == MAX_PIECE) {
                    return true; // Found a tile with value MAX_PIECE
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean checkIfSameVal(Board b) {
        int boardSize = b.size(); // Assuming the board is square. Adjust if rectangular.

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Tile currentTile = b.tile(i, j);
                int currentValue = currentTile.value();

                // Check right neighbor
                if (j < boardSize - 1) {
                    Tile rightTile = b.tile(i, j + 1);
                    if (currentValue == rightTile.value()) {
                        return true;
                    }
                }

                // Check bottom neighbor
                if (i < boardSize - 1) {
                    Tile bottomTile = b.tile(i + 1, j);
                    if (currentValue == bottomTile.value()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if (emptySpaceExists(b) || checkIfSameVal(b)) {
            return true;
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
