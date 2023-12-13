package assignment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment.Piece.PieceType;
import assignment.TetrisBoard;
import assignment.TetrisPiece;

import org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import assignment.Board;
import assignment.Board.Result;
import assignment.Piece;

import java.awt.Point;

public class TetrisBoardTester {
    private TetrisBoard board;

    @BeforeEach
    public void setUp() {
        board = new TetrisBoard(10, 20);

    }

    @Test
    public void testConstructor() {
        assertEquals(10, board.getWidth());
        assertEquals(20, board.getHeight());
        assertEquals(0, board.getRowsCleared());
        assertNull(board.getCurrentPiece());
        assertEquals(Board.Result.NO_PIECE, board.getLastResult());

    }

    @Test
    public void testMove() {
        Piece piece = new TetrisPiece(Piece.PieceType.LEFT_L);
        Point initialPosition = new Point(4, 0);
        board.nextPiece(piece, initialPosition);

        // test left
        Board.Result leftResult = board.move(Board.Action.LEFT);
        assertEquals(Board.Result.SUCCESS, leftResult);

        // test right
        Board.Result rightResult = board.move(Board.Action.RIGHT);
        assertEquals(Board.Result.SUCCESS, rightResult);

        // test down
        Board.Result downResult = board.move(Board.Action.DOWN);
        assertEquals(Board.Result.SUCCESS, downResult);

        // test clockwise
        Board.Result clockwiseResult = board.move(Board.Action.CLOCKWISE);
        assertEquals(Board.Result.SUCCESS, clockwiseResult);

        // test counterclockwise
        Board.Result counterclockwiseResult = board.move(Board.Action.COUNTERCLOCKWISE);
        assertEquals(Board.Result.SUCCESS, counterclockwiseResult);

        // move piece down until it's placed
        while (board.getLastResult() != Board.Result.PLACE) {
            downResult = board.move(Board.Action.DOWN);
        }

        // check if result is PLACE
        assertEquals(Board.Result.PLACE, downResult);
    }

    @Test
    public void testNextPiece() {
        Piece initialPiece = new TetrisPiece(Piece.PieceType.RIGHT_DOG);
        Point initialPosition = new Point(4, 0);
        board.nextPiece(initialPiece, initialPosition);

        Piece nextPiece = new TetrisPiece(Piece.PieceType.RIGHT_L);
        Point spawnPosition = new Point(0, 0);
        board.nextPiece(nextPiece, spawnPosition);

        assertEquals(nextPiece, board.getCurrentPiece());
        assertEquals(spawnPosition, board.getCurrentPiecePosition());
    }

    @Test
    public void testGetGrid() {
        Piece piece = new TetrisPiece(Piece.PieceType.STICK);
        Point initialPosition = new Point(3, 15);
        board.nextPiece(piece, initialPosition);

        board.move(Board.Action.DROP);

        int pieceWidth = piece.getWidth();
        int pieceHeight = piece.getHeight();

        for (int x = 0; x < pieceWidth; x++) {
            for (int y = 0; y < pieceHeight; y++) {
                Point gridPos = new Point(initialPosition.x + x, initialPosition.y + y);
                Piece.PieceType gridVal = board.getGrid(gridPos.x, gridPos.y);

                if (x < pieceWidth && y < pieceHeight) {
                    assertEquals(piece.getType(), gridVal);
                } else {
                    assertNull(gridVal);
                }

            }
        }

    }

    @Test
    public void testEquals() {
        TetrisBoard b1 = new TetrisBoard(10, 20);
        TetrisBoard b2 = new TetrisBoard(10, 20);

        // check that boards are equal upon creation
        assertTrue(b1.equals(b2));

        b1.move(Board.Action.LEFT);
        b1.move(Board.Action.DOWN);

        // check that boards are not equal after actions
        assertFalse(b1.equals(b2));

        TetrisBoard b3 = new TetrisBoard(10, 20);
        assertTrue(b1.equals(b3));
    }

    @Test
    public void testGetLastResult() {
        Piece piece = new TetrisPiece(Piece.PieceType.T);
        Point initialPosition = new Point(4, 0);
        board.nextPiece(piece, initialPosition);

        while (board.getLastResult() != Board.Result.PLACE) {
            board.move(Board.Action.DOWN);
        }

        assertEquals(Board.Result.PLACE, board.getLastResult());
    }

