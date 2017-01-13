/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author kamowskn
 */
public class PanelString extends JPanel {

    private String str;

    public PanelString(String msg) {
        str = msg;
    }

    @Override
    public void paintComponent(Graphics g) {
        String[] res = str.split("\n");
        int y = 20;
        for (String chaine : res) {
            if (chaine.startsWith("\\")) {
                g.setFont(new Font("default", Font.BOLD, 14));
                chaine=chaine.substring(1);
            }
            g.drawString(chaine, 2, y);
            g.setFont(new Font("default", Font.ROMAN_BASELINE, 14));
            y += 20;
        }
    }

}
