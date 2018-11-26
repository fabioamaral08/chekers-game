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
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class CheckerBoard extends JPanel {

    private int houseSide;
    private int rows;
    private int cols;
    private Map<Integer, CheckerHouse> houses;
    private Color blackHouseColor;
    private Color whiteHouseColor;
    private Color playerColor;
    private Color oponentColor;
    private Color selectionColor;
    private Point p;
    private CBController cb;
    private CheckerHouse selectedHouse;
    private JPopupMenu jPopupMenu;
    private JMenuBar menuBar;

    public CheckerBoard(int rows, int cols, int rowsPieces) {
        this.rows = rows;
        this.cols = cols;

        houseSide = 70;
        houses = new HashMap<>();

        cb = new CBController();

        blackHouseColor = Color.BLACK;
        playerColor = Color.RED.darker();
        whiteHouseColor = Color.WHITE;
        oponentColor = Color.LIGHT_GRAY;

        setLayout(null);
        rebuild(rows, cols, rowsPieces);
        addMouseListener(new MouseAdapter() {
            private Color background;

            @Override
            public void mouseClicked(MouseEvent e) {
                Point pIni = p;
                p = e.getPoint();

                if (!(p.x >= houseSide * cols || p.y >= houseSide * rows)) {
                    if (pIni != null && p != null) {
                        movePiece(pIni, p);
                        pIni = null;
                    } else {
                        houseSelected(p);
                    }

                }

                repaint();
            }

        });
    }

    /**
     * Função que seta as possíveis jogadas da casa selecionada
     *
     * @param pos Posição da casa selecionada
     */
    public void possiblePlays(Point pos) {
        CheckerHouse ch;
        ch = getHouseAt(pos.y / houseSide, pos.x / houseSide);
        if (ch.getContentType() == CheckerHouse.CONTENT_TYPE_KING) {
            possibleKingPlays(pos);
            return;
        }
        List<Move> moves = this.cb.possiblesPlays(pos, houseSide);
        ArrayList<Color> colors = getColors();

        for (int i = 0; i < moves.size(); i++) {
            for (Point p : moves.get(i).getPath()) {
                ch = getHouseAt(p.x, p.y);
                ch.setSelectionMode(CheckerHouse.SELECTION_MODE_MOVE);
                if (!ch.getPathColor().contains(colors.get(i))) {
                    ch.getPathColor().add(colors.get(i));
                }
                ch.getBoard().add(moves.get(i).getBoard());
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
        colors.add(Color.MAGENTA);
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
        CheckerHouse chB = getHouseAt(b.y / houseSide, b.x / houseSide);
        CheckerHouse chA = getHouseAt(a.y / houseSide, a.x / houseSide);

        if (chA.getContentType() == CheckerHouse.CONTENT_TYPE_EMPTY || chA == chB) {
            houseSelected(b);
            return;
        }

        if (chB.getSelectionMode() == CheckerHouse.SELECTION_MODE_MOVE) {
            Color c = chA.getFgColor();

            chA.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            chA.setFgColor(c);
            List<int[][]> moves = chB.getBoard();
            if (moves.size() == 1 || chB.getPathColor().size() == 1) {
                int[][] gameBoard = moves.get(0);
                this.cb.movePiece(gameBoard);
                setSelectionModeNone();
                repaintBoard(gameBoard);
            } else {
                jPopupMenu = new JPopupMenu();

                int i = 0;

                for (Color color : chB.getPathColor()) {
                    createPopupMenuItem(moves.get(i), color, i, chB.getPaths().get(i));
                    i++;

                }
                jPopupMenu.show(this, p.x, p.y);
                jPopupMenu.setInvoker(null);

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
     * @param moves Matriz de inteiros com as posições das peças relativas ao
     * caminho escolhido
     * @param color Cor do caminho
     * @param i Numero do caminho
     */
    public void createPopupMenuItem(int[][] moves, Color color, int i, Point p) {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setName("Caminho_" + i);
        jMenuItem.setText("Caminho " + i + " (" + p.x + "," + p.y + ")");
        jMenuItem.setBorder(BorderFactory.createLineBorder(color, 3));
        jPopupMenu.add(jMenuItem);
        jMenuItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int[][] gameBoard;
                cb.movePiece(moves);
                setSelectionModeNone();
                repaintBoard(moves);
                jPopupMenu.setVisible(false);
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
     * @param gameBoard - Matriz de inteiros que contém o tabuleiro com a nova
     * disposição das peças
     */
    public void repaintBoard(int[][] gameBoard) {

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

        repaint();
    }

    /**
     * Seta o mode de seleção da peça nas posições x, y do tabuleiro
     *
     * @param p Point com a posição do mouse
     */
    public void houseSelected(Point p) {
        int row = p.y / houseSide;
        int col = p.x / houseSide;

        CheckerHouse che = getHouseAt(row, col);

        if (che.getSelectionMode() == CheckerHouse.SELECTION_MODE_SELECTED) {
            setSelectionModeNone();
            p = null;
        } else {
            setSelectionModeNone();
            che.setSelectionMode(CheckerHouse.SELECTION_MODE_SELECTED);
            possiblePlays(p);

        }
    }

    /**
     * Seta como NONE o modo de seleção de todas as casas do tabuleiro
     */
    public void setSelectionModeNone() {
        for (CheckerHouse ch : this.houses.values()) {
            ch.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            ch.getBoard().clear();
            ch.getPathColor().clear();
        }
    }

    /**
     * Função que cria as peças e desenha o tabuleiro
     *
     * @param rows Numero de linhas do tabuleiro
     * @param cols Numero de colunas do tabuleiro
     * @param rowsPieces Numero de linhas que serão preenchiadas com peças
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

                house.setBounds(c * houseSide, (rows - r - 1) * houseSide, houseSide, houseSide);

                houses.put((rows - r - 1) * cols + c, house);
                // System.out.println((rows - r - 1) * cols + c);
                add(house);
            }
        }

    }

    public int getHouseSide() {
        return houseSide;
    }

    public void setHouseSide(int houseSide) {
        this.houseSide = houseSide;
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

    private void possibleKingPlays(Point pos) {
        int last;
        CheckerHouse ch = null;
        List<Move> moves = this.cb.possiblesPlays(pos, houseSide);
        ArrayList<Color> colors = getColors();

        for (int i = 0; i < moves.size(); i++) {
            last = moves.get(i).getPath().size() - 1;
            if (moves.get(i).getPiecesTaken() == 0) {
                Point p = moves.get(i).getPath().get(moves.get(i).getPath().size() - 1);
                ch = getHouseAt(p.x, p.y);
                ch.setSelectionMode(CheckerHouse.SELECTION_MODE_MOVE);
                if (!ch.getPathColor().contains(colors.get(0))) {
                    ch.getPathColor().add(colors.get(0));
                    ch.getPaths().add(moves.get(i).getPath().get(last));
                }
                ch.getBoard().add(moves.get(i).getBoard());
            } else {
                for (Point p : moves.get(i).getPath()) {
                    ch = getHouseAt(p.x, p.y);
                    ch.setSelectionMode(CheckerHouse.SELECTION_MODE_MOVE);
                    if (!ch.getPathColor().contains(colors.get(i))) {
                        ch.getPathColor().add(colors.get(i));
                        ch.getPaths().add(moves.get(i).getPath().get(last));
                        ch.getBoard().add(moves.get(i).getBoard());
                    }

                }
            }

        }
    }

}
