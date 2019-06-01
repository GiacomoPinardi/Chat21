package client;

import dominioPacchetto.Contenuto;

public class HomeBacheca{
	
	private Observer observer;

	public HomeBacheca(Observer observer) {
		super();
		this.observer = observer;
	}
	
	public void aggiungiConteunuto(Contenuto contenuto){
		observer.aggiungiContenuto(contenuto);
	}
}