package simu.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.Ajo;
import entity.PalvelupisteTiedot;
import entity.PalvelupisteTiedot.PpTiedotBuilder;
import simu.util.SimuFileHandler;
import simu.util.PalvelupisteData;
import simu.framework.Kello;

/**
 * Yleisten suureiden tallentamiseen ja datan koostamiseen käytettävä singleton-luokka
 */

@SuppressWarnings("deprecation")
public class StatTracker {
	
	private Kello kello = Kello.getInstance();
	private static StatTracker INSTANCE = null;
	private int palvellutAsiakkaat = 0;
	//Koko järjestelmän läpimenoaika
	private double asiakkaidenLapimeno = 0;
	
	//Ennen ruokapöytiin siirtymistä aika
	private double jonotusaika = 0;
	//Ruokapöytiin siirtyneet
	private int lapimenneet = 0;
	
	/**
	 * Private-konstruktori, koska luokka on singleton
	 */
	private StatTracker() {
	}
	
	/**
	 * Palauttaa StatTracker instanssin
	 * 
	 * @return StatTracker instanssi
	 */
	public static StatTracker getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new StatTracker();
		}
		return INSTANCE;
	}
	
	/**
	 * Palauttaa palveltujen asiakkaiden määrän
	 * 
	 * @return palveltujen asiakkaiden määrä
	 */
	public int getPalvellutAsiakkaat() {
		return palvellutAsiakkaat;
	}
	
	/**
	 * Palauttaa asiakkaiden läpimenoajan
	 * 
	 * @return asiakkaiden läpimenoaika
	 */
	public double getLapimeno() {
		return asiakkaidenLapimeno;
	}
	
	/**
	 * Lisää palveltujen asiakkaiden määrää yhdellä
	 */
	public void addPalveltu() {
		palvellutAsiakkaat++;
	}
	
	/**
	 * Laskee palvelupisteen suorituskykysuureet
	 * 
	 * @param p palvelupiste
	 * @return  Lista joka sisältää palvelupisteelle lasketut suureet järjestyksessä 
	 * 			käyttöaste, suoritusteho, keskim. palveluaika, keskim. läpimenoaika, keskim. jononpituus
	 */
	private List<Double> laskeSuureet(Palvelupiste p) {
		
		List<Double> arvot = new ArrayList<>(5);
		
		HashMap<String, Double> suureet = p.getSuureet();
		
		double kellonaika = kello.getAika();
		
		arvot.add(kayttoAste(suureet.get("aktiiviaika"), kellonaika));
		arvot.add(suoritusTeho(suureet.get("palvellutAsiakkaat"), kellonaika));
		arvot.add(avgPalveluaika(suureet.get("aktiiviaika"), suureet.get("palvellutAsiakkaat")));
		arvot.add(avgLapimeno(suureet.get("kokonaisoleskeluaika"), suureet.get("palvellutAsiakkaat")));
		arvot.add(avgJononpituus(suureet.get("kokonaisoleskeluaika"), kellonaika));
		
		return arvot;
	}
	
	/**
	 * Lisää annetun läpimenoajan
	 * <p>
	 * Kyseessä koko järjestelmän läpimenoaika
	 * <p>
	 * 
	 * @param lapimeno asiakkaan läpimenoaika
	 */
	public void addAsiakkaanLapimeno(double lapimeno) {
		asiakkaidenLapimeno += lapimeno;
	}
	
	/**
	 * Laskee palvelupisteen käyttöasteen
	 * 
	 * @param aktiiviaika palvelupisteen aktiiviaika
	 * @param kellonaika  kellonaika
	 * @return			  käyttoaste
	 */
	private double kayttoAste(double aktiiviaika, double kellonaika) {
		if(kellonaika == 0)
			return 0;
		
		return aktiiviaika / kellonaika;
	}
	
	/**
	 * Laskee palvelupisteen suoritustehon
	 * 
	 * @param palvellutAsiakkaat palveltujen asiakkaiden määrä
	 * @param kellonaika		 kellonaika
	 * @return					 suoritusteho
	 */
	private double suoritusTeho(double palvellutAsiakkaat, double kellonaika) {
		if(kellonaika == 0)
			return 0;
		
		return palvellutAsiakkaat / kellonaika;
	}
	
	/**
	 * Laskee palvelupisteen keskimääräisen palveluajan
	 * 
	 * @param aktiiviaika		 palvelupisteen aktiiviaika
	 * @param palvellutAsiakkaat palveltujen asiakkaiden määrä
	 * @return					 keskimääräinen palveluaika
	 */
	private double avgPalveluaika(double aktiiviaika, double palvellutAsiakkaat) {
		if(palvellutAsiakkaat == 0)
			return 0;
		
		return aktiiviaika / palvellutAsiakkaat;
	}
	
	/**
	 * Laskee palvelupisteen keskimääräisen läpimenoajan
	 * 
	 * @param kokonaisoleskeluaika palvelupisteen kokonaisoleskeluaika
	 * @param palvellutAsiakkaat   palvetujen asiakkaiden määrä
	 * @return					   keskimääräinen läpimenoaika
	 */
	private double avgLapimeno(double kokonaisoleskeluaika, double palvellutAsiakkaat) {
		if(palvellutAsiakkaat == 0)
			return 0;
		
		return kokonaisoleskeluaika / palvellutAsiakkaat;
	}
	
	/**
	 * Laskee palvelupisteen keskimääräisen jononpituuden
	 * 
	 * @param kokonaisoleskeluaika palvelupisteen kokonaisoleskeluaika
	 * @param kellonaika		   kellonaika
	 * @return					   keskimääräinen jononpituus
	 */
	private double avgJononpituus(double kokonaisoleskeluaika, double kellonaika) {
		if(kellonaika == 0)
			return 0;
		
		return kokonaisoleskeluaika / kellonaika;
	}
	
	/**
	 * Tulostaa konsoliin simulaation tulokset
	 * 
	 * @param pisteet palvelupisteet
	 */
	public void tulokset(Palvelupiste[] pisteet) {
		System.out.println("\n-------------------------------------\n");
		for(Palvelupiste p : pisteet) {
			
			//Datan alustaminen kirjoittamista varten
			List<Double> arvot = laskeSuureet(p);
			
			//Tulostukset
			System.out.println("Palvelupiste: " + p.getNimi());
			System.out.printf(
					"kayttoaste: %.2f \nsuoritusteho: %.2f \navg palveluaika: %.2f \nlapimenoaika: %.2f \njononpituus: %.2f\n",
					arvot.toArray());
			System.out.println("\n-------------------------------------\n");
		}
	}
	
	/**
	 * Laskee palvelupisteiden suureet ja palauttaa ne
	 * 
	 * @deprecated ei käytetä
	 * 
	 * @param pisteet palvelupisteet joiden suureet lasketaan
	 * @return        lista joka sisältää PalvelupisteData-olioita
	 */
	@Deprecated
	public List<PalvelupisteData> getData(Palvelupiste[] pisteet) {
		List<PalvelupisteData> tiedot = new ArrayList<PalvelupisteData>();
		for(Palvelupiste p : pisteet) {
			
			//Datan alustaminen kirjoittamista varten
			List<Double> arvot = laskeSuureet(p);
			PalvelupisteData dataPiste = new PalvelupisteData(p.getNimi(), arvot);
			tiedot.add(dataPiste);
		}
		
		return tiedot;
	}
	
	/**
	 * Laskee palvelupisteiden suureet ja palauttaa ne listassa
	 * <p>
	 * Käytetään datan tallentamiseksi tietokantaan
	 * <p>
	 * 
	 * @param pisteet palvelupisteet
	 * @param ajo	  kyseessä oleva simulaatiokerta
	 * @return		  palvelupisteiden tiedot listassa
	 */
	public List<PalvelupisteTiedot> getTiedot(Palvelupiste[] pisteet, Ajo ajo){
		List<PalvelupisteTiedot> tiedot = new ArrayList<PalvelupisteTiedot>();
		for(Palvelupiste p : pisteet) {
			
			List<Double> arvot = laskeSuureet(p);
			PalvelupisteTiedot.PpTiedotBuilder builder = new PpTiedotBuilder(p.getNimi());
			PalvelupisteTiedot t = builder.kayttoaste(arvot.get(0)).suoritusteho(arvot.get(1))
								   .avgpalveluaika(arvot.get(2)).avglapimenoaika(arvot.get(3))
								   .avgjononpituus(arvot.get(4)).ajo(ajo).build();
			tiedot.add(t);
		}
		
		return tiedot;
	}

	/**
	 * Tallentaa palvelupisteiden tiedot tiedostoon
	 * @deprecated Ei kirjoiteta tiedostoon vaan databaseen.
	 * 
	 * @param data Lista joka sisältää PalvelupisteData-olioita, jotka sisältävät palvelupisteiden tiedot
	 */
	@Deprecated
	public void tallennaTulokset(List<PalvelupisteData> data) {
		File tiedosto = new File("src/testi/testi.txt");
		SimuFileHandler.write(tiedosto, data, false);
	}
	
	/**
	 * Luo uuden StatTracker instanssin
	 */
	public static void reset() {
		INSTANCE = new StatTracker();
	}
	
	/**
	 * Lisää jonotusajan kokonaisjonotusaikaan 
	 * <p>
	 * Kyseessä on asiakkaan jonotusaika ennen ruokapöytiin siirtymistä
	 * <p>
	 *
	 * @param aika
	 */
	
	public void lisaaJonotus(double aika) {
		jonotusaika += aika;
		lapimenneet++;
	}
	
	/**
	 * Laskee keskimääräisen jonotusajan ennen ruokapöytiin siirtymistä
	 * 
	 * @return keskimääräinen jonotusaika
	 */
	
	public double getAvgJonotusEnnenRkp() {
		return jonotusaika / lapimenneet;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("Palvellut asiakkaat: %d, kellonaika: %.2f", palvellutAsiakkaat, Kello.getInstance().getAika());
	}
	
}