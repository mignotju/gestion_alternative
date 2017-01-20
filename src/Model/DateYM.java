package Model;

public class DateYM {
	private int year;
	private int month;

	public DateYM() {
		year = 0;
		month = 0;
	}

	// On considère qu'ici month est toujours compris entre 1 et 12
	public DateYM(int year, int month) {
		this.year = year;
		this.month = month;
	}

	public void setY(int y) {
		this.year = y;
	}

	public void setM(int m) {
		this.month = m;
	}

	public int getY() {
		return year;
	}

	public int getM() {
		return month;
	}

	public int inMonth() {
		return year * 12 + month;
	}
	

	// Renvoie true si la date que représente this est dans les "months" derniers mois
	// avant la reference date
	public boolean isInPastMonths(DateYM referenceDate, int months) {
		int nbMonth = referenceDate.inMonth() - this.inMonth();
		return (nbMonth <= months && nbMonth > 0);
	}

	@Override
	public int hashCode() {
		return year * 12 + month;
	}

	@Override
	public boolean equals(Object obj) {
		DateYM o = (DateYM) obj;
		return (year == o.year && month == o.month);
	}

	@Override
	public String toString() {
		return "y: " + year + " month: " + month;
	}

}
