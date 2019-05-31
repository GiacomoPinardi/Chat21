package client;

import java.util.List;

import dominioServer.Ruolo;

public class InformazioniSessione{
	private String username;
	private List<String> gruppi;
	private Ruolo ruolo;
	
	public InformazioniSessione(String username, List<String> gruppi, Ruolo ruolo) {
		super();
		this.username = username;
		this.gruppi = gruppi;
		this.ruolo = ruolo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getGruppi() {
		return gruppi;
	}

	public void setGruppi(List<String> gruppi) {
		this.gruppi = gruppi;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
	
	public boolean addGroup(String nomeGruppo) {
		return gruppi.add(nomeGruppo);
	}
	
	public boolean deleteGroup(String nomeGruppo) {
		return gruppi.remove(nomeGruppo);
	}
}