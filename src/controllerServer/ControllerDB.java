package controllerServer;

import java.util.List;

import dominioPacchetto.Contenuto;

public class ControllerDB {
	// TUTTO SYNCHRONIZED
	public boolean verificaPassword(String username, String hashPassword) {
		return true;
	}
	
	public List<Contenuto> getContenutiGruppo(String gruppo) {
		return null;
	}

	public void aggiungiGruppo(String nome) {
		// TODO Auto-generated method stub
		
	}

	public void eliminaGruppo(String nome) {
		// TODO Auto-generated method stub
		
	}

	public void aggiungiUtenteGruppo(String nome, String username) {
		// TODO Auto-generated method stub
		
	}

	public void eliminaUtenteGruppo(String nome, String username) {
		// TODO Auto-generated method stub
		
	}

	public void aggiungiUtente(String username) {
		// TODO Auto-generated method stub
		
	}

	public void eliminaUtente(String username) {
		// TODO Auto-generated method stub
		
	}
	
	public void modificaPassoword(String executor, String newOne) {
		
	}
}
