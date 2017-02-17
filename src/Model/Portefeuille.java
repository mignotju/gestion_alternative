package Model;

public class Portefeuille {
	public Stock[] shares;// tableau des 10 actifs concernés
	public Stock[] oldShares;// tableau des 10 actifs concernés
	public double newValue;
	public double oldValue;

	public Portefeuille() {
		shares = new Stock[10];
		oldShares = new Stock[10];
		Stock s = new Stock(-1);
		for (int i = 0; i < shares.length; i++) {
			shares[i] = s;
		}
		newValue = 100;
			
	}

	public Portefeuille(Stock[] shares) {
		if (shares.length == 10) {
			this.shares = shares;
		}
	}

	public Stock[] get() {
		return shares;
	}

	public void set(int i, Stock s) {
		shares[i] = s;
	}

	public String rentaMonth(DateYM date) {
		double sum = 0;
		double sumBeta = 0;
		for (int i = 0; i < shares.length; i++) {
			
			if (shares[i].dateValeur.containsKey(date)) {
				sum += shares[i].dateValeur.get(date);
				sumBeta += shares[i].dateBeta.get(date);
			} else {
				//System.out.println(i + " " + date.toString() + " " + shares[i].toStringName());
				sum += shares[i].getClosestRenta(date);
				sumBeta += shares[i].getClosestBeta(date);
			}
			
		}
		return sum/10.0 +","+ sumBeta/10.0;
	}
	
	public boolean oldSharesContains(Stock s) {
		for (int i = 0; i < oldShares.length; i++) {
			if (oldShares[i].stockNumber == s.stockNumber) {
				return true;
			}
		}
		return false;
	}
	
	public boolean newShareContains(Stock s) {
		for (int i = 0; i < shares.length; i++) {
			if (shares[i].stockNumber == s.stockNumber) {
				return true;
			}
		}
		return false;
	}
	
	// Renvoie la renta du ptf sur une année
	public double renta(int year) {
		double r = 0.0;
		double rentaForAShare;
		DateYM d;
		for (int i = 0; i < shares.length; i++) {
			
			rentaForAShare=1;
			for (int m = 1; m <= 12; m++) {
				d = new DateYM(year,m);
				if(shares[i].dateValeur.containsKey(d)) {
					rentaForAShare*=(1+shares[i].dateValeur.get(d));
				} else {
					rentaForAShare*=(1+shares[i].getClosestRenta(d));
				}
			}
			r+=(rentaForAShare-1);
		}
		r=r/10.0;

		return r;
	}
	
	public String toString() {
		String res = "Le portefeuille est composé des titres suivant : \n";
		res="";
		for (int i = 0; i < shares.length; i++) {
			if (i!=shares.length-1) {
				res += shares[i].toStringName() + ",";
			} else {
				res += shares[i].toStringName();
			}
			
		}
		return res ;
	}
	
	public double computeTransaction(DateYM date) {
		double res = 0;
		for (int i = 0; i < shares.length; i++) {
			if (oldSharesContains(shares[i])) {
				//J'avais déjà cet actif, je paye la différence seulement.
				//J'avais 10% de oldValue ->10€
				//J'ai besoin de 10% de newValue 
				// l'actif i vaut maintenant shares[i].rentaLastYear(date);
				res += Math.abs(newValue/10. - (oldValue/10.)*shares[i].rentaLastYear(date))*0.001;
			} else {
				//Je n'avais pas cet actif mais j'en ai besoin.
				//j'aurais dis : je dois le vendre. et du coup au prix quelle vaut
				res+= 0.001*newValue/10.;
			}
			if (!newShareContains(oldShares[i])) {
				res += (oldValue/10.)*shares[i].rentaLastYear(date)*0.001;
			}
		}
				
		return res;
	}
}





























