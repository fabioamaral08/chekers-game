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

    /**
     * Casa em branco
     */
    public static final int BLANK = 0;

    /**
     * Peça aliada
     */
    public static final int ALLY_MEN = 1;

    /**
     * Dama Aliada
     */
    public static final int ALLY_KING = 2;

    /**
     * Peça inimiga
     */
    public static final int ENEMY_MEN = -1;

    /**
     * Dama inimiga
     */
    public static final int ENEMY_KING = -2;

    /**
     * Indica se o player é o vencedor
     */
    private boolean winner;

    /**
     * Tabulerio inicial do jogo
     */
    private static final int[][] INIT_BOARD
            = {
                {0, -1, 0, -1, 0, -1, 0, -1},
                {-1, 0, -1, 0, -1, 0, -1, 0},
                {0, -1, 0, -1, 0, -1, 0, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0}};

    /**
     * Tabuleiro com estado atual do jogo
     */
    private int[][] board
            = {
    { 0, -1,  0, -1,  0, -1,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0,  0},
    { 0, -1,  0, -1,  0, -1,  0, -1},
    { 0,  0,  0,  0,  0,  0,  0,  0},
    { 0,  0,  0, -1,  0, -1,  0,  0},
    { 1,  0,  0,  0,  0,  0,  0,  0},
    { 0,  1,  0, -1,  0, -1,  0,  1},
    { 1,  0,  1,  0,  1,  0,  1,  0}};/*{
                {0,  -1, 0, -1, 0, -1, 0, -1},
                {-1, 0, -1, 0, 0, 0, 0, 0},
                {0, 0, 0, -1, 0, -1, 0, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, -1, 0, -1, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 2, 0, 1, 0}};*/
