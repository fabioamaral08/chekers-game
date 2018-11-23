package checkerboard;

import game.Move;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

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
                        houseSelected(p.x, p.y);
                    }

                }

                repaint();
            }

        });
    }

    /* Vamos ver o que vamos fazer né :) */
    public void possiblePlays(Point pos) {
        List<Move> moves = this.cb.possiblesPlays(pos, houseSide);
        ArrayList<Color> colors = getColors();
        CheckerHouse ch;

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

    public void movePiece(Point a, Point b) {
        CheckerHouse chB = getHouseAt(b.y / houseSide, b.x / houseSide);
        CheckerHouse chA = getHouseAt(a.y / houseSide, a.x / houseSide);

        if (chA.getContentType() == CheckerHouse.CONTENT_TYPE_EMPTY || chA == chB) {
            houseSelected(b.x, b.y);
            return;
        }

        if (chB.getSelectionMode() == CheckerHouse.SELECTION_MODE_MOVE) {
            Color c = chA.getFgColor();

            chA.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            chA.setFgColor(c);
            List<int[][]> moves = chB.getBoard();
            if (moves.size() == 1) {
                int[][] gameBoard = moves.get(0);
                this.cb.movePiece(gameBoard);
                setSelectionModeNone();
                repaintBoard(gameBoard);
            } else {
                JPopupMenu jPopupMenu = new JPopupMenu();
                JMenuItem jMenuItem;
                int i = 1;
                
                for (Color color: chB.getPathColor()) {
                    jMenuItem = new JMenuItem();
                    jMenuItem.setText("Caminho " + i);
                    jMenuItem.setBorder(BorderFactory.createLineBorder(color, 3));
                    i++;
                    jPopupMenu.add(jMenuItem);
                        
                }
                jPopupMenu.setLocation(p.x + (houseSide * 4), p.y + 10);
                jPopupMenu.setVisible(true);


            }

        } else {
            JOptionPane.showMessageDialog(null, "Esse movimento não é permitido!");
            setSelectionModeNone();
        }
        p = null;

    }

    /*Função para redesenhar o tabuleiro*/
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

    public void houseSelected(int x, int y) {
        int row = y / houseSide;
        int col = x / houseSide;

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

    public void setSelectionModeNone() {
        for (CheckerHouse ch : this.houses.values()) {
            ch.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            ch.getBoard().clear();
            ch.getPathColor().clear();
        }
    }

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
}
