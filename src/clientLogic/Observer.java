package clientLogic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import dominioPacchetto.Contenuto;
import dominioPacchetto.Operazione;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Ruolo;

public class Observer {
	private ObjectOutputStream sockOut;
	private InformazioniSessione informazioniSessione;
	
	public void accesso(String username, String password) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, password, null), TipoInfo.ACCESSO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setInfoSessione(String username, boolean esito, Ruolo ruolo, List<String> gruppi) {
		informazioniSessione = new InformazioniSessione(username, gruppi, ruolo);
	} // sono fatti prima o dopo la creazione del thread?
	
	// invio a server
	public void trasmettiPacchetto(Pacchetto p) {
		try {
			sockOut.writeObject(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void modificaPassword(String oldOne, String newOne) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(oldOne, newOne, null), TipoInfo.CAMBIA_PASSWORD));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnessinoe() {
		informazioniSessione = null;
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.DISCONNETTI));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// GRAFICA
	}
	
	public void inviaContenuto(Contenuto contenuto) {
		try {
			sockOut.writeObject(new Pacchetto(contenuto, TipoInfo.CONTENUTO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUtenti() {
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getGruppi() {
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_GRUPPI));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUtentiGruppp() {
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUtentiNonInGruppo() {
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI_NON));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aggiungiUtente(String username, String password, String ruolo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, ruolo, password), TipoInfo.AGG_UTENTE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void creaGruppo(String nome) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(nome, null, null), TipoInfo.CREA_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminaGruppo(String nome) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(nome, null, null), TipoInfo.ELIMINA_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aggiungiUteneGruppo(String username, String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, nomeGruppo, null), TipoInfo.AGG_UTENTE_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminaUteneGruppo(String username, String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, nomeGruppo, null), TipoInfo.ELIMINA_UTENTE_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getLog(Date date) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(date.toString(), null, null), TipoInfo.VISUALIZZA_LOG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//GRAFICA
	public void rimuoviGruppo(String nomeGruppo) {
		informazioniSessione.deleteGroup(nomeGruppo);
	}
	
	public void aggiungiGruppo(String nomeGruppo) {
		informazioniSessione.addGroup(nomeGruppo);
	}
	
	public synchronized void alertWindow(String mess) {
		
	}
	
	public synchronized void aggiungiContenuto(Contenuto contenuto) {
		
	}
	
	public void setUIGestioneGruppi(List<String> list) {
		
	}
	
	public void setUIGestioneUtenti(List<String> list) {
		
	}
	
	public void setUIGestioneUtentiGruppo(List<String> list) {
		
	}
	
	public void setUIGestioneNoninGruppo(List<String> list) {
		
	}
	
	public void setLog(String logContent) {
		
	}	
}
