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
