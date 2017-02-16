package Model;

import xlsx_manager.*;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.*;

public class MainAlternatif {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void main(String[] args) {
		int pourri=0;
		Map<Integer, Double> stockBeta = new TreeMap<Integer, Double>();
		Map<Integer, Double> sortedStockBeta;
		Map<Integer, Double> stockRentaGoodB = new TreeMap<Integer, Double>();
		Map<Integer, Double> stockRentaBadB = new TreeMap<Integer, Double>();
		Map<Integer, Double> sortedStockRentaBadB;
		Map<Integer, Double> sortedStockRentaGoodB;
		System.out.println("HelloAlternatif");
		File file = new File("data.xlsx");
		Parser.parse(file);
		// pour chaque actions, je calcule la perf de l'année (sur les 6
		// derniers mois)
		// On forme p1 à p10

		// P = p10
		// en t=0, j'achete 100€ de p10.
		// en t=1, je vend p10, je fais un bénéfice ou perte. je rachete p10.
		// for (int j = 1974; j <= 1997; j++) {
		Portefeuille P10 = new Portefeuille();
		Portefeuille P1 = new Portefeuille();
		double renta10;
		double renta1;
		for (int j = 1974; j <= 1997; j++) {
			DateYM date = new DateYM(j, 1);
			for (int i : Parser.stockList) {
				// System.out.println(i + " " + date.toString() + " " +
				// Parser.stockNbrStock.get(i).rentaLast6Month(date));
				stockBeta.put(i, Parser.stockNbrStock.get(i).betaLast6Month(date));
			}
			sortedStockBeta = sortByValue(stockBeta);
			int compteur = 0;
			for (int s : sortedStockBeta.keySet()) {
				if (compteur < 50) {
					stockRentaBadB.put(Parser.stockNbrStock.get(s).stockNumber,
							Parser.stockNbrStock.get(s).rentaLast6Month(date));
				} else {
					stockRentaGoodB.put(Parser.stockNbrStock.get(s).stockNumber,
							Parser.stockNbrStock.get(s).rentaLast6Month(date));
				}
				compteur++;
			}
			sortedStockRentaGoodB = sortByValue(stockRentaGoodB);
			sortedStockRentaBadB = sortByValue(stockRentaBadB);
			compteur = 0;
			if (Marche.rentaLast6Month(date) >= 0) {
				for (int s : sortedStockRentaBadB.keySet()) {
					if (compteur < 10) {
						P1.set(compteur, Parser.stockNbrStock.get(s));
					}
					compteur++;
				}
				compteur = 0;
				for (int s : sortedStockRentaGoodB.keySet()) {
					if (compteur >= 40) {
						P10.set(49 - compteur, Parser.stockNbrStock.get(s));
					}
					compteur++;
				}
			} else {
				for (int s : sortedStockRentaGoodB.keySet()) {
					if (compteur < 10) {
						P1.set(compteur, Parser.stockNbrStock.get(s));
					}
					compteur++;
				}
				compteur = 0;
				for (int s : sortedStockRentaBadB.keySet()) {
					if (compteur >= 40) {
						P10.set(49 - compteur, Parser.stockNbrStock.get(s));
					}
					compteur++;
				}
			}
			compteur = 0;
			renta10 = P10.renta(j);
			P10.oldValue = P10.newValue;
			P10.newValue = P10.newValue * (1 + renta10);
			
			//Je print la premiere date
			System.out.println(P1.rentaMonth(date));
			for (int i = 2; i <= 12; i++) {
				//date va faire tous les mois
				date = new DateYM(date.getY(), i);
				System.out.println(P1.rentaMonth(date));
			}
			renta1 = P1.renta(j);

			//System.out.println(renta1);
			P1.oldValue = P1.newValue;
			P1.newValue = P1.newValue * (1 + renta1);
			//System.out.println("p1 old value puis new value  \t\t  " + P1.oldValue + "  \t\t  " + P1.newValue);
			stockBeta.clear();
			stockRentaBadB.clear();
			stockRentaGoodB.clear();
			// System.out.println("p1 old value puis new value - " + P1.oldValue
			// + " - " + P1.newValue);
			// System.out.println("En " + j + " :");
			// System.out.println("Le portefeuille P10 a une renta de : " +
			// renta10);
			// System.out.println(P10.toString());
			// System.out.println("Le portefeuille P1 a une renta de : " +
			// renta1);
			// System.out.println(P1.toString());
			if (renta10 < renta1) {

				pourri++;
			}

		}
		// System.out.println(Marche.toStringStatic());
		System.out.println("POURRI: "+ pourri);

	}
}