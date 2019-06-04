

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import client.InformazioniSessione;
import client.Observer;
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
