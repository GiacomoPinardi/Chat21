package client;

import java.time.LocalDate;
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
	
	public void allertWindow(String allert) {
		
	}
	
	public Boolean aggiungiConteunuto(Contenuto contenuto){
		return homeGruppo.aggiungiConteunuto(contenuto);
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
	
	public void setAnomalie(String anomalie) {
		
	}
}