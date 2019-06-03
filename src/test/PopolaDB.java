package test;

import controllerServer.ControllerDB;

public class PopolaDB {

	public static void main(String[] args) {
		
		ControllerDB db = new ControllerDB();
		
		db.creaTabelle();
		db.pulisciTutto();
		
		db.aggiungiGruppo("Gruppo1");
		db.aggiungiGruppo("Gruppo2");
		
		db.aggiungiUtente("Mario", "mario", "amministratore");
		db.aggiungiUtente("Carlo", "carlo", "utente");
		
		db.aggiungiUtenteGruppo("Gruppo1", "Mario");
		db.aggiungiUtenteGruppo("Gruppo1", "Carlo");
		
	}

}
