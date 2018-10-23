/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.List;
import java.awt.Point;

/**
 *
 * @author gustavo
 */
public class Move {

    //Posição da peça a jogar;
    private Point curPos;
    //Número de peças comidas nesse movimento;
    private int piecesTaken;
    //Posições a serem percorridas nesse movimento;
    private List<Point> path;

    public Move(Point curPos, int piecesTaken, List<Point> path) {
        this.curPos = curPos;
        this.piecesTaken = piecesTaken;
        this.path = path;
    }

    public Point getCurPos() {
        return curPos;
    }

    public int getPiecesTaken() {
        return piecesTaken;
    }

    public List getPath() {
        return path;
    }

}
