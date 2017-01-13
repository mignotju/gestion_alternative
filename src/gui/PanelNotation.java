/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import souverain.GroupeNot;

/**
 *
 * @author kamowskn
 */
public class PanelNotation extends JPanel {

    List<GroupeNot> liste_notations;

    public PanelNotation(List<GroupeNot> set) {
        liste_notations = set;
    }

    @Override
    public void paintComponent(Graphics g) {
        int x = 2, y = 15;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2); //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits(2);
        df.setDecimalSeparatorAlwaysShown(true);
        for (GroupeNot not : liste_notations) {
            //Mise en place des labels
            g.setFont(new Font("default", Font.BOLD, 14));
            g.drawRect(x - 2, y - 15, 400, 225);
            for (int i = 0; i < 9; i++) {
                g.drawRect(x - 2, y - 15 + i * 25, 100, 25);
            }
            for (int i = 0; i < 3; i++) {
                g.drawRect(x - 2 + 100 + i * 100, y - 15, 100, 25);
            }
            g.drawString(not.getNom(), x, y);
            g.drawString("1 an", x + 100, y);
            g.drawString("5 ans", x + 200, y);
            g.drawString("10 ans", x + 300, y);
            g.drawString("Mean", x, y + 25);
            g.drawString("Median", x, y + 50);
            g.drawString("Minimum", x, y + 75);
            g.drawString("Maximum", x, y + 100);
            g.drawString("Volatility", x, y + 125);
            g.drawString("Skewness", x, y + 150);
            g.drawString("Kurtosis", x, y + 175);
            g.drawString("AC1", x, y + 200);
            g.setFont(new Font("default", Font.ROMAN_BASELINE, 14));
            //On remplit les informations
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    int xPrim = x + 100 + j * 100;
                    g.drawString(df.format(not.getMean().get(j + 1)), xPrim, y + 25);
                    g.drawString(df.format(not.getMedian().get(j + 1)), xPrim, y + 50);
                    g.drawString(df.format(not.getMin().get(j + 1)), xPrim, y + 75);
                    g.drawString(df.format(not.getMax().get(j + 1)), xPrim, y + 100);
                    g.drawString(df.format(not.getVolatility().get(j + 1)), xPrim, y + 125);
                    g.drawString(df.format(not.getSkewness().get(j + 1)), xPrim, y + 150);
                    g.drawString(df.format(not.getKurtosis().get(j + 1)), xPrim, y + 175);
                    g.drawString(df.format(not.getAC1().get(j + 1)), xPrim, y + 200);
                }
            }
            //Notation suivante
            x += 400;
            if (x > 402) {
                x = 2;
                y += 225;
            }
        }
    }

    public void exportToXslx() {
        DecimalFormat df = new DecimalFormat();
        Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
        df.setMaximumFractionDigits(2); //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits(2);
        df.setDecimalSeparatorAlwaysShown(true);
        int lignes = 0;
        for (GroupeNot not : liste_notations) {
            data.put(lignes++, new Object[]{not.getNom(), "1 ans", "5 ans", "10 ans"});
            data.put(lignes++, new Object[]{"Mean", not.getMean().get1y(), not.getMean().get5y(), not.getMean().get10y()});
            data.put(lignes++, new Object[]{"Median", not.getMedian().get1y(), not.getMedian().get5y(), not.getMedian().get10y()});
            data.put(lignes++, new Object[]{"Maximum", not.getMax().get1y(), not.getMax().get5y(), not.getMax().get10y()});
            data.put(lignes++, new Object[]{"Minimum", not.getMin().get1y(), not.getMin().get5y(), not.getMin().get10y()});
            data.put(lignes++, new Object[]{"Volatility", not.getVolatility().get1y(), not.getVolatility().get5y(), not.getVolatility().get10y()});
            data.put(lignes++, new Object[]{"Skewness", not.getSkewness().get1y(), not.getSkewness().get5y(), not.getSkewness().get10y()});
            data.put(lignes++, new Object[]{"Kurtosis", not.getKurtosis().get1y(), not.getKurtosis().get5y(), not.getKurtosis().get10y()});
            data.put(lignes++, new Object[]{"AC1", not.getAC1().get1y(), not.getAC1().get5y(), not.getAC1().get10y()});
            data.put(lignes++, new Object[]{});
        }
        //Il faut recuper le bon fichier xlsx
        try {
            File selectedFile = new File(".").getCanonicalFile();
            JFileChooser dialogue = new JFileChooser(selectedFile);
            dialogue.setApproveButtonText("Save");
            dialogue.setFileFilter(new FileNameExtensionFilter("Ficher Excel", "xls", "xlsx"));
            dialogue.showOpenDialog(null);
            dialogue.setApproveButtonText("Export");
            if (dialogue.getSelectedFile() == null) { //Cas "Annuler"
                return;
            }
            if (!dialogue.getSelectedFile().getCanonicalPath().endsWith(".xlsx")) {
                dialogue.setSelectedFile(new File(dialogue.getSelectedFile().getAbsolutePath().concat(".xlsx")));
            }
            FileOutputStream fos = new FileOutputStream(dialogue.getSelectedFile());
            XSSFWorkbook myWorkBook = new XSSFWorkbook();

            XSSFSheet mySheet = myWorkBook.createSheet("Rating");

            Set< Integer> newRows = data.keySet();
            int rownum = mySheet.getLastRowNum();

            for (int key : newRows) {

                Row row = mySheet.createRow(rownum++);
                Object[] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);
                    if (obj instanceof String) {
                        cell.setCellValue((String) obj);
                    } else if (obj instanceof Boolean) {
                        cell.setCellValue((Boolean) obj);
                    } else if (obj instanceof Date) {
                        cell.setCellValue((Date) obj);
                    } else if (obj instanceof Double) {
                        cell.setCellValue((Double) obj);
                    }
                }
            }

            // open an OutputStream to save written data into XLSX file
            myWorkBook.write(fos);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
