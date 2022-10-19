package entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * Luokka jota käytetään suureiden tallentamisessa tietokantaan
 */
@Entity
@Table(name="palvelupiste")
@NamedQueries({
	@NamedQuery(name="Palvelupiste.getAllFromAjo", query="SELECT p FROM PalvelupisteTiedot p WHERE ajo = ?1"),
	@NamedQuery(name="Palvelupiste.getAll", query="SELECT p FROM PalvelupisteTiedot p")
})
public class PalvelupisteTiedot {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String nimi;
	private double kayttoaste;
	private double suoritusteho;
	private double avgpalveluaika;
	private double avglapimenoaika;
	private double avgjononpituus;
	
	@ManyToOne
	private Ajo ajo;

	public PalvelupisteTiedot() {
	}
	
	/**
	 * @param builder PpDataBuilder-olio
	 */
	public PalvelupisteTiedot(PpTiedotBuilder builder) {
		this.nimi = builder.nimi;
		this.kayttoaste = builder.kayttoaste;
		this.suoritusteho = builder.suoritusteho;
		this.avgpalveluaika = builder.avgpalveluaika;
		this.avglapimenoaika = builder.avglapimenoaika;
		this.avgjononpituus = builder.avgjononpituus;
		this.ajo = builder.ajo;
	}
	
	public int getId() {
		return id;
	}
	
	public double getKayttoaste() {
		return kayttoaste;
	}

	public void setKayttoaste(double kayttoaste) {
		this.kayttoaste = kayttoaste;
	}

	public double getSuoritusteho() {
		return suoritusteho;
	}

	public void setSuoritusteho(double suoritusteho) {
		this.suoritusteho = suoritusteho;
	}

	public double getAvgpalveluaika() {
		return avgpalveluaika;
	}

	public void setAvgpalveluaika(double avgpalveluaika) {
		this.avgpalveluaika = avgpalveluaika;
	}

	public double getAvglapimenoaika() {
		return avglapimenoaika;
	}

	public void setAvglapimenoaika(double avglapimenoaika) {
		this.avglapimenoaika = avglapimenoaika;
	}

	public double getAvgjononpituus() {
		return avgjononpituus;
	}

	public void setAvgjononpituus(double avgjononpituus) {
		this.avgjononpituus = avgjononpituus;
	}
	
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	
	public String getNimi() {
		return nimi;
	}
	
	public Ajo getAjo() {
		return this.ajo;
	}
	
	public void setAjo(Ajo ajo) {
		this.ajo = ajo;
	}
	
	/***
	 * Suureet ovat järjestyksessä: käyttöaste, suoritusteho, avg palveluaika,
	 * läpimenoaika ja avg jonon pituus.
	 * 
	 * @return suureet listassa
	 */
	public List<Double> getSuureet(){
		List<Double> suureet = new ArrayList<Double>();
		suureet.add(kayttoaste);
		suureet.add(suoritusteho);
		suureet.add(avgpalveluaika);
		suureet.add(avglapimenoaika);
		suureet.add(avgjononpituus);
		return suureet;
	}

	@Override
	public String toString() {
		return String.format("Palvelupiste: %s, kayttoaste: %.2f, suoritusteho: %.2f, avgpalveluaika: %.2f, avglapimenoaika: %.2f, avgjononpituus: %.2f"
				, nimi, kayttoaste, suoritusteho, avgpalveluaika, avglapimenoaika, avgjononpituus);
				
	}
	
	/**
	 * Builder-luokka
	 */
	public static class PpTiedotBuilder{
		
		private String nimi;
		private double kayttoaste;
		private double suoritusteho;
		private double avgpalveluaika;
		private double avglapimenoaika;
		private double avgjononpituus;
		private Ajo ajo;
				
		public PpTiedotBuilder(String nimi) {
			this.nimi = nimi;
		}

		public PpTiedotBuilder kayttoaste(double kayttoaste) {
			this.kayttoaste = kayttoaste;
			return this;
		}
		public PpTiedotBuilder suoritusteho(double suoritusteho) {
			this.suoritusteho = suoritusteho;
			return this;
		}
		public PpTiedotBuilder avgpalveluaika(double avgpalveluaika) {
			this.avgpalveluaika = avgpalveluaika;
			return this;
		}
		public PpTiedotBuilder avglapimenoaika(double avglapimenoaika) {
			this.avglapimenoaika = avglapimenoaika;
			return this;
		}
		public PpTiedotBuilder avgjononpituus(double avgjononpituus) {
			this.avgjononpituus = avgjononpituus;
			return this;
		}
		public PpTiedotBuilder ajo(Ajo ajo) {
			this.ajo = ajo;
			return this;
		}
		public PalvelupisteTiedot build() {
			return new PalvelupisteTiedot(this);
		}
	}
}
