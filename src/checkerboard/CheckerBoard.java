/**
 * Guarda os dados do tabuleiro
 */

package checkerboard;

import game.Move;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

public class CheckerBoard extends JPanel {

    /**
     * Constante do tamanho do lado de cada casa
     */
    public static final int HOUSE_SIDE = 70;
    
    /**
     * Número de linhase colunas do tabuleiro
     */
    private int rows;
    private int cols;
    
    /**
     * Tabela das casas
     */
    private Map<Integer, CheckerHouse> houses;
    
    /**
     * Cores das casas do tabuleiro
     */
    private Color blackHouseColor;
    private Color whiteHouseColor;
    private Color playerColor;
    private Color oponentColor;
    private Color selectionColor;
    
    /**
     * Localização do click do mouse
     */
    private Point p;
    
    /**
     * Objeto da classe CBController
     */
    private CBController cb;
    
    /**
     * Menu popup para escolha de caminhos
     */
    private JPopupMenu pathChooser;

    /**
     * Construtor da classe - cria um mouse listener para captura da posição do click
     * 
     * @param rows int número de linhas
     * @param cols int número de colunas
     * @param rowsPieces int quantidade de linhas que serão preenchidas
     * @param cb CBController
     */
    public CheckerBoard(int rows, int cols, int rowsPieces, CBController cb) {
        this.cb = cb;
        this.rows = rows;
        this.cols = cols;

        houses = new HashMap<>();
        pathChooser = new JPopupMenu();

        blackHouseColor = Color.BLACK;
        playerColor = Color.RED.darker();
        whiteHouseColor = Color.WHITE;
        oponentColor = Color.LIGHT_GRAY;

        setLayout(null);
        rebuild(rows, cols, rowsPieces);
        addMouseListener(new MouseAdapter() {
            private Color background;

            @Override
            public void mouseReleased(MouseEvent e) {
                Point pIni = p;
                p = getMousePosition();

                if (!e.isPopupTrigger()) {
                    pathChooser.setVisible(false);
                }

                if (!(p.x >= HOUSE_SIDE * cols || p.y >= HOUSE_SIDE * rows)) {
                    if (pIni != null && p != null) {
                        movePiece(pIni, p);
                        pIni = null;
                    } else {
                        houseSelected(p);
                    }

                } else {
                    p = null;
                }

                //  repaint();
            }

        });
    }
    
    

    /**
     * Função que seta as possíveis jogadas da casa selecionada
     *
     * @param ch CheckerHouse casa selecionada
     */
    public void possiblePlays(CheckerHouse ch) {
        if (ch.getContentType() == CheckerHouse.CONTENT_TYPE_KING) {
            possibleKingPlays(ch);
            return;
        }
        List<Move> moves = this.cb.possiblesPlays(ch.getRow(), ch.getCol());
        ArrayList<Color> colors = getColors();

        for (int i = 0; i < moves.size(); i++) {//Percorre todas as possíveis jogadas
            for (Point p : moves.get(i).getPath()) {//Percorre o caminho de cada jogada
                ch = getHouseAt(p.x, p.y);
                ch.setSelectionMode(CheckerHouse.SELECTION_MODE_MOVE);
                if (!ch.getPathColor().contains(colors.get(i))) {//Verifica se já passou pelo caminho
                    ch.getPathColor().add(colors.get(i));
                    ch.getMoves().add(moves.get(i));
                }
                //ch.getMoves().add(moves.get(i));
            }
        }

    }

    /**
     * Função que retorna um ArrayList de cores predefinidas
     *
     * @return ArrayList de Color
     */
    public ArrayList<Color> getColors() {
        ArrayList<Color> colors = new ArrayList();

        colors.add(Color.GREEN.darker());
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.ORANGE);
        colors.add(Color.MAGENTA.darker());
        colors.add(Color.RED);

