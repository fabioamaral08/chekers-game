/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import checkerboard.CBController;
import game.Move;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabio
 */
public class Connection implements Runnable {

    private Socket soc;
    private Thread hostThread;
    private ServerSocket servSoc;
    private InetAddress ip;
    private int port;
    public CBController controller;
    private boolean myTurn;

    public Connection(CBController controller) {
        this.controller = controller;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    @Override
    public void run() {
        while (true) {
            recieveBoard();
            myTurn = true;

        }
    }

    public void initCon() {
        try {
            this.myTurn = true;
            this.ip = InetAddress.getLocalHost();
            this.servSoc = new ServerSocket(5000);
            this.port = this.servSoc.getLocalPort();
            this.soc = this.servSoc.accept();
            this.controller.playerFound();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param board
     */
    public void sendBord(Move move) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(this.soc.getOutputStream());
            out.writeObject(move);
            this.myTurn = false;
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recieveBoard() {
        try {
            ObjectInputStream in = new ObjectInputStream(this.soc.getInputStream());
            Move move = (Move) in.readObject();
            this.controller.setMove(move);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() {
        try {
            this.myTurn = false;
            this.soc = new Socket(ip, port);
            this.controller.playerFound();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            this.soc.close();
            this.servSoc.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void host() {
        Host h = new Host(this);
        this.hostThread = new Thread(h);
        this.hostThread.start();
    }

    public void cancelHost() {
        this.hostThread.interrupt();
    }

}
