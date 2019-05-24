package ControllerServer;

import java.util.List;

import dominioPacchetto.Contenuto;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Utente;

public class ControllerScambiaContenutiBacheca implements IControllerScambiaContenuti {
	private int dbConnection;
	private Utenti utenti;
	
	@Override
	public void smista(Contenuto contenuto) {
		List<Utente> utentiOnline;
		Pacchetto pacchetto = new Pacchetto(contenuto, TipoInfo.CONTENUTO);
		utenti.lockList();
		utentiOnline = utenti.getUtentiOnline();
		for (Utente utente : utentiOnline) {
			utente.invia(pacchetto);
		}
		utenti.unlockList();
	}

}
