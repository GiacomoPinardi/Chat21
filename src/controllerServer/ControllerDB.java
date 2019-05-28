package controllerServer;

import java.util.List;

import dominioPacchetto.Contenuto;

public class ControllerDB {
	
	public synchronized boolean verificaPassword(String username, String hashPassword) {
		return true;
	}
	
	public synchronized List<Contenuto> getContenutiGruppo(String gruppo) {
		return null;
	}

	public synchronized void aggiungiGruppo(String nome) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void eliminaGruppo(String nome) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void aggiungiUtenteGruppo(String nomeGruppo, String username) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void eliminaUtenteGruppo(String nomeGruppo, String username) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void aggiungiUtente(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void eliminaUtente(String username) {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void modificaPassoword(String executor, String newOne) {
		
	}
}
