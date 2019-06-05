

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;

public class ImpostazioniUtenteController implements Initializable{
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
	
	private Observer observer;

	// Event Listener on Button[#effettuaDisconnesione].onAction
	@FXML
	public void handlerEffettuaDisconnesione(ActionEvent event) {
		observer.disconnessione();
	}
	// Event Listener on Button[#modificaPassword].onAction
	@FXML
	public void handlerModificaPassword(ActionEvent event) {
		if(nuovaPassword.getText().equals(ripetiNuovaPassword.getText())) {
			observer.modificaPassword(vecchiaPassword.getText(), nuovaPassword.getText());
			vecchiaPassword.clear();
			nuovaPassword.clear();
			ripetiNuovaPassword.clear();
		}else {
			observer.allertWindow("I campi in cui è stata inserita la nuova password non coincidono");
			vecchiaPassword.clear();
			nuovaPassword.clear();
			ripetiNuovaPassword.clear();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.observer=(Observer)((MyResourceBundle) arg1).getObject("observer");
	}
}
