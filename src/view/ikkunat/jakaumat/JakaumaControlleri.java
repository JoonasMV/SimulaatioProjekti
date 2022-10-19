package view.ikkunat.jakaumat;

import simu.util.MoottoriOptions;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class JakaumaControlleri {

    @FXML
    private TextField saapumisField;
    @FXML
    private TextField linjastoField1;
    @FXML
    private TextField linjastoField2;
    @FXML
    private TextField kassaField1;
    @FXML
    private TextField kassaField2;
    @FXML
    private TextField leipapoytaField1;
    @FXML
    private TextField poytaField1;
    @FXML
    private TextField leipapoytaField2;
    @FXML
    private TextField poytaField2;

    private MoottoriOptions jakaumaSettings;
  	
    private Stage dialogStage;
    
    /**
     * Alustaa ikkunan
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage, MoottoriOptions jakaumaSettings) {
        this.dialogStage = dialogStage;
        this.jakaumaSettings = jakaumaSettings;
        setPrevValues();
    }
    
    private void setPrevValues() {
    	saapumisField.setText(Double.toString(jakaumaSettings.getSaapumisJakauma()));
    	linjastoField1.setText(Double.toString(jakaumaSettings.getLinjastoJakauma()[0]));
    	linjastoField2.setText(Double.toString(jakaumaSettings.getLinjastoJakauma()[1]));
    	kassaField1.setText(Double.toString(jakaumaSettings.getKassaJakauma()[0]));
    	kassaField2.setText(Double.toString(jakaumaSettings.getKassaJakauma()[1]));
    	leipapoytaField1.setText(Double.toString(jakaumaSettings.getLeipaJakauma()[0]));
    	leipapoytaField2.setText(Double.toString(jakaumaSettings.getLeipaJakauma()[1]));
    	poytaField1.setText(Double.toString(jakaumaSettings.getPoytaJakauma()[0]));
    	poytaField2.setText(Double.toString(jakaumaSettings.getPoytaJakauma()[1]));
    }
    
    /**
     * Asettaa jakaumien arvot käyttäjän syöttämiin arvoihin
     * 
     */
    public void initJakaumat() {
    	try {
    		jakaumaSettings.setSaapumisJakauma(Double.parseDouble(saapumisField.getText()));
    		jakaumaSettings.setLinjastoJakauma(new double[] {Double.parseDouble(linjastoField1.getText()), Double.parseDouble(linjastoField2.getText())});
    		jakaumaSettings.setKassaJakauma(new double[] {Double.parseDouble(kassaField1.getText()), Double.parseDouble(kassaField2.getText())});
    		jakaumaSettings.setLeipaJakauma(new double[] {Double.parseDouble(leipapoytaField1.getText()), Double.parseDouble(leipapoytaField2.getText())});
    		jakaumaSettings.setPoytaJakauma(new double[] {Double.parseDouble(poytaField1.getText()), Double.parseDouble(poytaField2.getText())});
    	}catch (NumberFormatException e) {
    		showAlert();
    	}
    }
   
    /**
     * Suoritetaan kun painetaan ok.
     */
    @FXML
    private void handleOk() {
    	try {
    		initJakaumat();
    	} catch (Exception e) {
    		showAlert();
    	}

    	dialogStage.close();
    }
    
    /**
     * Suoritetaan kun painetaan cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Suoritetaan kun painetaan defaults.
     */
    @FXML
    private void handleDefaults() {
    	jakaumaSettings.resetValues();
      	dialogStage.close();
    }
    
    private void showAlert() {
    	Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Vääriä syötteitä");
        alert.setHeaderText("Korjaa syötteet ja kokeile uudestaan");
        
        alert.showAndWait();
    }
}