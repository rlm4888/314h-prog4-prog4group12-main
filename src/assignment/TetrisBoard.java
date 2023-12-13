package assignment;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

import assignment.Piece.PieceType;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or
 * nulls). Supports
 * tetris pieces and row clearing. Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    private PieceType[][] board;
    private Piece currentPiece;
    private Point currentPiecePosition;
    private int rowsCleared;
    private int width;
    private int height;
    private int maxHeight;
    private Result lastResult;
    private Action lastAction;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.maxHeight = 0;
        this.currentPiece = null;
        this.currentPiecePosition = new Point(0, 0);
        this.rowsCleared = 0;
        this.board = new PieceType[height][width];
        this.lastResult = Result.NO_PIECE;
        this.lastAction = Action.NOTHING;
    }

    public TetrisBoard(int width, int height, TetrisBoard original) {
        this.width = width;
        this.height = height;
        lastResult = original.lastResult;
        lastAction = original.lastAction;
        board = original.board; //use for loop
        for(int i = 0; i < original.board.length; i++) {
            for(int j = 0; j < original.board[i].length; j++) {
                board[i][j] = original.board[i][j];
            }
        }
        currentPiece = original.currentPiece;
        currentPiecePosition = new Point(original.currentPiecePosition.x, original.currentPiecePosition.y);
        rowsCleared = original.rowsCleared;
        maxHeight = 0;
    }

    public TetrisBoard(int width, int height, int maxHeight, Piece currentPiece, Point currentPiecePosition, int rowsCleared, Result lastResult, Action lastAction, PieceType[][] board) {
        this.width = width;
        this.height = height;
        this.maxHeight = maxHeight;
        this.currentPiece = currentPiece;
        this.currentPiecePosition = new Point(currentPiecePosition.x, currentPiecePosition.y);
        this.rowsCleared = rowsCleared;
        this.lastResult = lastResult;
        this.lastAction = lastAction;
        this.board = new PieceType[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    @Override
    public Result move(Action act) {
        boolean canDrop = true;

        if (currentPiece == null)
            return Result.NO_PIECE;

        Point bottom = null;

        int[] skirt = currentPiece.getSkirt();
        int min = skirt[0];
        int index = 0;
        for (int i = 0; i < skirt.length; i++) {
            int skirtVal = skirt[i];
            if (min > skirtVal) {
                min = skirtVal;
                index = i;
            }
        }
        bottom = new Point(index, min);

        int minX = 0;
        int maxX = 0;
        for (Point p : currentPiece.getBody()) {
            if (p.getX() <= minX) {
                minX = (int) p.getX();
            }
            if (p.getX() >= maxX) {
                maxX = (int) p.getX();
            }
        }

        TetrisBoard tb = new TetrisBoard(width, height, this);

        switch (act) {
            case LEFT:
                Point temp3 = new Point(tb.currentPiecePosition.x - 1, tb.currentPiecePosition.y);
                
                if (!isValid(tb.board, tb.currentPiece, temp3)) {
                    lastResult = Result.OUT_BOUNDS;
                    return lastResult;
                }
                tb.currentPiecePosition.x--;
                break;
            case RIGHT:
                Point temp = new Point(tb.currentPiecePosition.x + 1, tb.currentPiecePosition.y);
                if (!isValid(tb.board, tb.currentPiece, temp)) {
                    lastResult = Result.OUT_BOUNDS;
                    return lastResult;
                }
                tb.currentPiecePosition.x++;
                break;
            case DOWN:
                if (tb.currentPiecePosition.y + bottom.getY() == 0) {
                    canDrop = false;
                    break;
                }
                Point temp2 = new Point(tb.currentPiecePosition.x, tb.currentPiecePosition.y - 1);
                if (!isValid(tb.board, tb.currentPiece, temp2)) {
                    updateGrid(tb.currentPiecePosition, tb.currentPiece);
                    return Result.PLACE;
                }
                tb.currentPiecePosition.y--;
                break;
            case DROP:
                tb.currentPiecePosition.y = dropHeight(currentPiece, currentPiecePosition.x);
                tb.lastResult = Result.PLACE;
                break;
            case CLOCKWISE:
                //access the next rotation
                tb.currentPiece = tb.currentPiece.clockwisePiece();
                //check if the next rotation is a valid placement. if not, then try to wallkick
                if (!isValid(tb.board, tb.currentPiece, tb.currentPiecePosition)) {
                    if (currentPiece.getType() == PieceType.STICK) {
                        // stick tetromino wall kicks
                        Point wallKickPosition = doWallKicks(Piece.I_CLOCKWISE_WALL_KICKS[(currentPiece.getRotationIndex())], tb.currentPiecePosition, tb.currentPiece);
                        //if there is a wall kick, update the current position
                        if (wallKickPosition != null) {
                            tb.currentPiecePosition = wallKickPosition;

                        } else {
                            lastResult = Result.OUT_BOUNDS;
                            return lastResult;
                        }
                    } else {
                        // normal tetromino wall kicks
                        Point wallKickPosition2 = doWallKicks(Piece.NORMAL_CLOCKWISE_WALL_KICKS[(currentPiece.getRotationIndex())], tb.currentPiecePosition, tb.currentPiece);
                        if (wallKickPosition2 != null) {
                            tb.currentPiecePosition = wallKickPosition2;
                        }
                        else {
                            lastResult = Result.OUT_BOUNDS;
                            return lastResult;
                        }
                    }
                }
                break;
            case COUNTERCLOCKWISE:
                //access the next rotation
                tb.currentPiece = tb.currentPiece.counterclockwisePiece();
                //check if the next rotation is a valid placement. if not, then try to wallkick
                if (!isValid(tb.board, tb.currentPiece, tb.currentPiecePosition)) {
                    if (currentPiece.getType() == PieceType.STICK) {
                        // stick tetromino wall kicks
                        Point wallKickPosition = doWallKicks(Piece.I_COUNTERCLOCKWISE_WALL_KICKS[(currentPiece.getRotationIndex())], tb.currentPiecePosition, tb.currentPiece);
                        
                        //if there is a wall kick, update the current position
                        if (wallKickPosition != null) {
                            tb.currentPiecePosition = wallKickPosition;
                        } else {
                            lastResult = Result.OUT_BOUNDS;
                            return lastResult;
                        }
                    } else {
                        // normal tetromino wall kicks
                        Point wallKickPosition2 = doWallKicks(Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[(currentPiece.getRotationIndex())], tb.currentPiecePosition, tb.currentPiece);
                        
                        if (wallKickPosition2 != null) {
                            tb.currentPiecePosition = wallKickPosition2;
                        }
                        else {
                            lastResult = Result.OUT_BOUNDS;
                            return lastResult;
                        }
                    }
                }
                break;
            default:
                break;
        }

        if (!isValid(tb.board, tb.currentPiece, tb.currentPiecePosition)) {
            lastResult = Result.OUT_BOUNDS;
            return lastResult;
        }

        this.board = tb.board;
        this.currentPiece = tb.currentPiece;
        this.currentPiecePosition = tb.currentPiecePosition;
        this.rowsCleared = tb.rowsCleared;
        this.lastAction = act;

        if (act == Action.DROP || canDrop == false) {
            updateGrid(currentPiecePosition, currentPiece);
            lastResult = Result.PLACE;
            return lastResult;
        } else {
            lastResult = Result.SUCCESS;
        }
        // make sure placement is valid
        shiftArray();
        return lastResult;
    }

    public boolean isRowFull(PieceType[] row) {
        for (PieceType block : row) {
            if (block == null) {
                return false; // If any cell is null, the row is not full
            }
        }
        return true; // All cells are filled, so the row is full
    }
    private void shiftArray() {
        PieceType[][] ret = board;
        int rowShift = 0; // Counter for the number of rows skipped
        
        for (int i = 0; i < height; i++) {
            if(isRowFull(board[i])) {
                rowsCleared++;
                
                for(int j = i; j < board.length - 1; j++) {
                    
                    ret[j] = Arrays.copyOf(board[j + 1], board[j+1].length);
                }
            }
        }
        
    }
    

    private void updateGrid(Point piecePosition, Piece p) {
        int x = piecePosition.x;
        int y = piecePosition.y;
        for (Point pc : p.getBody()) {
            board[y + (int) pc.getY()][x + (int) pc.getX()] = p.getType();
        }
    }

    @Override
    public Board testMove(Action act) {
        TetrisBoard test = new TetrisBoard(width, height, maxHeight, currentPiece, currentPiecePosition, rowsCleared, lastResult, lastAction, board);
        test.move(act);
        return test;
    }

    @Override
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    @Override
    public Point getCurrentPiecePosition() {
        return currentPiecePosition;
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        if (!isValid(board, p, spawnPosition)) {
            throw new IllegalArgumentException("This is an invalid position");
        }
        currentPiece = p;
        currentPiecePosition = spawnPosition;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        TetrisBoard given = (TetrisBoard) other;
        return rowsCleared == given.rowsCleared &&
                width == given.width &&
                height == given.height &&
                maxHeight == given.maxHeight &&
                lastResult == given.lastResult &&
                lastAction == given.lastAction &&
                Arrays.deepEquals(board, given.board) &&
                Objects.equals(currentPiece, given.currentPiece) &&
                Objects.equals(currentPiecePosition, given.currentPiecePosition);
    }

    @Override
    public Result getLastResult() {
        return lastResult;
    }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getMaxHeight() {
        int max = 0;
        for (int i = 0; i < width; i++)
            if (max < getColumnHeight(i))
                max = getColumnHeight(i);
        return max;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        if (piece == null) {
            return -1;
        }
        Point temp = new Point(currentPiecePosition.x, currentPiecePosition.y);
        int dropHeight = 0;
        
        boolean isValid = true;
        while (isValid) {
            temp.y--;
            if(!isValid(board, piece, temp)) {
                isValid = false;
            }
            else {
                dropHeight++;
            }
        }
        return currentPiecePosition.y - dropHeight;
    }

    @Override
    public int getColumnHeight(int x) {
        int columnHeight = 0;
        for (int y = height - 1; y >= 0; y--) {
            if (board[y][x] != null) {
                columnHeight = y + 1;
                break;
            }
        }
        return columnHeight;
    }

    @Override
    public int getRowWidth(int y) {
        int rowWidth = 0;
        for (int x = 0; x < width; x++) {
            if (board[y][x] != null) {
                rowWidth++;
            }
        }
        return rowWidth;
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        return board[y][x];
    }

    private boolean isValid(PieceType[][] board, Piece piece, Point position) {
        // is piece valid?
        if (piece == null)
            return false;

        // is position valid?

        Point[] body = piece.getBody();
        for (int i = 0; i < body.length; i++) {
            int x = position.x + body[i].x;
            int y = position.y + body[i].y;

            // Check if position is in bounds
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return false;
            }

            // Check if position is empty
            if (board[y][x] != null) {
                return false;
            }
        }
        return true;
    }

    private Point doWallKicks(Point[] wallKicks, Point rotated, Piece rotatedPiece) {
        for (Point kick : wallKicks) {
            Point newPosition = new Point(kick.x + rotated.x, kick.y + rotated.y);
            if (!isValid(board, rotatedPiece, newPosition)) {
                //break;
            }else{
                return new Point((rotated.x + kick.x), (rotated.y + kick.y));
            }
        }
        return null; // No valid wall kicks
    }

    public PieceType[][] getBoard() {
        return board;
    }
}