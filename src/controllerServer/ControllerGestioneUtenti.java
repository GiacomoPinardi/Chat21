package controllerServer;

import dominioPacchetto.Conferma;
import dominioPacchetto.ListaString;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Ruolo;
import dominioServer.Utente;

public class ControllerGestioneUtenti {
	private ControllerDB dbConnection;
	private Utenti utenti;
	private Gruppi gruppi;
	
	public ControllerGestioneUtenti(ControllerDB dbConnection, Utenti utenti, Gruppi gruppi) {
		super();
		this.dbConnection = dbConnection;
		this.utenti = utenti;
		this.gruppi = gruppi;
	}
	
	public boolean aggiungiUtente(String username, Ruolo ruolo, String executor, String passwordUtenteAggiunto) {
		utenti.lockList();
		boolean esito = utenti.aggiungi(new Utente(username, ruolo));
		utenti.unlockList();
		invioConferma("aggiungi utente", esito, executor);
		if (esito) {
			dbConnection.aggiungiUtente(username, passwordUtenteAggiunto);
		}
		return esito;
	}
	
	public boolean eliminaUtente(String username, String executor) {
		utenti.lockList();
		boolean esito = utenti.rimuovi(username);
		utenti.unlockList();
		invioConferma("elimina utente", esito, executor);
		if (esito) {
			dbConnection.eliminaUtente(username);
		}
		return esito;
	}
	
	public void inviaListaUtenti(String executor) {
		utenti.lockList();
		Pacchetto p = new Pacchetto(new ListaString(utenti.getUsernames()), TipoInfo.LISTA_UTENTI);
		utenti.getByUsername(executor).invia(p);
		utenti.unlockList();
	}
	
	private void invioConferma(String operazione, boolean esito, String executor) {
		String res;
		if (esito) {
			res = "l'operazione " + operazione + " è andata buon fine";
		}
		else {
			res = "l'operazione " + operazione + "  non è andata buon fine";
		}
		utenti.lockList();
		utenti.getByUsername(executor).invia(new Pacchetto(new Conferma(res) , TipoInfo.CONFERMA));
		utenti.unlockList();
	}
}
