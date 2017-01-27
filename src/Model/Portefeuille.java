package Model;

public class Portefeuille {
	private Stock[] shares;// tableau des 10 actifs concernés

	public Portefeuille() {
		shares = new Stock[10];
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
		r=r/10;
		return r;
	}
	
	public String toString() {
		String res = "Le portefeuille est composé des titres suivant : \n";
		for (int i = 0; i < shares.length; i++) {
			if (i!=shares.length-1) {
				res += shares[i].toStringName() + ",";
			} else {
				res += shares[i].toStringName();
			}
			
		}
		return res + "\n";
	}
}
