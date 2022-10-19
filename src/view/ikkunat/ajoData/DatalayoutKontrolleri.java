package view.ikkunat.ajoData;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;


import dao.AjoDAO;
import dao.AsetuksetDAO;
import dao.PalvelupisteTiedotDAO;
import entity.Ajo;
import entity.Asetukset;
import entity.PalvelupisteTiedot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import view.MainApp;


public class DatalayoutKontrolleri {
	
	private MainApp mainApp;
	private PalvelupisteTiedotDAO dao;
	private AjoDAO aDao;
	private AsetuksetDAO asetusDao;
	
	
	private Asetukset testiA;
	private List<PalvelupisteTiedot> testiP;
	private int testiI;
	
	private ObservableList<Ajo> tiedot = FXCollections.observableArrayList();
	
	
	@FXML
    private TableView<Ajo> ajoTable;
    @FXML
    private TableColumn<Ajo, String> idColumn;
   
    @FXML
    private Label Id;
    @FXML
    private Label Pvm;
    @FXML
    private Label Palvellut;
    @FXML
    private Label Kesto;
    @FXML
    private Label Nimi;
    @FXML
    private Label Jonotusaika;
    @FXML
    private Label Kayttoaste;
    @FXML
    private Label Suoritusteho;
    @FXML
    private Label Avgpalveluaika;
    @FXML
    private Label Avglapimenoaika;
    @FXML
    private Label Avgjononpituus;

    
    public DatalayoutKontrolleri() {
    	this.dao = new PalvelupisteTiedotDAO();
    	this.aDao = new AjoDAO();
    	this.asetusDao = new AsetuksetDAO();
    	List<Ajo> lista = aDao.haeKaikki();
    	for(int i = 0; i < lista.size(); i++) {
    		tiedot.add(lista.get(i));	
    	}
    	
    	
    }
    
    @FXML
    private void initialize() {
    	
    	
    	
    	idColumn.setCellValueFactory(new PropertyValueFactory<Ajo,String>("id"));

        naytaAjot(null);

       ajoTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> naytaAjot(newValue));
    }
    
    private void naytaAjot(Ajo ajo) {
    	DateFormat displayDate = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
    	
        if (ajo != null) {
        	Ajo ajoTiedot = aDao.hae(ajo.getId());

        	Id.setText(String.valueOf(ajoTiedot.getId()));
        	Pvm.setText(displayDate.format(ajoTiedot.getDate()));
        	Palvellut.setText(String.valueOf(ajoTiedot.getPalvellutAsiakkaat()));
        	Kesto.setText(String.format("%.0f Minuuttia",ajoTiedot.getAika()));
        	Jonotusaika.setText(String.format("%.1f Minuttia", ajoTiedot.getAvgAikaRuokapoydille()));
            
        } else {
            
            Id.setText("");
            Pvm.setText("");
            Palvellut.setText("");
            Kesto.setText("");
            Jonotusaika.setText("");
            
        }
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        ajoTable.setItems(tiedot);
        
    }
    
    public void handleNaytaPp() {
    	if(!(ajoTable.getSelectionModel().selectedItemProperty().get() == null)) {
    		int nykyinenId = ajoTable.getSelectionModel().selectedItemProperty().get().getId();
    		if(nykyinenId == testiI) {
    			mainApp.showPptiedot(testiP, testiA);
    		}else {
    			List<PalvelupisteTiedot> yhdetTiedot = dao.haeKaikkiAjosta(nykyinenId);
            	Asetukset asetusTiedot = asetusDao.haeAjonPerusteella(tiedot.get(nykyinenId - 1));
            	testiI = nykyinenId;
            	testiA = asetusTiedot;
            	testiP = yhdetTiedot;
            	mainApp.showPptiedot(yhdetTiedot, asetusTiedot);
    		}
        	
    	}
    	
    }
    
    public void handleNaytaGraph() {
    	HashMap<Integer, Double[]> graphTiedot = new HashMap<>();
    	if(!tiedot.isEmpty()) {
    		
    		for(Ajo a : tiedot) {
    			Asetukset as = asetusDao.haeAjonPerusteella(a);
    			Double[] taulukko = new Double[3]; //[0] = linjastolkm, [1] = kassalkm, [2] = avgaikapoydille
    			taulukko[0] = (double) as.getLinjastoLkm();
    			taulukko[1] = (double) as.getKassaLkm();
    			taulukko[2] = a.getAvgAikaRuokapoydille();
    			
    			graphTiedot.put(a.getId(), taulukko);
    		}
    		mainApp.showGrafiikka(graphTiedot);
    	}
    	
    }
}
