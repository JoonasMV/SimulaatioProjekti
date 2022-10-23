package view;

import simu.util.MoottoriOptions;
import view.visualisointi.IVisualisoija;
import view.visualisointi.VisuOptions;
import view.visualisointi.Visualisoija;
import controller.IKontrolleriVtoC;
import controller.Kontrolleri;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SimulaattoriGUI implements ISimulaattoriUI {
	final int VIIVEENMUUTOS = 10;
	
	private MainApp mainApp;

	@FXML private TextField aikaField;
	@FXML private TextField linjastoField;
	@FXML private TextField kassaField;
	@FXML private TextField ipField;
	@FXML private TextField leipaField;
	@FXML private TextField poytaField;
	
	@FXML private Text aikaInfo;
	@FXML private Text asiakasInfo;
	@FXML private Text poytaInfo;
	@FXML private Text palveluaikaInfo;
	@FXML private Text nopeusInfo;
	
	@FXML private Pane ankkuri;
	
	private IVisualisoija[] masterVisu;
	
	private IKontrolleriVtoC kontrolleri;
	private IVisualisoija vis;
	private VisuOptions visuSettings;
	
	private MoottoriOptions moottoriSettings;
	
	public void initScene(MainApp mainApp, MoottoriOptions jakaumaSettings) {
		this.mainApp = mainApp;
		this.moottoriSettings = jakaumaSettings;
	}
	
	@Override
	public void sammuta() {
		kontrolleri.sammutaSimulaatio();
	}
	
	/**
	 * Alustaa kontrollerin käynnistyksessä
	 */
	@FXML
	public void initialize() {
		this.kontrolleri = new Kontrolleri(this);
	}

	/**
	 * Muuttaa canvaksien kokoa, ikkunan kokoa muutettaessa
	 */
	private void resize() {
		for (int i = 0; i < ppYht(); i++) {
		masterVisu[i].resize(ankkuri.getWidth(), ankkuri.getHeight()/ppYht());
		}
	}

	/**
	 * Luo ja alustaa halutun määrän visualisoijia
	 */
	private void initMasterVisu() {
		masterVisu = new IVisualisoija[ppYht()];
		VBox ppBox = new VBox();
		
		for (int i = 0; i < ppYht(); i++) {
			masterVisu[i] = new Visualisoija(new Canvas());
			ppBox.getChildren().add(masterVisu[i].getCanvas());				
		}
		
		getSettings();
		visuSettings.setSettings(masterVisu);
		resize();
		ankkuri.getChildren().add(ppBox);
	}
	
	/**
	 * Aloittaa visualisoijien alustuksen, sekä lisää kuuntelijan ikkunan koon muutokselle
	 */
	public void handleSimuloiButton() {
		try {			
			initMasterVisu();
			
			ankkuri.widthProperty().addListener(e -> resize());
			ankkuri.heightProperty().addListener(e -> resize());
			kontrolleri.kaynnistaSimulointi();
			nopeusInfo.setText(kontrolleri.saaViive()+" ms");
		} catch (NumberFormatException e) {
			showNumAlert();
		}
	}
	
	/**
	 * Pause napin käsittelijä
	 */
	public void handlePause() {
		kontrolleri.togglePause();
	}
	
	/**
	 * Nopeutus napin käsittelijä
	 */
	public void handleNopeutus() {
		nopeusInfo.setText(kontrolleri.muutaViive(-VIIVEENMUUTOS) + " ms");
	}
	
	/**
	 * Hidastus napin käsittelijä
	 */
	public void handleHidastus() {
		nopeusInfo.setText(kontrolleri.muutaViive(VIIVEENMUUTOS) + " ms");
	}
	
	/**
	 * Avaa jakaumaikkunan
	 */
	public void handleJakaumadialog() {
		mainApp.showJakaumadialog();
	}
	
	 /**
	  * Avaa lisä-asetusikkunan
	  */
	public void handleLisaAsetusDialog() {
		mainApp.showLisaAsetukset();
	}
	
	/**
	 * Lopettaa simulaation kesken ajon
	 */
	public void handleLopetus() {
		for(IVisualisoija v : masterVisu) {
			Platform.runLater(new Runnable() {
				@Override
				public void run(){
					v.nollaa();
				}
			});
		}
		kontrolleri.sammutaSimulaatio();
	}
	
	public IVisualisoija getVisualisointi() { return this.vis; }
	@Override	
	public double getSimulaationPituus() throws NumberFormatException { return Double.parseDouble(this.aikaField.getText()); }
	@Override	
	public int getLinjastot() throws NumberFormatException { return Integer.parseInt(this.linjastoField.getText()); }
	@Override	
	public int getKassat() throws NumberFormatException { return Integer.parseInt(this.kassaField.getText()); }
	@Override	
	public int getLeipapoytaKapasiteetti() throws NumberFormatException { return Integer.parseInt(this.leipaField.getText()); }
	@Override	
	public int getRuokapoytaKapasiteetti() throws NumberFormatException { return Integer.parseInt(this.poytaField.getText()); }
	@Override	
	public int getIpKassat() throws NumberFormatException { return Integer.parseInt(this.ipField.getText()); }
	
	/**
	 * Palauttaa palvelupisteiden yhteismäärän
	 * 
	 * @return palvelupisteiden yhteismäärä
	 */
	private int ppYht()  {
		return getLinjastot() + getKassat() + getIpKassat() + 2;
	}

	/**
	 * Vie GUI:lle reaalidataa simuloinnin moottorista
	 * 
	 * @param aika Simuloinnissa kulunut aika
	 * @param asikkaat Asiaakkaat systeemissä
	 * @param poydissa Poydissä olevat asiakkaat
	 * @param keskiarvo Asiakkaiden keskivertainen läpimenoaika
	 */
	@Override
	public void setTamanhetkisetArvot(double aika, int asiakkaat, int poydissa, double keskiarvo) {
		aikaInfo.setText(String.format("%.2f/ %f", aika, getSimulaationPituus()));
		asiakasInfo.setText(asiakkaat+"");
		poytaInfo.setText(poydissa + "/ " + getRuokapoytaKapasiteetti());
		palveluaikaInfo.setText(String.format("%.2f asiakkaan keskimääräinen läpimenoaika", keskiarvo));
	}
	
	/**
	 * Luo virheilmoituksen
	 */
	@Override
	public void showNumAlert() {
		Alert numAlert = new Alert(AlertType.INFORMATION);
		numAlert.setTitle("Laiton syöte");
		numAlert.setHeaderText(null);
		numAlert.setContentText("Syötteen on oltava numero");
		numAlert.showAndWait();
	}
	
	/**
	 * Palauttaa kaikki visualisoijat sisältävän masterVisu:n
	 * @return kaikki visualisoijat sisältävä masterVisu taulukko
	 */
	@Override
	public IVisualisoija[] getMasterVisu() {
		return masterVisu;
	}

	/**
	 * Palauttaa tallennetut jakaumat
	 * 
	 * @return moottoriSettings moottorin asetukset sisältävä olio
	 */
	@Override
	public MoottoriOptions getMoottoriSettings() {
		return moottoriSettings;
	}
	
	/**
	 * Kerää asetukset masterVisun kokoamista varten
	 */
	public void getSettings() {
		this.visuSettings = mainApp.getVisuSettings();
		visuSettings.setLinjastot(getLinjastot());
		visuSettings.setKassat(getKassat());
		visuSettings.setIpKassat(getIpKassat());
		visuSettings.setLeipaJono(getLeipapoytaKapasiteetti());
		visuSettings.setRuokaPoydat(getRuokapoytaKapasiteetti());
	}

	@Override
	public int getViive() {
		String str = nopeusInfo.getText();
		String str2 = str.substring(0, str.length() - 3);
		return Integer.parseInt(str2);
	}
}
