package Model;
import java.util.*;

public class Stock {
	
	private int id;
	private Map<Date,Double> rentas;
	
	
	public Stock(int id){
		this.id = id;
		rentas = new Hashtable<>();
	}
	
	public Stock(int id, Map<Date,Double> rentas){
		this.id = id;
		this.rentas = rentas;
	}
	
	
	
}