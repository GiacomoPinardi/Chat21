package dominioPacchetto;

import java.util.ArrayList;
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

	public InfoSessione() {
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
	
	
	
}
