/*
 * Armazena os dados do movimento realizado
 */
package game;

import java.util.List;
import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author gustavo
 */
public class Move implements Comparable<Move>, Serializable {

    /**
     * Posição da peça a jogar
     */
    private Point curPos;

    /**
     * Número de peças comidas nesse movimento
     */
    private int piecesTaken;

    /**
     * Posições a serem percorridas nesse movimento
     */
    private List<Point> path;

    /**
     * Board final
     */
    private int[][] board;

    /**
     * Construtor da classe
     *
     * @param curPos int
     * @param piecesTaken int
     * @param path List<Path>
     * @param board int[][]
     */
    public Move(Point curPos, int piecesTaken, List<Point> path, int[][] board) {
        this.curPos = curPos;
        this.piecesTaken = piecesTaken;
        this.path = path;
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public Point getCurPos() {
        return curPos;
    }

    public int getPiecesTaken() {
        return piecesTaken;
    }

    public List<Point> getPath() {
        return path;
    }

    /**
     * Compara duas instancias de Move quanto ao número de peças tomadas, de 
     * forma crescente
     * 
     * @param o Move
     * 
     * @return int
     */
    @Override
    public int compareTo(Move o) {
        if (this.piecesTaken < o.piecesTaken) {
            return 1;
        }
        if (this.piecesTaken > o.piecesTaken) {
            return -1;
        }

        return 0;
    }

    /**
     * Espelha o tabuleiro para que seja enviado ao adversário
     */
    public void turnBoard() {

        if (path != null) {

            for (Point point : path) {
                point.x = 7 - point.x;
                point.y = 7 - point.y;
            }
        }
        int[][] newBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoard[i][j] = (-1) * this.board[7 - i][7 - j];
            }
        }
        this.board = newBoard;
    }

}
