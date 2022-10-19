package view.ikkunat.ajoData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Diagramkontrolleri {
	
    private ObservableList<String> xArvot = FXCollections.observableArrayList();

    @FXML
    private LineChart<String, Double> lineChart;

    @FXML
    private CategoryAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;


    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    
    @FXML
    private void initialize() {
           
    }

	@SuppressWarnings("unchecked")
	public void setContetnts(HashMap<Integer, Double[]> graphTiedot) {
		int korkeinArvo = 0;
		
		
		HashMap<Double, Double> linjastolkm = new HashMap<Double, Double>();
		HashMap<Double, Double> kassalkm = new HashMap<Double, Double>();
		
		XYChart.Series<String, Double> series1 = new XYChart.Series<>();
		XYChart.Series<String, Double> series2 = new XYChart.Series<>();
		
		series1.setName("Linjastot");
		series2.setName("Kassat");
		
		for(Map.Entry<Integer, Double[]> pari : graphTiedot.entrySet()) {
			Double[] taulukko = pari.getValue();
			if(taulukko[0] > korkeinArvo || taulukko[1] > korkeinArvo) {
				if(taulukko[0] > taulukko[1]) {
					korkeinArvo = taulukko[0].intValue();
				}else {
					korkeinArvo = taulukko[1].intValue();
				}
			}
			
			if((!(linjastolkm.size() == 0))  &&  linjastolkm.containsKey(taulukko[0])){
				double keskiarvo = ((linjastolkm.get(taulukko[0])  + taulukko[2]))/ 2;
				linjastolkm.put(taulukko[0], keskiarvo);
			}else {
				linjastolkm.put(taulukko[0], taulukko[2]);
			}
			
			if((!(kassalkm.size() == 0)) && kassalkm.containsKey(taulukko[1])){
				double keskiarvo = ((kassalkm.get(taulukko[1])  + taulukko[2])) / 2;
				kassalkm.put(taulukko[1], keskiarvo);
			}else {
				kassalkm.put(taulukko[1], taulukko[2]);
			}
			
			
			

		}
		
		for(Map.Entry<Double, Double> pari : linjastolkm.entrySet()) {
			String lkm = String.valueOf((pari.getKey().intValue())) ;
			series1.getData().add(new XYChart.Data<>(lkm , pari.getValue()));
		}
		
		for(Map.Entry<Double, Double> pari : kassalkm.entrySet()) {
			String lkm = String.valueOf((pari.getKey().intValue())) ;
			series2.getData().add(new XYChart.Data<>(lkm , pari.getValue()));
		}
		
		String[] arvoLista = new String[korkeinArvo];
		for(int i = 0; i < korkeinArvo; i++) {
			arvoLista[i] = String.valueOf(i + 1);
		}
		
		xArvot.addAll(Arrays.asList(arvoLista));
		xAxis.setCategories(xArvot);
		
        lineChart.getData().addAll(series1, series2);
		
	}
}