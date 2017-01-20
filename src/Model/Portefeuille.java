package Model;

public class Portefeuille {
	private Stock[] shares;//tableau des 10 actifs concernés
	
	
	public Portefeuille() {
		shares = new Stock[10];
	}
	
	public Portefeuille(Stock[] shares) {
		if(shares.length==10){
			this.shares=shares;
		}
	}
	
	public Stock[] get() {
		return shares;
	}
	
	public void set(int i, Stock s) {
		shares[i]=s;
	}
	
	
	//Renvoie la renta du ptf sur une année
	public double renta(int year) {
		double r=0;
		for(int i=0;i<shares.length;i++) {
			
		}
		return r;
	}
}
