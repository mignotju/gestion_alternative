/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlsx_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author mignotju
 */
public class ParserRegLinSimple {

    private Set<String> colonnes;
    private String title = "";
    private String resultat = "";
    private String objectif;
    private Date[] data_dates;
    private String[] data_pays;
    private int nbrNot;

    private static Set<String> liste_pays = new HashSet<>();
    private static Set<Date> listDate = new HashSet<>();
    private static Map<Date, Map<String, Param_et_Sensi>> datePays = new TreeMap<Date, Map<String, Param_et_Sensi>>();

    public String getResult() {
        return resultat;
    }

    public ParserRegLinSimple(File f, Set<String> colonnes, String cible) {
        this.colonnes = colonnes;
        objectif = cible;
        double[][] data = parseData(f);
        doRegLin(data);
        String s = objectif;
        for (String st : colonnes) {
            s += "_" + st;
        }
        exportSensibilite(f, s);
    }

    public double[][] parseData(File myFile) {
        double[][] data = null;
        try {
            FileInputStream fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = mySheet.iterator();
            Row row = rowIterator.next();
            nbrNot = mySheet.getLastRowNum();
            data_dates = new Date[nbrNot];
            data_pays = new String[nbrNot];
            data = new double[mySheet.getLastRowNum()][colonnes.size() + 1];
            int[] used = parseEnTete(row);
            int[] obj = parseObj(row);
            int ligne = 0;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                data[ligne] = parseLine(ligne, row, used, obj);
                ligne++;
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public double[] parseLine(int ligne, Row myLine, int[] colSelected, int[] colTargeted) {
        Iterator<Cell> cellIterator = myLine.cellIterator();
        double[] predicteurs = new double[colonnes.size() + 1];//+1 pour l'objectif
        int colData = 1;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getColumnIndex() >= colSelected.length) {
                break;
            }
            if (colSelected[cell.getColumnIndex()] == 1) {
                predicteurs[colData] = cell.getNumericCellValue();
                colData++;
            } else if (colSelected[cell.getColumnIndex()] == 2) {
                data_dates[ligne] = cell.getDateCellValue();
            } else if (colSelected[cell.getColumnIndex()] == 3) {
                data_pays[ligne] = cell.getStringCellValue();
            } else if (colTargeted[cell.getColumnIndex()] == 1) {
                predicteurs[0] = cell.getNumericCellValue();
            }
        }
        return predicteurs;
    }

    public int[] parseEnTete(Row enTete) {
        int size = enTete.getLastCellNum();
        int[] res = new int[size];
        Iterator<Cell> cellIterator = enTete.cellIterator();
        for (int i = 0; i < size; i++) {
            Cell cell = cellIterator.next();
            if (colonnes.contains(cell.getStringCellValue())) {
                res[i] = 1;//cette colonne contient des données pour la régréssion
            } else if (cell.getStringCellValue().equals("date") || cell.getStringCellValue().equals("Date")) {
                res[i] = 2;
            } else if (cell.getStringCellValue().equals("noms_pays")
                    || cell.getStringCellValue().equals("Noms_Pays")) {
                res[i] = 3;
            } else {
                res[i] = 0;
            }
        }
        return res;
    }

    public int[] parseObj(Row enTete) {
        int size = enTete.getLastCellNum();
        int[] res = new int[size];
        Iterator<Cell> cellIterator = enTete.cellIterator();
        for (int i = 0; i < size; i++) {
            Cell cell = cellIterator.next();
            if (objectif.equals(cell.getStringCellValue())) {
                res[i] = 1;//cette colonne contient des données pour la régréssion
            } else {
                res[i] = 0;
            }
        }
        return res;
    }

