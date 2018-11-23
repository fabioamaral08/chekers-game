/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.List;
import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author gustavo
 */
public class Move implements Comparable<Move>, Serializable {
    private static final long serialVersionUID = 1L;
    
    //Posição da peça a jogar;
    private final Point curPos;
    //Número de peças comidas nesse movimento;
    private final int piecesTaken;
    //Posições a serem percorridas nesse movimento;
    private final List<Point> path;
    //Board final
    private final int[][] board;

    public Move(Point curPos, int piecesTaken, List<Point> path, int[][] board) {
        this.curPos = curPos;
        this.piecesTaken = piecesTaken;
        this.path = path;
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public Point getCurPos() {
        return curPos;
    }

    public int getPiecesTaken() {
        return piecesTaken;
    }

    public List<Point> getPath() {
        return path;
    }



    @Override
    public int compareTo(Move o) {
        if (this.piecesTaken < o.piecesTaken) {
            return 1;
        }
        if (this.piecesTaken > o.piecesTaken) {
            return -1;
        }

        return 0;
    }

}
