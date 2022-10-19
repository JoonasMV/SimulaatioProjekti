package simu.util;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Serializable wrapper-luokka palvelupisteen datan tallentamiseksi.
 * @deprecated ei käytetä enää, entity.PalvelupisteTiedot korvaa
 */

@Deprecated
public class PalvelupisteData implements Serializable {
	
	private static final long serialVersionUID = 123456789L;
	
	private List<Double> suureet;
	private String nimi;

	/**
	 * PalvelupisteData konstruktori
	 * 
	 * @param nimi	  Palvelupisteen nimi
	 * @param suureet Palvelupisteen suorituskykysuureet listassa
	 */
	public PalvelupisteData(String nimi, List<Double> suureet) {
		this.suureet = Objects.requireNonNull(suureet);
		this.nimi = Objects.requireNonNull(nimi);
	}
	
	/**
	 * Suureet ovat järjestyksessä: käyttöaste, suoritusteho, avg palveluaika,
	 * läpimenoaika ja avg jonon pituus.
	 */
	
	@Deprecated
	public List<Double> getSuureet() {
		return suureet;
	}
	
	
	@Deprecated
	public String getNimi() {
		return nimi;
	}

	@Override
	public String toString() {
		return String.format("Palvelupisteen %s tiedot", nimi);
	}
}
