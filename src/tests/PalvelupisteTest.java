package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import simu.framework.*;
import simu.framework.Trace.Level;
import simu.model.*;
import eduni.distributions.*;
import java.util.HashMap;

class PalvelupisteTest {

	private Palvelupiste palvelupiste;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Trace.setTraceLevel(Level.TEST);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Asiakas.alustaId(1);
		Tapahtumalista.reset();
		palvelupiste = new Palvelupiste(new Normal(4, 2), TapahtumanTyyppi.TEST, "Test");
		palvelupiste.lisaaJonoon(new Asiakas());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Jonoon lisääminen")
	void lisaaJonoonTest() {
		Asiakas a = new Asiakas();
		palvelupiste.lisaaJonoon(a);
		assertEquals(2, palvelupiste.jononPituus(), "Jonoon lisääminen ei toimi");
	}
	
	@Test
	@DisplayName("Jonosta poistaminen")
	void otaJonostaTest() {
		Asiakas a = palvelupiste.otaJonosta();
		assertEquals(0, palvelupiste.jononPituus(), "Jonosta ottaminen ei toimi");
		assertEquals(1, a.getId(), "Palvelupiste palauttaa väärän asiakkaan");
	}
	
	@Test
	@DisplayName("Palvelun aloitus")
	void aloitaPalveluTest() {
		assertEquals(false, palvelupiste.onVarattu(), "Palvelupiste on varattu vaikka sen pitäisi olla vapaa");
		palvelupiste.aloitaPalvelu();
		assertEquals(true, palvelupiste.onVarattu(), "Palvelupiste ei ole varattu vaikka palvelu aloitettiin");
		Asiakas palveltava = palvelupiste.getPalveltava();
		assertEquals(palveltava.getPoistumisaika(), Tapahtumalista.getInstance().getSeuraavanAika(), "Tapahtumalistan päivittäminen tai asiakkaan poistumisajan asettaminen ei toimi");
	}
	
	@Test
	@DisplayName("Suureet ovat oikein")
	void getSuureetTest() {
		palvelupiste.aloitaPalvelu();
		palvelupiste.paivitaAktiiviaika(10);
		Kello.getInstance().setAika(10);
		Asiakas a = palvelupiste.otaJonosta();
		HashMap<String, Double> suureet = palvelupiste.getSuureet();
		assertEquals(10, suureet.get("aktiiviaika"), "Aktiiviaika väärin");
		assertEquals(1, suureet.get("saapuneetAsiakkaat"), "Saapuneet asiakkaat väärin");
		assertEquals(1, suureet.get("palvellutAsiakkaat"), "Palvellut asiakkaat väärin");
		assertEquals(a.getPoistumisaika(), suureet.get("kokonaisoleskeluaika"), "kokonaisoleskeluaika väärin");
		Tapahtumalista.getInstance().poista();
	}
	
	

}
