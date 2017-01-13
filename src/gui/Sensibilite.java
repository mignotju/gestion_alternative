/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import xlsx_manager.*;

/**
 *
 * @author mignotju
 */
public class Sensibilite extends JPanel {

    String objectif;
    String predicteur;

    public Sensibilite(Set<String> liste_elem, File f, Table t) {
        JRadioButton rb_o, rb_p;
        ButtonGroup groupe = new ButtonGroup();
        ButtonGroup groupe_o = new ButtonGroup();
        Iterator<String> it = liste_elem.iterator();
        int y = 22;
        int i = 0;
        while (it.hasNext()) {
            setLayout(null);
            String colonne = it.next();
            rb_o = new JRadioButton(colonne);//Permet de savoir quel éléments utiliser pour la regression linéaire
            rb_o.setBounds(40, y, 100, 20);
            rb_o.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    predicteur = colonne;
                }
            });
            groupe_o.add(rb_o);
            add(rb_o);
            rb_p = new JRadioButton(colonne);
            rb_p.setBounds(160, y, 100, 20);
            rb_p.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    objectif = colonne;
                }
            });
            groupe.add(rb_p);
            add(rb_p);
            y += 20;
            i++;
        }

        JButton btOk = new JButton("Ok");
        btOk.setBounds(50, y + 10, 30, 20);
        btOk.setBorder(null);
        btOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performedRegLin(f);
                t.dispose();
                JDialog window = new JDialog();
                JPanel pnl = new JPanel();
                window.setSize(180, 65);
                window.setTitle("Sensibility");
                window.setLocationRelativeTo(null);
                JButton btOk = new JButton("Opération effectuée");
                btOk.setBounds(30, 10, 100, 50);
                btOk.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        window.dispose();
                    }
                });
                pnl.add(btOk);
                window.setContentPane(pnl);
                window.setVisible(true);
            }
        });
        add(btOk);
    }

    public void paintComponent(Graphics g) {
        g.drawString("Prédicteur :", 2, 20);
        g.drawString("Objectif :", 152, 20);
    }

    public void performedRegLin(File f) {
        Set<String> colSelected = new HashSet<>();
        colSelected.add(predicteur);
        ParserRegLinSimple parseur = new ParserRegLinSimple(f, colSelected, objectif);
    }

}
