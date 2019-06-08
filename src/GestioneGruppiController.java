
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class GestioneGruppiController implements Initializable {
	@FXML
	private TextField nomeNuovoGruppo;
	@FXML
	private Button aggiungiNuovoGruppo;
	@FXML
	private ListView<String> elencoGruppi;
	@FXML
	private ToggleButton rimuoviUtente;
	@FXML
	private ToggleButton aggiungiUtente;
	@FXML
	private Button rimuoviGruppoSelezionato;
	@FXML
	private ListView<String> elencoUtenti;
	private Observer observer;

	// Event Listener on Button[#aggiungiNuovoGruppo].onAction
	@FXML
	public void handlerAggiungiNuovoGruppo(ActionEvent event) {
		observer.creaGruppo(nomeNuovoGruppo.getText());
		observer.richiediGruppi();
	}

	// Event Listener on Button[#rimuoviUtente].onAction
	@FXML
	public void handlerRimuoviUtente(ActionEvent event) {
		aggiungiUtente.setSelected(false);
		//observer.eliminaUtenteGruppo(elencoGruppi.getSelectionModel().getSelectedItem(),
			//	elencoUtenti.getSelectionModel().getSelectedItem());
		if (rimuoviUtente.isSelected()) {
			if(elencoGruppi.getSelectionModel().getSelectedIndex() >= 0) {
				observer.richiediUtentiGruppo(elencoGruppi.getSelectionModel().getSelectedItem());
			} else {
				observer.alertWindow("Nessun gruppo selezionato");
				rimuoviUtente.setSelected(false);
			}
		}
	}
	
	@FXML
	public void handlerAggiungiUtente(ActionEvent event) {
		rimuoviUtente.setSelected(false);
		//observer.eliminaUtenteGruppo(elencoGruppi.getSelectionModel().getSelectedItem(),
			//	elencoUtenti.getSelectionModel().getSelectedItem());
		if (aggiungiUtente.isSelected()) {
			if(elencoGruppi.getSelectionModel().getSelectedIndex() >= 0) {
				observer.richiediUtentiNonInGruppo(elencoGruppi.getSelectionModel().getSelectedItem());
			} else {
				observer.alertWindow("Nessun gruppo selezionato");
				aggiungiUtente.setSelected(false);
			}
		}
	}

	// Event Listener on Button[#rimuoviGruppoSelezionato].onAction
	@FXML
	public void handlerRimuoviGruppoSelezionato(ActionEvent event) {
		observer.eliminaGruppo(elencoGruppi.getSelectionModel().getSelectedItem());
		observer.richiediGruppi();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.observer = (Observer) ((MyResourceBundle) arg1).getObject("observer");
		observer.setListaGruppi(elencoGruppi);
		observer.setListaUtentiGruppo(elencoUtenti);
	}
}
