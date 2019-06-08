

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.TipoDestinatario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class GruppoController implements Initializable{
	@FXML
	private TextArea corpoGruppo;
	@FXML
	private Button richiediContenuti;
	@FXML
	private TextArea textInvia;
	@FXML
	private Button invia;
	@FXML
	private Button selezionaFile;
	private InformazioniSessione informazioniSessione;
	private Observer observer;
	private String nomeGruppo;

	// Event Listener on Button[#richiediContenuti].onAction
	@FXML
	public void handlerRichiediContenuti(ActionEvent event) {
		observer.richiediContenutiGruppo(this.nomeGruppo);		
	}
	
	// Event Listener on Button[#invia].onAction
	@FXML
	public void handlerInvia (ActionEvent event) {
		aggiungiMessaggio(informazioniSessione.getUsername() + " : " + textInvia.getText() + "\n");
		observer.inviaContenuto(new MessaggioTestuale(TipoDestinatario.GRUPPO, LocalDateTime.now(), informazioniSessione.getUsername(), nomeGruppo, textInvia.getText()));
		textInvia.setText("");
	}
	
	public void aggiungiMessaggio(String messaggio) {
		corpoGruppo.appendText(messaggio);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.observer = (Observer) ((MyResourceBundleGruppo) arg1).getObject("observer");		
		this.informazioniSessione = (InformazioniSessione) ((MyResourceBundleGruppo) arg1).getObject("informazioniSessione");
		this.nomeGruppo = (String) ((MyResourceBundleGruppo) arg1).getObject("nomeGruppo");
		
		// non bisogna impostare il gruppo????
		this.observer.setTextGruppo(nomeGruppo, corpoGruppo);
		
		this.corpoGruppo.setEditable(false);			
	}
	
}
