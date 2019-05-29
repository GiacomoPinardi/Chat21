package controllerServer;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		    ControllerDB db = new ControllerDB();
		    db.creaTabelle();
		    
		    db.aggiungiGruppo("prova");
		    db.aggiungiGruppo("gr2");
		    
		    List<String> gruppi = db.getGruppi();
		    for (String g : gruppi) {
		    	System.out.println("\t" + g);
		    }
		    
		    db.pulisciTutto();
		
	}

}
