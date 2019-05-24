package ControllerServer;

import java.util.List;

import dominioPacchetto.Conferma;
import dominioPacchetto.ListaString;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Gruppo;

public class ControllerGestioneGruppi {
	private ControllerDB dbConnection;
	private Utenti utenti;
	private Gruppi gruppi;
	
	public ControllerGestioneGruppi(ControllerDB dbConnection, Utenti utenti, Gruppi gruppi) {
		this.dbConnection = dbConnection;
		this.utenti = utenti;
		this.gruppi = gruppi;
	}

	public void creaGruppo(String nome, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.aggiungi(new Gruppo(nome));
		invioConferma("crea gruppo", esito, executor);
		gruppi.unlockList();
		dbConnection.aggiungiGruppo(nome);
	}
	
	public void eliminaGruppo(String nome, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.rimuovi(nome);
		invioConferma("elimina gruppo", esito, executor);
		gruppi.unlockList();
		dbConnection.eliminaGruppo(nome);
	}
	
	public void aggiungUtenteGruppo(String nome, String username, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.getByNome(nome).aggiungiUtente(username);
		invioConferma("aggiungi utente a gruppo", esito, executor);
		gruppi.unlockList();
		dbConnection.aggiungiUtenteGruppo(nome, username);
	}
	
	public void eliminaUtenteGruppo(String nome, String username, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.getByNome(nome).eliminaUtente(username);
		invioConferma("elimina utente da gruppo", esito, executor);
		gruppi.unlockList();
		dbConnection.eliminaUtenteGruppo(nome, username);
	}
	
	public void invioListaGruppi(String executor) {
		gruppi.lockList();
		Pacchetto p = new Pacchetto(new ListaString(gruppi.getNomeGruppi()), TipoInfo.LISTA_GRUPPI);
		gruppi.unlockList();
		utenti.lockList();
		utenti.getByUsername(executor).invia(p);
		utenti.lockList();
	}
	
	public void invioListaUtentInGruppo(String nome, String executor) {
		gruppi.lockList();
		Pacchetto p = new Pacchetto(new ListaString(gruppi.getByNome(nome).getUtenti()), TipoInfo.ELIMINA_UTENTE_GRUPPO);
		gruppi.unlockList();
		utenti.lockList();
		utenti.getByUsername(executor).invia(p);
		utenti.unlockList();
	}
	
	public void invioListaUtentiNonInGruppo(String nome, String executor) {
		gruppi.lockList();
		utenti.lockList();
		List<String> utentiIn = gruppi.getByNome(nome).getUtenti();
		Pacchetto p = new Pacchetto(new ListaString(utenti.getAltriUtenti(utentiIn)), TipoInfo.AGG_UTENTE_GRUPPO);
		utenti.getByUsername(executor).invia(p);
		utenti.unlockList();
		gruppi.unlockList();
	}
	
	private void invioConferma(String operazione, boolean esito, String executor) {
		String res;
		if (esito)
			res = "l'operazione " + operazione + " è andata buonfine";
		else
			res = "l'operazione " + operazione + "  non è andata buonfine";
		utenti.lockList();
		utenti.getByUsername(executor).invia(new Pacchetto(new Conferma(res) , TipoInfo.CONFERMA));
		utenti.unlockList();
	}
}
