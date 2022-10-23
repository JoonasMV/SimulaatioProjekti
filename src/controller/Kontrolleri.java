package controller;


import javafx.application.Platform;
import view.ISimulaattoriUI;
import view.SimulaattoriGUI;
import view.visualisointi.IVisualisoija;
import simu.framework.IMoottori;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.TestiMoottori;
import simu.util.MoottoriOptions;
import simu.util.PpRealtimeData;

/**
 * Kontrolleri vastaa ohjelman toiminnan ohjaamisesta
 */

public class Kontrolleri implements IKontrolleriMtoC, IKontrolleriVtoC {

	private ISimulaattoriUI ui;
	private IMoottori moottori;
	private IVisualisoija[] masterVisu;
	
	/**
	 * Kontrolleri konstruktori
	 * 
	 * @param ISimulaattoriUI näkymäluokka
	 */
	
	public Kontrolleri(ISimulaattoriUI ui) {
		this.ui = ui;
	}

	/**
	 * Luo uuden Moottorin, alustaa tarvittavat luokat ja käynnistää Moottorin
	 * 
	 */
	
	@Override
	public void kaynnistaSimulointi() {
		if(moottori != null) {
			moottori.lopeta();
			try {
				((Thread) moottori).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		Trace.setTraceLevel(Level.ERR);
		this.masterVisu = ui.getMasterVisu();
		moottori = new TestiMoottori(this);
		moottori.setViive(ui.getViive());
		try {
			alustaPalvelupisteet();
			moottori.setSimulointiaika(ui.getSimulaationPituus());
			
			((Thread) moottori).start();
		}catch(NumberFormatException e) {
			ui.showNumAlert();
		}
		
		
	}
	
	/**
	 * Sammuttaa simulaatiosäikeen hallitusti
	 */
	
	@Override
	public void sammutaSimulaatio() {
		if(moottori != null) {
			moottori.lopeta();
			try {
				((Thread) moottori).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Asettaa palvelupisteille halutut lukumäärät ja jakaumien parametrit
	 */
	
	@Override
	public void alustaPalvelupisteet(){
		/*if (!ui.isDefault()) {
			moottori.setJakaumat(ui.getJakaumat());
		} else {
			moottori.setDefaultjakaumat();
		}*/
		//moottori.setJakaumat(null);
		moottori.setLinjastot(ui.getLinjastot());
		moottori.setKassat(ui.getKassat());
		moottori.setIpkassat(ui.getIpKassat());
		moottori.setLeipapoytaKapasiteetti(ui.getLeipapoytaKapasiteetti());
		moottori.setRuokapoytaKapasiteetti(ui.getRuokapoytaKapasiteetti());
	}

	/**
	 * Lähettää Visualisoija oliolle reaaliaikaista dataa tulostettavaksi
	 * 
	 * @param data reaaliaikainen data (PpRealtimeData)
	 */
	
	@Override
	public void setRealtimeData(PpRealtimeData[] data) {
		for (int i = 0; i < data.length; i++) {
			masterVisu[i].setRealtimeData(data[i]);
		}
	}
	
	/**
	 * Pysäyttää tai jatkaa simulointia
	 */
	
	@Override
	public void togglePause() {
		if(moottori == null)
			return;
		
		moottori.togglePause();
		piirra();
	}

	/**
	 * Lähettää käyttöliittymälle reaaliaikaista dataa tulostettavaksi näytölle
	 * 
	 * @param aika tämänhetkinen aika (double)
	 * @param lapimeno koko systeemin kaikkien asiakkaiden läpimenoaikojen summa (double)
	 * @param asiakkaat systeemissä palvellut asiakkaat
	 * @param poydissa tällä hetkellä ruokapöydissa palveltavana olevien määrä (int)
	 */
	
	@Override
	public void naytaRealTimeData(double aika, double lapimeno, int asiakkaat, int poydissa) {
		double keskiarvo;
		if(asiakkaat == 0) {
			keskiarvo = 0;
		} else {
			keskiarvo = lapimeno / asiakkaat;
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ui.setTamanhetkisetArvot(aika, asiakkaat, poydissa, keskiarvo);
				piirra();
			}
		});
	}
	
	private void piirra() {
		for(int i = 0; i< masterVisu.length; i++) {
			masterVisu[i].piirra();
		}
	}
	
	/**
	 * Muuttaa viivettä
	 * @param viive määrä jolla viivettä muutetaan (ms)
	 * @return uusiViive viiveen uusi arvo (ms)
	 */
	
	@Override
	public int muutaViive(int viive) {
		int uusiViive = 0;
		if(moottori == null) {
			if(ui.getViive() + viive >= 0)
				return ui.getViive() + viive;
			else
				return 0;
		}
		
		uusiViive = moottori.getViive() + viive;
		if (uusiViive < 0) uusiViive = 0;
		moottori.setViive(uusiViive);
		return uusiViive;
	}
	
	/**
	 * Hakee moottorin viiveen
	 * 
	 * @return viive(ms)
	 */

	public int saaViive() {
		return moottori.getViive();
	}
	
	@Override
	public MoottoriOptions getMoottoriSettings() {
		return ui.getMoottoriSettings();
	}
}
