package simu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import controller.IKontrolleriMtoC;

import eduni.distributions.*;

import entity.*;
import entity.Asetukset.AsetuksetBuilder;
import dao.*;

import simu.framework.*;
import simu.util.MoottoriOptions;
import simu.util.PpRealtimeData;

/**
 * Simulaatiossa käytettävä moottori, älä anna nimen hämätä
 */

public class TestiMoottori extends Moottori {
	
	//Jakaumien seed
	private final long SEED = System.currentTimeMillis();
	
	//Singletonit
	private StatTracker st = StatTracker.getInstance();
	private Tapahtumalista tapahtumalista = Tapahtumalista.getInstance();
	private Kello kello = Kello.getInstance();
	
	private boolean ruuhka;
	private boolean ruuhkaMode;
	
	final int ruuhkanAloitus = 90;
	final double ruuhkaAjanPituus = 45;
	
	//Jakaumat
	private MoottoriOptions moottoriSettings;
	
	//Saapumisprosessi
	private Saapumisprosessi saapumisprosessi;
	
	//Palvelupisteiden lukumääriä (käyttäjä pystyy muuttamaan)
	private int linjastoLkm;
	private int kassaLkm;
	private int ipLkm;
	// Erityyppinen palvelupiste
	private final int RUOKAPOYDAT = 1;
	private final int LEIPAPISTEET = 1;
	private int TOTAL;
	private int leipaKapasiteetti = 5;
	private int poytaKapasiteetti = 100;
	private Random random = new Random();

	//Palvelupisteet
	private List<Palvelupiste> linjastot = new ArrayList<Palvelupiste>(linjastoLkm);
	private List<Palvelupiste> kassat = new ArrayList<Palvelupiste>(kassaLkm);
	private List<Palvelupiste> ipKassat = new ArrayList<Palvelupiste>(ipLkm);
	private Palvelupiste ruokapoyta;
	private Palvelupiste leipapiste;
	
	//Daot tuloksien tallentamista varten
	private AjoDAO ajoDAO = new AjoDAO();
	private PalvelupisteTiedotDAO palvelupisteTiedotDAO = new PalvelupisteTiedotDAO();
	private AsetuksetDAO asetuksetDAO = new AsetuksetDAO();
	


	/** 
	 * TestiMoottorin konstruktori
	 * 
	 * @param kontrolleri	simulaattorin kontrolleri
	 */
	
	public TestiMoottori(IKontrolleriMtoC kontrolleri) {
		super(kontrolleri);
	}
	
	/**
	 * Erilaisten Palvelupisteiden luominen ja ensimmäisen Asiakkaan saapumisen generoiminen
	 */
	
