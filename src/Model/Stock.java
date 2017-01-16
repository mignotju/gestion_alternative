package Model;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;


public class Stock {
	public int stockNumber;
	public Map<String,Double> dateValeur;
	
	public Stock (int stockNum) {
		stockNumber = stockNum;
		dateValeur = new LinkedHashMap<String, Double>();
	}
	
	public String toString() {
		String res = "";
		Set listKeys=dateValeur.keySet();  // Obtenir la liste des clés
		Iterator iterateur=listKeys.iterator();
		// Parcourir les clés et afficher les entrées de chaque clé;
		while(iterateur.hasNext())
		{
			Object key= iterateur.next();
			res += key + " => " + dateValeur.get(key) + "\n";
		}
		return res;
	}
}
