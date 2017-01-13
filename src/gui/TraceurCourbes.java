/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import souverain.Notation;
import xlsx_manager.Parser;
import org.jfree.chart.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author mignotju
 */
public class TraceurCourbes extends JFrame {

    public static void plotCourbesSlope() {
        JFrame frame = new JFrame();
        frame.setTitle("Average Slope");
        frame.setSize(800, 500);
        XYSeries courbe1 = new XYSeries("5y-1y");
        XYSeries courbe2 = new XYSeries("10y-5y");
        XYSeries courbe3 = new XYSeries("10y-1y");
        for (int i = 0; i < 1600; i++) {
            Iterator<Double> it = Parser.getData().keySet().iterator();
            Notation n = Parser.getData().get(it.next());
            double slope5_1 = 0;
            double slope10_5 = 0;
            double slope10_1 = 0;
            int size = 0;
            while (n.getCDS_5Y() < i) {
                //On saute les premiers éléments trop petits
                if (!it.hasNext()) {
                    break;
                }
                n = Parser.getData().get(it.next());
            }
            while (n.getCDS_5Y() < i + 400 && n.getCDS_5Y() >= i) {
                size++;
                slope5_1 += n.getCDS_5Y() - n.getCDS_1Y();
                slope10_5 += n.getCDS_10Y() - n.getCDS_5Y();
                slope10_1 += n.getCDS_10Y() - n.getCDS_1Y();
                if (!it.hasNext()) {
                    break;
                }
                n = Parser.getData().get(it.next());
            }
            if (size != 0) {
                courbe1.add(i, slope5_1 / (double) size);
                courbe2.add(i, slope10_5 / (double) size);
                courbe3.add(i, slope10_1 / (double) size);
            }

        }
        XYSeriesCollection xyDataset = new XYSeriesCollection(courbe1);

        xyDataset.addSeries(courbe2);

        xyDataset.addSeries(courbe3);
        JFreeChart Graph = ChartFactory.createXYLineChart("Average Slope as function of CDS spread", "CDS Spread", "Average Slope", (XYDataset) xyDataset);
        ChartPanel pnl = new ChartPanel(Graph);
        //Bouton de sauvegarde
        JPanel controlPanel = new JPanel();

        controlPanel.setBackground(Color.WHITE);
        ButtonSave btSave = new ButtonSave(Graph);

        btSave.setBounds(
                1, 1, 50, 50);
        controlPanel.add(btSave);

        frame.add(pnl, BorderLayout.CENTER);

        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.pack();

        frame.setVisible(
                true);

    }

    public static void plotCourbesFractions() {
        JFrame frame = new JFrame();
        frame.setTitle("Fractions of decreasing CDS");
        frame.setSize(800, 500);
        XYSeries courbe1 = new XYSeries("5y-1y");
        XYSeries courbe2 = new XYSeries("10y-5y");
        XYSeries courbe3 = new XYSeries("10y-1y");
        //pour un intervalle de 400bps, on sors le nb de slopes négatives et on divise par le nb de data
        for (int i = 0; i < 1600; i++) {
            Iterator<Double> it = Parser.getData().keySet().iterator();
            Notation n = Parser.getData().get(it.next());
            int nbNeg5_1 = 0;
            int nbNeg10_5 = 0;
            int nbNeg10_1 = 0;
            int size = 0;
            while (n.getCDS_5Y() < i) {
                //On saute les premiers éléments trop petits
                if (!it.hasNext()) {
                    break;
                }
                n = Parser.getData().get(it.next());
            }
            while (n.getCDS_5Y() < i + 400 && n.getCDS_5Y() >= i) {
                size++;
                if (n.getCDS_5Y() - n.getCDS_1Y() < 0) {
                    nbNeg5_1++;
                }
                if (n.getCDS_10Y() - n.getCDS_5Y() < 0) {
                    nbNeg10_5++;
                }
                if (n.getCDS_10Y() - n.getCDS_1Y() < 0) {
                    nbNeg10_1++;
                }
                if (!it.hasNext()) {
                    break;
                }
                n = Parser.getData().get(it.next());
            }
            if (size != 0) {
                courbe1.add(i, (double) nbNeg5_1 / (double) size);
                courbe2.add(i, (double) nbNeg10_5 / (double) size);
                courbe3.add(i, (double) nbNeg10_1 / (double) size);
            }

        }
        XYSeriesCollection xyDataset = new XYSeriesCollection(courbe1);
        xyDataset.addSeries(courbe2);
        xyDataset.addSeries(courbe3);
        JFreeChart Graph = ChartFactory.createXYLineChart("Fraction of decreasing CDS as function of CDS spread", "CDS Spread", "%", (XYDataset) xyDataset);
        ChartPanel pnl = new ChartPanel(Graph);
        //Bouton de sauvegarde
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        ButtonSave btSave = new ButtonSave(Graph);
        btSave.setBounds(1, 1, 50, 50);
        controlPanel.add(btSave);
        frame.add(pnl, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

    }

}
