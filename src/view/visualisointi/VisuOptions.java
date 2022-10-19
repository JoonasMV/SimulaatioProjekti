package view.visualisointi;

import javafx.scene.paint.Color;

public class VisuOptions {
	private int jononpituus = 10; //Jonon default pituus
	private int linjastot;
	private int kassat;
	private int ipKassat;
	private int leipaJono;
	private int ruokaPoydat;
	
	public VisuOptions() {
	}
	
	/***
	 * @param linjastot Linjastojen määrä
	 */
	public void setLinjastot(int linjastot) {
		this.linjastot = linjastot;
	}

	/***
	 * @param kassat Kassojen määrä
	 */
	public void setKassat(int kassat) {
		this.kassat = kassat;
	}
	
	/***
	 * @param ipKassat Itsepalvelukassojen määrä
	 */
	public void setIpKassat(int ipKassat) {
		this.ipKassat = ipKassat;
	}
	
	/**
	 * @param Piirrettävän jonon pituus
	 */
	public void setJononpituus(int jono) {
		this.jononpituus = jono;
	}
	
	/**
	 * @return Piirrettävän jonon pituus
	 */
	public int getJononpituus() {
		return this.jononpituus;
	}
	
	/***
	 *@return Kertoo leipäjonon kapasiteetin
	 */
	public int getLeipaJono() {
		return leipaJono;
	}

	/**
	 * @param leipaJono leipäpöydän kapasitetti
	 */
	public void setLeipaJono(int leipaJono) {
		this.leipaJono = leipaJono;
	}

	/***
	 *@return Kertoo ruokalan kapasiteetin
	 */
	public int getRuokaPoydat() {
		return ruokaPoydat;
	}

	/**
	 * @param ruokaPoydat Ruokalan kapasitetti
	 */
	public void setRuokaPoydat(int ruokaPoydat) {
		this.ruokaPoydat = ruokaPoydat;
	}

	/**
	 * Asettaa halutut asetukset visualisoijille
	 * @param masterVisu Lista, joka sisältää jokaisen visualisoijan
	 */
	public void setSettings(IVisualisoija[] masterVisu) {
		for(IVisualisoija v: masterVisu) {
			v.setJononpituus(jononpituus);
		}
		
		//Asettaa vihreän sävyn eri tyyppisille palvelupisteille
		int j = 0;
		for (int i = j; i < linjastot; i++) {
			masterVisu[i].setVari(Color.rgb(50,205,50));
		}
		j += linjastot;
		for (int i = j; i < j+kassat; i++) {
			masterVisu[i].setVari(Color.rgb(0,191,255));
		}
		j += kassat;
		for (int i = j; i < j + ipKassat; i++) {
			masterVisu[i].setVari(Color.rgb(255, 140,0));
		}
		//Leipäpöydän ja ruokapöytien värit
		masterVisu[masterVisu.length - 2].setVari(Color.rgb(148,0, 211));
		masterVisu[masterVisu.length - 2].setJononpituus(leipaJono);
		masterVisu[masterVisu.length - 1].setVari(Color.rgb(255,0,0));
		masterVisu[masterVisu.length - 1].setJononpituus(ruokaPoydat);
	}
}
