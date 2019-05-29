package client;

import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.List;

import dominioPacchetto.Pacchetto;
import dominioServer.Ruolo;
import dominioPacchetto.Contenuto;

public class Observer{
	private ObjectOutputStream outStream;
	private InformazioniSessione infoSessione;
	private InterfacciaUtente interfacciaUtente;

	public Observer(ObjectOutputStream outStream, InformazioniSessione infoSessione, InterfacciaUtente interfacciaUtente){
		this.outStream=outStream;
		this.infoSessione=infoSessione;
		this.interfacciaUtente=interfacciaUtente;
	}

	private Boolean trasmettiPacchetto(Pacchetto pacchetto){
		return null;

	}

	public Boolean modificaPassword(String username, String nuovaPassword){
		return null;

	}

	public Boolean accesso(String username, String password){
		return null;

	}

	public void setInfoSessione(Ruolo ruolo,String username, List<String> gruppi){

	}

	public void disconnessione(){

	}

	public Boolean  inviaContenuto(Contenuto contenuto){
		return null;

	}

	public synchronized void allertWindow(String allert){

	}

	public synchronized void aggiungiContenuto(Contenuto contenuto){

	}
	
	public void rimuoviGruppo(String nomeGruppo) {
		
	}

	public void aggiungiGruppi(List<String> nomeGruppi) {
		
	}

	public List<String> getUtenti(){
		return null;
		
	}
	
	public List<String> getGruppi(){
		return null;
		
	}
	
	public List<String> getUtentiGruppo(String nomeGruppo){
		return null;
		
	}
	
	public List<String> getUtentiNonInGruppo(String nomeGruppo){
		return null;
		
	}
	
	public Boolean aggiungiUtente(String username) {
		return null;
		
	}
	
	public Boolean eliminaUtente(String username) {
		return null;
		
	}
	
	public Boolean creaGruppo(String nomeGruppo) {
		return null;
		
	}
	
	public Boolean eliminaGruppo(String nomeGruppo) {
		return null;
		
	}
	
	public Boolean aggiungiUtenteGruppo(String username, String nomeGruppo) {
		return null;
		
	}
	
	public Boolean eliminaUtenteGruppo(String username, String nomeGruppo) {
		return null;
		
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
	
	public String getLog(LocalDate day) {
		return null;
		
	}
	
	public void setAnomalie(String anomalie) {
		
	}
	
	public String getAnomalie(LocalDate day) {
		return null;
		
	}
}