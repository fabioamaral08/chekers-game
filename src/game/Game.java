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
public class Game {
    private Move bestMove;
    private int[][] board;
    
    
    /**
     * Construtor da classe
     */
    public Game(Point piece){
        this.bestMove = new Move(piece);
        this.board = new int[8][8];
    }
    
    
}
