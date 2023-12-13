package assignment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assignment.Piece.PieceType;

import static org.junit.Assert.assertEquals;

import java.awt.Point;

/*
 * Any comments and methods here are purely descriptions or suggestions.
 * This is your test file. Feel free to change this as much as you want.
 */

public class TetrisTest {

    // This will run ONCE before all other tests. It can be useful to setup up
    // global variables and anything needed for all of the tests.
    TetrisPiece tester = new TetrisPiece(PieceType.T);

    @BeforeAll
    static void setupAll() {

    }

    // This will run before EACH test.
    @BeforeEach
    void setupEach() {
    }

    // You can test execute critter here. You may want to make additional tests and
    // your own testing harness. See spec section 2.5 for more details.
    // @Test
    // void testTetrisPiece() {
    //     // Point[] body = tester.getBody();
    //     // for(Point p : body)
    //     //     System.out.println(p.getX() + " " + p.getY());
    //     // System.out.println("clockwise");
    //     // Piece a = tester.clockwisePiece();
    //     // Point[] body2 = a.getBody();
    //     // for(Point p : body2)
    //     //     System.out.println(p.getX() + " " + p.getY());
    //     // System.out.println("clockwise");
    //     // Piece x = a.clockwisePiece();
    //     // Point[] body3 = x.getBody();
    //     // for(Point p : body3)
    //     //     System.out.println(p.getX() + " " + p.getY());
    //     // System.out.println("clockwise");
    //     // Piece y = x.clockwisePiece();
    //     // Point[] body4 = tester.clockwisePiece().clockwisePiece().clockwisePiece().getBody();
    //     // for(Point p : body4)
    //     //     System.out.println(p.getX() + " " + p.getY());
    //     //System.out.println(tester.clockwisePiece());
    //     TetrisPiece tester2 = (TetrisPiece) tester.counterclockwisePiece();
    //     System.out.println("original tp: " + tester);
    //     System.out.println("original: ");
    //     for(Point a : tester.getBody())
    //         System.out.println(a.getX() + " " + a.getY());
    //     tester = (TetrisPiece) tester.clockwisePiece();
    //     System.out.println("90 degrees: ");
    //     for(Point a : tester.getBody())
    //         System.out.println(a.getX() + " " + a.getY());
    //     tester = (TetrisPiece) tester.clockwisePiece();
    //     System.out.println("180 degrees: ");
    //     for(Point a : tester.getBody())
    //         System.out.println(a.getX() + " " + a.getY());
    //     tester = (TetrisPiece) tester.clockwisePiece();
    //     System.out.println("270 degrees: ");
    //     for(Point a : tester.getBody())
    //         System.out.println(a.getX() + " " + a.getY());
    //     //System.out.println("third rotation: " + tester + " should be same: " + tester2);
    //     tester = (TetrisPiece) tester.clockwisePiece();
    //     System.out.println("0 degrees: ");
    //     for(Point a : tester.getBody())
    //         System.out.println(a.getX() + " " + a.getY());
    //     System.out.println("should be original tp: " + tester);
    

    @Test
    void testTetrisBoard() {
        TetrisBoard test = new TetrisBoard(10, 24);
        test.nextPiece(tester, null);
    }
}
