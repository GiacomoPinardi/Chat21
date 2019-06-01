package client;

public class HomeLog{
	
	private Observer observer;
	
	public HomeLog(Observer observer) {
		super();
		this.observer=observer;
		// TODO Auto-generated constructor stub
	}

	public void setLog(String log) {
		observer.setLog(log);
		
	}
	
	public void setAnomalie(String anomalie) {
		observer.setAnomalie(anomalie);
	}
}