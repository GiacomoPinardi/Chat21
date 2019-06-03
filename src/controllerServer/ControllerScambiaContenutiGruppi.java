package controllerServer;

import java.util.List;

import dominioPacchetto.Contenuto;
import dominioPacchetto.ListaContenuti;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Utente;

public class ControllerScambiaContenutiGruppi implements IControllerScambiaContenuti {
	private ControllerDB dbConnection;
	private Utenti utenti;
	private Gruppi gruppi;
	
	public ControllerScambiaContenutiGruppi (Utenti utenti, Gruppi gruppi, ControllerDB dbConnection) {
		this.utenti = utenti;
		this.gruppi = gruppi;
		this.dbConnection = dbConnection;
	}
	
	@Override
	public void smista(Contenuto contenuto) {
		dbConnection.addContenutoGruppo((MessaggioTestuale) contenuto);
		
		Pacchetto pacchetto = new Pacchetto(contenuto, TipoInfo.CONTENUTO);
		gruppi.lockList();
		List<String> utentiGruppo = gruppi.getByNome(contenuto.getDestinario()).getUtenti();
		utenti.lockList();
		for (String username: utentiGruppo) {
			if (!username.equals(contenuto.getMittente())) {
				utenti.getByUsername(username).invia(pacchetto);
			}
			
		}
		utenti.unlockList();
		gruppi.unlockList();
	}

	public void getContenutiGruppo(String nomeGruppo, String executor) {
		ListaContenuti lista = new ListaContenuti(dbConnection.getContenutiGruppo(nomeGruppo));
		utenti.lockList();
		utenti.getByUsername(executor).invia(new Pacchetto(lista, TipoInfo.CONTENUTI_GRUPPO));
		utenti.unlockList();		
	}
}
