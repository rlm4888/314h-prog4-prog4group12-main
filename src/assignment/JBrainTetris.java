package assignment;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

import assignment.Piece.PieceType;

public class JBrainTetris extends JTetris {

    LameBrain lameBrain = new LameBrain();
    PromaBrain promaBrain = new PromaBrain();
    protected javax.swing.Timer timer2;

    JBrainTetris() {
        super();
        timer2 = new javax.swing.Timer(10, new ActionListener() {
            //Board.Action action = lameBrain.nextMove(board);
            public void actionPerformed(ActionEvent e) {
                tick(promaBrain.nextMove(board));
            }
    });
    }

    public void startGame() {
        super.startGame();
        timer2.start();
    }

    public void stopGame() {
        super.stopGame();
        timer2.stop();
    }

    public static void main(String[] args) {
        createGUI(new JBrainTetris());
    }
}