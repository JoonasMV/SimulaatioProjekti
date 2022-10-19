package simu.framework;

import java.util.PriorityQueue;

/**
 * Tapahtumalista on prioriteettilista, jonne lisätään ja josta poistetaan Tapapahtuma-olioita
 *
 */

public class Tapahtumalista {
	
	private static Tapahtumalista INSTANCE = null;
	private PriorityQueue<Tapahtuma> lista = new PriorityQueue<Tapahtuma>();
	
	/**
	 * Tapahtumalistan tyhjä konstruktori
	 */
	private Tapahtumalista(){
	}
	
	/**
	 * Palauttaa Tapahtumalista-luokan instanssin
	 * 
	 * @return INSTANCE Tapahtumalista-luokan instanssi
	 */
	public static Tapahtumalista getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Tapahtumalista();
		}
		return INSTANCE;
	}
	
	/**
	 * Poistaa Tapahtuman Tapahtumalistasta
	 * 
	 * @return palauttaa prioriteettilistasta poistetun olion
	 */
	public Tapahtuma poista(){
		Trace.out(Trace.Level.INFO,"Tapahtumalistasta poisto " + lista.peek().getTyyppi() + " " + lista.peek().getAika() );
		return lista.remove();
	}
	
	/**
	 * Lisää saadun Tapahtuman PriorityQueue-listaan
	 * 
	 * @param t listaan lisättävä Tapahtuma-olio
	 */
	public void lisaa(Tapahtuma t){
		Trace.out(Trace.Level.INFO,"Tapahtumalistaan lisätään uusi " + t.getTyyppi() + " " + t.getAika());
		lista.add(t);
	}
	
	/**
	 * Saa seuraavan Tapahtuman ajan
	 * 
	 * @return Tapahtumalistasta selvitetty seuraavan Tapahtuman aika
	 */
	public double getSeuraavanAika(){
		return lista.peek().getAika();
	}
	
	/**
	 * Palauttaa Tapahtumalistassa olevien tapahtumien määrän
	 * 
	 * @return Tapahtumalistan tapahtumien määrä
	 */
	public int tapahtumienMaara() {
		return lista.size();
	}
	
	/**
	 * Alustaa Tapahtumalistan instanssin uudeksi Tapahtumalista-olioksi
	 */
	public static void reset() {
		INSTANCE = new Tapahtumalista();
	}

	@Override
	public String toString() {
		return String.format("Tapahtumalista: %d-tapahtumaa jonossa", lista.size());
	}
	
}
