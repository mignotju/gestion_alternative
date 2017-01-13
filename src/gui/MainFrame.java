/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author kamowskn
 */
public class MainFrame extends JFrame {

    public MainFrame(int x, int y) {
        setTitle("Fenetre principale");
        setSize(x, y);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainPanel fond = new MainPanel();
        fond.setLayout(null);
        setContentPane(fond);
        setVisible(true);
        
    }

}
