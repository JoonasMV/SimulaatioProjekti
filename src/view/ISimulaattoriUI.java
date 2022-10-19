package view;

import java.util.Map;

import simu.util.MoottoriOptions;
import view.visualisointi.IVisualisoija;

public interface ISimulaattoriUI {
	void sammuta();
	double getSimulaationPituus() throws NumberFormatException;
	int getLinjastot() throws NumberFormatException;
	int getKassat() throws NumberFormatException;
	int getLeipapoytaKapasiteetti() throws NumberFormatException;
	int getRuokapoytaKapasiteetti() throws NumberFormatException;
	int getIpKassat() throws NumberFormatException;
	void setTamanhetkisetArvot(double aika, int asiakkaat, int poydissa, double keskiarvo);
	void showNumAlert();
	IVisualisoija[] getMasterVisu();
	MoottoriOptions getMoottoriSettings();
	int getViive();

}
