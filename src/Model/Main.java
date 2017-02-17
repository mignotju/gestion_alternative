package Model;

import xlsx_manager.*;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.*;

public class Main {
	
	public static <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue( Map<K, V> map )
{
    List<Map.Entry<K, V>> list =
        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
    {
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
        {
            return (o1.getValue()).compareTo( o2.getValue() );
        }
    } );

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
    }
    return result;
}
	
	public static void main(String [ ] args) {
		double richesse = 200;
		double coutTransaction = richesse * 0.1/100;

		Map<Integer, Double> stockRenta = new TreeMap<Integer,Double>();
		Map<Integer, Double> sortedStockRenta;
		System.out.println("Hello");
		File file = new File("data.xlsx");
		Parser.parse(file);
		
		Portefeuille P10 = new Portefeuille();
		Portefeuille P1 = new Portefeuille();
		double renta10;
		double renta1;
		int pourri=0;
		for (int j = 1974; j <= 1997; j++) {
			DateYM date = new DateYM(j,1);
			for (int i : Parser.stockList) {
				//System.out.println(i + " " + date.toString() + " " + Parser.stockNbrStock.get(i).rentaLast6Month(date));
				stockRenta.put(i, Parser.stockNbrStock.get(i).rentaLast6Month(date));
			}
	        sortedStockRenta = sortByValue(stockRenta);
	        int compteur = 0;
        	//System.out.println("debut ");
	        P1.oldShares = P1.shares;
	        P10.oldShares = P10.shares;
	        for (int s : sortedStockRenta.keySet()) {
	        	if (compteur < 10) {
	        		P1.set(compteur, Parser.stockNbrStock.get(s));
	        	}
	        	if (compteur >= 90) {
	        		P10.set(99-compteur, Parser.stockNbrStock.get(s));
	        	} 
	        	compteur++;
	        }
	        double coutCetteAnnee=0.2;
	        if (j>1974) {
	        	coutCetteAnnee = P1.computeTransaction(date) + P10.computeTransaction(date);
	        	coutTransaction += P1.computeTransaction(date);
	        	coutTransaction += P10.computeTransaction(date);
	        }
	        System.out.print(coutCetteAnnee+",");
	        renta10 = P10.renta(j);
	        P10.oldValue = P10.newValue;
	        P10.newValue = P10.newValue * (1+renta10);
	        //System.out.println("p10 old value puis new value  -  " + P10.oldValue + "  -  " + P10.newValue);
	        renta1 = P1.renta(j);
	        P1.oldValue = P1.newValue;
	        P1.newValue = P1.newValue * (1+renta1);

	        //System.out.println("p1 old value puis new value  -  " + P1.oldValue + "  -  " + P1.newValue);
	        //System.out.println("En " + j + " :");
	        //System.out.println("Le portefeuille P10 a une renta de : " + renta10);
	        //System.out.println(P10.toString());
	        //System.out.println("Le portefeuille P1 a une renta de : " + renta1);
	        //System.out.println(P1.toString());
	        if(renta10<renta1){
	        	
	        	pourri++;
	        }
	        
		}
		System.out.println("POURRI:"+pourri);
		//System.out.println(Marche.toStringStatic());
		
		
	}
}