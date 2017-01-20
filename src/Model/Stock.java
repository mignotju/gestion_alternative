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
	
	
	public double getClosestRenta(DateYM d){
		DateYM dBefore, dAfter;
		dBefore=d;
		dAfter=d;
		do  {
			dBefore= new DateYM(dBefore.getY(),dBefore.getM()-1);
			if(dBefore.getM()==0){
				dBefore.setY(dBefore.getY()-1);
				dBefore.setM(12);
			}
		}while(dateValeur.containsKey(dBefore));
		
		do  {
			dAfter= new DateYM(dAfter.getY(),dAfter.getM()+1);
			if(dAfter.getM()==13){
				dAfter.setY(dAfter.getY()+1);
				dAfter.setM(1);
			}
		}while(dateValeur.containsKey(dAfter));
		
		return (dateValeur.get(dBefore) + dateValeur.get(dAfter))/2;
	}
}
