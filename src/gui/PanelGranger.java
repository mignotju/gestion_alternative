/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xlsx_manager.*;

/**
 *
 * @author mignotju
 */
public class PanelGranger extends JPanel {

    String objectif;
    String predicteur;

    public PanelGranger(Set<String> liste_elem, File f, Table t, int type) {
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
                performedGranger(f, type);
                t.dispose();
            }
        });

        add(btOk);
    }

    public void paintComponent(Graphics g) {
        g.drawString("Liste des prédicteurs :", 2, 20);
        g.drawString("Objectif :", 152, 20);
    }

    public void performedGranger(File f, int type) {
        if (type == 0) {
            Table tab = new Table("Test de Granger y: " + objectif + " x: " + predicteur, 500, 80 * (Parser.getListePays().size() + 1));
            double[] x = getCol(f, predicteur);
            double[] y = getCol(f, objectif);
            PanelString pnl = new PanelString("");
            pnl = new PanelString(Parser.testGranger(x, y));
            MainPanel.addScrollBar(tab, pnl, 500, 80 * (Parser.getListePays().size() + 1));
        } else if (type == 1) {
            Table tab = new Table("Test de Granger y: " + objectif + " x: " + predicteur, 500, 200);
            double[] x = getCol(f, predicteur);
            double[] y = getCol(f, objectif);
            PanelString pnl = new PanelString("");
            pnl = new PanelString(ParserRegLin.testGranger(x, y));
            MainPanel.addScrollBar(tab, pnl, 500, 200);
        }

    }

    public double[] getCol(File f, String name) {
        double[] data = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = mySheet.iterator();
            Row row = rowIterator.next();
            int selected = 0;
            Iterator<Cell> cellIt = row.cellIterator();
            while (cellIt.hasNext()) {
                Cell c = cellIt.next();
                if (!c.getStringCellValue().equals(name)) {
                    selected++;
                } else {
                    break;
                }
            }
            data = new double[mySheet.getLastRowNum()];
            int ligne = 0;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                data[ligne] = row.getCell(selected).getNumericCellValue();
                ligne++;
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
