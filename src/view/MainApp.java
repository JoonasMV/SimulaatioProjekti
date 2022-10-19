package view;

import simu.util.MoottoriOptions;
import view.ikkunat.ajoData.DatalayoutKontrolleri;
import view.ikkunat.ajoData.Diagramkontrolleri;
import view.ikkunat.ajoData.PptietoKontrolleri;
import view.ikkunat.asetukset.AsetusController;
import view.ikkunat.jakaumat.JakaumaControlleri;
import view.visualisointi.VisuOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import entity.Asetukset;
import entity.PalvelupisteTiedot;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;

/**
 * Pääsovellus jonka päälle käyttöliittymä luodaan
 */
public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private SimulaattoriGUI gui;
    private boolean simuStageIsUp = true; //false jos ajotiedot on näytöllä ja true jos simulaatio on näytöllä
    private VisuOptions visuSettings = new VisuOptions();
    private MoottoriOptions moottoriSettings = new MoottoriOptions();
    
    /**
     * Luo juurilayoutin kutsumalla {@link view.MainApp#initRootLayout()}, simulaattorin kutsumalla {@link view.MainApp#showSimu()}
     * sekä tietokantayhteyden
     * <p>
     * {@inheritDoc}
     * <p>
     */
    
	@Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(575);
        this.primaryStage.setTitle("Simulaattori");
        
        this.primaryStage.getIcons().add(new Image("file:resources/images/app_icon.png"));

        initRootLayout();
        datasource.DatabaseConnection.createConnection();
        showSimu();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void stop() {
		gui.sammuta();
	}
	
	/**
     * Tekee RootLayoutin ohjelmalle
     */
    public void initRootLayout() {
        try {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            Scene scene = new Scene(rootLayout);
            
            Rootkontrolleri controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Asettaa simulaattorin päänäytön näkyviin
     */
    public void showSimu() {
		try {
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("Simulaattori.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			SimulaattoriGUI controller = loader.getController();
            gui = controller;
            controller.initScene(this, moottoriSettings);
			rootLayout.setCenter(pane);
			simuStageIsUp = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Tekee dialogStagen jakaumien syöttämistä varten
     */
    public void showJakaumadialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ikkunat/jakaumat/Jakaumadialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Jakaumaeditori");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            
            dialogStage.getIcons().add(new Image("file:resources/images/jakaumat_icon.png"));
            
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            JakaumaControlleri controller = loader.getController();
            controller.setDialogStage(dialogStage, moottoriSettings);
            //controller.setMainApp(this);
           
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Näyttää lisäasetukset-ikkunan
     */
    public void showLisaAsetukset() {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("/view/ikkunat/asetukset/Lisa-asetukset.fxml"));
    		AnchorPane page = (AnchorPane) loader.load();
    		
    		Stage dialogStage = new Stage();
            dialogStage.setTitle("Lisäasetukset");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            
            dialogStage.getIcons().add(new Image("file:resources/images/jakaumat_icon.png"));
            
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            AsetusController controller = loader.getController();
            controller.initStage(dialogStage, visuSettings, moottoriSettings);
            //controller.setMainApp(this);
           
            dialogStage.showAndWait();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     *  Näyttää palvelupistetiedot-ikkunan
     *  
     * @param lista     lista, joka sisältää {@link entity.PalvelupisteTiedot}-olioita
     * @param asetukset simulaation asetukset
     */
    public void showPptiedot(List<PalvelupisteTiedot> lista, Asetukset asetukset) {
        try {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ikkunat/ajoData/PPtiedot.FXML"));
            AnchorPane page = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Palvelupistetiedot");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            
            PptietoKontrolleri controller = loader.getController();
            
            controller.setContetnts(lista, asetukset);
            
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
  
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showGrafiikka(HashMap<Integer, Double[]> graphTiedot) {
        try {
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ikkunat/ajoData/diagram.FXML"));
            AnchorPane page = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("diagrammi");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            
            Diagramkontrolleri controller = loader.getController();
            
            controller.setContetnts(graphTiedot);
            
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
  
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Avaa ikkunan, jossa on simulaattorin ajojen tietoja
     */
    public void showData() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ikkunat/ajoData/DataLayout.fxml"));
            AnchorPane dataOw = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(dataOw);

            // Give the controller access to the main app.
            DatalayoutKontrolleri controller = loader.getController();
            controller.setMainApp(this);
            simuStageIsUp = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isSimuUp() {
    	return simuStageIsUp;
    }
    
    public VisuOptions getVisuSettings() {
    	return this.visuSettings;
    }
    
    public MoottoriOptions getJakaumaSettings() {
    	return this.moottoriSettings;
    	
    }
}
