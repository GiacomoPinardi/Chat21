

import java.util.List;

public class HomeGestioneGruppi{
	
	private Observer observer;

	public HomeGestioneGruppi(Observer observer) {
		super();
		this.observer = observer;
	}
	
	public void setUIGestioneGruppi(List<String> gruppi) {
		observer.setUIGestioneGruppi(gruppi);
	}
	
	public void setUIGestioneUtentiGruppo(List<String> utentiGruppo) {
		observer.setUIGestioneUtentiGruppo(utentiGruppo);
	}
	
	public void setUIGestioneUtentiNonInGruppo(List<String> utentiNonInGruppo) {
		observer.setUIGestioneUtentiNonInGruppo(utentiNonInGruppo);
	}
}