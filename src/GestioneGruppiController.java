

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class GestioneGruppiController implements Initializable{
	@FXML
	private TextField nomeNuovoGruppo;
	@FXML
	private Button aggiungiNuovoGruppo;
	@FXML
	private ListView<String> elencoGruppi;
	@FXML
	private Button rimuoviUtente;
	@FXML
	private Button aggiungiUtente;
	@FXML
	private Button rimuoviGruppoSelezionato;
	@FXML
	private ListView<String> elencoUtenti;
	private Observer observer;
	private InformazioniSessione informazioniSessione;
	private ObservableList<String> tuttiUtenti;
	
	// Event Listener on Button[#aggiungiNuovoGruppo].onAction
	@FXML
	public void handlerAggiungiNuovoGruppo(ActionEvent event) {
		observer.creaGruppo(nomeNuovoGruppo.getText());
	}
	// Event Listener on Button[#rimuoviUtente].onAction
	@FXML
	public void handlerRimuoviUtente(ActionEvent event) {
		observer.eliminaUtenteGruppo(elencoGruppi.getSelectionModel().getSelectedItem(), elencoUtenti.getSelectionModel().getSelectedItem());
	}
	// Event Listener on Button[#aggiungiUtente].onAction
	@FXML
	public void handlerAggiungiUtente(ActionEvent event) {
		//ricordarsi dal lato server di inviare a quel utente la notifica di aggiunta
		observer.aggiungiUtenteGruppo(elencoUtenti.getSelectionModel().getSelectedItem(), elencoGruppi.getSelectionModel().getSelectedItem());
	}
	// Event Listener on Button[#rimuoviGruppoSelezionato].onAction
	@FXML
	public void handlerRimuoviGruppoSelezionato(ActionEvent event) {
		observer.eliminaGruppo(elencoGruppi.getSelectionModel().getSelectedItem());
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
