package simu.util;

public class MoottoriOptions {
	private double saapumisJakauma;
  	private double[] linjastoJakauma;
  	private double[] kassaJakauma;
  	private double[] leipaJakauma;
  	private double[] poytaJakauma;
  	private boolean ruuhkaMode;
  	
  	public MoottoriOptions() {
  		resetValues();
  	}

  	public void resetValues() {
  		saapumisJakauma = 1;
  	  	linjastoJakauma = new double[] {1, 0.25};
  	  	kassaJakauma = new double[] {1, 0.5};
  	  	leipaJakauma = new double[] {1, 1};
  	  	poytaJakauma = new double[] {15, 2};	
  	  	ruuhkaMode = true;
  	}
  	
  	public void setHidas() {
  		saapumisJakauma = 1;
  	  	linjastoJakauma = new double[] {2, 0.25};
  	  	kassaJakauma = new double[] {2, 0.5};
  	  	leipaJakauma = new double[] {2, 1};
  	  	poytaJakauma = new double[] {25, 5};	
  	}
  	
	public double getSaapumisJakauma() {
		return saapumisJakauma;
	}

	public void setSaapumisJakauma(double saapumisJakauma) {
		this.saapumisJakauma = saapumisJakauma;
	}

	public double[] getLinjastoJakauma() {
		return linjastoJakauma;
	}

	public void setLinjastoJakauma(double[] linjastoJakauma) {
		this.linjastoJakauma = linjastoJakauma;
	}

	public double[] getKassaJakauma() {
		return kassaJakauma;
	}

	public void setKassaJakauma(double[] kassaJakauma) {
		this.kassaJakauma = kassaJakauma;
	}

	public double[] getLeipaJakauma() {
		return leipaJakauma;
	}

	public void setLeipaJakauma(double[] leipaJakauma) {
		this.leipaJakauma = leipaJakauma;
	}

	public double[] getPoytaJakauma() {
		return poytaJakauma;
	}

	public void setPoytaJakauma(double[] poytaJakauma) {
		this.poytaJakauma = poytaJakauma;
	}
	
	public void setRuuhkaMode(boolean ruuhka) {
		this.ruuhkaMode = ruuhka;
	}
	
	public boolean getRuuhkaMode() {
		return ruuhkaMode;
	}
	
	public void setValmisSimulaatio(String simu) {
		switch (simu.toLowerCase()) {
		case "nopea":
			saapumisJakauma = 0.75;
	  	  	linjastoJakauma = new double[] {0.5, 0.125};
	  	  	kassaJakauma = new double[] {1, 0.25};
	  	  	leipaJakauma = new double[] {1, 0.5};
	  	  	poytaJakauma = new double[] {10, 2.5};
			break;
			
		case "hidas":
			saapumisJakauma = 3;
	  	  	linjastoJakauma = new double[] {3, 2};
	  	  	kassaJakauma = new double[] {3, 2};
	  	  	leipaJakauma = new double[] {2, 1};
	  	  	poytaJakauma = new double[] {30, 5};
			break;
			
		
		case "kallen keittiö":
			saapumisJakauma = 0.5;
	  	  	linjastoJakauma = new double[] {1, 0.25};
	  	  	kassaJakauma = new double[] {2, 0.5};
	  	  	leipaJakauma = new double[] {2, 1};
	  	  	poytaJakauma = new double[] {30, 5};
			break;
		}
		System.out.println(saapumisJakauma);
	}
}
