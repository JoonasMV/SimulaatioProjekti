package view.ikkunat.ajoData;


import java.util.List;

import entity.Asetukset;
import entity.PalvelupisteTiedot;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PptietoKontrolleri {

	public PptietoKontrolleri() {
		
	}
	
	@FXML
	private Text txt;
	
	@FXML
	public void Initialize() {
		
	}
	
	/**
     * Asettaa luokalle MainApp:n
     * 
     * @param MainApp mainApp
     */
	public void setContetnts(List<PalvelupisteTiedot> lista, Asetukset asetukset) {
		String text = "";
		for(PalvelupisteTiedot t : lista) {
			text += t.toString();
			text += "\n";
		}
		text += "\n\n";
		text += asetukset.toString();
		txt.setText(text);;
	}
}
