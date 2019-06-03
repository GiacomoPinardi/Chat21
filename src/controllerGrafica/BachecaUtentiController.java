package controllerGrafica;

import java.util.List;

import javafx.fxml.FXML;

import javafx.scene.control.TextArea;

public class BachecaUtentiController {
	@FXML
	private TextArea corpoBacheca;
	
	public void aggiungiMessaggio(String messaggio) {
		corpoBacheca.appendText(messaggio);
	}
	
	public void aggiungiMessaggi(List<String> messaggi) {
		for(String messaggio : messaggi) {
			corpoBacheca.appendText(messaggio);
		}
	}
}
