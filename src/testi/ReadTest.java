package testi;

import java.io.File;
import simu.util.*;
import java.util.ArrayList;

public class ReadTest {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		File file = new File("src/testi/testi.txt");
		ArrayList<PalvelupisteData> luettu = (ArrayList<PalvelupisteData>) SimuFileHandler.read(file);
		
		for(PalvelupisteData data : luettu) {
			System.out.print("\n--------------------------\n");
			System.out.println(data.getNimi());
			System.out.printf(
					"kayttoaste: %.2f \nsuoritusteho: %.2f \navg palveluaika: %.2f \nlapimenoaika: %.2f \njononpituus: %.2f\n",
					data.getSuureet().toArray());
		}
	}

}
