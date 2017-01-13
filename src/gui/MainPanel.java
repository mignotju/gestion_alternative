/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import souverain.*;
import xlsx_manager.Parser;
import static xlsx_manager.Parser.regLin_Sensibilite;

/**
 *
 * @author kamowskn
 */
public class MainPanel extends JPanel {

    ButtonSelectFile btFile;
    Boolean parsed = false;

    public MainPanel() {
        // Bouton de selection de fichier excel
        btFile = new ButtonSelectFile(this);
        btFile.setBounds(2, 12, 150, 20);
        btFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parsed = false;
            }
        });
        add(btFile);
        JButton notation = new JButton("Resume by Rating");
        // Bouton pour gerer l'appartition du tableau récapitulatif par Notation
        notation.setBounds(2, 40, 150, 20);
        notation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotSumRating();
            }
        });
        add(notation);
        // Bouton pour gerer l'appartition du tableau récapitulatif par tranches de spread 
        JButton table9 = new JButton("Resume by CDS");
        table9.setBounds(2, 60, 150, 20);
        table9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotSumCDS();
            }
        });
        add(table9);
        // Bouton pour afficher les courbes de fractions
        JButton figure1a = new JButton("Plot Fraction");
        figure1a.setBounds(2, 80, 150, 20);
        figure1a.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotFraction();
            }
        });
        add(figure1a);
        // Bouton pour afficher les courbes de slope de spread
        JButton figure1b = new JButton("Plot Slope");
        figure1b.setBounds(2, 100, 150, 20);
        figure1b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotAverageSlope();
            }
        });
        add(figure1b);
        // Bouton pour sortie zone AAA
        JButton sortieAAA = new JButton("In/Out AAA");
        sortieAAA.setBounds(2, 120, 150, 20);
        sortieAAA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotOutAAA();
            }
        });
        add(sortieAAA);
        // Bouton pour regression linéaire
        JButton btRL = new JButton("Regression lin.");
        btRL.setBounds(162, 40, 150, 20);
        btRL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotRegLin();
            }
        });
        add(btRL);
        // Bouton pour récuperer des données en plus dans notre fichier
        JButton btExp = new JButton("Add Data");
        btExp.setBounds(162, 60, 150, 20);
        btExp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addData();
            }
        });
        add(btExp);
        // Bouton pour regression linéaire simple qui donne la sensibilite d'une colonne par rapport à une autre
        JButton btSensi = new JButton("Sensibility");
        btSensi.setBounds(162, 100, 150, 20);
        btSensi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotSensi();
            }
        });
        add(btSensi);
        // Bouton pour test de granger en prenenant en compte un écart de 41 lignes entre chaque éléments
        JButton btGranger = new JButton("Granger Country");
        btGranger.setBounds(322, 40, 150, 20);
        btGranger.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotGranger(0);
            }
        });
        add(btGranger);
        // Bouton pour test de granger classique
        JButton btGrCl = new JButton("Granger Classic");
        btGrCl.setBounds(322, 60, 150, 20);
        btGrCl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotGranger(1);
            }
        });
        add(btGrCl);
        // Bouton pour add les data du SSI
        JButton btSSI = new JButton("Add ExtraFi Data");
        btSSI.setBounds(162, 80, 150, 20);
        btSSI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSSI();
            }
        });
        add(btSSI);
        // Bouton pour creer un fichier où les donnée sont sur le mois et pas sur le jour
        JButton btMonth = new JButton("Sum Monthly");
        btMonth.setBounds(162, 120, 150, 20);
        btMonth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sumByMonth();
            }
        });
        add(btMonth);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        if (btFile.getFile() != null) {
            g.drawString(": " + btFile.getFile().getName(), 160, 27);
        }

    }

    public void plotSumRating() {
        Table tab = new Table("Summary by Rating", 822, 1187);

        List<GroupeNot> list = new LinkedList<GroupeNot>();
        list.add(GroupeNot.AAA);
        list.add(GroupeNot.AA);
        list.add(GroupeNot.A);
        list.add(GroupeNot.BBB);
        list.add(GroupeNot.BB);
        list.add(GroupeNot.B);
        list.add(GroupeNot.CCC);
        list.add(GroupeNot.CC);
        list.add(GroupeNot.C);
        list.add(GroupeNot.D);
        ensureParsed();
        //On calcule ce qu'il faut
        GroupeNot.calculAllTable8();
        //On affiche
        PanelNotation pnl = new PanelNotation(list);
        pnl.repaint();
        pnl.setLayout(null);
        addScrollBar(tab, pnl, 822, 1187);
        //Bouton d'exportation
        JButton btExp = new JButton("Export to XSLX");
        btExp.setBorder(null);
        btExp.setBounds(1, 1125, 100, 20);
        btExp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pnl.exportToXslx();
            }
        });
        pnl.add(btExp);

    }

    public void plotSumCDS() {
        Table tab = new Table("Summary gy CDS Spread", 900, 803);
        List<GroupeBps> list = new LinkedList<GroupeBps>();
        list.add(GroupeBps._1_100);
        list.add(GroupeBps._100_200);
        list.add(GroupeBps._200_300);
        list.add(GroupeBps._300_400);
        list.add(GroupeBps._400_500);
        list.add(GroupeBps._500_600);
        list.add(GroupeBps._600_700);
        list.add(GroupeBps._700_800);
        list.add(GroupeBps._800_900);
        list.add(GroupeBps._900_1000);
        list.add(GroupeBps._1000_1100);
        list.add(GroupeBps._1100_1200);
        list.add(GroupeBps._1200_1300);
        list.add(GroupeBps._1300_1400);
        list.add(GroupeBps._1400_1500);
        list.add(GroupeBps._1500_1700);
        list.add(GroupeBps._1700_1900);
        list.add(GroupeBps._1900_2100);
        list.add(GroupeBps._2100_2300);
        list.add(GroupeBps._2300_2500);
        list.add(GroupeBps._2500_2700);
        list.add(GroupeBps._2700_2900);
        list.add(GroupeBps._2900_3100);
        list.add(GroupeBps._3100_3300);
        list.add(GroupeBps._3300_3500);
        list.add(GroupeBps._3500_3700);
        list.add(GroupeBps._3900_4100);
        list.add(GroupeBps._3700_3900);
        list.add(GroupeBps._4100_Plus);
        ensureParsed();
        //On calcule ce qu'il faut
        GroupeBps.calculAllTable9();
        //On affiche
        PanelBPS pnl = new PanelBPS(list);
        pnl.repaint();
        pnl.setLayout(null);
        addScrollBar(tab, pnl, 900, 803);
        //Bouton d'exportation
        JButton btExp = new JButton("Export to XSLX");
        btExp.setBorder(null);
        btExp.setBounds(1, 750, 100, 20);
        btExp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pnl.exportToXslx();
            }
        });
        pnl.add(btExp);
    }

    public void plotAverageSlope() {
        ensureParsed();
        TraceurCourbes.plotCourbesSlope();
    }

    public void plotFraction() {
        ensureParsed();
        TraceurCourbes.plotCourbesFractions();
    }

    public void plotOutAAA() {
        Table tab = new Table("Entrée/Sortie AAA", 550, 300);
        String res = Parser.parse(btFile.getFile(), true);
        parsed = true;
        PanelString pnl = new PanelString(res);
        addScrollBar(tab, pnl, 550, 300);

    }

    public void plotRegLin() {
        //ensureParsed();
        if (parsed == false) {
            try {
                FileInputStream fis = new FileInputStream(btFile.getFile());
                XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
                XSSFSheet mySheet = myWorkBook.getSheetAt(0);
                Row row = mySheet.getRow(mySheet.getFirstRowNum());
                Parser.parseEnTete(row);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //parsed = true;
        }
        Table tab = new Table("Selection des critères de régression", 380, Parser.getNomColonnes().size() * 20 + 100);
        PanelChoice pnl = new PanelChoice(Parser.getNomColonnes(), btFile.getFile(), tab);
        addScrollBar(tab, pnl, 380, Parser.getNomColonnes().size() * 20 + 100);
    }

    public void addData() {
        ensureParsed();
        Parser.remplirMoyCDS();
        Parser.regLin();
        Parser.regLin_Sensibilite(true);
        Parser.regLin_Sensibilite(false);
        Parser.exportToXLSX(btFile.getFile());
        acknowlegdementScreen("add Data");
    }

    public void regLinCDSNotPhi() {
        ensureParsed();
        Table tab = new Table("RL CDS par rapport à la note financière moyenne", 500, 400);
        PanelString pnl = new PanelString(Parser.regLin());
        tab.setContentPane(pnl);
        addScrollBar(tab, pnl, 500, 400);
    }

    public void regLinCDSMult() {
        ensureParsed();
        Parser.remplirMoyCDS();
        Parser.regLin_Sensibilite(true);
        Parser.regLin_Sensibilite(false);
        Table tab = new Table("RL CDS par rapport à la note financière moyenne", 650, 550);
        PanelString pnl = new PanelString(Parser.regLinMult());
        tab.setContentPane(pnl);
        addScrollBar(tab, pnl, 650, 550);
    }

    public void plotSensi() {
        if (parsed == false) {
            try {
                FileInputStream fis = new FileInputStream(btFile.getFile());
                XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
                XSSFSheet mySheet = myWorkBook.getSheetAt(0);
                Row row = mySheet.getRow(mySheet.getFirstRowNum());
                Parser.parseEnTete(row);
            } catch (Exception e) {
                e.printStackTrace();
            }
            parsed = true;
        }
        Table tab = new Table("Selection des critères", 380, Parser.getNomColonnes().size() * 20 + 100);
        Sensibilite sensi = new Sensibilite(Parser.getNomColonnes(), btFile.getFile(), tab);
        addScrollBar(tab, sensi, 380, Parser.getNomColonnes().size() * 20 + 100);
    }

    public void plotGranger(int type) {
        if (type == 0) {
            ensureParsed();
        } else {
            if (parsed == false) {
                try {
                    FileInputStream fis = new FileInputStream(btFile.getFile());
                    XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
                    XSSFSheet mySheet = myWorkBook.getSheetAt(0);
                    Row row = mySheet.getRow(mySheet.getFirstRowNum());
                    Parser.parseEnTete(row);
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Table tab = new Table("Selection des critères de régression", 380, Parser.getNomColonnes().size() * 20 + 100);
        PanelGranger pnl = new PanelGranger(Parser.getNomColonnes(), btFile.getFile(), tab, type);
        addScrollBar(tab, pnl, 380, Parser.getNomColonnes().size() * 20 + 100);

    }

    public void addSSI() {
        Parser.addExtraFinancialData(btFile.getFile());
        acknowlegdementScreen("add ExtraFinancial Data");
    }

    public void sumByMonth() {
        ensureParsed();
        Parser.remplirMoyCDS();
        regLin_Sensibilite(true);
        regLin_Sensibilite(false);
        Parser.exportMonthlyToXLSX();
        acknowlegdementScreen("sort by Month");
    }

    public void ensureParsed() {
        if (parsed == false) {
            //On parse
            Parser.parse(btFile.getFile(), false);
            parsed = true;
        }
    }

    public static void addScrollBar(Table tab, JPanel pnl, int x, int y) {
        JScrollPane scrollPane = new JScrollPane(pnl);
        scrollPane.setViewportView(pnl);
        scrollPane.getViewport().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                pnl.repaint();
            }
        });
        pnl.setPreferredSize(new Dimension(Math.max(tab.getWidth() - 22, x - 22),
                Math.max(tab.getHeight() - 42, y - 42)));
        tab.setContentPane(scrollPane);
    }

    public void acknowlegdementScreen(String title) {
        JDialog window = new JDialog();
        JPanel pnl = new JPanel();
        window.setSize(180, 65);
        window.setTitle(title);
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

}
