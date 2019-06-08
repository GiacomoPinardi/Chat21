package dominioServer;

import java.util.ArrayList;
import java.util.List;

import dominioPacchetto.Contenuto;

public class Gruppo {
	private String nome;
	//private List<Contenuto> contenuti;
	private List<String> utenti;
	
	public Gruppo(String nome) {
		this.nome = nome;
		//contenuti = new ArrayList<>();
		utenti = new ArrayList<>();
	}
	
	public String getNome() {
		return nome;
	}

//	public List<Contenuto> getContenuti() {
//		return contenuti;
//	}

	public List<String> getUtenti() {
		return utenti;
	}
	
	public boolean aggiungiUtente(String username) {
		boolean res = true;
		for (String u : utenti)
			if (u.equals(username))
				return false;
		return utenti.add(username);
	}
	
	public boolean eliminaUtente(String username) {
		return utenti.remove(username);
	}
	
	public boolean isUtenteInGruppo(String username) { // AGGIUNTO
		return utenti.contains(username);
	}
	
//	public boolean aggiungiContenuto(Contenuto content) {
//		return contenuti.add(content);
//	}
}
