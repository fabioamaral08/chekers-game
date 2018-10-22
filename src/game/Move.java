/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Point;

/**
 *
 * @author gustavo
 */
public class Move {

    // Objeto Point que guarda a coordenada da peça a ser movimentada
    private Point piece;

    // Guarda a quantidade de peças tomadas no melhor movimento
    private int numPieces;

    // Matriz que exboça a(s) melhores jogadas
    private boolean[][] move;

    // Matriz que exboça o estado atual do tabuleiro
    private int[][] curBoard;

    /**
     * Calcula o(s) movimento(s) possível de acordo com a peça
     *
     * @param board estado do tabuleiro atual
     * @param piece peça a se movimentar
     * @param queen informa se a peça é uma dama ou não
     */
    public boolean[][] move(int[][] board, Point piece, boolean queen) {
        this.curBoard = board;
        this.piece = piece;
        this.numPieces = 0;
        this.move = new boolean[8][8];
        boolean[][] possibleMove = new boolean[8][8];

        return null;
    }

    private void initializeMove(boolean[][] pmove) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pmove[i][j] = false;
            }
        }
    }

    private boolean isEnemy(int row, int col) {
        return (this.curBoard[row][col] == -1);
    }

    
    private void nextHouse(int row, int col, int lastRow, int lastCol) {
        
    }
    
    /**
     * Calcula o possível caminho de uma peça a partir de uma possibilidade
     *
     * @param possibleMove matriz que receberá o possível movimento a ser feito
     */
    private int peasentMove(boolean[][] possibleMove, int row, int col) {
        int piecesTaken = 0;
        int i = row;
        boolean canTake = true;
        int lastCol = this.piece.y;

        while (i < 8) {
            int j = col;
            while (j < 8) {
                if (this.isEnemy(i, j)) {
                    if (canTake) {
                        canTake = false;
                        
                        if (j < lastCol) {
                            j--;
                        } else {
                            j++;
                        }
                    } else {
                        return piecesTaken;
                    }
                } else {
                    possibleMove[i][j] = true;
                    if (!canTake) {
                        canTake = true;
                    }
                }
                
                if (j + 1 < row)
                j++;
            }
        }

        return piecesTaken;
    }

}
