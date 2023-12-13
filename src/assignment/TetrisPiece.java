package assignment;

import java.awt.*;
import java.util.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {

    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    private PieceType type;
    private int width;
    private int height;
    private int[] skirt;
    private Point[] body;
    private int rotationIndex; 
    private TetrisPiece nextRotation;
    private TetrisPiece prevRotation;


    public TetrisPiece(PieceType type) {
        this.type = type;
        rotationIndex = 0;
        Dimension d = type.getBoundingBox();
        width = (int) d.getWidth();
        height = (int) d.getHeight();
        body = type.getSpawnBody();
        skirt = new int[width];

        int ymin = Integer.MAX_VALUE;
        for(int i = 0; i < width; i++) {
            for(Point point : body) {
                if(point.getX() == i && ymin >= point.getY()) {
                    ymin = (int) point.getY();
                }  
                    skirt[i] = ymin;
            }
        }
        if(type != PieceType.SQUARE)
            initializePiece(rotationIndex + 1);    
    }

    public TetrisPiece(PieceType type, Point[] body, int rotationIndex, TetrisPiece original) {
        this.type = original.type;
        this.body = original.body;
        this.rotationIndex = rotationIndex;
        Dimension d = type.getBoundingBox();
        this.width = (int) d.getWidth();
        this.height = (int) d.getHeight();
        this.body = body;
        this.skirt = new int[width];
        this.prevRotation = original;
        initializePiece(rotationIndex + 1);
    }

    private void initializePiece(int index) {     
        if(index == 4) {
            this.prevRotation.prevRotation.prevRotation.prevRotation = this;
            this.nextRotation = this.prevRotation.prevRotation.prevRotation;
            return;  //returns the 0 tetris piece
        }
        
        Point[] rotatedBody = rotateBody(body, width);
        TetrisPiece rotation = new TetrisPiece(type, rotatedBody, index, this);
        this.nextRotation = rotation;
    }
    
    private Point[] rotateBody(Point[] original, int width) {
        // Rotate body clockwise
        Point[] rotated = new Point[original.length];
        for (int i = 0; i < original.length; i++) {
            int x = original[i].x;  
            int y = original[i].y;
            int modifier = width -1;
            Point temp = new Point(y, modifier - x);
            rotated[i] = temp;
        }
        return rotated;
    }

    @Override
    public PieceType getType() {
        return type;
    }

    @Override
    public int getRotationIndex() {
        return rotationIndex;
    }

    @Override
    public Piece clockwisePiece() {
        if(type == PieceType.SQUARE)
            return this;
        return nextRotation;
    }

    @Override
    public Piece counterclockwisePiece() {
        if(type == PieceType.SQUARE)
            return this;
        return prevRotation;
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
    public Point[] getBody() {
        return body;
    }

    @Override
    public int[] getSkirt() {
        return skirt;
    }

    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;
        System.out.println(otherPiece.type + " " + otherPiece.rotationIndex);
        return type == otherPiece.type && rotationIndex == otherPiece.rotationIndex;
    }
}