package simu.framework;
import eduni.distributions.*;

import simu.model.TapahtumanTyyppi;

/**
 * Saapumisprosessia käytetään uusien saapumistapahtumien luomiseen
 *
 */
public class Saapumisprosessi {
	
	private ContinuousGenerator generaattori;
	private Tapahtumalista tapahtumalista = Tapahtumalista.getInstance();
	private TapahtumanTyyppi tyyppi;

	/**
	 * Saapumisprosessin konstruktori
	 * 
	 * @param g Saapumisprosessin käyttämä jakauma
	 * @param tyyppi generoitavien Tapahtumien tyyppi
	 */
	public Saapumisprosessi(ContinuousGenerator g, TapahtumanTyyppi tyyppi){
		this.generaattori = g;
		this.tyyppi = tyyppi;
	}
	
	public Saapumisprosessi(TapahtumanTyyppi tyyppi){
		this.tyyppi = tyyppi;
	}
	
	/**
	 * Asettaa jakauman arvot
	 * 
	 * @param generator jakauma
	 */
	public void setJakauma(ContinuousGenerator generator) {
		this.generaattori = generator;
	}

	/**
	 * Luo uuden Tapahtuma-olion, joka lisätään Tapahtumalistaan
	 */
	public void generoiSeuraava(){
		Tapahtuma t = new Tapahtuma(tyyppi, Kello.getInstance().getAika()+generaattori.sample());
		tapahtumalista.lisaa(t);
	}
	
	@Override
	public String toString() {
		return "Lisättävien tapahtumien tyyppi: "+tyyppi;
	}
}
