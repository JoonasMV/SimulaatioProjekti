package view;

public class Rootkontrolleri {
	
	private MainApp mainApp;
	
	public void initialize() {
		
	}
	
	public void handleShowdata() {
		if(mainApp.isSimuUp()) {
			mainApp.showData();
		}
		
	}
	
	public void handleMainview() {
		if(!mainApp.isSimuUp()) {
			mainApp.showSimu();
		}
		
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
	}
	
	public void handleClose() {
		System.exit(1);
	}
}
