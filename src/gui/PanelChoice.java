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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import xlsx_manager.*;

/**
 *
 * @author kamowskn
 */
public class PanelChoice extends JPanel {

    String objectif;
    Set<JCheckBox> predicteurs = new HashSet<JCheckBox>();

    public PanelChoice(Set<String> liste_elem, File f, Table t) {
        JCheckBox cb;
        JRadioButton rb;
        ButtonGroup groupe = new ButtonGroup();
        Iterator<String> it = liste_elem.iterator();
        int y = 22;
        while (it.hasNext()) {
            setLayout(null);
            String colonne = it.next();
            cb = new JCheckBox(colonne);//Permet de savoir quel éléments utiliser pour la regression linéaire
            cb.setName(colonne);
            cb.setEnabled(true);
            cb.addActionListener(null);
            cb.setBounds(40, y, 150, 20);
            cb.setMnemonic(KeyEvent.VK_C);
            add(cb);
            predicteurs.add(cb);
            rb = new JRadioButton(colonne);
            rb.setBounds(200, y, 150, 20);
            rb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    objectif = colonne;
                }
            });
            groupe.add(rb);
            add(rb);
            y += 20;
        }

        JButton btOk = new JButton("Ok");
        btOk.setBounds(50, y + 10, 30, 20);
        btOk.setBorder(null);
        btOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performedRegLin(f);
                t.dispose();
            }
        });
        add(btOk);
    }

    public void paintComponent(Graphics g) {
        g.drawString("Liste des prédicteurs :", 2, 20);
        g.drawString("Objectif :", 152, 20);
    }

    public void performedRegLin(File f) {
        Set<String> colSelected = new HashSet<>();
        for (JCheckBox cb : predicteurs) {//Attention ne pas mettre l'objectif dans les prédicteurs!!
            if (cb.isSelected()) {
                colSelected.add(cb.getName());
            }
        }
        if (colSelected.contains(objectif)) {
            colSelected.remove(objectif);
        }
        ParserRegLin parseur = new ParserRegLin(f, colSelected, objectif);
        Table tab = new Table("Resultat de la régression lin.", 600, 400);
        PanelString pnl = new PanelString(parseur.getResult());
        MainPanel.addScrollBar(tab, pnl,500,400);
    }

}
