

import java.util.ArrayList;
import java.util.List;

import dominioPacchetto.Contenuto;
import dominioServer.Ruolo;

public class InformazioniSessione{
	private String username;
	private List<String> gruppi;
	private Ruolo ruolo;
	private Boolean esitoAccesso;
	List<Contenuto> contenutiBacheca;
	
	public InformazioniSessione(String username, List<String> gruppi, Ruolo ruolo,Boolean esito,List<Contenuto> contenutiBacheca) {
		super();
		this.username = username;
		this.gruppi = gruppi;
		this.ruolo = ruolo;
		this.esitoAccesso=esito;
		this.contenutiBacheca=contenutiBacheca;
	}
	public InformazioniSessione() {
		this.ruolo=Ruolo.AMMINISTRATORE;
		this.gruppi=new ArrayList<>();
		this.gruppi.add("Gruppo1");
		this.gruppi.add("Gruppo2");
		this.gruppi.add("Gruppo21");
	}
	
	public List<Contenuto> getContenutiBacheca(){
		return this.contenutiBacheca;
	}
	
	public Boolean isEsitoAccesso() {
		return esitoAccesso;
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