package simu.framework;

public interface IMoottori {
	public void setSimulointiaika(double aika);
	public void setViive(int viive);
	public int getViive();
	public void setLinjastot(int maara);
	public void setKassat(int maara);
	public void setIpkassat(int maara);
	public void setLeipapoytaKapasiteetti(int kapasiteetti);
	public void setRuokapoytaKapasiteetti(int kapasiteetti);
	public void togglePause();
	public void lopeta();
	void updateSettings();
}