    @Test
    public void testGetLastAction() {
        Piece piece = new TetrisPiece(Piece.PieceType.SQUARE);
        Point initialPos = new Point(4, 0);
        board.nextPiece(piece, initialPos);

        board.move(Board.Action.LEFT);
        assertEquals(Board.Action.LEFT, board.getLastAction());

        board.move(Board.Action.DOWN);
        assertEquals(Board.Action.DOWN, board.getLastAction());

        board.move(Board.Action.CLOCKWISE);
        assertEquals(Board.Action.CLOCKWISE, board.getLastAction());
    }

    @Test
    public void testGetRowsCleared() {
        Piece piece = new TetrisPiece(Piece.PieceType.LEFT_L);
        Point initialPos = new Point(4, 0);
        board.nextPiece(piece, initialPos);

        while (board.getLastResult() != Board.Result.PLACE) {
            board.move(Board.Action.DOWN);
        }
        assertEquals(2, board.getRowsCleared());

    }

    @Test
    public void testGetWidth() {
        int width = 10;
        int height = 20;
        TetrisBoard board = new TetrisBoard(width, height);

        assertEquals(width, board.getWidth());
    }

    @Test
    public void testGetHeight() {
        int width = 10;
        int height = 20;
        TetrisBoard board = new TetrisBoard(width, height);

        assertEquals(height, board.getHeight());
    }

    @Test
    public void testGetMaxHeight() {
        int width = 10;
        int height = 20;
        TetrisBoard board = new TetrisBoard(width, height);

        board.nextPiece(new TetrisPiece(Piece.PieceType.RIGHT_L), new Point(4, 0));
        board.move(Board.Action.DOWN);
        board.move(Board.Action.DOWN);

        assertEquals(4, board.getMaxHeight());

    }

    @Test
    public void testDropHeight() {
        int width = 10;
        int height = 20;
        TetrisBoard board = new TetrisBoard(width, height);

        Piece piece = new TetrisPiece(Piece.PieceType.STICK);
        Point initialPosition = new Point(4, 0);
        board.nextPiece(piece, initialPosition);

        int calculatedDropHeight = board.dropHeight(piece, initialPosition.x);

        assertEquals(19, calculatedDropHeight);

    }

    @Test
    public void testGetColumnHeight() {
        int width = 10;
        int height = 20;
        TetrisBoard board = new TetrisBoard(width, height);

        Piece piece = new TetrisPiece(Piece.PieceType.T);
        Point initialPosition = new Point(4, 0);
        board.nextPiece(piece, initialPosition);

        int columnHeight = board.getColumnHeight(4);

        assertEquals(0, columnHeight);

    }

    @Test
    public void testGetRowWidth() {
        int width = 10;
        int height = 20;
        TetrisBoard board = new TetrisBoard(width, height);

        Piece piece = new TetrisPiece(Piece.PieceType.RIGHT_L);
        Point initialPosition = new Point(4, 0);
        board.nextPiece(piece, initialPosition);

        int rowWidth = board.getColumnHeight(0);

        assertEquals(4, rowWidth);

    }

    @Test
    public void testRowClear() {
        // // Test clearing a row
        // tetrisBoard.nextPiece(Piece.getPiece(Piece.PieceType.L_SHAPE), new Point(0, 0));
        // tetrisBoard.move(Action.DROP);

        // // Move a piece to clear the first row
        // tetrisBoard.nextPiece(Piece.getPiece(Piece.PieceType.L_SHAPE), new Point(0, 1));
        // tetrisBoard.move(Action.DROP);

        // int rowsCleared = tetrisBoard.getRowsCleared();
        // assertEquals(1, rowsCleared);
        PieceType[][] tester = {{PieceType.RIGHT_DOG, null, null},
                                {PieceType.LEFT_DOG, null, null},
                                {PieceType.SQUARE, null, null}, 
                                {PieceType.LEFT_DOG, null, null}, 
                                {PieceType.RIGHT_DOG, null, null}};
        
        assertEquals(true, board.isRowFull(tester[0]));

    }

}