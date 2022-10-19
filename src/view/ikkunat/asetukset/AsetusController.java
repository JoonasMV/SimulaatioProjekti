package view.ikkunat.asetukset;

import simu.util.MoottoriOptions;
import view.visualisointi.VisuOptions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AsetusController {

	@FXML TextField jonottajaField;
	@FXML RadioButton ruuhkaButton;
	@FXML ComboBox<String> simulaatioBox;
	
	private Stage dialogStage;
	private VisuOptions visuSettings;
	private MoottoriOptions moottoriSettings;
	
	/**
	 * Alustaa lisäasetuks ikkunan
	 * @param dialogStage Ikkunan sisältö
	 */
  	public void initStage(Stage dialogStage, VisuOptions visuSettings, MoottoriOptions moottoriSettings) {
        this.dialogStage = dialogStage;
        this.visuSettings = visuSettings;
        this.moottoriSettings = moottoriSettings;
        setPrevValues();
        initSimuBox();
    }
  	
  	private void setPrevValues() {
  		jonottajaField.setText(Integer.toString(visuSettings.getJononpituus()));
  		ruuhkaButton.setSelected(moottoriSettings.getRuuhkaMode());
  		if (ruuhkaButton.isSelected()) ruuhkaButton.setText("Päällä");
  	}
    
    /**
     * Käsittelee sateenkaari moden napin
     * @param click Napin painallus
     */
	public void handleRuuhka(ActionEvent click) {
		String onOff = ruuhkaButton.isSelected() ? "Päällä" : "Pois";
		ruuhkaButton.setText(onOff);
	}
	
	/**
	 * Tarkastaa onko jononpituus int ja asettaa arvon
	 */
	private void handleJononpituus() throws NumberFormatException {
		visuSettings.setJononpituus(Integer.parseInt(jonottajaField.getText()));
	}
	
	/**
	 * OK napin käsittelijä, tallentaa asetukset
	 */
	public void handleOK() {
		try {
			handleJononpituus();
			moottoriSettings.setRuuhkaMode(ruuhkaButton.isSelected());
			dialogStage.close();
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Virheellinen syöte");
            alert.setHeaderText("Tarkista syöte ja kokeile uudelleen");
            
            alert.showAndWait();
		}
	}
	
	/**
	 * Cancel napin käsittelijä, hylkää muutetut asetukset
	 */
	public void handleCancel() {
		dialogStage.close();
	}
	
	public void getSimuChoice(ActionEvent event) {
		moottoriSettings.setValmisSimulaatio(simulaatioBox.getValue());
	}
	
	private void initSimuBox() {
		simulaatioBox.setItems(FXCollections.observableArrayList("Nopea", "Hidas", "Kallen keittiö"));
	}
	
}
