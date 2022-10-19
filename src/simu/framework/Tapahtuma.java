package simu.framework;

import simu.model.TapahtumanTyyppi;

/**
 * Tapahtuma-luokka, käytetään simulaatiossa
 * <p>
 * Tapahtumia suoritetaan simulaation aikana. Niillä on tapahtuman tyyppi ja aika,
 * jonka avulla tiedetään mitä tapahtuu ja milloin
 * <p>
 */

public class Tapahtuma implements Comparable<Tapahtuma> {
	
		
	private TapahtumanTyyppi tyyppi;
	private double aika;
	
	/**
	 * Tapahtuma-luokan konstruktori
	 * 
	 * @param tyyppi Tapahtuman tyyppi
	 * @param aika tapahtuma-aika sekunteina
	 */
	public Tapahtuma(TapahtumanTyyppi tyyppi, double aika) throws ArithmeticException{
		if(Double.isNaN(aika))
			throw new ArithmeticException("Aika == NaN");
		
		this.tyyppi = tyyppi;
		this.aika = aika;
	}
	
	/**
	 * Asettaa Tapahtuman tyypin
	 * 
	 * @param tyyppi Tapahtuman tyyppi
	 */
	public void setTyyppi(TapahtumanTyyppi tyyppi) {
		this.tyyppi = tyyppi;
	}
	
	/**
	 * Palauttaa Tapahtuman tyypin
	 * 
	 * @return tyyppi Tapahtuman tyyppi
	 */
	public TapahtumanTyyppi getTyyppi() {
		return tyyppi;
	}
	
	/**
	 * Asettaa Tapahtuman ajan
	 * 
	 * @param aika tapahtuma-aika sekunteina
	 */
	public void setAika(double aika) {
		this.aika = aika;
	}
	
	/**
	 * Palauttaa Tapahtuman ajan
	 * 
	 * @return aika tapahtuma-aika sekunteina
	 */
	public double getAika() {
		return aika;
	}
	
	@Override
	public int compareTo(Tapahtuma arg) {
		if (this.aika < arg.aika) return -1;
		else if (this.aika > arg.aika) return 1;
		return 0;
	}

	@Override
	public String toString() {
		return "Tapahtuman tyyppi: "+tyyppi+" Tapahtuma-aika: "+aika;
	}
	

}
