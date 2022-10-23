package simu.model;

import simu.framework.Kello;
import simu.framework.Trace;

/**
 * Asiakas-luokan oliot siirtyvät Palvelupisteeltä Palvelupisteelle
 *
 */
// TODO:
// Asiakas koodataan simulointimallin edellyttämällä tavalla (data!)
public class Asiakas {
	private double saapumisaika = 0;
	private double poistumisaika = 0;
	private double yhtAika = 0;
	private int id;
	private static int i = 1;
	private static long sum = 0;
	
	/**
	 * Alustetaan Id-numeroita ylläpitävä i annettuun numeroon
	 * 
	 * @param newI	uusien Id-numeroiden aloituskohta
	 */
	public static void alustaId(int newI) {
		i = newI;
	}
	
	/**
	 * Asiakkaan konstruktori, jossa sille asetetaan Id ja saapumisaika
	 */
	public Asiakas(){
	    id = i++;
		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+saapumisaika);
	}
	
	/**
	 * Palauttaa Asiakkaan poistumisajan
	 * 
	 * @return poistumisaika aika, jolloin Asiakas poistui palvelupisteeltä
	 */
	public double getPoistumisaika() {
		return poistumisaika;
	}

	/**
	 * Asettaa Asiakkaan poistumisajan ja päivittää asiakkaan viettämän ajan järjestelmässä
	 * 
	 * @param poistumisaika aika, jolloin Asiakas poistui palvelupisteeltä
	 */
	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
		yhtAika += (this.poistumisaika - this.saapumisaika);
	}

	/**
	 * Palauttaa Asiakkaan saapumisajan
	 * 
	 * @return saapumisaika aika, jolloin Asiakas saapui palvelupisteelle
	 */
	public double getSaapumisaika() {
		return saapumisaika;
	}
	
	/**
	 * Asettaa Asiakkaan saapumisajan
	 * 
	 * @param saapumisaika aika, jolloin Asiakas saapui palvelupisteelle
	 */
	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	
	/**
	 * Palauttaa Asiakkaan Id:n
	 * 
	 * @return id Asiakkaan tunniste
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Palauttaa asiakkaan tämän hetkisen viettämän ajan järjestelmässä
	 * 
	 * @return kokonaisaika järjestelmässä
	 */
	
	public double getYhtAika() {
		return yhtAika;
	}
	
	/**
	 * Lähettää Trace-luokalle raportin Asiakkaasen liittyvistä tiedoista
	 */
	public void raportti(){
		Trace.out(Trace.Level.INFO, "\nAsiakas "+id+ " valmis! ");
		Trace.out(Trace.Level.INFO, "Asiakas "+id+ " saapui: " +saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " poistui: " +poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas "+id+ " viipyi: " +(poistumisaika-saapumisaika));
		sum += (poistumisaika-saapumisaika);
		double keskiarvo = sum/id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ keskiarvo);
	}
	
	/**
	 * Asettaa saapumis- ja poistumisajan nollaksi
	 */
	public void resetoi() {
		saapumisaika = 0;
		poistumisaika = 0;
	}
	
	
	@Override
	public String toString() {
		return "Asiakas "+id+" saapui: "+saapumisaika+" ja poistui: "+poistumisaika;
	}
}
