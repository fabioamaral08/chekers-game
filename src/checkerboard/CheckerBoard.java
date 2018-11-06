package checkerboard;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

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

    public CheckerBoard(int rows, int cols, int rowsPieces) {
        this.rows = rows;
        this.cols = cols;

        houseSide = 60;
        houses = new HashMap<>();

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
                    houseSelected(p.x, p.y);
                }

                if (pIni != null && p != null) {
                    movePiece(pIni.y, pIni.x, p.y, p.x);
                    pIni = null;
                }

                repaint();
            }

        });
    }

    /* Vamos ver o que vamos fazer né :) */
//    public void possiblePlays(ArrayList<Jogadas> jogadas) {
//        ArrayList<Color> colors = new ArrayList();
//
//        colors.add(Color.GREEN);
//        colors.add(Color.BLUE);
//        colors.add(Color.MAGENTA);
//        colors.add(Color.ORANGE);
//        colors.add(Color.CYAN);
//        colors.add(Color.YELLOW);
//        colors.add(Color.RED);
//        
//        for (Jogadas jogada : jogadas) {
//            
//        }
//    }
    public void movePiece(int rowP, int colP, int rowN, int colN) {
        CheckerHouse ch = getHouseAt(rowP / houseSide, colP / houseSide);
        Color c = ch.getFgColor();

//        if (ch.getContentType() == CheckerHouse.CONTENT_TYPE_MEN || ch.getContentType() == CheckerHouse.CONTENT_TYPE_KING) {
//            int type = ch.getContentType();
//            ch.setContentType(CheckerHouse.CONTENT_TYPE_EMPTY);
//            ch = getHouseAt(rowN / houseSide, colN / houseSide);
//            if ((rowN / houseSide) == 0 || (rowN / houseSide) == 7) {
//                ch.setContentType(CheckerHouse.CONTENT_TYPE_KING);
//            } else {
//                ch.setContentType(type);
//            }
            p = null;
            ch.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            ch.setFgColor(c);

//        }
    }

    /*Função para redesenhar o tabuleiro*/
    public void repaintBoard(int[][] gameBoard) {
//        int[][] teste = {{0, -1, 0, -1, 0, -1, 0, 2}, 
//            {-1, 0, -1, 0, -1, 0, -1, 0}, 
//            {0, -1, 0, -1, 0, 1, 0, -1},
//            {0, 0, 0, 0, 0, 0, 0, 0},
//        {0, 0, 0, 0, 0, 0, 0, 0},
//        {1, 0,1,0,1,0,1,0},
//        {0,1,0,1,0,1,0,1},{1, 0,1,0,-1,0,1,0}};
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
            che.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            p = null;
        } else {
            for (CheckerHouse ch : this.houses.values()) {
                ch.setSelectionMode(CheckerHouse.SELECTION_MODE_NONE);
            }
            che.setSelectionMode(CheckerHouse.SELECTION_MODE_SELECTED);

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
