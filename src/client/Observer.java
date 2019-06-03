package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Ruolo;
import dominioPacchetto.Contenuto;
import dominioPacchetto.Operazione;

public class Observer{
	private ObjectOutputStream sockOut;
	private client.InformazioniSessione informazioniSessione;
	private InterfacciaUtente interfacciaUtente;

	public Observer(ObjectOutputStream outStream, client.InformazioniSessione infoSessione, InterfacciaUtente interfacciaUtente){
		this.sockOut=outStream;
		this.informazioniSessione=infoSessione;
		this.interfacciaUtente=interfacciaUtente;
	}
	public Observer() {
		// TODO Auto-generated method stub
		//solo per test grafica
		try {
			sockOut=new ObjectOutputStream(new FileOutputStream(new File("prova.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		informazioniSessione=new client.InformazioniSessione();
		interfacciaUtente=new InterfacciaUtente(this);
	}

	public void trasmettiPacchetto(Pacchetto pacchetto){
		try {
			sockOut.writeObject(pacchetto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void modificaPassword(String vecchiaPassword, String nuovaPassword){
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(vecchiaPassword, nuovaPassword, null), TipoInfo.CAMBIA_PASSWORD));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void accesso(String username, String password){
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, password, null), TipoInfo.ACCESSO));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setInfoSessione(Ruolo ruolo,String username, List<String> gruppi){
		informazioniSessione = new client.InformazioniSessione(username, gruppi, ruolo);
	} // sono fatti prima o dopo la creazione del thread?

	public void disconnessione(){
		informazioniSessione = null;
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.DISCONNETTI));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// GRAFICA
	}

	public void  inviaContenuto(Contenuto contenuto){
		try {
			sockOut.writeObject(new Pacchetto(contenuto, TipoInfo.CONTENUTO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void allertWindow(String allert){

	}

	public synchronized void aggiungiContenuto(Contenuto contenuto){

	}
	
	public void rimuoviGruppo(String nomeGruppo) {
		informazioniSessione.deleteGroup(nomeGruppo);
	}

	public void aggiungiGruppo(String nomeGruppo) {
		informazioniSessione.addGroup(nomeGruppo);
	}

	public void getUtenti(){
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getGruppi(){
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_GRUPPI));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUtentiGruppo(String nomeGruppo){
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUtentiNonInGruppo(String nomeGruppo){
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
	
	public void eliminaUtente(String username) {

	}
	
	public void creaGruppo(String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(nomeGruppo, null, null), TipoInfo.CREA_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminaGruppo(String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(nomeGruppo, null, null), TipoInfo.ELIMINA_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aggiungiUtenteGruppo(String username, String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, nomeGruppo, null), TipoInfo.AGG_UTENTE_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminaUtenteGruppo(String username, String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, nomeGruppo, null), TipoInfo.ELIMINA_UTENTE_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setUIGestioneGruppi(List<String> gruppi) {
		
	}
	
	public void setUIGestioneUtenti(List<String> utenti) {
		
	}
	
	public void setUIGestioneUtentiGruppo(List<String> utentiGruppo) {
		
	}
	
	public void setUIGestioneUtentiNonInGruppo(List<String> utentiNonInGruppo) {
		
	}
	
	public void setLog(String log) {
		
	}
	
	public void getLog(Date date) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(date.toString(), null, null), TipoInfo.VISUALIZZA_LOG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setAnomalie(String anomalie) {
		
	}
	
	public void getAnomalie(Date date) {

	}
}