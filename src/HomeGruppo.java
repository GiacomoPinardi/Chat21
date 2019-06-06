
import dominioPacchetto.Contenuto;

public class HomeGruppo {
		
	private Observer observer;
	
	public HomeGruppo(Observer observer) {
		super();
		this.observer = observer;		
	}
	
	public void aggiungiConteunuto(Contenuto contenuto){
		observer.aggiungiContenuto(contenuto);
	}
	
}