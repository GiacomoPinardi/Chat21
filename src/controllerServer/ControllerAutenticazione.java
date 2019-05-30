package controllerServer;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

import dominioPacchetto.Conferma;
import dominioPacchetto.InfoSessione;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Ruolo;
import dominioServer.Utente;

public class ControllerAutenticazione {
	Utenti utenti;
	Gruppi gruppi;
	ControllerDB dbConnection;
	
	public ControllerAutenticazione(Utenti utenti, Gruppi gruppi, ControllerDB dbConnection) {
		this.utenti = utenti;
		this.gruppi = gruppi;
		this.dbConnection = dbConnection;
	}
	
	public boolean verificaPassword (String username, String password) {
		return dbConnection.verificaPassword(username, password);
	}
	
	public void connetti (String username, ObjectOutputStream oos) {
		utenti.lockList();
		Utente ut = utenti.getByUsername(username);
		ut.setConnesso(true);
		ut.setObjectOutputStream(oos);
		utenti.unlockList();
	}

	public void confermaAccesso(String username) {		
		utenti.lockList();
		Utente u = utenti.getByUsername(username);
		gruppi.lockList();
		u.invia(new Pacchetto(new InfoSessione(username, true, u.getRuolo(), gruppi.getGruppiCon(username)), TipoInfo.CONFERMA));
		gruppi.unlockList();
		u.setConnesso(true);
		utenti.unlockList();		
	}
	
	public void negaAccesso (String username, ObjectOutputStream oos) {
		// creato un utente solo per inviargli il negato accesso. Ruolo non e' importante!
		Utente tmp = new Utente(username, Ruolo.UTENTE);
		tmp.setObjectOutputStream(oos);
		tmp.setConnesso(true);
		tmp.invia(new Pacchetto(new InfoSessione(username, false, null, null), TipoInfo.CONFERMA));
		System.out.println("Inviato negato accesso!");
	}
	
	public void disconnetti(String executor) {
		utenti.lockList();
		utenti.getByUsername(executor).setConnesso(false);
		utenti.unlockList();
	}
	
	public boolean modicaPassword(String executor, String oldOne, String newOne) {
		if (dbConnection.verificaPassword(executor, oldOne)) {
			dbConnection.modificaPassoword(executor, newOne);
			invioConferma("modifica password", true, executor);
			return true;
		} else {
			invioConferma("modifica password", false, executor);
			return false;
		}
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
