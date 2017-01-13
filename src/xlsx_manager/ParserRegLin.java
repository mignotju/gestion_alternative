/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xlsx_manager;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.poi.ss.usermodel.*; //Pour les row et les Cells
import org.apache.poi.xssf.usermodel.XSSFSheet; // Pour les feuilles
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Pour le fichier excel

/**
 *
 * @author kamowskn
 */
public class ParserRegLin {

    private Set<String> colonnes;
    private String[] nom_col;
    private String title = "";
    private String resultat = "";
    private String objectif;

    public String getResult() {
        return resultat;
    }

    public ParserRegLin(File f, Set<String> colonnes, String cible) {
        this.colonnes = colonnes;
        nom_col = new String[colonnes.size() + 1];
        objectif = cible;
        double[][] data = parseData(f);
        doRegLin(data);
    }

    public double[][] parseData(File myFile) {
        double[][] data = null;
        try {
            FileInputStream fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = mySheet.iterator();
            Iterator<Row> rowIterator_under = mySheet.iterator();
            int i = 0;
            Row row_under = rowIterator_under.next();
            int nb_pays = Parser.getListePays().size();
            while (i < nb_pays) {
                i++;
                row_under = rowIterator_under.next();
            }
            Row row = rowIterator.next();
            data = new double[mySheet.getLastRowNum() - nb_pays][colonnes.size() + 1];
            int[] used = parseEnTete(row);
            int[] obj = parseObj(row);
            int ligne = 0;
            while (rowIterator_under.hasNext()) {
                row = rowIterator.next();
                row_under = rowIterator_under.next();
                data[ligne] = parseLine(row, row_under, used, obj);
                ligne++;
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public double[] parseLine(Row myLine, Row under, int[] colSelected, int[] colTargeted) {
        Iterator<Cell> cellIterator = myLine.cellIterator();
        Iterator<Cell> underIt = under.cellIterator();
        double[] predicteurs = new double[colonnes.size() + 1];//+1 pour l'objectif
        int colData = 1;
        while (cellIterator.hasNext() && underIt.hasNext()) {
            Cell cell = cellIterator.next();
            Cell under_cell = underIt.next();
            if (cell.getColumnIndex() >= colSelected.length || under_cell.getColumnIndex() >= colSelected.length) {
                break;
            }
            if (colSelected[cell.getColumnIndex()] == 1) {
                predicteurs[colData] = cell.getNumericCellValue();
                colData++;
            } else if (colTargeted[cell.getColumnIndex()] == 1) {
                predicteurs[0] = under_cell.getNumericCellValue();
            }
        }
        return predicteurs;
    }

    public int[] parseEnTete(Row enTete) {
        int j = 1;
        nom_col[0] = "Intercept";
        int size = enTete.getLastCellNum();
        int[] res = new int[size];
        Iterator<Cell> cellIterator = enTete.cellIterator();
        for (int i = 0; i < size; i++) {
            Cell cell = cellIterator.next();
            String nom = cell.getStringCellValue();
            if (colonnes.contains(nom)) {
                res[i] = 1;//cette colonne contient des données pour la régréssion
                nom_col[j] = nom;
                j++;
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

    //data doit être sour forme [nbDonnées][nbPredic]
    public String doRegLin(double[][] data) {
        double vector[] = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            vector[i] = data[i][0];
        }
        double matrix[][] = new double[data.length][data[0].length - 1];
        for (int i = 0; i < data[0].length - 1; i++) {
            for (int j = 0; j < data.length; j++) {
                matrix[j][i] = data[j][i + 1];
            }
        }
        OLSMultipleLinearRegression reg = new OLSMultipleLinearRegression();
        reg.newSampleData(vector, matrix);
        Iterator<String> it = colonnes.iterator();
        title = "\\Regression lineaire multiple sur " + objectif + " avec parametres: \n\\";
        while (it.hasNext()) {
            title += it.next() + " ";
        }
        resultat += title + "\n";
        int j = 0;
        for (double d : reg.estimateRegressionParameters()) {
            resultat += "Beta " + nom_col[j] + " : " + d + "\n";
            resultat += "p-value: " + Parser.getPValue(d, reg.estimateRegressionParametersStandardErrors()[j], data.length - data[0].length) + "\n";
            j++;
        }
        double Rcarre = reg.calculateRSquared();
        resultat += "Rcarre = " + Rcarre + "\n";
        return "";
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

    //Test de granger généralisé sur 2 colonnes
    public static String testGranger(double[] x, double[] y) {
        if (x.length != y.length) {
            return "Taille incompatibles";
        }
        double[] beta = new double[2];
        String s = "\\Etude de causalité par test de Granger\n";
        double[] vector = new double[y.length - 1];
        double[][] matrix = new double[y.length - 1][2];
        for (int j = 0; j < y.length - 1; j++) {
            matrix[j][0] = x[j];
            matrix[j][1] = y[j];
            vector[j] = y[j + 1];
        }
        OLSMultipleLinearRegression reg = new OLSMultipleLinearRegression();
        reg.newSampleData(vector, matrix);
        beta[0] = reg.estimateRegressionParameters()[1];//bx
        beta[1] = reg.estimateRegressionParameters()[2];// by
        s += "betaX : " + beta[0] + " betaY: " + beta[1] + "\n";
        return s;
    }

}
