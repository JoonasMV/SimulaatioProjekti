package view.visualisointi;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import simu.util.PpRealtimeData;

public interface IVisualisoija {

	void setRealtimeData(PpRealtimeData data);
	Canvas getCanvas();
	void piirra();
	void resize(double w, double h);
	void setCanvas(Canvas n);
	void setJononpituus(int jonottajia);
	void setVari(Color vari);
	void nollaa();
}