	@Override
	protected void alustukset() {
		TOTAL = linjastoLkm + kassaLkm + ipLkm + RUOKAPOYDAT + LEIPAPISTEET;
		palvelupisteet = new Palvelupiste[TOTAL];
		updateSettings();
		
		
		saapumisprosessi = new Saapumisprosessi(new Negexp(moottoriSettings.getSaapumisJakauma(), SEED), TapahtumanTyyppi.ARR1);
		
		//Luodaan linjastot
		for(int i = 0; i < linjastoLkm; i++) {
			Palvelupiste p = new Palvelupiste(new Normal(moottoriSettings.getLinjastoJakauma()[0], moottoriSettings.getLinjastoJakauma()[1], SEED), TapahtumanTyyppi.RUOKA, String.format("Linjasto %d", i+1));
			linjastot.add(p);
		}
		
		//Luodaan kassat
		for(int i = 0; i < kassaLkm; i++) {
			Palvelupiste p = new Palvelupiste(new Normal(moottoriSettings.getKassaJakauma()[0], moottoriSettings.getKassaJakauma()[1], SEED), TapahtumanTyyppi.KASSA, String.format("Kassa %d", i+1));
			kassat.add(p);
		}
		
		for(int i = 0; i < ipLkm; i++) {
			Palvelupiste p = new Palvelupiste(new Normal(moottoriSettings.getKassaJakauma()[0], moottoriSettings.getKassaJakauma()[1], SEED), TapahtumanTyyppi.IP, String.format("IPkassa %d", i+1));
			ipKassat.add(p);
		}
		
		//Luodaan yksi leipäpiste
		leipapiste = new Palvelupiste(new Normal(moottoriSettings.getLeipaJakauma()[0], moottoriSettings.getLeipaJakauma()[1], SEED), TapahtumanTyyppi.LEIPA, "Leipäpiste", leipaKapasiteetti);
		
		
		//Luodaan yksi ruokapöytä
		ruokapoyta = new Palvelupiste(new Normal(moottoriSettings.getPoytaJakauma()[0], moottoriSettings.getPoytaJakauma()[1], SEED), TapahtumanTyyppi.RUOKAP, "Ruokapoydat", poytaKapasiteetti);
		
		//Alustetaan palvelupisteet[]
		palvelupisteet[TOTAL - 1] = ruokapoyta;
		palvelupisteet[TOTAL - 2] = leipapiste;
		int i = 0;
		for(Palvelupiste p : linjastot) {
			palvelupisteet[i] = p;
			i++;
		}
		for(Palvelupiste p : kassat) {
			palvelupisteet[i] = p;
			i++;
		}
		for(Palvelupiste p : ipKassat) {
			palvelupisteet[i] = p;
			i++;
		}
		saapumisprosessi.generoiSeuraava();
	}
	
	/**
	 * Suorittaa saadun Tapahtuman sen tyypin määrittämällä tavalla
	 * 
	 * @param t suoritettava Tapahtuma
	 */
	
	@Override
	protected void suoritaTapahtuma(Tapahtuma t) {
		Palvelupiste palvelupiste = null;
		Asiakas asiakas = null;
		
		if(ruuhkaMode) {
			ruuhka();
		}
		
		switch(t.getTyyppi()) {
		
			case RUOKA:
				palvelupiste = getLyhinJono(ipKassat);
				Palvelupiste lyhinKassajono = getLyhinJono(kassat);
				
				//asiakas EI mene ip kassalle jos jonon pituus on yli 5 tai jos normikassalla on lyhempi jono tai 20% todennäköisyydellä myös skippaa
				if((palvelupiste.jononPituus() > 4) || (lyhinKassajono.jononPituus() < palvelupiste.jononPituus()) || (random.nextFloat() < 0.2)) {
					palvelupiste = lyhinKassajono;
				}
				asiakas = selvita(linjastot);
				asiakas.resetoi();
				Trace.out(Trace.Level.INFO, "Siirtyy -> " + palvelupiste.getNimi());
				palvelupiste.lisaaJonoon(asiakas);
				break;
			case KASSA:
				asiakas = selvita(kassat);
				asiakas.resetoi();
				
				if(leipapiste.jononPituus() < leipapiste.getMaxMaara()) {
					Trace.out(Trace.Level.INFO, "Siirtyy -> leipapiste");
					leipapiste.lisaaJonoon(asiakas);
				} else {
					Trace.out(Trace.Level.INFO, "Siirtyy -> ruokapoyta");
					st.lisaaJonotus(asiakas.getYhtAika());
					ruokapoyta.lisaaJonoon(asiakas);					
				}
				break;
			case IP:
				asiakas = selvita(ipKassat);
				asiakas.resetoi();
				
				if(leipapiste.jononPituus() < leipapiste.getMaxMaara()) {
					Trace.out(Trace.Level.INFO, "Siirtyy -> leipapiste");
					leipapiste.lisaaJonoon(asiakas);
				} else {
					Trace.out(Trace.Level.INFO, "Siirtyy -> ruokapoyta");
					st.lisaaJonotus(asiakas.getYhtAika());
					ruokapoyta.lisaaJonoon(asiakas);					
				}
				break;
			case LEIPA:
				asiakas = leipapiste.otaJonosta();
				asiakas.resetoi();
				Trace.out(Trace.Level.INFO, "Siirtyy -> ruokapoyta");
				st.lisaaJonotus(asiakas.getYhtAika());
				ruokapoyta.lisaaJonoon(asiakas);
				break;
			case RUOKAP:
				Asiakas a = ruokapoyta.otaJonosta();
				
				Trace.out(Trace.Level.INFO, String.format("Yhtaika: %f", a.getYhtAika()));
				st.addAsiakkaanLapimeno(a.getYhtAika());
				st.addPalveltu();
				break;
			case ARR1:
				palvelupiste = getLyhinJono(linjastot);
				palvelupiste.lisaaJonoon(new Asiakas());
				saapumisprosessi.generoiSeuraava();
				break;
			default:
				Trace.out(Trace.Level.INFO, "Tuntematon tapahtuma");
				Trace.out(Trace.Level.INFO, t.toString());
				break;
		}
	}
	
