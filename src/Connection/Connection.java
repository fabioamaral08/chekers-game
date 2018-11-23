/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import checkerboard.CBController;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initCon() {
        try {
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
    private void sendBord(int[][] board) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(this.soc.getOutputStream());
            out.writeObject(board);
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
    
}
