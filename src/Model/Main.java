package Model;

import xlsx_manager.*;
import java.io.File;

public class Main {
	public static void main(String [ ] args) {
		System.out.println("Hello");
		File file = new File("data.xlsx");
		Parser.parse(file);
		System.out.println(Parser.getStock(467).toString());
	}
}