	/**
	 * Selvittää tietyntyyppisistä Palvelupisteistä sen, jossa on lyhin jono
	 * 
	 * @param ipKassat2 ArrayList tietyntyyppisistä Palvelupisteistä
	 * @return p tietyntyyppisistä Palvelupisteistä se, jossa on lyhin jono
	 */
	
	private Palvelupiste getLyhinJono(List<Palvelupiste> jono) {
		Palvelupiste p = jono.get(0);
		
		for(Palvelupiste pst : jono) {
			if(pst.jononPituus() < p.jononPituus()) {
				p = pst;
			}
		}

		return p;
	}
	
	/**
	 * Selvittää, kuka Palvelupisteessä olevista Asiakkaista poistuu seuraavaksi
	 * 
	 * @param pisteet ArrayList tietyntyyppisistä Palvelupisteistä
	 * @return Palvelupisteestä poistuva Asiakas
	 * @throws IllegalStateException jos Palvelupisteen jonossa ei ole Asiakasta
	 */
	
	private Asiakas selvita(List<Palvelupiste> pisteet) {
		for(Palvelupiste p : pisteet) {
			if(p.getPalveltava() != null) {
				if(p.getPalveltava().getPoistumisaika() == kello.getAika()) {
					Trace.out(Trace.Level.INFO, "selvita(): " + p.getPalveltava());
					return p.otaJonosta();
				}				
			}
		}
		
		//Heitetään exception, koska asiakas pitää aina löytyä jos tapahtumalistassa on tapahtuma
		throw new IllegalStateException("Asiakasta ei löytynyt");
	}

	/**
	 * Tulostaa simulaation aikana kerätyt tulokset konsoliin sekä tallentaa ne tietokantaan
	 */
	
	@Override
	protected void tulokset(double simulointiaika) {
		Ajo ajo = new Ajo();
		ajo.setDate(System.currentTimeMillis());
		ajo.setAika(simulointiaika);
		st.tulokset(palvelupisteet);
		ajo.setAvgAikaRuokapoydille(st.getAvgJonotusEnnenRkp());
		ajo.setPalvellutAsiakkaat(st.getPalvellutAsiakkaat());
		List<PalvelupisteTiedot> tulokset = st.getTiedot(palvelupisteet, ajo);
		ajoDAO.tallenna(ajo);
		
		Asetukset.AsetuksetBuilder builder = new AsetuksetBuilder(linjastoLkm, kassaLkm, ipLkm, ajo);
		Asetukset asetukset = builder.leipaKapasiteetti(leipaKapasiteetti).rkpKapasiteetti(poytaKapasiteetti)
									 .saapumisprosessi(moottoriSettings.getSaapumisJakauma()).linjastot(moottoriSettings.getLinjastoJakauma()/*[0]*/).kassat(moottoriSettings.getKassaJakauma())
									 .leipapiste(moottoriSettings.getLeipaJakauma()).ruokapoydat(moottoriSettings.getPoytaJakauma()).build();
		asetuksetDAO.tallenna(asetukset);
		
		for(PalvelupisteTiedot p : tulokset) {
			palvelupisteTiedotDAO.tallenna(p);
		}
		
		for (Palvelupiste p : palvelupisteet) {
			Trace.out(Trace.Level.INFO, p.toString());
		}
		
		Trace.out(Trace.Level.INFO, tapahtumalista.toString());
	}

