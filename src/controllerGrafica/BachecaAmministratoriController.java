package controllerGrafica;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import java.time.LocalDateTime;
import java.util.List;

import client.InformazioniSessione;
import client.Observer;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.TipoDestinatario;
import javafx.event.ActionEvent;

import javafx.scene.control.TextArea;

public class BachecaAmministratoriController {
	@FXML
	private TextArea corpoBacheca;
	@FXML
	private TextArea inserisciBacheca;
	@FXML
	private Button invia;
	private Observer observer;
	private InformazioniSessione informazioniSessione;
	
	public BachecaAmministratoriController(Observer observer,InformazioniSessione informazioniSessione) {
		super();
		this.informazioniSessione=informazioniSessione;
		this.observer = observer;
	}
	
	// Event Listener on Button[#invia].onAction
	@FXML
	public void handlerPubblicaInBacheca(ActionEvent event) {
		corpoBacheca.appendText(informazioniSessione.getUsername()+" : "+inserisciBacheca.getText());
		//bisogna inviare al server
		observer.inviaContenuto(new MessaggioTestuale(TipoDestinatario.BACHECA,LocalDateTime.now(),informazioniSessione.getUsername(),"Bacheca",inserisciBacheca.getText()));
		inserisciBacheca.setText("");
	}
	
	public void aggiungiMessaggio(String messaggio) {
		corpoBacheca.appendText(messaggio);
	}
	
	public void aggiungiMessaggi(List<String> messaggi) {
		for(String messaggio : messaggi) {
			corpoBacheca.appendText(messaggio);
		}
	}
}
