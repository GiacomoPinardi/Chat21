package controllerServer;

import java.io.ObjectOutputStream;

import dominioPacchetto.Conferma;
import dominioPacchetto.Inizializzazione;
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
	
	public void connetti(String username, ObjectOutputStream oos) {
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
		u.invia(new Pacchetto(new Inizializzazione(username, true, u.getRuolo(), gruppi.getGruppiCon(username), dbConnection.getContenutiBacheca()), TipoInfo.INFO_SESSIONE));
		gruppi.unlockList();
		u.setConnesso(true);
		utenti.unlockList();		
	}
	
	public void negaAccesso (String usernameTentativo, ObjectOutputStream oos) {
		// creato un utente solo per inviargli il negato accesso. Ruolo non e' importante!
		Utente tmp = new Utente("tmp", Ruolo.UTENTE);
		tmp.setObjectOutputStream(oos);
		tmp.setConnesso(true);
		tmp.invia(new Pacchetto(new Inizializzazione(usernameTentativo, false, null, null, null), TipoInfo.INFO_SESSIONE));
		System.out.println("Inviato negato accesso!");
	}
	
	public void disconnetti(String executor) {
		utenti.lockList();
		Utente u = utenti.getByUsername(executor);
		if (u != null) {
			u.setConnesso(false);
		}
		utenti.unlockList();
	}
	
	public boolean modicaPassword(String executor, String oldOne, String newOne) {
		if (dbConnection.verificaPassword(executor, oldOne)) {
			dbConnection.modificaPassoword(executor, newOne);
			invioConferma("modifica password", true, executor);
			return true;
		}
		else {
			invioConferma("modifica password", false, executor);
			return false;
		}
	}
	
	private void invioConferma(String operazione, boolean esito, String executor) {
		String res;
		if (esito)
			res = "l'operazione " + operazione + " e' andata a buon fine";
		else
			res = "l'operazione " + operazione + " non e' andata a buon fine";
		utenti.lockList();
		utenti.getByUsername(executor).invia(new Pacchetto(new Conferma(res) , TipoInfo.CONFERMA));
		utenti.unlockList();
	}
}
