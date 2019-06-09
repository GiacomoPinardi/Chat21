package controllerServer;

import java.util.ArrayList;
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
	
	public ControllerGestioneGruppi(Utenti utenti, Gruppi gruppi, ControllerDB dbConnection) {
		this.utenti = utenti;
		this.gruppi = gruppi;
		this.dbConnection = dbConnection;
	}

	public boolean creaGruppo(String nome, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.aggiungi(new Gruppo(nome));
		invioConferma("crea gruppo", esito, executor);
		gruppi.unlockList();
		if (esito) {
			dbConnection.aggiungiGruppo(nome);
		}
		return esito;
	}
	
	public boolean eliminaGruppo(String nome, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.rimuovi(nome);
		invioConferma("elimina gruppo", esito, executor);
		gruppi.unlockList();
		if (esito) {
			dbConnection.eliminaGruppo(nome);
		}
		return esito;
	}
	
	public boolean aggiungiUtenteGruppo(String nomeGruppo, String username, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.getByNome(nomeGruppo).aggiungiUtente(username);
		invioConferma("aggiungi utente a gruppo", esito, executor);
		gruppi.unlockList();
		if (esito)
			dbConnection.aggiungiUtenteGruppo(nomeGruppo, username);
		return esito;
	}
	
	public boolean eliminaUtenteGruppo(String nomeGruppo, String username, String executor) {
		gruppi.lockList();
		boolean esito = gruppi.getByNome(nomeGruppo).eliminaUtente(username);
		invioConferma("elimina utente da gruppo", esito, executor);
		gruppi.unlockList();
		if (esito)
			dbConnection.eliminaUtenteGruppo(nomeGruppo, username);
		return esito;
	}
	
	public void invioListaGruppi(String executor) {
		gruppi.lockList();
		Pacchetto p = new Pacchetto(new ListaString(gruppi.getNomeGruppi()), TipoInfo.LISTA_GRUPPI);
		gruppi.unlockList();
		utenti.lockList();
		utenti.getByUsername(executor).invia(p);
		utenti.unlockList();
	}
	
	public void invioListaUtentInGruppo(String nome, String executor) {
		Pacchetto p;
		gruppi.lockList();
		Gruppo g = gruppi.getByNome(nome);
				
		if (g != null) {
			p = new Pacchetto(new ListaString(gruppi.getByNome(nome).getUtenti()), TipoInfo.LISTA_UTENTI_GRUPPO);
		}
		else {
			p = new Pacchetto(new ListaString(new ArrayList<>()), TipoInfo.LISTA_UTENTI_GRUPPO);
		}
		gruppi.unlockList();
		utenti.lockList();
		//((ListaString) p.getInformazione()).getListaContenuti().add("Franco");
		utenti.getByUsername(executor).invia(p);
		utenti.unlockList();
	}
	
	public void invioListaUtentiNonInGruppo(String nome, String executor) {
		Pacchetto p;
		
		gruppi.lockList();
		utenti.lockList();
		Gruppo g = gruppi.getByNome(nome);
		if (g != null) {
			List<String> utentiIn = g.getUtenti();
			p = new Pacchetto(new ListaString(utenti.getAltriUtenti(utentiIn)), TipoInfo.LISTA_UTENTI_NON);
		}
		else {
			p = new Pacchetto(new ListaString(new ArrayList<>()), TipoInfo.LISTA_UTENTI_NON);
		}
		
		utenti.getByUsername(executor).invia(p);
		utenti.unlockList();
		gruppi.unlockList();
	}
	
	private void invioConferma(String operazione, boolean esito, String executor) {
		String res;
		if (esito)
			res = "l'operazione " + operazione + " e' andata buonfine";
		else
			res = "l'operazione " + operazione + " non e' andata buonfine";
		utenti.lockList();
		utenti.getByUsername(executor).invia(new Pacchetto(new Conferma(res) , TipoInfo.CONFERMA));
		utenti.unlockList();
	}
}
