
package game;

import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author gustavo
 */
public class Game {
    private int[][] board = {
    { 0, -1,  0, -1,  0, -1,  0, -1},
    {-1,  0, -1,  0, -1,  0, -1,  0},
    { 0, -1,  0, -1,  0, -1,  0, -1},
    { 0,  0,  0,  0,  0,  0,  0,  0},
    { 0,  0,  0,  0,  0,  0,  0,  0},
    { 1,  0,  1,  0,  1,  0,  1,  0},
    { 0,  1,  0,  1,  0,  1,  0,  1},
    { 1,  0,  1,  0,  1,  0,  1,  0}};
    
    /* Para teste{
    { 0, -1,  0, -1,  0, -1,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0,  0},
    { 0, -1,  0, -1,  0, -1,  0, -1},
    { 0,  0,  0,  0,  0,  0,  0,  0},
    { 0,  0,  0, -1,  0, -1,  0,  0},
    { 1,  0,  0,  0,  0,  0,  0,  0},
    { 0,  1,  0, -1,  0, -1,  0,  1},
    { 1,  0,  1,  0,  1,  0,  1,  0}};
    */
            
            
    /* Para teste{
    { 0, -1,  0, -1,  0, -1,  0, -1},
    {-1,  0, -1,  0, -1,  0, -1,  0},
    { 0, -1,  0, -1,  0, -1,  0, -1},
    { 0,  0,  0,  0,  0,  0,  0,  0},
    { 0,  0,  0,  0,  0,  0,  0,  0},
    { 1,  0,  1,  0,  1,  0,  1,  0},
    { 0,  1,  0,  1,  0,  1,  0,  1},
    { 1,  0,  1,  0,  1,  0,  1,  0}};
    */


    public Game() {

    }
    
    
    public int borderChecker(int i, int j, int[][] board) {
        if (i < 0 || j < 0 || i > 7 || j > 7) {
            return 1;
        }
        return board[i][j];
    }

    /**
     * Primeiro movimento de uma peça normal, antes de começar a tomar outras peças
     * 
     * @param pos Point
     * @return List de movimentos
     */
    public List moveInit(Point pos) {
        List<Move> moves = new ArrayList();
        List<Point> path;
        int[][] newBoard;
        int piece;
        if (this.board[pos.x][pos.y] > 0) {

            for (int i = -1; i <= 1; i = i + 2) {
                for (int j = -1; j <= 1; j = j + 2) { //Percorre os vizinhos                    
                    piece = borderChecker(pos.x + i, pos.y + j, this.board);
                    path = new LinkedList();
                    if (piece == 0) { //Se o caminho estiver vazio
                        if (i == -1) {
                            newBoard = cloneBoard(this.board);
                            newBoard[pos.x + i][pos.y + j] = 1;
                            newBoard[pos.x][pos.y] = 0;
                            path.add(new Point(pos.x + i, pos.y + j));
                            moves.add(new Move(pos, 0, path, newBoard));
                        }
                    }
                    if (piece < 0) {//Se a peça for do oponente
                        piece = borderChecker(pos.x + 2 * i, pos.y + 2 * j, this.board);//Posição depois de comer a peça
                        if (piece == 0) { //Se a posição é possível
                            newBoard = cloneBoard(this.board);
                            Point p = new Point(pos.x + 2 * i, pos.y + 2 * j);
                            newBoard[pos.x + i][pos.y + j] = 0;
                            newBoard[pos.x][pos.y] = 0;
                            newBoard[pos.x + 2 * i][pos.y + 2 * j] = 1;
                            moveTake(p, newBoard, 1, path, moves); //Verifica os caminhos
                        }
                    }
                }
            }
            if (moves.size() > 0) {
                moves = sort(moves);
            }

        }
        return (moves);
    }

    /**
     * Ordena os movimentos de acordo com o número de peças que tomam do adversário
     * 
     * @param moves List<Move>
     * @return List<Move>
     */
    private List<Move> sort(List<Move> moves) {
        Collections.sort(moves);

        int maxPieces = moves.get(0).getPiecesTaken();
        int qtd = 0;
        for (Move move : moves) {
            if (move.getPiecesTaken() == maxPieces) {
                qtd++;
            }
        }
        return moves.subList(0, qtd);

    }

    /**
     * Cria uma cópia do tabuleiro
     * 
     * @param clonedBoard int[][]
     * @return int[][] tabuleiro
     */
    private int[][] cloneBoard(int[][] clonedBoard) {
        int[][] newBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoard[i][j] = clonedBoard[i][j];
            }
        }
        return newBoard;
    }

    /**
     * Parte do movimento onde a peça escolhida esta tomando peças do adversário
     * 
     * @param pos Point posição da peça
     * @param newBoard int[][]
     * @param piecesTaken int
     * @param path List<Point>
     * @param moves List<Move>
     */
    private void moveTake(Point pos, int[][] newBoard, int piecesTaken, List<Point> path, List<Move> moves) {
        List<Point> newPath = new LinkedList();
        newPath.addAll(path);
        newPath.add(pos);
        int[][] newBoard1 = newBoard;
        int piece;
        for (int i = -1; i <= 1; i = i + 2) {
            for (int j = -1; j <= 1; j = j + 2) { //Percorre os vizinhos
                piece = borderChecker(pos.x + i, pos.y + j, newBoard);

                if (piece < 0) {//Se a peça for do oponente
                    piece = borderChecker(pos.x + 2 * i, pos.y + 2 * j, newBoard);//Posição depois de comer a peça
                    if (piece == 0) { //Se a posição é possível
                        Point p = new Point(pos.x + 2 * i, pos.y + 2 * j);
                        newBoard1 = cloneBoard(newBoard);
                        newBoard1[pos.x + i][pos.y + j] = 0;
                        newBoard1[pos.x][pos.y] = 0;
                        newBoard1[pos.x + 2 * i][pos.y + 2 * j] = 1;
                        moveTake(p, newBoard1, piecesTaken + 1, newPath, moves); //Verifica os caminhos
                    }
                }//if
            }
        }//for

        moves.add(new Move(pos, piecesTaken, newPath, newBoard1));
    }

    /**
     * Seta o tabuleiro depois de uma jogada
     * 
     * @param board 
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }

}
