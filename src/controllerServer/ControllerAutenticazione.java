package controllerServer;

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

	public boolean confermaAccesso(String username, String hashPassword) {
		utenti.lockList();
		if(dbConnection.verificaPassword(username, hashPassword)) {
			Utente u = utenti.getByUsername(username);
			gruppi.lockList();
			u.invia(new Pacchetto(new InfoSessione(true, u.getRuolo(), gruppi.getGruppiCon(username)), TipoInfo.CONFERMA));
			gruppi.unlockList();
			u.setConnesso(true);
			utenti.unlockList();
			return true;
		} else {
			utenti.getByUsername(username).invia(new Pacchetto(new InfoSessione(false, null, null), TipoInfo.CONFERMA));
			utenti.unlockList();
			return false;
		}
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
