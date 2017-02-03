package xlsx_manager;

import java.io.File;
import Model.*;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Parser {
	public static Set<Integer> stockList = new HashSet<>();
	public static Map<Integer, Stock> stockNbrStock = new TreeMap<Integer,Stock>();
	
	
	private static Set<String> nom_colonnes = new HashSet<>();

    public static Date premiereDate;
    private static boolean firstDate = true;
	
    public static Stock getStock(int stockNbr) {
    	return stockNbrStock.get(stockNbr);
    }
	
	public static String parse(File myFile) {
        String res = "";
        System.out.println("Début du parsing");
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
            e.printStackTrace();
        }
        return res;
    }

    // Fonction permettant de parser une feuille (XSSFSheet) enti�rement
    public static String parseSheet(XSSFSheet feuille) {
    	System.out.println("début du parsing de la feuille");
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
    	//Ne parse qu'une seule ligne, les variables ne sont donc pas gardeées.
    	int stockNumber;
    	int year = 0;
    	int month = 0;
    	double beta = 0;
    	double returnRf;
    	Stock myStock = null;
    	double stockPrice = 0;

    	Iterator<Cell> cellIterator = myLine.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getColumnIndex() > 5)
            	break;
            switch (cell.getColumnIndex()) {
                case 0:
                    stockNumber = (int) cell.getNumericCellValue();
                    if (stockList.contains(stockNumber)) {
                    	//Pas besoin de créer l'element, j'ajouterais juste un entier à la fin, dans CE stock
                    	myStock = getStock(stockNumber);
                    } else {
                    	//Je crée le stock pour la première fois.
                    	myStock = new Stock(stockNumber);
                    	stockList.add(stockNumber);
                    	stockNbrStock.put(stockNumber,myStock);
                    }
                    break;
                case 2:
                    year = (int) cell.getNumericCellValue();

                    break;
                case 3:
                    month = (int) cell.getNumericCellValue();
                    break;
                case 4:
                    stockPrice = cell.getNumericCellValue();
                    break;
                case 5:
                    beta = cell.getNumericCellValue();
                    break;
                    /*case 8:
                    Moodys = cell.getStringCellValue();

                    break;*/
                default:
            }
        }
//        if git a(pays.equals("Austria")) {
//            System.out.println(Fitch +" " + SP + " " + Moodys);
//        }
        /*Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String annee = Integer.toString(calendar.get(Calendar.YEAR));
        String mois = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        int jour = calendar.get(Calendar.DAY_OF_MONTH);
        if (calendar.get(Calendar.MONTH) + 1 < 10) {
            mois = "0" + mois;
        }*/
        //String anneemois = "01/" + month + "/" + year;
        DateYM d = new DateYM(year,month);
        myStock.dateValeur.put(d, stockPrice);
        myStock.dateBeta.put(d, beta);
//        calendar.add(Calendar.DATE, -(jour-1));
//        long date2 = calendar.getTimeInMillis();
//        Date date3 = new Date(date2);
//        String anneemois = date3.toString();
        return "";
    }
    
	/*public static void main(String [ ] args) {
		System.out.println("Hello");
		File file = new File("/user/0/.base/maubertj/home/gestion_alternative/data.xlsx");
		parse(file);
		System.out.println(getStock(7).toString());
	}*/
}
