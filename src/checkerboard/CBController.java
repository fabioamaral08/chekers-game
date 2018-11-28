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
    private MainFrame mf;

    public CBController() {
        this.g = new Game();
        this.con = new Connection(this);
    }

    public void setMF(MainFrame mf) {
        this.mf = mf;
    }

    public boolean isMyTurn() {
        return this.con.isMyTurn();
    }

    public void movePiece(Move move) {
        String str;
        if (this.con.isMyTurn()) {
            this.con.sendBord(move);
            this.g.setBoard(move.getBoard());
            str = "Sua jogada:\n" + "Caminho: " + getPath(move) + "\n"
                    + "Número de peças tomadas: " + move.getPiecesTaken() + "\n\n";
            this.mf.setLogText(str);
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

        this.mf.getCheckerBoard().rebuild(8, 8, 3);
        this.g.resetBoard();
    }

    public void setMove(Move move) {
        String str;
        move.turnBoard();
        this.g.setBoard(move.getBoard());
        this.mf.getCheckerBoard().opponentPlays(move);
        str = "Jogada do oponente:\n" + "Caminho: " + getPath(move) + "\n"
                + "Número de peças tomadas: " + move.getPiecesTaken() + "\n\n";
        this.mf.setLogText(str);

    }

    private String getPath(Move move) {
        String str = "";
        for (Point p : move.getPath()) {
            str += "(" + Integer.toString(p.x + 1) + "," + Integer.toString(p.y + 1) + ") ";
        }
        return str;
    }

}
