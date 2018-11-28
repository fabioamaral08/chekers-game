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
import javax.swing.JOptionPane;

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
            if (move.getPath() == null) {
                str = "Sua jogada: PERDEU A VEZ!\n\n";
                this.mf.setLogText(str);
            } else {
                str = "Sua jogada:\n" + "Caminho: " + getPath(move) + "\n"
                        + "Número de peças tomadas: " + move.getPiecesTaken() + "\n\n";
            }
            this.mf.setLogText(str);
            this.mf.setTurn("Vez do oponente");
        }

        endGame();

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

        JOptionPane.showMessageDialog(null, "Oponente conectado!");
        this.mf.setTitle("Damas - Em jogo");
        this.mf.getConcede().setEnabled(true);
        this.mf.getMenu().setEnabled(false);

        this.mf.getCheckerBoard().rebuild(8, 8, 3);
        this.g.resetBoard();
    }

    public void setMove(Move move) {
        String str;
        //Verifica desistência
        if (move.getBoard() == null) {
            JOptionPane.showMessageDialog(null, "Seu Oponente Desistiu !");
            this.mf.setTitle("Damas");
            this.mf.getConcede().setEnabled(false);
            this.mf.getMenu().setEnabled(true);
            this.con.disconnect();
            return;
        }
        move.turnBoard();
        this.g.setBoard(move.getBoard());
        //Verifica se o oponente perdeu a vez
        if (move.getPath() == null) {
            JOptionPane.showMessageDialog(mf, "Seu oponente não possui movimentos válidos e perdeu a vez!");
            str = "Jogada do oponente: PERDEU A VEZ\n\n";
            this.mf.setLogText(str);
        } else {
            this.mf.getCheckerBoard().opponentPlays(move);
            str = "Jogada do oponente:\n" + "Caminho: " + getPath(move) + "\n"
                    + "Número de peças tomadas: " + move.getPiecesTaken() + "\n\n";
            this.mf.setLogText(str);
            this.mf.setTurn("Sua vez!");
        }
        if (!endGame()) {
            if (!this.g.isPossible2Move()) {
                Move m = new Move(null, 0, null, move.getBoard());
                JOptionPane.showMessageDialog(mf, "Você não possui movimentos válidos\nPerdeu a vez!");
                this.con.setMyTurn(true);
                movePiece(m);
            }
        }

    }

    private String getPath(Move move) {
        CheckerBoard cb = this.mf.getCheckerBoard();
        String str = "";
        for (Point p : move.getPath()) {
            str += "(" + Integer.toString(p.x + 1) + "," + cb.changeNumber(p.y) + ") ";
        }
        return str;
    }

    public void concede() {
        Move m = new Move(null, 0, null, null);
        this.mf.setTitle("Damas");
        this.con.sendBord(m);
        this.gameThread.interrupt();
        this.mf.getConcede().setEnabled(false);
        this.mf.getMenu().setEnabled(true);
    }

    public boolean endGame() {
        if (this.g.isEndGame()) {
            if (this.g.isWinner()) {
                JOptionPane.showMessageDialog(null, "Parabéns, você é o vencedor");
            } else {
                JOptionPane.showMessageDialog(null, "Você foi derrotado");
            }

            this.con.disconnect();
            this.gameThread.interrupt();
            this.mf.getConcede().setEnabled(false);
            this.mf.getMenu().setEnabled(true);
            return true;
        }
        return false;
    }

    public void interrupt() {
        this.gameThread.interrupt();
    }

}
