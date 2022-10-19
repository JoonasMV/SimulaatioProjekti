package testi;
import simu.framework.*;
import simu.framework.Trace.Level;
import simu.model.*;

public class Simulaattori { //Tekstipohjainen

	public static void main(String[] args) {
		
		Trace.setTraceLevel(Level.INFO);
		IMoottori m = new TestiMoottori(null);
		m.setSimulointiaika(1000);
		Thread thread = (Thread) m;
		thread.start();
	}
}
