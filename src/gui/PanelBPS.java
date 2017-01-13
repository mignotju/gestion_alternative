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
import souverain.*;

/**
 *
 * @author kamowskn
 */
public class PanelBPS extends JPanel {

    List<GroupeBps> liste_notations;

    public PanelBPS(List<GroupeBps> set) {
        liste_notations = set;
    }

    @Override
    public void paintComponent(Graphics g) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2); //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits(2);
        df.setDecimalSeparatorAlwaysShown(true);
        int x = 2, y = 15;
        //Mise en place des labels
        g.setFont(new Font("default", Font.BOLD, 14));
        g.drawRect(x - 2, y - 15, 900, 750);
        for (int i = 0; i < 30; i++) {
            g.drawRect(x - 2, y - 15 + i * 25, 100, 25);
        }
        for (int i = 0; i < 9; i++) {
            g.drawRect(x - 2 + i * 100, y - 15, 100, 25);
        }
        g.drawString("Groups", x, y);
        g.drawString("1 an", x + 100, y);
        g.drawString("5 ans", x + 200, y);
        g.drawString("10 ans", x + 300, y);
        g.drawString("5-1 ans", x + 400, y);
        g.drawString("10-5 ans", x + 500, y);
        g.drawString("10-1 ans", x + 600, y);
        g.drawString("#pays", x + 700, y);
        g.drawString("#données", x + 800, y);
        g.setFont(new Font("default", Font.ROMAN_BASELINE, 14));
        //Affichage des données
        y = 40;
        int i = 0;
        for (GroupeBps bps : liste_notations) {
            g.setFont(new Font("default", Font.BOLD, 14));
            g.drawString(bps.getName(), x, y + i * 25);
            g.setFont(new Font("default", Font.ROMAN_BASELINE, 14));
            g.drawString(df.format(bps.getAverageCDS().get1y()), x + 100, y + i * 25);
            g.drawString(df.format(bps.getAverageCDS().get5y()), x + 200, y + i * 25);
            g.drawString(df.format(bps.getAverageCDS().get10y()), x + 300, y + i * 25);
            g.drawString(df.format(bps.getAverageSlope().get1y()), x + 400, y + i * 25);
            g.drawString(df.format(bps.getAverageSlope().get5y()), x + 500, y + i * 25);
            g.drawString(df.format(bps.getAverageSlope().get10y()), x + 600, y + i * 25);
            //_1Y = 1_5; _5Y = 10_5; _10Y = 10_1;
            g.drawString(Integer.toString(bps.getEntities()), x + 700, y + i * 25);
            g.drawString(Integer.toString(bps.getCurves()), x + 800, y + i * 25);
            i++;
        }
    }

    public void exportToXslx() {
        DecimalFormat df = new DecimalFormat();
        Map<Integer, Object[]> data = new HashMap<Integer, Object[]>();
        df.setMaximumFractionDigits(2); //arrondi à 2 chiffres apres la virgules
        df.setMinimumFractionDigits(2);
        df.setDecimalSeparatorAlwaysShown(true);
        data.put(0, new Object[]{"Groups", "1 an", "5 ans", "10 ans", "5-1 ans", "10-5 ans", "10-1 ans", "#pays", "#données"});
        int lignes = 1;
        for (GroupeBps bps : liste_notations) {
            data.put(lignes, new Object[]{bps.getName(), df.format(bps.getAverageCDS().get1y()),
                df.format(bps.getAverageCDS().get5y()), df.format(bps.getAverageCDS().get10y()),
                df.format(bps.getAverageSlope().get1y()), df.format(bps.getAverageSlope().get5y()),
                df.format(bps.getAverageSlope().get10y()), Integer.toString(bps.getEntities()),
                Integer.toString(bps.getCurves())});
            lignes++;
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
            XSSFSheet mySheet = myWorkBook.createSheet("BPS");

            Set < Integer > newRows = data.keySet();
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
