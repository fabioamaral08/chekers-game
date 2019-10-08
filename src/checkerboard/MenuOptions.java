/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkerboard;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Gi
 */
public class MenuOptions  extends JPanel{
    private JButton host;
    private JButton conect;
    private JButton alone;
    
    public MenuOptions(){
        setSize(new Dimension(400, 100));
        createButtons();
       
    }
    
     private void createButtons() {
        this.host = new JButton();
        this.host.setText("Ser Host");
        this.add(this.host);
        
        this.conect = new JButton();
        this.conect.setText("Conectar");
        this.add(this.conect);
        
        this.alone = new JButton();
        this.alone.setText("Jogar");
        this.add(this.alone);

    }

    public JButton getHost() {
        return host;
    }

    public JButton getConect() {
        return conect;
    }

    public JButton getAlone() {
        return alone;
    }
     
     
    
}
