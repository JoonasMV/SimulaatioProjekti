package simu.framework;

/**
 * Kello on singleton-luokka, joka pitää yllä simulaattorin aikaa
 */
public class Kello {

	private double aika = 0;
	private static Kello instanssi;
	
	/**
	 * Asettaa kellonajaksi 0
	 */
	private Kello(){
		aika = 0;
	}
	
	/**
	 * Palauttaa Kello-luokan instanssin
	 * 
	 * @return instanssi Kellon instanssi
	 */
	public static Kello getInstance(){
		if (instanssi == null){
			instanssi = new Kello();	
		}
		return instanssi;
	}
	
	/**
	 * Asettaa Kellolle uuden ajan 
	 * 
	 * @param aika Kello-luokan muuttuja simulaatioajan laskemiseksi
	 */
	public void setAika(double aika){
		if(Double.isNaN(aika))
			throw new ArithmeticException("aika == NaN");
		
		this.aika = aika;
		
	}
	
	/**
	 * Palauttaa Kelloon asetetun ajan
	 * 
	 * @return aika Kello-luokan muuttuja simulaatioajan laskemiseksi
	 */
	public double getAika(){
		return aika;
	}
	
	/**
	 * Resetoi Kello-instanssin uudeksi Kelloksi
	 */
	public static void reset() {
		instanssi = new Kello();
	}
	
	
	@Override
	public String toString() {
		return String.format("Kulunut aika: %.2f",aika);
	}
}
