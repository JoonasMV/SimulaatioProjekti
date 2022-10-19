package entity;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name="ajot")
@NamedQueries({
	@NamedQuery(name="Ajot.getAllAjot", query="SELECT a FROM Ajo a")
})
public class Ajo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Column(name="palvellutAsiakkaat")
	private int palvellutAsiakkaat;
	@Column(name="avgAikaRuokapoydille")
	private double avgAikaRuokapoydille;
	@Column(name="aika")
	private double aika;
	
	public Ajo() {
	}
	
	public Ajo(Date date, int palvellutAsiakkaat, double aika) {
		this.date = date;
		this.palvellutAsiakkaat = palvellutAsiakkaat;
		this.aika = aika;
	}

	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDate(long date) {
		this.date = new Date(date);
	}

	public int getPalvellutAsiakkaat() {
		return palvellutAsiakkaat;
	}

	public void setPalvellutAsiakkaat(int palvellutAsiakkaat) {
		this.palvellutAsiakkaat = palvellutAsiakkaat;
	}

	public double getAika() {
		return aika;
	}

	public void setAika(double aika) {
		this.aika = aika;
	}

	public int getId() {
		return id;
	}

	public double getAvgAikaRuokapoydille() {
		return avgAikaRuokapoydille;
	}

	public void setAvgAikaRuokapoydille(double avgAikaRuokapoydille) {
		this.avgAikaRuokapoydille = avgAikaRuokapoydille;
	}
	
}
