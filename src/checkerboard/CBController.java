/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkerboard;

import game.Game;
import game.Move;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author Gi
 */
public class CBController {

    private Game g;

    public CBController() {
        this.g = new Game();
    }

    public void movePiece(Move m) {
      this.g.setBoard(m.getBoard());
    }

    public List possiblesPlays(int row, int col) {
        Point p = new Point(row, col);
        return this.g.moveInit(p);
    }

    public void setBoard(int[][] board) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
