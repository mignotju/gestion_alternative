/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import javax.swing.*;

/**
 *
 * @author kamowskn
 */
public class Table extends JFrame {

    public Table(String name, int x, int y) {
        //Dimension de l'écran
        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) dimension.getHeight();
        int width = (int) dimension.getWidth();
        if (x < width) {//Pas besoin de barre de scrolling
            width = x;
        }
        if (y < height) {//Pas besoin de barre de scrolling
            height = y;
        }
        setSize(width, height);
        setTitle(name);
        setVisible(true);
    }

}
