package controllerServer;

import java.util.List;

import dominioPacchetto.Contenuto;
import dominioPacchetto.ListaContenuti;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Utente;

public class ControllerScambiaContenutiBacheca implements IControllerScambiaContenuti {
	private ControllerDB dbConnection;
	private Utenti utenti;
	
	public ControllerScambiaContenutiBacheca (Utenti utenti, ControllerDB dbConnection) {
		this.utenti = utenti;
		this.dbConnection = dbConnection;
	}
	
	@Override
	public void smista(Contenuto contenuto) {
		dbConnection.addContenutoBacheca((MessaggioTestuale) contenuto);
		
		List<Utente> utentiOnline;
		Pacchetto pacchetto = new Pacchetto(contenuto, TipoInfo.CONTENUTO);
		utenti.lockList();
		utentiOnline = utenti.getUtentiOnline();
		for (Utente utente : utentiOnline) {
			if (!utente.getUsername().equals(contenuto.getMittente())) {
				utente.invia(pacchetto);
			}
		}
		utenti.unlockList();
	}
	
	/*
	public void getContenutiBacheca (String executor) {
			
	}
	*/
}
