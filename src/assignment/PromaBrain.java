package assignment;

import java.util.*;

import assignment.Board.Action;

/**
 * A Lame Brain implementation for JTetris; tries all possible places to put the
 * piece (but ignoring rotations, because we're lame), trying to minimize the
 * total height of pieces on the board.
 */
public class PromaBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;
    private int holes;
    private int completeRows;
    private int bumpiness;
    private int aggHeight;
    private int pits;
    private int deepestWell;
    // Piece.PieceType[][] board;
    TetrisBoard tetris;
    int width;
    int height;
    private int rowsCleared;

    // public PromaBrain() {
    // width = 10;
    // height = 24;
    // tetris = new TetrisBoard(width, height);
    // board = tetris.getBoard();
    // }

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);
        rowsCleared = currentBoard.getRowsCleared();
        // rowsCleared = currentBoard.getRowsCleared();

        double best = Integer.MIN_VALUE;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            double score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        // System.out.println("piece: " + currentBoard.getCurrentPiece().getType() + " options: " + firstMoves + " best score: " + best + " move: " + firstMoves.get(bestIndex));
        return firstMoves.get(bestIndex);
    }


    public ArrayList<Board.Action> getFirstMoves() {
        return firstMoves;
    }

    public void setFirstMoves(ArrayList<Board.Action> newOne) {
        firstMoves = newOne;
    }
    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {
        options.add(currentBoard.testMove(Board.Action.DROP));
        // System.out.println("adding drop as a test move");
        firstMoves.add(Board.Action.DROP);
        Board left = currentBoard.testMove(Board.Action.LEFT);
        Board.Action rotation = null;
        for (int i = 0; i < 4; i++) {
            if (i == 3) {
                rotation = Board.Action.COUNTERCLOCKWISE;
            }
            // Now we'll add all the places to the left we can DROP
            while (left.getLastResult() == Board.Result.SUCCESS) {
                // System.out.println("adding left as a test move");
                options.add(left.testMove(Board.Action.DROP));
                if (rotation == null)
                    firstMoves.add(Board.Action.LEFT);
                else
                    firstMoves.add(rotation);
                left.move(Board.Action.LEFT);
            }

            // And then the same thing to the right
            Board right = currentBoard.testMove(Board.Action.RIGHT);
            while (right.getLastResult() == Board.Result.SUCCESS) {
                // System.out.println("adding right as a test move");
                options.add(right.testMove(Board.Action.DROP));
                if (rotation == null)
                    firstMoves.add(Board.Action.RIGHT);
                else
                    firstMoves.add(rotation);
                right.move(Board.Action.RIGHT);
            }
            currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);
            rotation = Board.Action.CLOCKWISE;
        }
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private double scoreBoard(Board newBoard) {
        double score = 2000;
        score += (19.6 * countCompleteRows(newBoard));
        score -= (11 * countBumpiness(newBoard));
        score -= (19.8 * countBarricades(newBoard));
        score -= (2.6 * rowGaps(newBoard));
        return score;
    }

    private int rowGaps(Board currentBoard) {
        int rowsTransitions = 0;
        for(int i = 0; i < currentBoard.getHeight(); i++) {
            for(int j = 0; j < currentBoard.getWidth() - 1; j++) {
                if(currentBoard.getGrid(j, i) == null && currentBoard.getGrid(j + 1, i) != null)
                    rowsTransitions++;
            }
        }
        return rowsTransitions;
    }
    
    private int standardDeviationHeight(Board currentBoard) {
        int mean = calcAggHeight(currentBoard) / currentBoard.getWidth();
        int difference = 0;
        for (int i = 0; i < currentBoard.getWidth(); i++) {
            difference += Math.abs(mean - currentBoard.getColumnHeight(i));
        }
        // return difference/width;
        return mean;
    }

    private int countCompleteRows(Board currentBoard) {
        return currentBoard.getRowsCleared() - rowsCleared;
    }

    // private int checkDistanceFrom() {
    // }

    private int countHoles(Board currentBoard) {
        for (int i = 0; i < currentBoard.getHeight() - 1; i++) {
            for (int j = 0; j < currentBoard.getWidth(); j++) {
                if (currentBoard.getGrid(j, i) == null) {
                    if (currentBoard.getGrid(j, i + 1) != null) {
                        holes++;
                        break;
                    }
                }
            }
        }
        return holes;
    }

    private int countBarricades(Board currentBoard) {
        int barricades = 0;
        for (int i = currentBoard.getHeight() - 1; i > 0; i--) {
            for (int j = 0; j < currentBoard.getWidth(); j++) {
                if (currentBoard.getGrid(j, i) != null) {
                    int index = i;
                    while (index > 0 && currentBoard.getGrid(j, index - 1) == null) {
                        barricades++;
                        index--;
                    }
                }
            }
        }
        return barricades;
    }

    private int countPits(Board currentBoard) {
        for (int i = 0; i < width; i++) {
            if (currentBoard.getColumnHeight(i) == 0)
                pits++;
        }
        return pits;
    }

    private int countBumpiness(Board currentBoard) {
        int bumpiness = 0;
        for (int i = 0; i < currentBoard.getWidth() - 1; i++) {
            int current = currentBoard.getColumnHeight(i);
            int next = currentBoard.getColumnHeight(i + 1);
            int difference = Math.abs(current - next);
            bumpiness += difference;
        }

        return bumpiness;
    }

    private int calcAggHeight(Board currentBoard) {
        for (int i = 1; i < currentBoard.getWidth(); i++) {
            aggHeight += currentBoard.getColumnHeight(i);
        }

        return aggHeight;
    }

    private int calcAverageRowWidths(Board currentBoard) {
        int avgRowWidth = 0;
        int occupiedRows = 0;
        for (int i = 0; i < height; i++) {
            int rowWidth = 0;
            for (int j = 0; j < width; j++) {
                if (currentBoard.getGrid(j, i) != null) {
                    rowWidth++;
                }
            }
            if (rowWidth != 0) {
                occupiedRows++;
                avgRowWidth += rowWidth;
            }
        }
        if (occupiedRows == 0) {
            return 0;
        }
        avgRowWidth /= occupiedRows;
        return avgRowWidth;
    }
}