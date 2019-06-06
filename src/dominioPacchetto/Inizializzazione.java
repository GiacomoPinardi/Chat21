package dominioPacchetto;

import java.util.ArrayList;
import java.util.List;

import dominioServer.Ruolo;

public class Inizializzazione implements Info {

	private static final long serialVersionUID = 1L;
	
	private boolean esitoCredenziali;
	private String username;
	private Ruolo ruolo;
	private List<String> gruppi;
	private List<Contenuto> contenutiBacheca;
	
	public Inizializzazione(String username, boolean esitoCredenziali, Ruolo ruolo, List<String> gruppi, List<Contenuto> contenutiBacheca) {
		this.username = username;
		this.esitoCredenziali = esitoCredenziali;
		this.ruolo = ruolo;
		this.gruppi = gruppi;
		this.contenutiBacheca = contenutiBacheca;
	}

	public Inizializzazione() {
		// TODO Auto-generated constructor stub
		//solo per test grafica
		this.ruolo=Ruolo.AMMINISTRATORE;
		this.gruppi=new ArrayList<>();
		this.gruppi.add("Gruppo1");
		this.gruppi.add("Gruppo2");
		this.gruppi.add("Gruppo21");
		this.esitoCredenziali=true;
	}

	public boolean isEsitoCredenziali() {
		return esitoCredenziali;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public List<String> getGruppi() {
		return gruppi;
	}
	
	public String getUsername() {
		return username;
	}
	
	public List<Contenuto> getContenutiBacheca(){
		return contenutiBacheca;
	}
}


