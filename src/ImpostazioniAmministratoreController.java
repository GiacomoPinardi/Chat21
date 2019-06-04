

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import dominioServer.Ruolo;
import javafx.event.ActionEvent;

import javafx.scene.control.ListView;

import javafx.scene.control.TextArea;

import javafx.scene.control.PasswordField;

import javafx.scene.control.CheckBox;

import javafx.scene.control.DatePicker;

public class ImpostazioniAmministratoreController implements Initializable{
	@FXML
	private Button effettuaDisconnesione;
	@FXML
	private PasswordField vecchiaPassword;
	@FXML
	private PasswordField nuovaPassword;
	@FXML
	private PasswordField ripetiNuovaPassword;
	@FXML
	private Button modificaPassword;
	@FXML
	private TextField usernameNuovoUtente;
	@FXML
	private PasswordField passwordNuovoUtente;
	@FXML
	private CheckBox checkAmministratore;
	@FXML
	private Button aggiungiNuovoUtente;
	@FXML
	private ListView<String> listaUtenti;
	@FXML
	private Button rimuoviUtenteSelezionato;
	@FXML
	private DatePicker dataLog;
	@FXML
	private Button richiediLog;
	@FXML
	private Button richiediAnalisiLog;
	@FXML
	private TextArea textLog;
	
	private Observer observer;

	// Event Listener on Button[#effettuaDisconnesione].onAction
	@FXML
	public void handlerEffettuaDisconnesione(ActionEvent event) {
		observer.disconnessione();
	}
	// Event Listener on Button[#modificaPassword].onAction
	@FXML
	public void handlerModificaPassword(ActionEvent event) {
		if(nuovaPassword.getText().equals(ripetiNuovaPassword.getText()))
			observer.modificaPassword(vecchiaPassword.getText(), nuovaPassword.getText());
		else observer.allertWindow("I campi in cui � stata inserita la nuova password non coincidono");
	}
	// Event Listener on Button[#aggiungiNuovoUtente].onAction
	@FXML
	public void handlerAggiungiNuovoUtente(ActionEvent event) {
		if(checkAmministratore.isSelected())
			observer.aggiungiUtente(usernameNuovoUtente.getText(), passwordNuovoUtente.getText(), "Amministratore");
		else
			observer.aggiungiUtente(usernameNuovoUtente.getText(), passwordNuovoUtente.getText(), "Utente");
	}
	// Event Listener on Button[#rimuoviUtenteSelezionato].onAction
	@FXML
	public void handlerRimuoviUtenteSelezionato(ActionEvent event) {
		observer.eliminaUtente(listaUtenti.getSelectionModel().getSelectedItem());
	}
	// Event Listener on Button[#richiediLog].onAction
	@FXML
	public void handlerRichiediLog(ActionEvent event) {
		observer.getLog(dataLog.getValue());
	}
	// Event Listener on Button[#richiediAnalisiLog].onAction
	@FXML
	public void handlerRichiediAnalisiLog(ActionEvent event) {
		observer.getAnomalie(dataLog.getValue());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}
