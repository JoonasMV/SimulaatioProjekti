package simu.model;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import eduni.distributions.ContinuousGenerator;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;
import simu.framework.Trace;

/**
 * Simulaatiossa käytettävä palvelupiste
 */

public class Palvelupiste implements Comparable<Palvelupiste> {

	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();
	private ContinuousGenerator generator;
	private Tapahtumalista tapahtumalista = Tapahtumalista.getInstance();
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;
	private int saapuneetAsiakkaat;
	private int palvellutAsiakkaat;
	private double aktiiviaika;
	private double kokonaisoleskeluaika;
	private Kello kello = Kello.getInstance();
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean varattu = false;
	private String nimi;
	private int maxMaara;
	private int palveltavana = 0;
	
	/**
	 * Testausta varten
	 */
	
	public Palvelupiste(double palvellutAsiakkaat, double aktiiviaika, double kokonaisoleskeluaika, double kellonaika, String nimi) {
		this.palvellutAsiakkaat = (int)palvellutAsiakkaat;
		this.aktiiviaika = aktiiviaika;
		this.kokonaisoleskeluaika = kokonaisoleskeluaika;
		this.nimi = nimi;
		kello.setAika(kellonaika);
	}
	
	/**
	 * Palvelupisteen (default) konstruktori
	 * 
	 * @param generator jakauma, jota Palvelupiste käyttää palveluaikojen generoimiseen
	 * @param tyyppi Tapahtuman tyyppi
	 * @param nimi Palvelupisteen nimi
	 */
	public Palvelupiste(ContinuousGenerator generator, TapahtumanTyyppi tyyppi, String nimi){
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.nimi = nimi;
	}
	
	/**
	 * Palvelupisteen konstruktori
	 * <p>
	 * Käytetään, jos halutaan, että Palvelupiste palvelee useampaa kuin yhtä Asiakasta samaan aikaan.
	 * <p>
	 * @param generator jakauma, jota Palvelupiste käyttää palveluaikojen generoimiseen
	 * @param tyyppi Tapahtuman tyyppi
	 * @param nimi palvelupisteen nimi
	 * @param maxMaara samanaikaisesti palveltavien asiakkaiden suurin määrä
	 */
	public Palvelupiste(ContinuousGenerator generator, TapahtumanTyyppi tyyppi, String nimi, int maxMaara){
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.nimi = nimi;
		this.maxMaara = maxMaara;
	}


	/**
	 * Lisää Palvelupisteen jonoon Asiakkaan. Asiakas ei saa olla null.
	 * 
	 * @param a jonoon lisättävä Asiakas
	 * 
	 * @throws	NullPointerException jos Asiakas on null
	 */
	public void lisaaJonoon(Asiakas a){   // Jonon 1. asiakas aina palvelussa
		if(a == null)
			throw new NullPointerException("Asiakas = null");
		
		jono.add(a);
		a.setSaapumisaika(kello.getAika());
		saapuneetAsiakkaat++;
	}

	/**
	 * Ottaa asiakkaan jonosta ja palauttaa sen. Asiakas otetaan joko jonon alusta tai poistumisajan perusteella.
	 * 
	 * @return asiakas Asiakas, joka oli ensimmäisenä jonossa
	 */
	public Asiakas otaJonosta(){  // Poistetaan palvelussa ollut
		Trace.out(Trace.Level.INFO, "otaJonosta(): " + nimi + " JONON PITUUS: " + jono.size() + " " + nimi);

		Asiakas asiakas = null;
		if(jono.peek().getPoistumisaika() == kello.getAika()) {
			asiakas = jono.poll();
			Trace.out(Trace.Level.INFO, "jono.poll()");
		} else {
			Iterator<Asiakas> it = jono.iterator();
			boolean done = false;
			while(it.hasNext() && !done) {
				asiakas = it.next();
				if(asiakas.getPoistumisaika() == kello.getAika()) {
					done = true;
				}
			}
			Trace.out(Trace.Level.INFO, "jono.remove()");
			jono.remove(asiakas);			
		}
		
		Trace.out(Trace.Level.INFO, asiakas.toString());
		palveltavana = palveltavana <= 0 ? 0 : --palveltavana;
		palvellutAsiakkaat++;
		varattu = false;
		
		kokonaisoleskeluaika += (asiakas.getPoistumisaika() - asiakas.getSaapumisaika());
		return asiakas;
	}

