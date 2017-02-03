package Model;

import java.util.*;

public class Stock {
	public int stockNumber;
	public Map<DateYM, Double> dateValeur;
	public Map<DateYM, Double> dateBeta;

	public Stock(int stockNum) {
		stockNumber = stockNum;
		dateValeur = new LinkedHashMap<DateYM, Double>();
		dateBeta = new LinkedHashMap<DateYM, Double>();
	}

	// Renvoie la rentabilité du stock au cours des 6 derniers mois avant
	// referenceDate
	public double rentaLast6Month(DateYM referenceDate) {
		double r = 1.0;
		DateYM dBefore = referenceDate;
		for (int i = 0; i < 6; i++) {
			dBefore = new DateYM(dBefore.getY(), dBefore.getM() - 1);
			if (dBefore.getM() == 0) {
				dBefore.setY(dBefore.getY() - 1);
				dBefore.setM(12);
			}
			if (dateValeur.containsKey(dBefore)) {
				r *= (1 + dateValeur.get(dBefore));
			} else {
				r *= (1 + getClosestRenta(dBefore));
			}
		}
		r -= 1;
		return r;
	}
	
	public double rentaLastYear(DateYM referenceDate) {
		double r = 1;
		DateYM dBefore = referenceDate;
		for (int i = 0; i < 12; i++) {
			dBefore = new DateYM(dBefore.getY(), dBefore.getM() - 1);
			if (dBefore.getM() == 0) {
				dBefore.setY(dBefore.getY() - 1);
				dBefore.setM(12);
			}
			if (dateValeur.containsKey(dBefore)) {
				r *= (1 + dateValeur.get(dBefore));
			} else {
				r *= (1 + getClosestRenta(dBefore));
			}
		}
		r -= 1;
		return r;
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
	
	public String toStringName() {
		return Integer.toString(stockNumber);
	}

	public double getClosestRenta(DateYM d) {
		DateYM dBefore, dAfter;
		int spreadMonthBefore, spreadMonthAfter;
		dBefore = d;
		dAfter = d;
		do {
			dBefore = new DateYM(dBefore.getY(), dBefore.getM() - 1);
			if (dBefore.getM() == 0) {
				dBefore.setY(dBefore.getY() - 1);
				dBefore.setM(12);
			}
		} while (!dateValeur.containsKey(dBefore));

		do {
			dAfter = new DateYM(dAfter.getY(), dAfter.getM() + 1);
			if (dAfter.getM() == 13) {
				dAfter.setY(dAfter.getY() + 1);
				dAfter.setM(1);
			}
		} while (!dateValeur.containsKey(dAfter));
		spreadMonthBefore = d.inMonth() - dBefore.inMonth();
		spreadMonthAfter = dAfter.inMonth() - d.inMonth();
		return (dateValeur.get(dBefore)* spreadMonthAfter + dateValeur.get(dAfter)* spreadMonthBefore) /(spreadMonthAfter+spreadMonthBefore);
	}
}
