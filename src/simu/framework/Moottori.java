package simu.framework;

import controller.IKontrolleriMtoC;
import simu.model.Palvelupiste;
import simu.model.StatTracker;
import simu.util.PpRealtimeData;

public abstract class Moottori extends Thread implements IMoottori {
	
	private double simulointiaika = 0;
	private boolean skip = false;
	private boolean pause = false;
	private boolean lopeta = false;
	private int viive = 0;
	private PpRealtimeData[] realtimeData;
	
	private StatTracker st = StatTracker.getInstance();
	private Tapahtumalista tapahtumalista = Tapahtumalista.getInstance();
	private Kello kello = Kello.getInstance();
	
	protected Palvelupiste[] palvelupisteet;
	protected IKontrolleriMtoC kontrolleri;
	/**
	 * Luo Kellon ja Tapahtumalistan instanssit, saa kontrollerin
	 * 
	 * @param kontrolleri järjestelmän kontrolleri
	 */
	public Moottori(IKontrolleriMtoC kontrolleri){
		this.kontrolleri = kontrolleri;
	}
	/**
	 * Asettaa simulointiin käytettävän ajan
	 * 
	 * @param aika annettu aika sekunteina
	 */
	public void setSimulointiaika(double aika) {
		simulointiaika = aika;
	}
	/**
	 * Säie suorittaa A-, B- ja C-Tapahtumat, jos simulointi on käynnissä
	 * <p>
	 * Metodi vastaa simulaation ajamisesta. Se alustaa simulaation ajamiseen tarvittavat asiat,
	 * suorittaa sen ajon aikana tapahtuvat asiat sekä sen loputtua tapahtuvat asiat.
	 * <p>
	 */
	@Override
	public void run(){
		alustukset(); // luodaan mm. ensimmäinen tapahtuma
		// moottorin sykli, käytetään visualisoinnin hidastamiseen
		int cycle = 0;
		while (simuloidaan()){
			if(lopeta) {
				resetoiSingletonit();
				return;
			}
			
			if (!skip) {
				try {
					Trace.out(Trace.Level.INFO, "Viive " + viive);
					sleep(viive);
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (pause) {
				synchronized(this) {
					while(pause) {						
						try {
							//Odotetaan 1 sekunti ja tarkastetaan haluaako käyttäjä lopettaa/aloittaa simuloinnin uudelleen
							wait(1000);
							if(lopeta) {
								resetoiSingletonit();
								return;
							}
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			realtimeData = laskeRealtimeData(palvelupisteet);
			kontrolleri.setRealtimeData(realtimeData);
			
			// Piirretään palvelupisteet harvemmin jos viive on nolla
			if (viive == 0) {
				cycle++;
				if (cycle % 100 == 0) {
					kontrolleri.naytaRealTimeData(kello.getAika(), st.getLapimeno() , st.getPalvellutAsiakkaat(), this.getPoydissa());
					cycle = 0;
				}
			} else {
				kontrolleri.naytaRealTimeData(kello.getAika(), st.getLapimeno() , st.getPalvellutAsiakkaat(), this.getPoydissa());
			}
			
			Trace.out(Trace.Level.INFO, "\nA-vaihe: kello on " + nykyaika());

			double muutos = nykyaika() - kello.getAika();
			for(Palvelupiste p : palvelupisteet) {
				p.paivitaAktiiviaika(muutos);
			}
			
			kello.setAika(nykyaika());
			
			Trace.out(Trace.Level.INFO, "\nB-vaihe:" );
			suoritaBTapahtumat();
			
			Trace.out(Trace.Level.INFO, "\nC-vaihe:" );
			yritaCTapahtumat();
			
			skip = false;
		}
		tulokset(simulointiaika);
		resetoiSingletonit();
	}
	/**
	 * Suorittaa Tapahtumalistasta seuraavan Tapahtuman, jos Kellon antama
	 * aika ja Tapahtumalistan ensimmäisen Tapahtuman aika vastaavat toisiaan
	 */
	private void suoritaBTapahtumat(){
		while (tapahtumalista.getSeuraavanAika() == kello.getAika()){
			suoritaTapahtuma(tapahtumalista.poista());
		}
	}
	/**
	 * Suorittaa C-Tapahtumat, jos Palvelupiste ei ole varattu ja
	 * joku on Palvelupisteen jonossa
	 */
	private void yritaCTapahtumat(){
		for (Palvelupiste p: palvelupisteet){
			if (!p.onVarattu() && p.onJonossa()){
				p.aloitaPalvelu();
			}
		}
	}
	/**
	 * Palauttaa Tapahtumalistan ensimmäisen Tapahtuman ajan
	 * 
	 * @return Tapahtumalistan ensimmäisen Tapahtuman aika
	 */
	private double nykyaika(){
		return tapahtumalista.getSeuraavanAika();
	}
	/**
	 * Tarkistaa, onko simulointiaika ohittanut Kellon ajan
	 * 
	 * @return true, jos Kellon aika on vähemmän kuin simulointiaika
	 */
	private boolean simuloidaan(){
		return kello.getAika() < simulointiaika;
	}

	@Override
	public String toString() {
		return String.format("Asetettu simulointiaika: %.2f, kellonaika: %.2f, palvelupisteiden määrä: %d", simulointiaika, kello.getAika(), palvelupisteet.length)+tapahtumalista;
	}

	protected abstract void alustukset(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
	protected abstract void suoritaTapahtuma(Tapahtuma t);  // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
	protected abstract void tulokset(double simulointiaika); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
	protected abstract int getPoydissa();
	
	protected abstract void resetoiSingletonit();
	
	protected abstract PpRealtimeData[] laskeRealtimeData(Palvelupiste[] palvelupisteet);

	/**
	 * Palauttaa Kellolta saadun kellonajan
	 * 
	 * @return Kello-instanssilta saatu aika
	 */
	public double getKellonaika() {
		return kello.getAika();
	}
	
	/**
	 * Pysäyttää simuloinnin tai jatkaa simulointia
	 */
	public void togglePause() {
		synchronized(this) {
			pause = pause ? false : true;
			if (!pause)
				notifyAll();
		}
		// Varmistaa, että visualisoija ja näyttö näyttävät samat arvot
		kontrolleri.naytaRealTimeData(kello.getAika(), st.getLapimeno() , st.getPalvellutAsiakkaat(), this.getPoydissa());
	}
	/**
	 * Palauttaa Palvelupisteistä tehdyn taulukon
	 * 
	 * @return palvelupisteet simulaatiossa käytettävistä Palvelupisteistä koottu taulukko
	 */
	public Palvelupiste[] getPalvelupisteet() {
		return palvelupisteet;
	}
	/**
	 * Lopettaa simuloinnin
	 */
	@Override
	public void lopeta() {
		Trace.out(Trace.Level.INFO, "Lopetus");
		lopeta = true;
	}
	
	@Override 
	public int getViive() {
		return this.viive;
	}
	
	@Override
	public void setViive(int viive) {
		this.viive = viive;
	}
}