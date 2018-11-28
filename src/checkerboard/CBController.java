/*
 * Controlador geral
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

    /**
     * Objeto da classe Connection
     */
    private Connection con;
    
    /**
     * Thread para receber a jogada do oponente
     */
    private Thread gameThread;
    
    /**
     * Objeto da classe Game
     */
    private Game game;
    
    /**
     * Objeto da classe MainFrame
     */
    private MainFrame mf;

    public CBController() {
        this.game = new Game();
        this.con = new Connection(this);
        this.game.resetBoard();
    }

    public void setMF(MainFrame mf) {
        this.mf = mf;
    }

    public boolean isMyTurn() {
        return this.con.isMyTurn();
    }

    /**
     * Movimento que este jogador realizará
     * 
     * @param move Move
     */
    public void movePiece(Move move) {
        String str;
        if (this.con.isMyTurn()) {
            this.con.sendBord(move);
            this.game.setBoard(move.getBoard());
            if (move.getPath() == null) {
                str = "Sua jogada: PERDEU A VEZ!\n\n";
            } else {
                str = "Sua jogada:\n" + "Caminho: " + getPath(move) + "\n"
                        + "Número de peças tomadas: " + move.getPiecesTaken() + "\n\n";
            }
            this.mf.setLogText(str);
            this.mf.setTurn("Vez do oponente");
        }

        endGame();
    }

    /**
     * Pega as jogadas possíveis para a peça selecionada
     * 
     * @param row int 
     * @param col int
     * 
     * @return List de objetos Move com as jogadas possíveis
     */
    public List possiblesPlays(int row, int col) {
        Point p = new Point(row, col);
        return this.game.moveInit(p);
    }

    /**
     * Conecta a um host
     * 
     * @param ip String
     * @param port int
     */
    public void connect(String ip, int port) {
        try {
            this.con.setPort(port);
            this.con.setIp(InetAddress.getByName(ip));
            this.con.connect();
        } catch (UnknownHostException ex) {
            Logger.getLogger(CBController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIP() {
        return this.con.getIp().getHostAddress();
    }

    public String getPort() {
        return Integer.toString(this.con.getPort());
    }

    /**
     * Faz este jogadar se tornar um host e esperar a conexão de um outro jogador
     */
    public void host() {
        this.con.host();
    }

    /**
     * Deixar de escutar por novos jogadores
     */
    void cancelHost() {
        this.con.cancelHost();
    }

    /**
     * Seta uma novo jogo quando um jogador é encontrado
     */
    public void playerFound() {
        this.gameThread = new Thread(this.con);
        this.gameThread.start();

        JOptionPane.showMessageDialog(null, "Oponente conectado!");
        this.mf.setTitle("Damas - Em jogo");
        this.mf.getConcede().setEnabled(true);
        this.mf.getMenu().setEnabled(false);
        this.mf.setHost(false);
        this.mf.getCheckerBoard().rebuild(8, 8, 3);
        this.game.resetBoard();
        Move m = new Move(null,0,null,this.game.getBoard());
        if(this.con.isMyTurn()){
            this.mf.setTurn("Sua vez!");
        }else{
            this.mf.setTurn("Vez do oponente");
        }
        this.mf.clearLog();
        this.mf.getCheckerBoard().repaintBoard(m);
    }

    /**
     * Atualiza o tabuleiro depois de receber a jogada do adversário
     * 
     * @param move Move
     */
    public void setMove(Move move) {
        String str;
        //Verifica desistência
        if (move.getBoard() == null) {
            JOptionPane.showMessageDialog(null, "Seu Oponente Desistiu !");
            this.mf.setTitle("Damas");
            this.mf.getConcede().setEnabled(false);
            this.mf.getMenu().setEnabled(true);
            this.con.disconnect();
            this.con.setMyTurn(false);
            return;
        }
        move.turnBoard();
        this.game.setBoard(move.getBoard());
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
            if (!this.game.isPossible2Move()) {
                Move m = new Move(null, 0, null, move.getBoard());
                JOptionPane.showMessageDialog(mf, "Você não possui movimentos válidos\nPerdeu a vez!");
                this.con.setMyTurn(true);
                movePiece(m);
            }
        }

    }

    /**
     * Pega a String do caminho
     * 
     * @param move Move
     * 
     * @return String
     */
    private String getPath(Move move) {
        CheckerBoard cb = this.mf.getCheckerBoard();
        String str = "";
        for (Point p : move.getPath()) {
            str += "(" + Integer.toString(p.x + 1) + "," + cb.changeNumber(p.y) + ") ";
        }
        return str;
    }

    /**
     * Trata o click no botão Desistir, finalizando a partida
     */
    public void concede() {
        Move m = new Move(null, 0, null, null);
        this.mf.setTitle("Damas");
        this.con.sendBord(m);
        this.gameThread.interrupt();
        this.mf.getConcede().setEnabled(false);
        this.mf.getMenu().setEnabled(true);
    }

    /**
     * Verifica se a partida terminou, a finalizando caso terminou
     * 
     * @return boolean
     */
    public boolean endGame() {
        if (this.game.isEndGame()) {
            if (this.game.isWinner()) {
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

    /**
     * Mata a thread do jogo
     */
    public void interrupt() {
        this.gameThread.interrupt();
    }

}