    //data doit être sour forme [nbDonnées][nbPredic+1]
    public void doRegLin(double[][] data) {
        boolean firstTimeThisDate;
        for (int i = 0; i < data.length; i++) {
            Param_et_Sensi d = new Param_et_Sensi(data[i][0], data[i][1]);
            liste_pays.add(data_pays[i]);
            firstTimeThisDate = listDate.add(data_dates[i]);
            if (firstTimeThisDate) {
                TreeMap<String, Param_et_Sensi> myMap = new TreeMap<String, Param_et_Sensi>();
                datePays.put(data_dates[i], myMap);
            }

            datePays.get(data_dates[i]).put(data_pays[i], d);
        }

        int l = 0;
        //intervalle de temps : 60 avant t
        double matrix[][] = new double[60][2];
        int i = 59;
        for (String p : liste_pays) {
            i = 59;
            Set<Date> dates = datePays.keySet();
            Iterator<Date> itd = dates.iterator();
            for (int j = 0; j < 59; j++) {
                Date d = itd.next();
                matrix[j][1] = datePays.get(d).get(p).getParam1();
                matrix[j][0] = datePays.get(d).get(p).getParam2();
            }
            while (itd.hasNext()) {
                Date d = itd.next();
                matrix[i][1] = datePays.get(d).get(p).getParam1();
                matrix[i][0] = datePays.get(d).get(p).getParam2();
                SimpleRegression reg = new SimpleRegression();
                reg.addData(matrix);
                datePays.get(d).get(p).setSensi60(reg.getSlope());
                resultat += reg.getSlope() + "\n";
                l++;
                i = (i + 1) % 60;
            }
        }

        // intervalle de temps 30 jours avant 30 jours apres 
        double matrix30[][] = new double[60][2];
        int k = 59;
        for (String p : liste_pays) {
            k = 59;
            Set<Date> dates = datePays.keySet();
            Iterator<Date> itd = dates.iterator();
            Iterator<Date> itd2 = dates.iterator();
            for (int j = 0; j < 59; j++) {
                if (j < 29) {
                    itd2.next();
                }
                Date d = itd.next();
                matrix30[j][1] = datePays.get(d).get(p).getParam1();
                matrix30[j][0] = datePays.get(d).get(p).getParam2();
            }
            while (itd.hasNext()) {
                Date d = itd.next();
                Date d2 = itd2.next();
                matrix30[k][1] = datePays.get(d).get(p).getParam1();
                matrix30[k][0] = datePays.get(d).get(p).getParam2();
                SimpleRegression reg2 = new SimpleRegression();
                reg2.addData(matrix30);
                datePays.get(d2).get(p).setSensi30(reg2.getSlope());
                resultat += reg2.getSlope() + "\n";
                l++;
                k = (k + 1) % 60;
            }
        }
    }

    public static double[][] transpose(double[][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }

    public static void exportSensibilite(File myFile, String nomColone) {
        try {

            //Fichier destination pour les données
            final Workbook workbook = WorkbookFactory.create(myFile);
            final Sheet sheet = workbook.getSheetAt(0);
            //1ère ligne, le titre, OK
            final Row titreRow = sheet.getRow(0);
            //je rajoute 3 cases dans cette ligne
            int start = titreRow.getLastCellNum();
            Cell Cell = titreRow.createCell(start);
            Cell.setCellValue("Sensi60" + nomColone);
            Cell Cell2 = titreRow.createCell(start + 1);
            Cell2.setCellValue("Sensi30" + nomColone);

            //je repars de la ligne 1
            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            style.setDataFormat(workbook.createDataFormat().getFormat("#.##"));
            int index = 1;
            Row row = sheet.getRow(index);
            while (row != null) {
                double toAdd = datePays.get(row.getCell(1).getDateCellValue()).get(row.getCell(2).getStringCellValue()).getSensi60();
                if (toAdd != -1) {
                    Cell = row.createCell(start);
                    Cell.setCellValue(toAdd);
                }
                toAdd = datePays.get(row.getCell(1).getDateCellValue()).get(row.getCell(2).getStringCellValue()).getSensi30();
                if (toAdd != -1) {
                    Cell2 = row.createCell(start + 1);
                    Cell2.setCellValue(toAdd);
                }
                row = sheet.getRow(index++);
            }
            //Fichier destination physique
            File selectedFile = new File(".").getCanonicalFile();
            JFileChooser dialogue = new JFileChooser(selectedFile);
            dialogue.setDialogTitle("Select Destination File");
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
            workbook.write(fos);
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
