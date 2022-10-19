package simu.framework;

/**
 * Trace-luokkaa käytetään muiden luokkien tulosteiden keräämiseen ja tulostamiseen
 *
 */
public class Trace {

	public enum Level{INFO, WAR, ERR, TEST}
	
	private static Level traceLevel;
	
	/**
	 * Asettaa Tracen tason
	 * 
	 * @param lvl annettu taso
	 */
	public static void setTraceLevel(Level lvl){
		traceLevel = lvl;
	}
	
	/**
	 * Tulostaa annetun tekstin, jos taso on sama kuin asetettu taso
	 * 
	 * @param lvl annettu taso	
	 * @param txt annettu teksti tulostusta varten
	 */
	public static void out(Level lvl, String txt){
		if(traceLevel == null) {
			System.err.println("TRACE LEVEL NOT SET, DEFAULTING TO LEVEL.INFO");
			traceLevel = Level.INFO;
		}
			

		
		if (lvl.ordinal() >= traceLevel.ordinal()){
			System.out.println(txt);
		}
	}
	
	@Override
	public String toString() {
		return "Tracelevel: "+traceLevel;
	}
	
	
}