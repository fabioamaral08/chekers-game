/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkerboard;

import Connection.Connection;
import game.Game;
import game.Move;
import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gi
 */
public class CBController {

    private Connection con;
    private Thread gameThread;
    private Game g;
    private CheckerBoard cb;

    public CBController() {
        this.g = new Game();
        this.con = new Connection(this);
    }

    public void setCb(CheckerBoard cb) {
        this.cb = cb;
    }
    
     public boolean isMyTurn(){
        return this.con.isMyTurn();
    }

    public void movePiece(Move m) {
        if (this.con.isMyTurn()) {
            this.con.sendBord(m);
            this.g.setBoard(m.getBoard());
        }
    }

    public List possiblesPlays(int row, int col) {
        Point p = new Point(row, col);
        return this.g.moveInit(p);
    }

    void connect(String ip, int port) {
        try {
            this.con.setPort(port);
            this.con.setIp(InetAddress.getByName(ip));
            this.con.connect();
        } catch (UnknownHostException ex) {
            Logger.getLogger(CBController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String getIP() {
        return this.con.getIp().getHostAddress();
    }

    String getPort() {
        return Integer.toString(this.con.getPort());
    }

    void host() {
        this.con.host();
    }

    void cancelHost() {
        this.con.cancelHost();
    }

    public void playerFound() {
        this.gameThread = new Thread(this.con);
        this.gameThread.start();

        this.cb.rebuild(8, 8, 3);
        this.g.resetBoard();
    }

    public void setMove(Move move) {
        move.turnBoard();
        this.g.setBoard(move.getBoard());
        cb.repaintBoard(move);
    }

}
