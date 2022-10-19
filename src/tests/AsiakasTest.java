package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import simu.model.Asiakas;

class AsiakasTest {
	
	private Asiakas a;
	private final int ARRIVE = 10;
	private final int LEAVE = 20;
	
	@BeforeEach
	public void beforeAll() {
		Asiakas.alustaId(1);
		a = new Asiakas();
	}

	@Test
	public void yhtaikaTest() {
		a.setSaapumisaika(ARRIVE);
		a.setPoistumisaika(LEAVE);
		assertTrue(a.getYhtAika() == LEAVE - ARRIVE, "Yhtaika väärin");
	}
	
	@Test
	public void resetoiTest() {
		a.resetoi();
		assertTrue(a.getSaapumisaika() == 0 && a.getPoistumisaika() == 0, "Resetoi-metodi ei toimi");
	}
}
