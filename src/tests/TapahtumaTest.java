package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import simu.framework.Tapahtuma;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.TapahtumanTyyppi;

class TapahtumaTest {
	
	private static Tapahtuma t;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Trace.setTraceLevel(Level.TEST);
		t = new Tapahtuma(TapahtumanTyyppi.LEIPA, 4.6);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testVertaaminen() {
		Tapahtuma testi = new Tapahtuma(TapahtumanTyyppi.LEIPA, 4.5);
		assertEquals(-1, testi.compareTo(t), "Vertaaminen ei onnistunut");
	}

}
