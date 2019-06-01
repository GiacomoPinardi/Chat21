package client;

import java.util.ArrayList;
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
	public InformazioniSessione() {
		this.ruolo=Ruolo.AMMINISTRATORE;
		this.gruppi=new ArrayList<>();
		this.gruppi.add("Gruppo1");
		this.gruppi.add("Gruppo2");
		this.gruppi.add("Gruppo21");
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