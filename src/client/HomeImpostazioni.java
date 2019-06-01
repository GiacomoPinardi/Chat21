package client;

import java.util.List;

public class HomeImpostazioni{
	
	private Observer observer;

	public HomeImpostazioni(Observer observer) {
		super();
		this.observer = observer;
	}
	
	public void setUIGestioneUtenti(List<String> utenti) {
		observer.setUIGestioneUtenti(utenti);
	}
}