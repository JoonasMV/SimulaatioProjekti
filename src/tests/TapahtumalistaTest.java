package tests;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.TapahtumanTyyppi;


class TapahtumalistaTest {

	private Tapahtumalista tl;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Trace.setTraceLevel(Level.TEST);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		tl = Tapahtumalista.getInstance();
		Tapahtuma tt = new Tapahtuma(TapahtumanTyyppi.RUOKA, 100.9);
		Tapahtuma t = new Tapahtuma(TapahtumanTyyppi.KASSA, 120.4);
		tl.lisaa(tt);
		tl.lisaa(t);
	}

	@AfterEach
	void tearDown() throws Exception {
		Tapahtumalista.reset();
	}

	
	@Test
	@DisplayName("Listaan lisääminen")
	void testLisaaListaan() throws Exception{
		Tapahtuma t = new Tapahtuma(TapahtumanTyyppi.ARR1, 68.9);
		tl.lisaa(t);
		assertEquals(3 , tl.tapahtumienMaara(), "Lisääminen ei onnistu");
	}

	@Test
	@DisplayName("Listasta poistaminen")
	void testPoistaListasta() throws Exception{
		tl.poista();
		assertEquals(1 , tl.tapahtumienMaara(), "Poistaminen ei onnistu");
	}
	
	@Test
	@DisplayName("Seuraavan ajan saaminen")
	void testGetSeuraavanAika() throws Exception{
		assertEquals(100.9, tl.getSeuraavanAika(), "Ajan saaminen ei onnistu");
	}
	
	
	@Test
	@DisplayName("Tapahtumalistan resetoiminen")
	void testaaResetoiminen() throws Exception{
		Tapahtumalista.reset();
		tl = Tapahtumalista.getInstance();
		assertEquals(0, tl.tapahtumienMaara(), "Ajan saaminen ei onnistu");
	}

}

