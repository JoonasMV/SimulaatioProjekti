package controller;


import simu.util.MoottoriOptions;
import simu.util.PpRealtimeData;

public interface IKontrolleriMtoC {
	public void naytaRealTimeData(double aika, double lapimeno, int asiakkaat, int poydissa);
	public void setRealtimeData(PpRealtimeData[] data);
	MoottoriOptions getMoottoriSettings();
}