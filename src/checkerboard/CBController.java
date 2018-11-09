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

    public void movePiece(int[][] board) {
      this.g.setBoard(board);
    }

    public List possiblesPlays(Point pos, int houseSide) {
        Point p = new Point(pos.y / houseSide, pos.x / houseSide);
        return this.g.moveInit(p);
    }

}
