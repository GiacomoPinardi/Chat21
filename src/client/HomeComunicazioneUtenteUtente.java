package client;

import dominioPacchetto.Contenuto;

public class HomeComunicazioneUtenteUtente{
	
	private Observer observer;

	public HomeComunicazioneUtenteUtente(Observer observer) {
		super();
		this.observer = observer;
	}
	
	public void aggiungiConteunuto(Contenuto contenuto){
		observer.aggiungiContenuto(contenuto);
	}
}