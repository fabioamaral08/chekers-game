/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkerboard;

import java.awt.Point;

/**
 *
 * @author Gi
 */
public class CBController {

    public CBController() {
    }
    
    public int[][] movePiece (Point a, Point b, int houseSide){
        int[][] gameBoard = null;
        int rowA = a.x / houseSide;
        int colA = a.y / houseSide;
        int rowB = b.x / houseSide;
        int colB = b.y / houseSide;
        
        return gameBoard;        
        
    }
    
}
