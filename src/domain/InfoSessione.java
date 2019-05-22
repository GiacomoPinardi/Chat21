package domain;

import java.util.ArrayList;
import java.util.List;

public class InfoSessione implements Info {

	private static final long serialVersionUID = 1L;
	
	private boolean esitoCredenziali;
	private Ruolo ruolo;
	private List<String> gruppi;
	
	public InfoSessione () {
		gruppi = new ArrayList<>();
	}
	
	public InfoSessione (boolean esitoCredenziali, Ruolo ruolo, List<String> gruppi) {
		this.esitoCredenziali = esitoCredenziali;
		this.ruolo = ruolo;
		this.gruppi = gruppi;
	}

	public boolean getEsitoCredenziali() {
		return esitoCredenziali;
	}

	public void setEsitoCredenziali(boolean esitoCredenziali) {
		this.esitoCredenziali = esitoCredenziali;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public List<String> getGruppi() {
		return gruppi;
	}

	public void setGruppi(List<String> gruppi) {
		this.gruppi = gruppi;
	}
	
}
