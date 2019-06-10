import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;

public class GestioneAggiornamentiController implements Initializable{
	@FXML
	private Label versioneInstallata;
	@FXML
	private Label versioneDisponibile;
	@FXML
	private Button aggiorna;
	@FXML
	private Button verificaAggiornamenti;
	
	private Observer observer;
	private InformazioniSessione informazioniSessione;

	// Event Listener on Button[#aggiorna].onAction
	@FXML
	public void aggiornaHandler(ActionEvent event) {
		this.versioneInstallata.setText(this.versioneDisponibile.getText());
		//dovrebbe aprire una pagina online da cui scaricare la nuova versione
	}
	// Event Listener on Button[#verificaAggiornamenti].onAction
	@FXML
	public void verificaAggiornamentiHandler(ActionEvent event) {
		this.versioneDisponibile.setText("Versione disponibile: 1.1");
		//si potrebbe aggiungere in infoSessione le info sulla versione disponibile qundo un utente logga(così nel metodo inizialize setto le verioni per bene)
		//ed aggiornale quando preme verifica aggiornamenti
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.observer=(Observer)((MyResourceBundle) arg1).getObject("observer");
		this.informazioniSessione = (InformazioniSessione)((MyResourceBundle) arg1).getObject("informazioniSessione");
		
	}
}
