package simu.util;

import simu.model.Palvelupiste;

/**
 * Immutable-luokka joka sisältää palvelupisteen reaaliaikaista dataa.
 * <p>
 * Käytetään datan kuljettamisessa käyttöliittymälle
 * <p>
 */

public class PpRealtimeData {
	
	
	private final String nimi;
	private final int jononpituus;
	private final int palveltavana;
	
	/**
	 * Luo PpRealtimeData-olion
	 * 
	 * @param jononpituus  palvelupisteen jonon pituus
	 * @param palveltavana palvelupisteen palveltavana olevien määrä
	 * @param nimi		   palvelupisteen nimi
	 * @return			   PpRealtimeData-olio, jossa on palvelupisteen tiedot
	 */
	public static PpRealtimeData generate(int jononpituus, int palveltavana, String nimi) {
		return new PpRealtimeData(jononpituus, palveltavana, nimi);
	}
	
	/**
	 * Luo PpRealtimeData-olion
	 * 
	 * @param p palvelupiste jonka tiedoista olio luodaan
	 * @return  PpRealtimeData-olio, jossa on palvelupisteen tiedot
	 */
	public static PpRealtimeData generate(Palvelupiste p) {
		return new PpRealtimeData(p.jononPituus(), p.getPalveltavana(), p.getNimi());
	}
	
	private PpRealtimeData(int jononpituus, int palveltavana, String nimi) {
		this.jononpituus = jononpituus;
		this.palveltavana = palveltavana;
		this.nimi = nimi;
	}

	
	public int getJononpituus() {
		return jononpituus;
	}

	public int getPalveltavana() {
		return palveltavana;
	}
	
	public String getNimi() {
		return nimi;
	}
	
	public String toString() {
		return "Jononpituus " + this.jononpituus + "\nPalveltavana " + this.palveltavana + "\nNimi " +this.nimi;
	}
		
}