//    {
            //        {0, -1, 0, -1, 0, -1, 0, -1},
            //        {-1, 0, -1, 0, -1, 0, -1, 0},
            //        {0, -1, 0, -1, 0, -1, 0, -1},
            //        {0, 0, 0, 0, 0, 0, 0, 0},
            //        {0, 0, 0, 0, 0, 0, 0, 0},
            //        {1, 0, 1, 0, 1, 0, 1, 0},
            //        {0, 1, 0, 1, 0, 1, 0, 1},
            //        {1, 0, 1, 0, 1, 0, 1, 0}};
            /* Para teste{
    { 0, -1,  0, -1,  0, -1,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0,  0},
    { 0, -1,  0, -1,  0, -1,  0, -1},
    { 0,  0,  0,  0,  0,  0,  0,  0},
    { 0,  0,  0, -1,  0, -1,  0,  0},
    { 1,  0,  0,  0,  0,  0,  0,  0},
    { 0,  1,  0, -1,  0, -1,  0,  1},
    { 1,  0,  1,  0,  1,  0,  1,  0}};
             */ /* Para teste{
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
        this.winner = false;
    }

    public boolean isWinner() {
        return winner;
    }
    
    

    /**
     * Verifica a peça na poção passada por parametro, verificando também se não está fora do tabuleiro
     * @param i Linha
     * @param j Coluna
     * @param board Tabulerio a ser verificado
     * @return Retorna a peça na posição indicada (1 caso a posição esteja fora do tabuleiro)
     */
    public int borderChecker(int i, int j, int[][] board) {
        if (i < 0 || j < 0 || i > 7 || j > 7) {
            return 1;
        }
        return board[i][j];
    }

    /**
     * Primeiro movimento de uma peça normal, antes de começar a tomar outras
     * peças
     *
     * @param pos Point
     * @return List de movimentos
     */
    public List moveInit(Point pos) {
        List<Move> moves = new ArrayList();

        if (this.board[pos.x][pos.y] == Game.ALLY_KING) {
            this.moveInitKing(pos, moves, new LinkedList());

            if (moves.size() > 0) {
                moves = sort(moves);
            }
            return moves;
        }

        List<Point> path;
        int[][] newBoard;
        int piece;

        if (this.board[pos.x][pos.y] > Game.BLANK) {
            for (int i = -1; i <= 1; i = i + 2) {
                for (int j = -1; j <= 1; j = j + 2) { //Percorre os vizinhos                    
                    piece = borderChecker(pos.x + i, pos.y + j, this.board);
                    path = new LinkedList();
                    if (piece == Game.BLANK) { //Se o caminho estiver vazio
                        if (i == -1) {
                            newBoard = cloneBoard(this.board);
                            if (pos.x + i == 0) {
                                newBoard[pos.x + i][pos.y + j] = Game.ALLY_KING;
                            } else {
                                newBoard[pos.x + i][pos.y + j] = Game.ALLY_MEN;
                            }
                            newBoard[pos.x][pos.y] = Game.BLANK;
                            path.add(new Point(pos.x + i, pos.y + j));
                            moves.add(new Move(pos, 0, path, newBoard));
                        }
                    }
                    if (piece < Game.BLANK) {//Se a peça for do oponente
                        piece = borderChecker(pos.x + 2 * i, pos.y + 2 * j, this.board);//Posição depois de comer a peça
                        if (piece == 0) { //Se a posição é possível
                            newBoard = cloneBoard(this.board);
                            Point p = new Point(pos.x + 2 * i, pos.y + 2 * j);
                            newBoard[pos.x + i][pos.y + j] = Game.BLANK;
                            newBoard[pos.x][pos.y] = Game.BLANK;
                            newBoard[pos.x + 2 * i][pos.y + 2 * j] = Game.ALLY_MEN;
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
     * Verifica movimentos possíveis para uma Dama
     *
     * @param pos Ponto com a posição atual da peça
     * @param moves Lista que armazenas os possiveis movimentos de se fazer com
     * essa peça
     * @param path Lista que armazena as posições que a peça percorreu
     */
    public void moveInitKing(Point pos, List<Move> moves, List<Point> path) {
        List<Point> newPath;
        int[][] board = this.board;
        Point posAux = new Point();
        int[][] newBoard;
        int piece;

        for (int i = -1; i <= 1; i = i + 2) {
            for (int j = -1; j <= 1; j = j + 2) { //Percorre os vizinhos

                piece = borderChecker(pos.x + i, pos.y + j, board);
                posAux.setLocation(pos.x + i, pos.y + j);
                newBoard = cloneBoard(board);
                newPath = new LinkedList();
                newPath.addAll(path);
                while (piece == Game.BLANK) {
                    newBoard[posAux.x][posAux.y] = Game.ALLY_KING;
                    newBoard[posAux.x - i][posAux.y - j] = Game.BLANK;
                    newPath.add(new Point(posAux.x, posAux.y));
                    moves.add(new Move(posAux, 0, new LinkedList(newPath), cloneBoard(newBoard)));
                    posAux.x += i;
                    posAux.y += j;
                    piece = borderChecker(posAux.x, posAux.y, newBoard);

                }
                if (piece < Game.BLANK) {//Se a peça for do oponente
                    piece = borderChecker(posAux.x + i, posAux.y + j, newBoard);//Posição depois de comer a peça
                    if (piece == Game.BLANK) { //Se a posição é possível
                        Point p = new Point(posAux.x + i, posAux.y + j);
                        newPath.add(p);
                        newBoard[posAux.x][posAux.y] = Game.BLANK;
                        newBoard[posAux.x - i][posAux.y - j] = Game.BLANK;
                        newBoard[p.x][p.y] = Game.ALLY_KING;
                        moveInitKingTake(p, 1, newBoard, moves, newPath); //Verifica os caminhos
                    }
                }

            }
        }

    }

    /**
     * Movimento da peça enquanto está em uma série da tomadas consecutivas em
     * uma única jogada
     *
     * @param pos posição atual da peça
     * @param piecesTaken Número de peças tomadas até agora
     * @param board Estado atual do tabuleiro com as peças tomadas
     * @param moves Lista de movimentos possiveis dessa peça
     * @param path Lista do caminho percorrido até a posição atual da peça
     */
    public void moveInitKingTake(Point pos, int piecesTaken, int[][] board, List<Move> moves, List<Point> path) {
        moves.add(new Move(pos, piecesTaken, new LinkedList(path), board));
        List<Point> newPath;
        Point posAux = new Point();
        int[][] newBoard;
        int piece;

        for (int i = -1; i <= 1; i = i + 2) {
            for (int j = -1; j <= 1; j = j + 2) { //Percorre os vizinhos

                piece = borderChecker(pos.x + i, pos.y + j, board);
                posAux.setLocation(pos.x + i, pos.y + j);
                newBoard = cloneBoard(board);
                newPath = new LinkedList();
                newPath.addAll(path);
                while (piece == Game.BLANK) {
                    newBoard[posAux.x][posAux.y] = Game.ALLY_KING;
                    newBoard[posAux.x - i][posAux.y - j] = Game.BLANK;
                    newPath.add(new Point(posAux.x, posAux.y));

                    posAux.x += i;
                    posAux.y += j;
                    piece = borderChecker(posAux.x, posAux.y, newBoard);

                }
                if (piece < Game.BLANK) {//Se a peça for do oponente
                    piece = borderChecker(posAux.x + i, posAux.y + j, newBoard);//Posição depois de comer a peça
                    if (piece == Game.BLANK) { //Se a posição é possível
                        Point p = new Point(posAux.x + i, posAux.y + j);
                        newPath.add(p);
                        newBoard[posAux.x][posAux.y] = Game.BLANK;
                        newBoard[posAux.x - i][posAux.y - j] = Game.BLANK;
                        newBoard[p.x][p.y] = Game.ALLY_KING;
                        moveInitKingTake(p, piecesTaken + 1, newBoard, moves, newPath); //Verifica os caminhos
                    }
                }

            }
        }

    }

    /**
     * Ordena os movimentos de acordo com o número de peças que tomam do
     * adversário
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
            if (pos.x == 0) {
                newBoard1[pos.x][pos.y] = Game.ALLY_KING;
                break;
            }
            for (int j = -1; j <= 1; j = j + 2) { //Percorre os vizinhos
                piece = borderChecker(pos.x + i, pos.y + j, newBoard);

                if (piece < Game.BLANK) {//Se a peça for do oponente
                    piece = borderChecker(pos.x + 2 * i, pos.y + 2 * j, newBoard);//Posição depois de comer a peça
                    if (piece == 0) { //Se a posição é possível
                        Point p = new Point(pos.x + 2 * i, pos.y + 2 * j);
                        newBoard1 = cloneBoard(newBoard);
                        newBoard1[pos.x + i][pos.y + j] = Game.BLANK;
                        newBoard1[pos.x][pos.y] = Game.BLANK;
                        newBoard1[pos.x + 2 * i][pos.y + 2 * j] = Game.ALLY_MEN;
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

    /**
     * Verifica se é fim de jogo por falta de peças de um dos lados
     *
     * @return true se um dos lados não possui mais peças e false caso
     * contrario;
     */
    public boolean isEndGame() {
        int lin, col;
        boolean ally = false, enemy = false;
        for (int i = 1; i < 64; i += 2) {
            lin = i / 8;
            col = i % 8;
            if (this.board[lin][col] > Game.BLANK) {
                ally = true;
            } else if (this.board[lin][col] < Game.BLANK) {
                enemy = true;
            }
            if (ally && enemy) {
                return false;
            }
        }
        this.winner = ally;
        return true;
    }

    /**
     *  Retorna o tabuleiro pro estado de inicio de jogo
     */
    public void resetBoard() {
        this.board = cloneBoard(INIT_BOARD);
        this.winner = false;
    }
}
