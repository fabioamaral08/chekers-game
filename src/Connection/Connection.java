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
    private ServerSocket servSoc;
    private InetAddress ip;
    private int port;
    public CBController controller;
    private boolean myTurn;

    public Connection() {
        try {
            this.controller = new CBController();
            this.servSoc = new ServerSocket();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while(true){
            if(!myTurn){
                recieveBoard();
                myTurn = true;
            }
        }
    }

    public void initCon() {
        try {
            this.myTurn = true;
            this.ip = this.servSoc.getInetAddress();
            this.soc = this.servSoc.accept();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param board
     */
    public void sendBord(int[][] board) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(this.soc.getOutputStream());
            out.writeObject(board);
            this.myTurn = false;
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recieveBoard() {
        try {
            ObjectInputStream in = new ObjectInputStream(this.soc.getInputStream());
            int[][] board = (int[][]) in.readObject();
            this.controller.setBoard(board);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void connect() {
        try {
            this.myTurn = false;
            this.soc = new Socket(ip, port);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void disconnect() {
        try {
            this.soc.close();
            this.servSoc.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void host(){
        Host h = new Host(this);
        Thread t = new Thread(h);
        t.start();
    }
    
}
