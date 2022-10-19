package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import simu.framework.IMoottori;
import simu.model.TestiMoottori;

class MoottoriTest{

	private IMoottori moottori;

	
	@BeforeEach
	public void beforeEach() throws Exception{
		moottori = new TestiMoottori(null);
		
	}
	
	
	@Test
	@DisplayName("Jakaumien päivitys")
	public void initJakaumaTest() {
		
		   	
	}
	

}