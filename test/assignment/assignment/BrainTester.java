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
import java.util.ArrayList;

public class BrainTester {
    private TetrisBoard board;

    @BeforeEach
    public void setUp() {
        board = new TetrisBoard(10, 20);
        
    }

    @Test
    public void testEnumerate() {
        PromaBrain pb = new PromaBrain();
        pb.nextMove(board);
        ArrayList<Board.Action> moves = pb.getFirstMoves();
        boolean containsAll = false;
        boolean containsClockwise = false;
        boolean containsLeft = false;
        boolean containsCounterClockwise = false;
        boolean containsRight = false;
        boolean containsDrop = false;
        for(Board.Action act : moves) {
            if(act.equals(Board.Action.CLOCKWISE))
                containsClockwise = true;
            if(act.equals(Board.Action.LEFT))
                containsLeft = true;
            if(act.equals(Board.Action.COUNTERCLOCKWISE))
                containsCounterClockwise = true;
            if(act.equals(Board.Action.RIGHT))
                containsRight = true;
            if(act.equals(Board.Action.DROP))
                containsDrop = true;
        }

        if(containsClockwise && containsCounterClockwise && containsDrop && containsLeft && containsRight)
            containsAll = true;
        assertTrue(containsAll);
    }

    @Test
    public void testNextMove() {
        PromaBrain pb = new PromaBrain();
        pb.setFirstMoves(null);
        Board.Action nextA = pb.nextMove(board);
        assertEquals(Board.Action.DROP, nextA);
    }
}