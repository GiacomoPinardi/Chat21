

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dominioPacchetto.Contenuto;
import dominioPacchetto.MessaggioTestuale;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;


public class BachecaUtentiController implements Initializable{
	@FXML
	private TextArea corpoBacheca;
	private Observer observer;
	private InformazioniSessione informazioniSessione;
	
	public void aggiungiMessaggio(String messaggio) {
		corpoBacheca.appendText(messaggio);
	}
	
	public void aggiungiMessaggi(List<String> messaggi) {
		for(String messaggio : messaggi) {
			aggiungiMessaggio(messaggio);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.observer=(Observer)((MyResourceBundle) arg1).getObject("observer");
		this.informazioniSessione = (InformazioniSessione)((MyResourceBundle) arg1).getObject("informazioniSessione");
		this.observer.setTextBacheca(corpoBacheca);
		this.corpoBacheca.setEditable(false);
		for(Contenuto c : this.informazioniSessione.getContenutiBacheca()) {
			MessaggioTestuale m = (MessaggioTestuale) c;
			this.corpoBacheca.appendText(m.getMittente() + " : " + m.getMessaggio() + "\n");
		}
	}
}
