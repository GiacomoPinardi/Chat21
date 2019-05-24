package dominioPacchetto;

import java.util.List;

import dominioServer.Ruolo;

public class InfoSessione implements Info {
	private boolean esitoCredenziali;
	private Ruolo ruolo;
	private List<String> gruppi;
	
	public InfoSessione(boolean esitoCredenziali, Ruolo ruolo, List<String> gruppi) {
		this.esitoCredenziali = esitoCredenziali;
		this.ruolo = ruolo;
		this.gruppi = gruppi;
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
	
	
	
}
