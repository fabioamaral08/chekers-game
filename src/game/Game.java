/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author gustavo
 */
public class Game {

    private int[][] board = new int[8][8];

    public List moveInit(Point pos) {
        List<Move> moves = new ArrayList();
        List<Point> path = new LinkedList();
        int piece;
        for (int i = -1; i <= 1; i = i + 2) {
            for (int j = -1; j <= 1; j = j + 2) {
                piece = this.board[pos.x + i][pos.y + j];
                path.clear();
                if (piece == 0) {
                    if (i == -1) {
                        path.add(new Point(pos.x + i, pos.y + j));
                        moves.add(new Move(pos, 0, path));
                    }
                }
                if (piece < 0) {
                    piece = this.board[pos.x + 2 * i][pos.y + 2 * j];
                    if (piece == 0) {
                        Point p = new Point(pos.x + 2 * i, pos.y + 2 * j);
                        int[][] newBoard = new int[8][8];
                        newBoard = this.board.clone();
                        newBoard[pos.x + i][pos.y + j] = 0;
                        newBoard[pos.x][pos.y] = 0;
                        newBoard[pos.x + 2 * i][pos.y + 2 * j] = 1;
                        moveTake(p, newBoard,0,path,moves);
                    }
                }
            }
        }
        return (moves);
    }

    private void moveTake(Point p, int[][] newBoard, int i, List<Point> path, List<Move> moves) {
        
    }
    
    

}
