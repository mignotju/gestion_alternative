package xlsx_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.*; //Pour les row et les Cells
import org.apache.poi.xssf.usermodel.XSSFSheet; // Pour les feuilles
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Pour le fichier excel
import souverain.Notation;
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

    public static void exportMonthlyToXLSX() {
        try {

            //Fichier source
            int index = 1;
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("ValuesByMonth");
            Row row = sheet.createRow((short) 0);
            FileInputStream fis = new FileInputStream(new File("..").getCanonicalPath() + "/Ranking-lists-2014-SSF.xlsx");
            XSSFWorkbook SSIWorkBook = new XSSFWorkbook(fis);
            XSSFSheet SSIsheet = SSIWorkBook.getSheetAt(0);
            Row SSIRow;
            row.createCell(0).setCellValue("Pays");
            row.createCell(1).setCellValue("Date");
            row.createCell(2).setCellValue("NomPays");
            row.createCell(3).setCellValue("CDS5Y");
            row.createCell(4).setCellValue("Sensi60");
            row.createCell(5).setCellValue("Sensi30-30");
            row.createCell(6).setCellValue("NotMoy");
            row.createCell(7).setCellValue("Human_WB");
            row.createCell(8).setCellValue("Env_WB");
            row.createCell(9).setCellValue("Eco_WB");
            for (String s : Mois.listeMois) {
                int annee = Integer.valueOf(s.split("/")[2]);
                int colonne = (annee - 2006) / 2 + 1;
                for (Pays p : Mois.dateMois.get(s).getLesPays()) {
                    Iterator<Row> itSSI = SSIsheet.iterator();
                    SSIRow = itSSI.next();
                    SSIRow = itSSI.next();
                    while (itSSI.hasNext()) {
                        SSIRow = itSSI.next();
                        if (SSIRow.getCell(0).getStringCellValue().equalsIgnoreCase(p.getNom())) {
                            break;
                        }
                    }

                    p.calcData();
                    row = sheet.createRow(index++);
                    row.createCell(0).setCellValue(p.getNum());
                    row.createCell(1).setCellValue(s);
                    row.createCell(2).setCellValue(p.getNom());
                    row.createCell(3).setCellValue(p.getCDS5YMois());
                    row.createCell(4).setCellValue(p.getSensi60Mois());
                    row.createCell(5).setCellValue(p.getSensi30Mois());
                    row.createCell(6).setCellValue(p.getNotMoyMois());
                    row.createCell(7).setCellValue(SSIRow.getCell(colonne).getNumericCellValue());
                    row.createCell(8).setCellValue(SSIRow.getCell(colonne + 6).getNumericCellValue());
                    row.createCell(9).setCellValue(SSIRow.getCell(colonne + 12).getNumericCellValue());
                }
            }
            //Fichier dest
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
            FileOutputStream fileOut = new FileOutputStream(dialogue.getSelectedFile());
            wb.write(fileOut);
            fileOut.close();
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    public static void exportToXLSX(File myFile) {
        try {

            //Fichier source
            final Workbook workbook = WorkbookFactory.create(myFile);
            final Sheet sheet = workbook.getSheetAt(0);
            //1ère ligne, le titre, OK
            final Row titreRow = sheet.getRow(0);
            //je rajoute 3 cases dans cette ligne
            int start = titreRow.getLastCellNum();
            Cell Cell3 = titreRow.createCell(start);
            Cell3.setCellValue("moyCDS");
            Cell Cell4 = titreRow.createCell(start + 1);
            Cell4.setCellValue("Not. Moy.");
            Cell Cell5 = titreRow.createCell(start + 2);
            Cell5.setCellValue("Groupe");
            Cell Cell6 = titreRow.createCell(start + 3);
            Cell6.setCellValue("ln(CDS5Y)");

            //je repars de la ligne 1
            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            style.setDataFormat(workbook.createDataFormat().getFormat("#.##"));
            int index = 1;
            Row row = sheet.getRow(index);
            while (row != null) {
                Cell3 = row.createCell(start);
                Cell3.setCellValue(moyCDS.get(row.getCell(1).getDateCellValue()));
                Cell4 = row.createCell(start + 1);
                Cell4.setCellValue(datePays.get(row.getCell(1).getDateCellValue()).get(row.getCell(2).getStringCellValue()).getNotMoyenne());
                Cell4.setCellStyle(style);
                Cell5 = row.createCell(start + 2);
                Cell5.setCellValue(datePays.get(row.getCell(1).getDateCellValue()).get(row.getCell(2).getStringCellValue()).getGroup());
                Cell6 = row.createCell(start + 3);
                Cell6.setCellValue(Math.log(datePays.get(row.getCell(1).getDateCellValue()).get(row.getCell(2).getStringCellValue()).getCDS_5Y()));
                row = sheet.getRow(index++);
            }
            //Fichier dest
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
            Logger.getLogger(Parser.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(Parser.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (EncryptedDocumentException ex) {
            Logger.getLogger(Parser.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addExtraFinancialData(File dst) {
        try {

            //Fichier destination pour les données
            Workbook workbook = WorkbookFactory.create(dst);
            Sheet sheet = workbook.getSheetAt(0);
            //Fichier source
            FileInputStream fis = new FileInputStream(new File("..").getCanonicalPath() + "/Ranking-lists-2014-SSF.xlsx");
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator<Row> rowIterator = mySheet.iterator();
            Row row = rowIterator.next();// on saute le titre
            double HW, EnW, EcW;
            long time1970_2012 = 1325372400000L;
            long time1970_2010 = 1262300400000L;
            long time1970_2013 = 1356994800000L;
            int newFirstCell;
            Cell newCell;
            Row rowDst = sheet.getRow(0);
            newFirstCell = rowDst.getLastCellNum();
            newCell = rowDst.createCell(newFirstCell++);
            newCell.setCellValue("Human_Wellbeing");
            newCell = rowDst.createCell(newFirstCell++);
            newCell.setCellValue("Environmental_Wellbeing");
            newCell = rowDst.createCell(newFirstCell++);
            newCell.setCellValue("Economic_Wellbeing");
            rowIterator.next();// et la date
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    rowDst = sheet.getRow(i);
                    if (rowDst.getCell(2).getStringCellValue().equalsIgnoreCase(row.getCell(0).getStringCellValue())) {
                        newFirstCell = rowDst.getLastCellNum();
                        if (rowDst.getCell(1).getDateCellValue().getTime() >= time1970_2012 && rowDst.getCell(1).getDateCellValue().getTime() < time1970_2013) {//2012
                            HW = row.getCell(4).getNumericCellValue();
                            EnW = row.getCell(10).getNumericCellValue();
                            EcW = row.getCell(16).getNumericCellValue();
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(HW);
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(EnW);
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(EcW);

                        } else if (rowDst.getCell(1).getDateCellValue().getTime() < time1970_2012 && rowDst.getCell(1).getDateCellValue().getTime() >= time1970_2010) {//2010
                            HW = row.getCell(3).getNumericCellValue();
                            EnW = row.getCell(9).getNumericCellValue();
                            EcW = row.getCell(15).getNumericCellValue();
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(HW);
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(EnW);
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(EcW);
                        } else {//2013
                            HW = row.getCell(5).getNumericCellValue();
                            EnW = row.getCell(11).getNumericCellValue();
                            EcW = row.getCell(17).getNumericCellValue();
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(HW);
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(EnW);
                            newCell = rowDst.createCell(newFirstCell++);
                            newCell.setCellValue(EcW);
                        }
                    }
                }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String parse(File myFile, boolean movingFromAAA) {
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
            res = parseSheet(mySheet, movingFromAAA);
            fis.close();
        } catch (Exception e) {
            System.out.println("erreur dans le parsing du fichier \n");
            e.printStackTrace();
        }
        return res;
    }

    // Fonction permettant de parser une feuille (XSSFSheet) enti�rement
    public static String parseSheet(XSSFSheet feuille, boolean movingFromAAA) {
        // Donne l'it�rateur des LIGNES
        String res = "";
        Iterator<Row> rowIterator = feuille.iterator();
        Row row = rowIterator.next();
        parseEnTete(row);
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            res += parseLine(row, movingFromAAA);
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

    public static String parseLine(Row myLine, boolean movingFromAAA) {
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

    public static void remplirMoyCDS() {
        double res = 0;
        int compteur = 0;
        double moy;
        Set<Date> dates = datePays.keySet();
        for (Date d : dates) {
            Set<String> paysS = datePays.get(d).keySet();
            for (String p : paysS) {
                double resInt = datePays.get(d).get(p).getCDS_5Y();
                if (resInt != -1) {
                    res += resInt;
                    compteur++;
                }
            }
            moy = res / compteur;
            moyCDS.put(d, moy);
            compteur = 0;
            res = 0;
        }
    }

    //Seulement avec les not financières
    public static String regLin() {
        int i = 0;
        double matrix[][] = new double[nbrNot][2];
        Set<Date> dates = datePays.keySet();
        for (String p : liste_pays) {
            Iterator<Date> itd = dates.iterator();
            while (itd.hasNext()) {
                Date d = itd.next();
                matrix[i][1] = datePays.get(d).get(p).getCDS_5Y();
                matrix[i][0] = datePays.get(d).get(p).getNotMoyenne();
                i++;
            }
        }

        SimpleRegression reg = new SimpleRegression();
        reg.addData(matrix);
        String s = "\\Regression lineaire simple de CDS5Y par rapport à NotePhiMoyenne : \n";
        s += "Intercept = " + reg.getIntercept()
                + "\nBeta = " + reg.getSlope()
                + "\nR**2 = " + reg.getRSquare()
                + "\nSlopeConfidenceInterval() 95% = " + reg.getSlopeConfidenceInterval();

        int j = 0;
        double matrix2[][] = new double[nbrNot][2];
        for (String p : liste_pays) {
            Iterator<Date> itd = dates.iterator();
            Iterator<Date> itd_ante = dates.iterator();
            Date d_ante = itd_ante.next();
            itd.next();
            Date d = itd.next();
            matrix2[j][1] = datePays.get(d).get(p).getCDS_5Y();
            matrix2[j][0] = datePays.get(d_ante).get(p).getNotMoyenne();
            j++;

            while (itd.hasNext()) {
                d = itd.next();
                d_ante = itd_ante.next();
                matrix2[j][1] = datePays.get(d).get(p).getCDS_5Y();
                matrix2[j][0] = datePays.get(d_ante).get(p).getNotMoyenne();
                j++;
            }
        }

        SimpleRegression reg2 = new SimpleRegression();
        reg2.addData(matrix2);
        s += "\n \n\\Regression lineaire simple de CDS5Y par rapport à NotePhiMoyenne à t-1 : \n";
        s += "Intercept = " + reg2.getIntercept()
                + "\nBeta = " + reg2.getSlope()
                + "\nR**2 = " + reg2.getRSquare()
                + "\nSignificance = " + reg2.getSignificance()
                + "\nSlopeConfidenceInterval() 95% = " + reg2.getSlopeConfidenceInterval();

        return s;
    }

    //sensi60 vrai si on veut une sensibilité avec des données prises 60 jours avant
    // faux pour 30 jours avant 30 jours apres
    public static double[] regLin_Sensibilite(boolean sensi60) {
        double tab[] = new double[nbrNot];
        int l = 0;
        //intervalle de temps : 60 avant t
        if (sensi60) {
            double matrix[][] = new double[60][2];
            int i = 59;
            for (String p : liste_pays) {
                i = 59;
                Set<Date> dates = datePays.keySet();
                Iterator<Date> itd = dates.iterator();
                for (int j = 0; j < 59; j++) {
                    Date d = itd.next();
                    matrix[j][1] = datePays.get(d).get(p).getCDS_5Y();
                    matrix[j][0] = moyCDS.get(d);
                }
                while (itd.hasNext()) {
                    Date d = itd.next();
                    matrix[i][1] = datePays.get(d).get(p).getCDS_5Y();
                    matrix[i][0] = moyCDS.get(d);

                    SimpleRegression reg = new SimpleRegression();
                    reg.addData(matrix);
                    tab[l] = reg.getSlope();
                    l++;
                    datePays.get(d).get(p).setSensiMarche60(reg.getSlope());
                    i = (i + 1) % 60;
                }
            }
        } else {

            // intervalle de temps 30 jours avant 30 jours apres 
            double matrix30[][] = new double[60][2];
            int k = 29;
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
                    matrix30[j][1] = datePays.get(d).get(p).getCDS_5Y();
                    matrix30[j][0] = moyCDS.get(d);
                }
                while (itd.hasNext()) {
                    Date d = itd.next();
                    Date d2 = itd2.next();
                    matrix30[k][1] = datePays.get(d).get(p).getCDS_5Y();
                    matrix30[k][0] = moyCDS.get(d);
                    SimpleRegression reg2 = new SimpleRegression();
                    reg2.addData(matrix30);
                    tab[l] = reg2.getSlope();
                    l++;
                    datePays.get(d2).get(p).setSensiMarche30(reg2.getSlope());
                    k = (k + 1) % 60;
                }
            }
        }
        return tab;
    }

    public static String regLinMult() {
        double vector[] = new double[nbrNot];
        double matrix[][] = new double[nbrNot][2];
        int i = 0;
        Set<Date> dates = datePays.keySet();
        for (Date d : dates) {
            Set<String> paysS = datePays.get(d).keySet();
            for (String p : paysS) {
                vector[i] = datePays.get(d).get(p).getCDS_5Y();
                matrix[i][0] = datePays.get(d).get(p).getNotMoyenne();
                matrix[i][1] = moyCDS.get(d);
                i++;
            }
        }
        String s = "";
        OLSMultipleLinearRegression reg = new OLSMultipleLinearRegression();
        reg.newSampleData(vector, matrix);
        s += "\\Regression lineaire multiple avec parametres NotePhiMoy et CDSMoy";
        int j = 0;
        for (double d : reg.estimateRegressionParameters()) {
            s += "\nBeta" + j + " : " + d;
            s += "\n p-value " + getPValue(d, reg.estimateRegressionParametersStandardErrors()[j], data.size() - matrix.length);
            j++;
        }
        double Rcarre = reg.calculateRSquared();
        s += "Rcarre = " + Rcarre + "\n";

        double vectorB[] = new double[nbrNot];
        double matrixB[][] = new double[nbrNot][2];
        i = 0;
        for (Date d : dates) {
            Set<String> paysS = datePays.get(d).keySet();
            for (String p : paysS) {
                vectorB[i] = datePays.get(d).get(p).getCDS_5Y();
                matrixB[i][0] = datePays.get(d).get(p).getNotMoyenne();
                matrixB[i][1] = datePays.get(d).get(p).getSensiMarche60();
                i++;
            }
        }
        OLSMultipleLinearRegression reg2 = new OLSMultipleLinearRegression();
        reg2.newSampleData(vectorB, matrixB);
        s += "\n\\Regression lineaire multiple avec parametres NotePhiMoy et Beta(i,t) 60 jours avant";
        j = 0;
        for (double d : reg2.estimateRegressionParameters()) {
            s += "\nBeta" + j + " : " + d;
            s += "\nIntervalle de confiance: " + getPValue(d, reg2.estimateRegressionParametersStandardErrors()[j], data.size() - matrixB.length);
            j++;
        }
        Rcarre = reg2.calculateRSquared();
        s += "Rcarre = " + Rcarre + "\n";

        double vectorB30[] = new double[nbrNot];
        double matrixB30[][] = new double[nbrNot][2];
        i = 0;
        for (Date d : dates) {
            Set<String> paysS = datePays.get(d).keySet();
            for (String p : paysS) {
                vectorB30[i] = datePays.get(d).get(p).getCDS_5Y();
                matrixB30[i][0] = datePays.get(d).get(p).getNotMoyenne();
                matrixB30[i][1] = datePays.get(d).get(p).getSensiMarche30();
                i++;
            }
        }
        return s;
    }

    public static String getPValue(double beta, double var, int freedom) {
        TDistribution t = new TDistribution(freedom);
        return Double.toString(2 * t.cumulativeProbability(-Math.abs(beta / var)));
    }

    //test de granger considérant les 41 pays différents
    public static String testGranger(double[] x, double[] y) {
        if (x.length != y.length) {
            return "Taille incompatibles";
        }
        //Il faut le faire pour chaque pays!
        String s = "\\Etude de causalité par test de Granger\n";
        int nbPays = liste_pays.size();
        double[][] beta = new double[nbPays][2];
        Iterator<String> itPays = liste_pays.iterator();
        for (int i = 0; i < nbPays; i++) { // On va faire les régréssions par ordre alphabétique
            int nbDate = (y.length / nbPays) - 1;
            double[][] matrix = new double[nbDate][2];
            double[] vector = new double[nbDate];
            for (int j = 0; j < nbDate; j++) {
                matrix[j][0] = x[j * nbPays + i];
                matrix[j][1] = y[j * nbPays + i];
                vector[j] = y[(j + 1) * nbPays + i];
            }
            OLSMultipleLinearRegression reg = new OLSMultipleLinearRegression();
            reg.newSampleData(vector, matrix);
            s += "\\ " + itPays.next() + "\n";
            try { // problème de matrice non inversible (valeur d'une colonne de data constante)
                beta[i][0] = reg.estimateRegressionParameters()[1];//bx
                beta[i][1] = reg.estimateRegressionParameters()[2];// by
                s += "betaX : " + beta[i][0] + " betaY: " + beta[i][1] + "\n";
                s += "p-value X : " + getPValue(beta[i][0], reg.estimateRegressionParametersStandardErrors()[1], nbDate - 3)
                        + "\np-value Y " + getPValue(beta[i][1], reg.estimateRegressionParametersStandardErrors()[2], nbDate - 3) + "\n";
            } catch (SingularMatrixException e) {
                beta[i][0] = 0;
                beta[i][1] = 0;
                s += "données constantes\n";
            }
        }
        return s;
    }
}
