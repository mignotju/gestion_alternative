package xlsx_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.*; //Pour les row et les Cells
import org.apache.poi.xssf.usermodel.XSSFSheet; // Pour les feuilles
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Pour le fichier excel

import Model.Notation;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class Parser {

    private static int nbrNot = 0;
    private static Map<Double, Notation> data;
    public static Date premiereDate;
    private static boolean firstDate = true;
    private static List<String> liste_pays = new LinkedList<>();
    private static Set<String> nom_colonnes = new HashSet<>();
    private static Set<Date> listDate = new HashSet<>();
    private static Set<String> listMois = new HashSet<>();
    private static Map<Date, Map<String, Notation>> datePays = new TreeMap<Date, Map<String, Notation>>();
    private static Map<String, Map<String, List<Double>>> moisanneePays = new TreeMap<String, Map<String, List<Double>>>();
    private static Map<Date, Double> moyCDS = new TreeMap<Date, Double>();
    private static Date firstDayThisMonth;

    public static Map<Double, Notation> getData() {
        return data;
    }

    public static Set<String> getNomColonnes() {
        return nom_colonnes;
    }

    public static List<String> getListePays() {
        return liste_pays;
    }

    

    

   

    public static String parse(File myFile) {
        String res = "";
        data = new TreeMap<Double, Notation>();
        try {
            FileInputStream fis = new FileInputStream(myFile);

            // Repr�sente le fichier excel entier (avec toutes les feuilles
            // qu'il peut contenir
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

            // on r�cup�re la feuille 1 du fichier
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // On parse la premi�re feuille
            res = parseSheet(mySheet);
            fis.close();
        } catch (Exception e) {
            System.out.println("erreur dans le parsing du fichier \n");
            e.printStackTrace();
        }
        return res;
    }

    // Fonction permettant de parser une feuille (XSSFSheet) enti�rement
    public static String parseSheet(XSSFSheet feuille) {
        // Donne l'it�rateur des LIGNES
        String res = "";
        Iterator<Row> rowIterator = feuille.iterator();
        Row row = rowIterator.next();
        parseEnTete(row);
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            res += parseLine(row);
        }
        return res;
    }

    public static void parseEnTete(Row enTete) {
        nom_colonnes = new HashSet<>();
        Iterator<Cell> cellIterator = enTete.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            nom_colonnes.add(cell.getStringCellValue());
        }
    }

    public static String parseLine(Row myLine) {
        boolean firstTimeThisDate;
        int numeroPays = 0;
        double CDS_1Y = -1;
        double CDS_5Y = -1;
        double CDS_10Y = -1;
        String Fitch = "", SP = "", Moodys = "";
        String pays = "";
        Date date = null;
        Iterator<Cell> cellIterator = myLine.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            switch (cell.getColumnIndex()) {
                case 0:
                    numeroPays = (int) cell.getNumericCellValue();
                    break;
                case 1:
                    date = cell.getDateCellValue();
                    if (firstDate) {
                        firstDate = false;
                        premiereDate = date;
                    }
                    break;
                case 2:
                    pays = cell.getStringCellValue();

                    break;
                case 3:
                    CDS_5Y = cell.getNumericCellValue();

                    break;
                case 4:
                    CDS_1Y = cell.getNumericCellValue();

                    break;
                case 5:
                    CDS_10Y = cell.getNumericCellValue();

                    break;
                case 6:
                    Fitch = cell.getStringCellValue();

                    break;
                case 7:
                    SP = cell.getStringCellValue();

                    break;
                case 8:
                    Moodys = cell.getStringCellValue();

                    break;
                default:
            }
        }
//        if git a(pays.equals("Austria")) {
//            System.out.println(Fitch +" " + SP + " " + Moodys);
//        }
        Notation myNotation = new Notation(CDS_1Y, CDS_5Y, CDS_10Y, Fitch, SP, Moodys, pays, date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String annee = Integer.toString(calendar.get(Calendar.YEAR));
        String mois = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        int jour = calendar.get(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.MONTH) + 1 < 10) {
            mois = "0" + mois;
        }
        String anneemois = "01/" + mois + "/" + annee;
//        calendar.add(Calendar.DATE, -(jour-1));
//        long date2 = calendar.getTimeInMillis();
//        Date date3 = new Date(date2);
//        String anneemois = date3.toString();
        if (!liste_pays.contains(pays)) {
            liste_pays.add(pays);
        }
        data.put(CDS_5Y, myNotation);
        nbrNot++;
        boolean firstTimeThisMonth = Mois.listeMois.add(anneemois);
        firstTimeThisDate = listDate.add(date);
        if (firstTimeThisDate) {
            TreeMap<String, Notation> myMap = new TreeMap<String, Notation>();
            datePays.put(date, myMap);
        }
        if (firstTimeThisMonth) {
            firstDayThisMonth = date;
            Mois ceMois = new Mois(anneemois);
            Mois.dateMois.put(anneemois, ceMois);
        }
        if (firstDayThisMonth.equals(date)) {
            Pays cePays = new Pays(numeroPays, pays, anneemois);
            Mois.dateMois.get(anneemois).addPays(cePays, pays);

        }
        Mois.dateMois.get(anneemois).getPays(pays).addDataNot(myNotation);
        datePays.get(date).put(pays, myNotation);
        String res = myNotation.assignGroupeNot(movingFromAAA);
        myNotation.assignGroupeBps();
        return res;
    }
}
