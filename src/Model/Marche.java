package Model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Marche {
	public static Map<DateYM, Double> dateValeurMarket = new LinkedHashMap<DateYM, Double>();

	public Marche() {
		dateValeurMarket = new LinkedHashMap<DateYM, Double>();
	}

	public static double rentaLast6Month(DateYM referenceDate) {
		double r = 1.0;
		DateYM dBefore = referenceDate;
		for (int i = 0; i < 6; i++) {
			dBefore = new DateYM(dBefore.getY(), dBefore.getM() - 1);
			if (dBefore.getM() == 0) {
				dBefore.setY(dBefore.getY() - 1);
				dBefore.setM(12);
			}
			r *= (1 + dateValeurMarket.get(dBefore));
		}
		r -= 1;
		return r;
	}

	public static void addValue(DateYM d, double value) {
		if (!dateValeurMarket.containsKey(d)) {
			dateValeurMarket.put(d, value);
		}
	}

	public static String toStringStatic() {
		String res = "";
		Set listKeys = dateValeurMarket.keySet(); // Obtenir la liste des clés
		Iterator iterateur = listKeys.iterator();
		// Parcourir les clés et afficher les entrées de chaque clé;
		while (iterateur.hasNext()) {
			Object key = iterateur.next();
			res += key + " => " + dateValeurMarket.get(key) + "\n";
		}
		return res;
	}

	public String toStringName() {
		return "marché";
	}
}