	/**
	 * Tekee simulaatioon ruuhkan
	 * <p>
	 * Muuttaa Saapumisjakauman odotusarvon todella pieneksi, jos ruuhka on laitettu päälle ja 
	 * kello näyttää tiettyä aikaa. Lopettaa ruuhkan määrättynä aikana
	 * <p>
	 */
	private void ruuhka() {
		if(!ruuhka && kello.getAika() > ruuhkanAloitus) {
			saapumisprosessi.setJakauma(new Negexp(moottoriSettings.getSaapumisJakauma() * 0.25, SEED));
			ruuhka = true;
		}
		
		if (ruuhka && kello.getAika() > ruuhkanAloitus + ruuhkaAjanPituus) {
			saapumisprosessi.setJakauma(new Negexp(moottoriSettings.getSaapumisJakauma(), SEED));
			ruuhka = false;
		}
	}
	
	
	/**
	 * Asettaa linjastojen määrän
	 * 
	 * @param maara saatu linjastojen määrä
	 */
	
	@Override
	public void setLinjastot(int maara) {
		linjastoLkm = maara;
	}

	/**
	 * Asettaa kassojen määrän
	 * 
	 * @param maara saatu kassojen määrä
	 */
	
	@Override
	public void setKassat(int maara) {
		kassaLkm = maara;
	}
	
	/**
	 * Asettaa itsepalvelukassojen määrän
	 * 
	 * @param maara saatu ip-kassojen määrä
	 */
	
	@Override
	public void setIpkassat(int maara) {
		ipLkm = maara;
	}

	/**
	 * Asettaa leipäpöydän kapasiteetin
	 * 
	 * @param kapasitetti saatu leipäpöytäkapasiteetti
	 */
	
	@Override
	public void setLeipapoytaKapasiteetti(int kapasiteetti) {
		leipaKapasiteetti = kapasiteetti;
	}

	/**
	 * Asettaa ruokapöydän kapasiteetin
	 * 
	 * @param kapasiteetti saatu ruokapöytäkapasiteetti
	 */
	
	@Override
	public void setRuokapoytaKapasiteetti(int kapasiteetti) {
		poytaKapasiteetti = kapasiteetti;
	}
	
	/**
	 * Palauttaa ruokapöydässä olevien palveltavien Asiakkaiden määrän
	 * 
	 * @return ruokapöydässä olevien Asiakkaiden määrä
	 */
	
	@Override
	protected int getPoydissa() {
		return ruokapoyta.getPalveltavana();
	}

	/**
	 * Resetoi Kellon, Tapahtumalistan ja StatTrackerin, sekä alustaa Asiakkaiden id:n alkamaan taas yhdestä
	 */
	
	@Override
	protected void resetoiSingletonit() {
		Kello.reset();
		Tapahtumalista.reset();
		StatTracker.reset();
		Asiakas.alustaId(1);
	}
	
	/**
	 * Laskee palvelupisteiden reaaliaikaista dataa
	 * 
	 * @return PpRealtimeData-array
	 */
	@Override
	protected PpRealtimeData[] laskeRealtimeData(Palvelupiste[] palvelupisteet) {
		PpRealtimeData[] data = new PpRealtimeData[palvelupisteet.length];
		for(int i = 0; i < palvelupisteet.length; i++) {
			data[i] = PpRealtimeData.generate(palvelupisteet[i]);
		}
		return data;
	}
	
	@Override
	public void updateSettings() {
		this.moottoriSettings = kontrolleri.getMoottoriSettings();
		ruuhkaMode = moottoriSettings.getRuuhkaMode();
	}
}
