package Model;

import java.util.*;

public class Stock {
	public int stockNumber;
	public Map<DateYM, Double> dateValeur;

	public Stock(int stockNum) {
		stockNumber = stockNum;
		dateValeur = new LinkedHashMap<DateYM, Double>();
	}

	//Renvoie la rentabilité du stock au cours des 6 derniers mois avant referenceDate
	public double rentaLast6Month(DateYM referenceDate) {
		double moy = 0;
		int size = 0;
		for (DateYM d : dateValeur.keySet()) {
			if (d.isInPastMonths(referenceDate,6)) {
				moy += dateValeur.get(d);
				size++;
			}
		}
		moy = moy / (double) size;
		return moy;
	}

	public String toString() {
		String res = "";
		Set listKeys = dateValeur.keySet(); // Obtenir la liste des clés
		Iterator iterateur = listKeys.iterator();
		// Parcourir les clés et afficher les entrées de chaque clé;
		while (iterateur.hasNext()) {
			Object key = iterateur.next();
			res += key + " => " + dateValeur.get(key) + "\n";
		}
		return res;
	}
}
