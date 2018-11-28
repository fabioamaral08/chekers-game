/*
 * Classe responsável pela conexão entre os jogadores
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
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author fabio
 */
public class Connection implements Runnable {

    /**
     * Objeto da classe Socket para a troca de mensagens durante o jogo
     */
    private Socket soc;
    
    /**
     * Thread que aguarda um novo oponente
     */
    private Thread hostThread;
    
    /**
     * Socket que aguarda um novo oponente
     */
    private ServerSocket servSoc;
    
    /**
     * Objeto que guarda o IP
     */
    private InetAddress ip;
    
    /**
     * Guarda a porta
     */
    private int port;
    
    /**
     * Objeto da classe CBController
     */
    public CBController controller;
    
    /**
     * Informa se o turno é do jogador ou do oponente
     */
    private boolean myTurn;

    /**
     * Construtor da classe
     * 
     * @param controller CBController
     */
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

    /**
     * Cria a conexão do host
     */
    public void initCon() {
        try {
            this.myTurn = true;
            this.ip = InetAddress.getLocalHost();
            this.servSoc = new ServerSocket(5000);
            this.port = this.servSoc.getLocalPort();
            this.soc = this.servSoc.accept();
            this.servSoc.close();
            this.controller.playerFound();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Envia o tabuleiro com a jogada do jogador
     * 
     * @param move Move
     */
    public void sendBord(Move move) {
        try {
            this.myTurn = false;
            ObjectOutputStream out = new ObjectOutputStream(this.soc.getOutputStream());
            out.writeObject(move);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Recebe o tabuleiro com a jogada do oponente
     */
    private void recieveBoard() {
        try {
            ObjectInputStream in = new ObjectInputStream(this.soc.getInputStream());
            Move move = (Move) in.readObject();
            this.controller.setMove(move);
        } catch (IOException ex) {
            if (ex instanceof SocketException) {
                this.controller.interrupt();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Conecta com o host
     */
    public void connect() {
        try {
            this.myTurn = false;
            this.soc = new Socket(ip, port);
            this.controller.playerFound();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Falha na conexão!");
        }
    }

    /**
     * Disconecta
     */
    public void disconnect() {
        try {
            this.soc.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cria a thread do host
     */
    public void host() {
        Host h = new Host(this);
        this.hostThread = new Thread(h);
        this.hostThread.start();
    }

    /**
     * Mata a thread do host
     */
    public void cancelHost() {
        this.hostThread.interrupt();
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

}
