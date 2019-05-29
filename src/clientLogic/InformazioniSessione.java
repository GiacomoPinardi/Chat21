package clientLogic;

import java.util.List;

import dominioServer.Ruolo;

public class InformazioniSessione {
	private String Username;
	private List<String> gruppi;
	private Ruolo ruolo;
	
	public InformazioniSessione(String username, List<String> gruppi, Ruolo ruolo) {
		Username = username;
		gruppi = gruppi;
		this.ruolo = ruolo;
	}

	public String getUsername() {
		return Username;
	}

	public List<String> getGruppi() {
		return gruppi;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}
	
	public boolean addGroup(String nome) {
		return gruppi.add(nome);
	}
	
	public boolean deleteGroup(String nome) {
		return gruppi.remove(nome);
	}
	
}
