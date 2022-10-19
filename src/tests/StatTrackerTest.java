package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Ajo;
import entity.PalvelupisteTiedot;

import simu.model.StatTracker;
import simu.model.Palvelupiste;

class StatTrackerTest {
	
	private StatTracker st = StatTracker.getInstance();
	private final double kellonaika = 10;
	private final double aktiiviaika = 5;
	private final double palvellutAsiakkaat = 1;
	private final double kokonaisoleskeluaika = 5;
	
	private Palvelupiste p;
	
	@BeforeEach
	public void beforeEach() {
		p = new Palvelupiste(palvellutAsiakkaat, aktiiviaika, kokonaisoleskeluaika, kellonaika, "testi");
	}
	
	@Test
	void suureidenLaskenta() {
		List<PalvelupisteTiedot> data = st.getTiedot(new Palvelupiste[]  { p }, new Ajo());
		boolean dataOikein = true;
		double[] vastaukset = {0.5, 0.1, 5.0, 5.0, 0.5};
		List<Double> suureet = data.get(0).getSuureet();
		for(int i = 0; i < suureet.size(); i++) {
			if(suureet.get(i) != vastaukset[i])
				dataOikein = false;
		}
		assertTrue(dataOikein, "Suureiden laskenta on väärin");
	}

}
