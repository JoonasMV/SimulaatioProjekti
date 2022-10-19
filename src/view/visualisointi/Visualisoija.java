package view.visualisointi;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simu.util.PpRealtimeData;

public class Visualisoija implements IVisualisoija{
	Canvas naytto;
	GraphicsContext gc;
	PpRealtimeData data;  
	int jonottajia;
	protected Color vari;
	
	/***
	 * 
	 * @param n Canvas, johon palvelupisteiden jolle piirretään
	 */
	public Visualisoija(Canvas n) {
		this.naytto = n;
		this.gc = this.naytto.getGraphicsContext2D();
	}
	
	/***
	 * Piirtää canvakselle palvelupisteen jonon
	 */
	@Override
	public void piirra() {	
		double	kulmionLeveys = naytto.getWidth() / jonottajia;
		nollaa();
		
		gc.setFill(vari);
		if (data == null) return;
		
		// Jos jono on pidempi kuin haluttu määrä, katkaisee piirtäjän piirtoalueen maksimi leveyseksi
		int KatkaistuLeveys = data.getJononpituus();
		if (KatkaistuLeveys > jonottajia) KatkaistuLeveys = jonottajia;
		gc.fillRect(0, 0, kulmionLeveys * KatkaistuLeveys, naytto.getHeight());
		
		
		gc.strokeLine(0, naytto.getHeight(), naytto.getWidth(), naytto.getHeight());
		gc.setStroke(Color.BLACK);
		gc.strokeText(String.format("%s: %d asiakasta", data.getNimi(), data.getJononpituus()), 10, naytto.getHeight() / 2 + 5);
	}
	
	/***
	 * Muuttaa canvaksen kokoa, jos ikkunan kokoa muutetaan
	 */
	@Override
	public void resize(double w, double h) {
		naytto.setWidth(w);
		naytto.setHeight(h);
		piirra();
	}
	
	/***
	 * Pyyhkii canvaksen, ennen kuin jono piirretään uudelleen
	 */
	@Override
	public void nollaa() {
		gc.setFill(Color.rgb(150, 150, 150)); // harmaa
		gc.fillRect(0, 0, naytto.getWidth(), naytto.getHeight());
	}

	/***
	 * @param data Data, jonka perusteella jono piirretään
	 */
	@Override
	public void setRealtimeData(PpRealtimeData data) {
		this.data = data;
	}

	/***
	 * @return palauttaa canvaksen
	 */
	@Override
	public Canvas getCanvas() {
		return this.naytto;
	}
	
	/***
	 * @param n uusi canvas
	 */
	@Override
	public void setCanvas(Canvas n) {
		this.naytto = n;
		this.gc = this.naytto.getGraphicsContext2D();
		piirra();
	}
	
	/***
	 * @param jonottajia määrittää kuinka pitkä piirrettävä jono on
	 */
	@Override
	public void setJononpituus(int jonottajia) {
		this.jonottajia = jonottajia;
	}
	
	/***
	 * @param vari Väri jolla jono piirrettään
	 */
	@Override
	public void setVari(Color vari) {
		this.vari = vari;
	}
	
}
