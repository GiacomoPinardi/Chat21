

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import dominioPacchetto.Contenuto;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.TipoDestinatario;
import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;

public class BachecaAmministratoriController implements Initializable{
	@FXML
	private TextArea corpoBacheca;
	@FXML
	private TextArea inserisciBacheca;
	@FXML
	private Button invia;
	private Observer observer;
	private InformazioniSessione informazioniSessione;

	// Event Listener on Button[#invia].onAction
	@FXML
	public void handlerPubblicaInBacheca(ActionEvent event) {
		corpoBacheca.appendText(informazioniSessione.getUsername()+" : "+inserisciBacheca.getText()+"\n");
		//bisogna inviare al server
		observer.inviaContenuto(new MessaggioTestuale(TipoDestinatario.BACHECA,LocalDateTime.now(),informazioniSessione.getUsername(),"Bacheca",inserisciBacheca.getText()));
		inserisciBacheca.setText("");
		//non aggiungo alla lista locale di contenuti bacheca perchè il server invierà a tutti gli utenti il messaggio publicato in bacheca
	}
	
	private void aggiungiMessaggio(String messaggio) {
		corpoBacheca.appendText(messaggio);
	}
	
	public void aggiungiMessaggi(List<String> messaggi) {
		for(String messaggio : messaggi) {
			corpoBacheca.appendText(messaggio);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.observer=(Observer)((MyResourceBundle) arg1).getObject("observer");
		this.informazioniSessione=(InformazioniSessione)((MyResourceBundle) arg1).getObject("informazioniSessione");
		this.observer.setBacheca(corpoBacheca);
		this.corpoBacheca.setEditable(false);
		for(Contenuto c : this.informazioniSessione.getContenutiBacheca()) {
			MessaggioTestuale m= (MessaggioTestuale) c;
			this.corpoBacheca.appendText(m.getMittente()+" : "+m.getMessaggio()+"\n");
		}
	}
}
