package test;

import java.time.LocalDateTime;

import controllerServer.ControllerDB;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.TipoDestinatario;

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
		
		db.addContenutoBacheca(new MessaggioTestuale(TipoDestinatario.BACHECA, LocalDateTime.now(), "Mario", "bacheca", "Prova in bacheca!"));
		db.addContenutoGruppo(new MessaggioTestuale(TipoDestinatario.GRUPPO, LocalDateTime.now(), "Mario", "Gruppo1", "Prova nel gruppo!"));
	}

}