        return colors;
    }

    /**
     * Função que trata a escolha do caminho desejado para a realização da
     * jogada
     *
     * @param a Point com a posição inicial da peça que se deseja mover
     * @param b Point com a posição final da peça que se deseja mover
     */
    public void movePiece(Point a, Point b) {
        CheckerHouse chB = getHouseAt(b.y / HOUSE_SIDE, b.x / HOUSE_SIDE);
        CheckerHouse chA = getHouseAt(a.y / HOUSE_SIDE, a.x / HOUSE_SIDE);

        if (!cb.isMyTurn()) {
            setSelectionModeNone();
            return;
        }

        if (chA.getContentType() == CheckerHouse.CONTENT_TYPE_EMPTY || chA == chB) {
            houseSelected(b);
            return;
        }

        if (chB.getSelectionMode() == CheckerHouse.SELECTION_MODE_MOVE) {
            Color c = chA.getFgColor();

            chA.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            chA.setFgColor(c);
            List<Move> moves = chB.getMoves();
            if (moves.size() == 1 || chB.getPathColor().size() == 1) {
                this.cb.movePiece(moves.get(0));
                repaintBoard(moves.get(0));
                setSelectionModeNone();
            } else {
                pathChooser = new JPopupMenu();

                int i = 0;
                int last;

                for (Move m : chB.getMoves()) {
                    last = m.getPath().size() - 1;
                    createPopupMenuItem(m, chB.getPathColor().get(i), i, last);
                    i++;

                }
                pathChooser.show(this, p.x, p.y);
                pathChooser.setInvoker(null);

            }

        } else {
            JOptionPane.showMessageDialog(null, "Esse movimento não é permitido!");
            setSelectionModeNone();
        }
        p = null;

    }

    /**
     * Cria um item do PopupMenu e adiciona um evento MouseListener a este.
     * 
     * @param m Move
     * @param color Color Cor do caminho
     * @param i int Numero do caminho
     * @param last int última caminho
     */
    public void createPopupMenuItem(Move m, Color color, int i, int last) {
        Point p = m.getPath().get(last);
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setName("Caminho_" + i);
        jMenuItem.setText("Caminho " + Integer.toString(i + 1) + " ("
                + Integer.toString(p.x + 1) + "," + changeNumber(p.y) + ")");
        jMenuItem.setBorder(BorderFactory.createLineBorder(color, 3));
        pathChooser.add(jMenuItem);
        jMenuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cb.movePiece(m);
                repaintBoard(m);
                setSelectionModeNone();
                pathChooser.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * Função que redesenha o tabuleiro a cada jogada
     *
     * @param m Move
     */
    public void repaintBoard(Move m) {
        int[][] gameBoard = m.getBoard();
        CheckerHouse ch;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ch = getHouseAt(i, j);
                switch (gameBoard[i][j]) {
                    case 1://Men
                        ch.setContentType(CheckerHouse.CONTENT_TYPE_MEN);
                        ch.setFgColor(playerColor);
                        break;
                    case 2://King
                        ch.setContentType(CheckerHouse.CONTENT_TYPE_KING);
                        ch.setFgColor(playerColor);
                        break;
                    case -1://Men Op
                        ch.setContentType(CheckerHouse.CONTENT_TYPE_MEN);
                        ch.setFgColor(oponentColor);
                        break;
                    case -2://King Op
                        ch.setContentType(CheckerHouse.CONTENT_TYPE_KING);
                        ch.setFgColor(oponentColor);
                        break;
                    default://Empty house                        
                        ch.setContentType(CheckerHouse.CONTENT_TYPE_EMPTY);
                        break;

                }
            }
        }

        //repaint();
    }

    /**
     * Seta o mode de seleção da peça nas posições x, y do tabuleiro
     *
     * @param p Point com a posição do mouse
     */
    public void houseSelected(Point p) {
        int row = p.y / HOUSE_SIDE;
        int col = p.x / HOUSE_SIDE;

        CheckerHouse che = getHouseAt(row, col);

        if (che.getSelectionMode() == CheckerHouse.SELECTION_MODE_SELECTED) {
            setSelectionModeNone();
            this.p = null;
        } else {
            setSelectionModeNone();
            che.setSelectionMode(CheckerHouse.SELECTION_MODE_SELECTED);
            possiblePlays(che);

        }
    }

    /**
     * Seta como NONE o modo de seleção de todas as casas do tabuleiro
     */
    public void setSelectionModeNone() {
        for (CheckerHouse ch : this.houses.values()) {
            ch.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            ch.getMoves().clear();
            ch.getPathColor().clear();
        }
    }

    /**
     * Destaca a jogada do oponente
     *
     * @param m Move
     */
    public synchronized void opponentPlays(Move m) {
        PrintOpponent po = new PrintOpponent(m);
        po.start();
    }

    /**
     * Função que cria as peças e desenha o tabuleiro
     *
     * @param rows int linhas do tabuleiro
     * @param cols int colunas do tabuleiro
     * @param rowsPieces int linhas que serão preenchiadas com peças
     */
    public void rebuild(int rows, int cols, int rowsPieces) {
        removeAll();

        houses.clear();
        this.rows = rows;
        this.cols = cols;;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CheckerHouse house = new CheckerHouse(rows - r - 1, c);

                if ((r + c) % 2 == 0) {
                    house.setBgColor(blackHouseColor);
                    if (r < rowsPieces) {
                        house.setContentType(CheckerHouse.CONTENT_TYPE_MEN);
                        house.setFgColor(playerColor);
                    } else if (r >= rows - rowsPieces) {
                        house.setContentType(CheckerHouse.CONTENT_TYPE_MEN);
                        house.setFgColor(oponentColor);
                    }

                } else {
                    house.setBgColor(whiteHouseColor);
                }

                house.setBounds(c * HOUSE_SIDE, (rows - r - 1) * HOUSE_SIDE, HOUSE_SIDE, HOUSE_SIDE);

                houses.put((rows - r - 1) * cols + c, house);
                add(house);
            }
        }
        setLabels();
    }

    /**
     * Cria a numerção em torno do tabuleiro
     */
    public void setLabels() {
        for (int i = 0; i < 8; i++) {
            JLabel jl = new JLabel(Integer.toString(i + 1), JLabel.CENTER);
            jl.setOpaque(true);
            jl.setBackground(new Color(245, 245, 245));
            jl.setBorder(BorderFactory.createRaisedSoftBevelBorder());
            jl.setSize(HOUSE_SIDE / 2, HOUSE_SIDE);
            jl.setLocation(8 * HOUSE_SIDE, i * HOUSE_SIDE);
            add(jl);

            JLabel jc = new JLabel(changeNumber(i), JLabel.CENTER);
            jc.setOpaque(true);
            jc.setBackground(new Color(245, 245, 245));
            jc.setBorder(BorderFactory.createRaisedSoftBevelBorder());
            jc.setSize(HOUSE_SIDE, HOUSE_SIDE / 2);
            jc.setHorizontalTextPosition(SwingConstants.CENTER);
            jc.setLocation(i * HOUSE_SIDE, 8 * HOUSE_SIDE);
            add(jc);

        }
    }

    /**
     * Função que retorna a casa na posição row, col
     *
     * @param row Linha da casa
     * @param col Coluna da casa
     * @return CheckerHouse
     */
    public CheckerHouse getHouseAt(int row, int col) {
        if (houses.containsKey((row) * cols + col)) {
            return houses.get((row) * cols + col);
        } else {
            return null;
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Color getBlackHouseColor() {
        return blackHouseColor;
    }

    public void setBlackHouseColor(Color blackHouseColor) {
        this.blackHouseColor = blackHouseColor;
    }

    public Color getWhiteHouseColor() {
        return whiteHouseColor;
    }

    public void setWhiteHouseColor(Color whiteHouseColor) {
        this.whiteHouseColor = whiteHouseColor;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(Color selectionBorderColor) {
        this.selectionColor = selectionBorderColor;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public Color getOponentColor() {
        return oponentColor;
    }

    public void setOponentColor(Color oponentColor) {
        this.oponentColor = oponentColor;
    }

    /**
     * Trata as possíveis jogadas da dama
     * 
     * @param ch CheckerHouse
     */
    private void possibleKingPlays(CheckerHouse ch) {
        int i = 0;
        List<Move> moves = this.cb.possiblesPlays(ch.getRow(), ch.getCol());
        ArrayList<Color> colors = getColors();

        for (Move m : moves) {//Percorre as possíveis jogadas
            if (m.getPiecesTaken() == 0) {//Se não toma nenhuma peça
                Point p = m.getPath().get(m.getPath().size() - 1); //Pega o último caminho
                ch = getHouseAt(p.x, p.y);
                ch.setSelectionMode(CheckerHouse.SELECTION_MODE_MOVE);
                if (!ch.getPathColor().contains(colors.get(0))) {//Verifica se já passou por aquele caminho
                    ch.getPathColor().add(colors.get(0));//Adiciona caminho
                    ch.getMoves().add(m);
                }
            } else {
                for (Point p : m.getPath()) { //Se toma peças percorre os caminhos
                    ch = getHouseAt(p.x, p.y);
                    ch.setSelectionMode(CheckerHouse.SELECTION_MODE_MOVE);
                    if (!ch.getPathColor().contains(colors.get(i))) {
                        ch.getPathColor().add(colors.get(i));
                        ch.getMoves().add(m);
                    }//if caminhos repetidos
                }//for caminhos
            }//else toma peças
            i++;

        }//for jogadas
    }
    
    public String changeNumber (int i){
        String str = "";
       switch(i){
           case 0:
               str = "A";
               break;
           case 1:
               str = "B";
               break;
           case 2:
               str = "C";
               break;
           case 3:
               str = "D";
               break;
           case 4:
               str = "E";
               break;
           case 5:
               str = "F";
               break;
           case 6:
               str = "G";
               break;
           case 7:
               str = "H";
               break;
       }
       return str;
    }

    /**
     * Classe auxiliar para detacar as jogadas do oponente após o recebimento do
     * novo tabuleiro
     */
    class PrintOpponent extends Thread {

        /**
         * Movimento feito
         */
        private Move m;

        /**
         * Contrutor da classe
         * 
         * @param m Move 
         */
        public PrintOpponent(Move m) {
            this.m = m;
        }

        /**
         * Método run da thread para que tenha um delay na jogada, facilitando
         * a visualização
         */
        @Override
        public void run() {
            CheckerHouse ch;
            for (Point pos : m.getPath()) {
                ch = getHouseAt(pos.x, pos.y);
                ch.setSelectionMode(CheckerHouse.SELECTION_MODE_SELECTED);

            }
            try {
                synchronized (this) {

                    this.wait(2000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CheckerBoard.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                setSelectionModeNone();
                repaintBoard(m);

            }
        }

    }

}
