package client;

import java.util.List;

import dominioPacchetto.Contenuto;

public class InterfacciaUtente{
	
	private HomeGruppo homeGruppo;
	private HomeGestioneGruppi homeGestioneGruppi;
	private HomeLog homeLog;
	private HomeComunicazioneUtenteUtente homeComunicazioneUtenteUtente;
	private HomeBacheca homeBacheca;
	private HomeImpostazioni homeImpostazioni;
	private Observer observer;
	
	public InterfacciaUtente(HomeGruppo homeGruppo, HomeGestioneGruppi homeGestioneGruppi, HomeLog homeLog,
			HomeComunicazioneUtenteUtente homeComunicazioneUtenteUtente, HomeBacheca homeBacheca,
			HomeImpostazioni homeImpostazioni, Observer observer) {
		this.homeGruppo = homeGruppo;
		this.homeGestioneGruppi = homeGestioneGruppi;
		this.homeLog = homeLog;
		this.homeComunicazioneUtenteUtente = homeComunicazioneUtenteUtente;
		this.homeBacheca = homeBacheca;
		this.homeImpostazioni = homeImpostazioni;
		this.observer = observer;
	}
	
	public InterfacciaUtente(Observer observer) {
		// TODO Auto-generated constructor stub
		//solo per test grafica
		this.homeGruppo=new HomeGruppo(observer);
		this.homeGestioneGruppi=new HomeGestioneGruppi(observer);
		this.homeLog= new HomeLog(observer);
		this.homeComunicazioneUtenteUtente=new HomeComunicazioneUtenteUtente(observer);
		this.homeBacheca=new HomeBacheca(observer);
		this.homeImpostazioni=new HomeImpostazioni(observer);
		this.observer=observer;
	}

	public void allertWindow(String allert) {
		observer.allertWindow(allert);
	}
	
	public void aggiungiConteunuto(Contenuto contenuto){
		switch (contenuto.getTipoDestinatario()) {
		case BACHECA:
			homeBacheca.aggiungiConteunuto(contenuto);
			break;
		case GRUPPO:
			homeGruppo.aggiungiConteunuto(contenuto);
			break;
		case UTENTE:
			homeComunicazioneUtenteUtente.aggiungiConteunuto(contenuto);
			break;
		}
	}
	
	public void setUIGestioneGruppi(List<String> gruppi) {
		homeGestioneGruppi.setUIGestioneGruppi(gruppi);
	}
	
	public void setUIGestioneUtenti(List<String> utenti) {
		homeImpostazioni.setUIGestioneUtenti(utenti);
	}
	
	public void setUIGestioneUtentiGruppo(List<String> utentiGruppo) {
		homeGestioneGruppi.setUIGestioneUtentiGruppo(utentiGruppo);
	}
	
	public void setUIGestioneUtentiNonInGruppo(List<String> utentiNonInGruppo) {
		homeGestioneGruppi.setUIGestioneUtentiNonInGruppo(utentiNonInGruppo);
	}
	
	public void setLog(String log) {
		homeLog.setLog(log);
	}
	
	public void setAnomalie(String anomalie) {
		homeLog.setAnomalie(anomalie);
	}
}