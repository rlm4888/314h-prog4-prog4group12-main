package assignment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment.Piece;
import assignment.Piece.PieceType;
import assignment.TetrisPiece;

import org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.util.Arrays;

public class TetrisPieceTester {
    private TetrisPiece piece;
    private TetrisPiece piece2;

    @BeforeEach
    public void setUp(){
        piece = new TetrisPiece(PieceType.STICK);
        piece2 = new TetrisPiece(PieceType.LEFT_L);
    }
    @Test
    public void testGetType() {
        assertEquals(PieceType.STICK, piece.getType());
        assertEquals(PieceType.LEFT_L, piece2.getType());

    }
     @Test
    public void testGetRotationIndex() {
        assertEquals(0, piece.getRotationIndex());
        assertEquals(0, piece.getRotationIndex());

    }
     @Test
    public void testGetWidth() {
        assertEquals(4, piece.getWidth());
        assertEquals(3, piece.getWidth());

    }
     @Test
    public void testGetHeight() {
        assertEquals(4, piece.getHeight());
        assertEquals(3, piece.getHeight());

    }
     @Test
    public void testGetBody() {
        Point[] body = piece.getBody();
        assertEquals(4, body.length);
        assertTrue(Arrays.asList(body).contains(new Point(0,0)));
        assertTrue(Arrays.asList(body).contains(new Point(1,0)));
        assertTrue(Arrays.asList(body).contains(new Point(2,0)));
        assertTrue(Arrays.asList(body).contains(new Point(3,0)));
        Point[] body2 = piece2.getBody();
        assertEquals(4, body2.length);
        assertTrue(Arrays.asList(body2).contains(new Point(0,0)));
        assertTrue(Arrays.asList(body2).contains(new Point(1,0)));
        assertTrue(Arrays.asList(body2).contains(new Point(0,2)));
        assertTrue(Arrays.asList(body2).contains(new Point(0,3)));
    }
      @Test
    public void testGetSkirt() {
        int[] skirt = piece.getSkirt();
        assertEquals(4, skirt.length);
        assertArrayEquals(new int[]{0,0,0,0}, skirt);

    }
      @Test
    public void testClockwisePiece() {
        Piece rotatedPiece = piece.clockwisePiece();
        assertEquals(1, rotatedPiece.getRotationIndex());
        assertEquals(piece.getType(), rotatedPiece.getType());

    }
     @Test
    public void testCounterclockwisePiece() {
        Piece rotatedPiece = piece.counterclockwisePiece();
        assertEquals(3, rotatedPiece.getRotationIndex());
        assertEquals(piece.getType(), rotatedPiece.getType());
        //can add more to validate the rotated piece's properties

    }
     @Test
    public void testEquals() {
        Piece piece1 = new TetrisPiece(PieceType.STICK);
        Piece piece2 = new TetrisPiece(PieceType.STICK);
        Piece piece3 = new TetrisPiece(PieceType.SQUARE);

        assertTrue(piece1.equals(piece2)); // Pieces with the same type and rotation are equal
        assertTrue(piece2.equals(piece1)); // Equality should be symmetric
        assertTrue(piece1.equals(piece1)); // An object should be equal to itself
        assertTrue(piece2.equals(piece2)); // An object should be equal to itself

        assertTrue(piece3.equals(piece3)); // An object should be equal to itself
        assertTrue(piece3.equals(new TetrisPiece(PieceType.SQUARE))); // Pieces with the same type and rotation are equal
        assertTrue(new TetrisPiece(PieceType.SQUARE).equals(piece3)); // Equality should be symmetric

        // Unequal cases
        assertTrue(!piece1.equals(piece3)); // Pieces with different types are not equal
        assertTrue(!piece3.equals(piece1)); // Equality should be symmetric
    }
}
   
    

