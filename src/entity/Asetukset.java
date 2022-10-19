package entity;

import jakarta.persistence.*;

@Entity
@Table(name="asetukset")
@NamedQueries({
	@NamedQuery(name="Asetukset.getAjonPerusteella", query="SELECT a FROM Asetukset a WHERE ajo = ?1")
})
public class Asetukset {
	
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int linjastoLkm;
	private int kassaLkm;
	private int ipkassaLkm;
	private int leipaKapasiteetti;
	private int rkpKapasiteetti;
	//Jakaumat
	private double saapumisMean;
	private double linjastoMean;
	private double kassaMean;
	private double kassaVariance;
	private double leipaMean;
	private double leipaVariance;
	private double rkpMean;
	private double rkpVariance;
	
	@OneToOne
	private Ajo ajo;

	public Asetukset() {
	}
	
	
	public Asetukset(AsetuksetBuilder builder) {
		this.linjastoLkm = builder.linjastoLkm;
		this.kassaLkm = builder.kassaLkm;
		this.ipkassaLkm = builder.ipkassaLkm;
		this.leipaKapasiteetti = builder.leipaKapasiteetti;
		this.rkpKapasiteetti = builder.rkpKapasiteetti;
		this.saapumisMean = builder.saapumisMean;
		this.linjastoMean = builder.linjastoMean;
		this.kassaMean = builder.kassaMean;
		this.kassaVariance = builder.kassaVariance;
		this.leipaMean = builder.leipaMean;
		this.leipaVariance = builder.leipaVariance;
		this.rkpMean = builder.rkpMean;
		this.rkpVariance = builder.rkpVariance;
		this.ajo = builder.ajo;
	}
	
	public int getLinjastoLkm() {
		return linjastoLkm;
	}


	public void setLinjastoLkm(int linjastoLkm) {
		this.linjastoLkm = linjastoLkm;
	}


	public int getKassaLkm() {
		return kassaLkm;
	}


	public void setKassaLkm(int kassaLkm) {
		this.kassaLkm = kassaLkm;
	}


	public int getIpkassaLkm() {
		return ipkassaLkm;
	}


	public void setIpkassaLkm(int ipkassaLkm) {
		this.ipkassaLkm = ipkassaLkm;
	}


	public int getLeipaKapasiteetti() {
		return leipaKapasiteetti;
	}


	public void setLeipaKapasiteetti(int leipaKapasiteetti) {
		this.leipaKapasiteetti = leipaKapasiteetti;
	}


	public int getRkpKapasiteetti() {
		return rkpKapasiteetti;
	}


	public void setRkpKapasiteetti(int rkpKapasiteetti) {
		this.rkpKapasiteetti = rkpKapasiteetti;
	}


	public double getSaapumisMean() {
		return saapumisMean;
	}


	public void setSaapumisMean(double saapumisMean) {
		this.saapumisMean = saapumisMean;
	}


	public double getLinjastoMean() {
		return linjastoMean;
	}


	public void setLinjastoMean(double linjastoMean) {
		this.linjastoMean = linjastoMean;
	}


	public double getKassaMean() {
		return kassaMean;
	}


	public void setKassaMean(double kassaMean) {
		this.kassaMean = kassaMean;
	}


	public double getKassaVariance() {
		return kassaVariance;
	}


	public void setKassaVariance(double kassaVariance) {
		this.kassaVariance = kassaVariance;
	}


	public double getLeipaMean() {
		return leipaMean;
	}


	public void setLeipaMean(double leipaMean) {
		this.leipaMean = leipaMean;
	}


	public double getLeipaVariance() {
		return leipaVariance;
	}


	public void setLeipaVariance(double leipaVariance) {
		this.leipaVariance = leipaVariance;
	}


	public double getRkpMean() {
		return rkpMean;
	}


	public void setRkpMean(double rkpMean) {
		this.rkpMean = rkpMean;
	}


	public double getRkpVariance() {
		return rkpVariance;
	}


	public void setRkpVariance(double rkpVariance) {
		this.rkpVariance = rkpVariance;
	}


	public int getId() {
		return id;
	}


	public Ajo getAjo() {
		return ajo;
	}
	
	@Override
	public String toString() {
		return "Ajon asetukset:\n" + "linjastoLkm: " + linjastoLkm + ", kassaLkm: " + kassaLkm + ", ipkassaLkm: "
				+ ipkassaLkm + "\nleipaKapasiteetti: " + leipaKapasiteetti + ", rkpKapasiteetti: " + rkpKapasiteetti
				+ ", saapumisodotusarvo: " + saapumisMean + "\nlinjastoodotusarvo: " + linjastoMean + ", kassaodotusarvo: " + kassaMean
				+ ", leipaodotusarvo: " + leipaMean + ", rkpodotusarvo: " + rkpMean + "\nkassavarianssi: " + kassaVariance  
				+ ", leipavarianssi: " + leipaVariance + ", rkpvarianssi: " + rkpVariance;
	}

	public static class AsetuksetBuilder{
		
		private int linjastoLkm;
		private int kassaLkm;
		private int ipkassaLkm;
		private int leipaKapasiteetti;
		private int rkpKapasiteetti;
		//Jakaumat
		private double saapumisMean;
		private double linjastoMean;
		private double kassaMean;
		private double kassaVariance;
		private double leipaMean;
		private double leipaVariance;
		private double rkpMean;
		private double rkpVariance;
		
		private Ajo ajo;
		
		public AsetuksetBuilder(int linjastoLkm, int kassaLkm, int ipkassaLkm, Ajo ajo) {
			this.linjastoLkm = linjastoLkm;
			this.kassaLkm = kassaLkm;
			this.ipkassaLkm = ipkassaLkm;
			this.ajo = ajo;
		}
		public AsetuksetBuilder leipaKapasiteetti(int kapasiteetti) {
			this.leipaKapasiteetti = kapasiteetti;
			return this;
		}
		public AsetuksetBuilder rkpKapasiteetti(int kapasiteetti) {
			this.rkpKapasiteetti = kapasiteetti;
			return this;
		}
		public AsetuksetBuilder saapumisprosessi(double saapumisMean) {
			this.saapumisMean = saapumisMean;
			return this;
		}
		public AsetuksetBuilder linjastot(double[] tiedot) {
			this.linjastoMean = tiedot[0];
			return this;
		}
		public AsetuksetBuilder kassat(double[] tiedot) {
			this.kassaMean = tiedot[0];
			this.kassaVariance = tiedot[1];
			return this;
		}
		public AsetuksetBuilder leipapiste(double[] tiedot) {
			this.leipaMean = tiedot[0];
			this.leipaVariance = tiedot[1];
			return this;
		}
		public AsetuksetBuilder ruokapoydat(double[] tiedot) {
			this.rkpMean = tiedot[0];
			this.rkpVariance = tiedot[1];
			return this;
		}
		public Asetukset build() {
			return new Asetukset(this);
		}
		
		
		
	}
}
