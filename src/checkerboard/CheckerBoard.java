package checkerboard;

import java.awt.Color;
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
        if (houses.containsKey((rows - row - 1) * cols + col)) {
            return houses.get((rows - row - 1) * cols + col);
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
