package controllerServer;

public class SingleLog {

	private static ControllerLog controllerLog = null;
			
	public SingleLog () {}
	
	public static ControllerLog getControllerLog (Utenti utenti) {
		if (controllerLog == null) {
			controllerLog = new ControllerLog(utenti);
		}
		
		return controllerLog;
	}
	
}