	/**
	 * Aloittaa palvelun seuraavalle asiakkaalle. 
	 * <p>
	 * Jos kyseessä on ruokapöytä
	 * aloitetaan palvelu usealle asiakkaalle. Asiakkaat pysyvät palvelupisteen jonossa
	 * palvelun suorituksen ajan, kunnes ne poistetaan listalta.
	 * <p>
	 * 
	 */
	public void aloitaPalvelu(){
		if(this.skeduloitavanTapahtumanTyyppi.equals(TapahtumanTyyppi.RUOKAP) || this.skeduloitavanTapahtumanTyyppi.equals(TapahtumanTyyppi.LEIPA)) {
			
			Iterator<Asiakas> it =  jono.iterator();
			while(it.hasNext() && !varattu) {
				Asiakas a = it.next();
				if(a.getPoistumisaika() == 0) {
					palveltavana++;
					Trace.out(Trace.Level.INFO, "Aloitetaan palvelu asiakkaalle " + a.getId() + " pisteella " + nimi);
					double palveluaika = this.generator.sample();
					double asetettava = kello.getAika() + palveluaika;
					tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, asetettava));
					a.setPoistumisaika(asetettava);					
				}
				if(palveltavana >= maxMaara)
					varattu = true;
			}
		}
		else {
			varattu = true;
			palveltavana++;
			Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + jono.peek().getId() + " pisteella " + nimi);
			
			double palveluaika = this.generator.sample();
			double asetettava = Kello.getInstance().getAika() + palveluaika;
			Trace.out(Trace.Level.INFO, "asetettava: " + asetettava);
			Trace.out(Trace.Level.INFO, "sample: " + palveluaika);
			Trace.out(Trace.Level.INFO, "kello: " + kello.getAika());
			
			try {
				tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, asetettava));				
			} catch (ArithmeticException e) {
				e.printStackTrace();
				System.out.printf("Palveluaika %f asetettava %f", palveluaika, asetettava);
				System.exit(1);
			}
 
			jono.peek().setPoistumisaika(asetettava);
			Trace.out(Trace.Level.INFO, nimi + " palveltavana: " + jono.peek());
		}
	}
	
	/**
	 * Kertoo, onko palvelupiste varattu vai ei.
	 * Jos palvelupiste pystyy palvelemaan useita ihmisiä, se on varattu vain jos se on täynnä
	 * 
	 * @return varattu boolean
	 */
	public boolean onVarattu(){
		return varattu;
	}
	
	/**
	 * Palauttaa asiakkaan, joka on jonossa ensimmäisenä. 
	 * <p>
	 * Jos kyseessä on palvelupiste, joka palvelee vain yhtä 
	 * asiakasta kerrallaan, kyseessä on varmasti palveltava asiakas. Jos palvelupiste palvelee useita asiakkaita 
	 * samaan aikaan palautettava asiakas ei VÄLTTÄMÄTTÄ ole seuraavaksi palvelusta poistuva asiakas.
	 * <p>
	 * 
	 * @return asiakas Asiakas, joka on jonossa ensimmäisenä
	 */
	public Asiakas getPalveltava() {
		return jono.peek();
	}

	/**
	 * Kertoo, onko palvelupisteen jono tyhjä.
	 * 
	 * @return	boolean
	 */
	public boolean onJonossa(){
		return jono.size() != 0;
	}
	
	/**
	 * Palauttaa palvelupisteen jonon pituuden.
	 * 
	 * @return	jonon pituus
	 */
	public int jononPituus() {
		return jono.size();
	}
	
	/**
	 * Kertoo, kuinka monta asiakasta palvelupisteellä voi olla samaan aikaan palveltavana. Ei saa kutsua,
	 * jos palvelupiste ei palvele useampaa kuin yhtä asiakasta samaan aikaan.
	 * 
	 * @return	palveltavien Asiakkaiden suurin määrä
	 * 
	 * @throws	IllegalCallerException jos maxMaara = 0 eli, ei asetettu.
	 */
	public int getMaxMaara() {
		
		if(maxMaara == 0)
			throw new IllegalCallerException("maxMaara = 0");
		
		return maxMaara;
	}
	
	/**
	 * Palauttaa HashMap-muodossa palvelupisteen suureet.
	 * 
	 * @return suureet palvelupisteen suureet muodossa: {@code HashMap<String, Double> }
	 */
	public HashMap<String, Double> getSuureet(){
		HashMap<String, Double> suureet = new HashMap<>();
		suureet.put("saapuneetAsiakkaat", (double)saapuneetAsiakkaat);
		suureet.put("palvellutAsiakkaat", (double)palvellutAsiakkaat);
		suureet.put("aktiiviaika", aktiiviaika);
		suureet.put("kokonaisoleskeluaika", kokonaisoleskeluaika);
		
		return suureet;
	}
	
	/**
	 * Palauttaa Palvelupisteen nimen
	 * 
	 * @return nimi Palvelupisteen nimi
	 */
	public String getNimi() {
		return nimi;
	}
	
	/**
	 * Palauttaa palveltavana olevien määrän
	 * 
	 * @return palveltavana palveltavana olevien määrä
	 */
	public int getPalveltavana() {
		return palveltavana;
	}
	
	/**
	 * Päivittää palvelupisteen aktiiviajan, jos se on varattu.
	 * 
	 * @param muutos delta Time (ajan muutos)
	 */
	public void paivitaAktiiviaika(double muutos) {
			if(palveltavana > 0) {
				Trace.out(Trace.Level.INFO, String.format("%s ajan muutos: %f aktiiviaika: %f \n", nimi, muutos, aktiiviaika));
				aktiiviaika += muutos;
			}
		}
	
	/**
	 * Palauttaa skeduloitavan Tapahtuman tyypin
	 * 
	 * @return skeduloitavanTapahtumanTyyppi 
	 */
	public TapahtumanTyyppi getTapahtumanTyyppi() {
		return skeduloitavanTapahtumanTyyppi;
	}
	
		
	@Override
	public String toString() {
		return "Palvelupisteen "+ nimi +" jonon pituus: "+jono.size();
	}


	@Override
	public int compareTo(Palvelupiste palvelupiste) {
		if(this.jononPituus() < palvelupiste.jononPituus()) {
			return 1;
		}else if (this.jononPituus() > palvelupiste.jononPituus()) {
			return -1;
		
		}
		return 0;
	}
}
