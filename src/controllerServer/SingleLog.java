package controllerServer;

public class SingleLog {

	private static ControllerLog controllerLog = null;
			
	public SingleLog () {}
	
	public static ControllerLog getControllerLog () {
		if (controllerLog == null) {
			controllerLog = new ControllerLog();
		}
		
		return controllerLog;
	}
	
}
