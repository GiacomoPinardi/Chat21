package client;

import java.io.IOException;

import dominioServer.Ruolo;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	
	private TabPane tabs;
	private TextField textUser;
	private PasswordField textPassword;
	private Observer observer;
	private InformazioniSessione infoSessione;
	private Stage stage;
	private InterfacciaUtente interfacciaUtente;

	@Override
	public void start(Stage stage) throws Exception {
		//uso costruttori vuoti solo per test
		observer=new Observer();
		interfacciaUtente=new InterfacciaUtente(this.observer);
		infoSessione=new InformazioniSessione();
		this.stage=stage;
		this.stage.setTitle("Chat21");
		VBox login=new VBox();
		Label chat21=new Label("Chat21");
		login.getChildren().add(chat21);
		HBox user=new HBox();
		Label userName=new Label("Username:");
		textUser=new TextField();
		user.getChildren().addAll(userName,textUser);
		login.getChildren().add(user);
		HBox psw=new HBox();
		Label password=new Label("Password:");
		textPassword=new PasswordField();
		psw.getChildren().addAll(password,textPassword);
		login.getChildren().add(psw);
		Button accedi=new Button("Accedi");
		accedi.setOnAction(this::accediHandle);
		login.getChildren().add(accedi);
		Scene scene = new Scene(login,Color.WHITE);
		this.stage.setScene(scene);
		this.stage.show();
		
		
		
	}
	
	private void accediHandle(ActionEvent event){ 
		//parla con il server che andr� a verificare i dati dell'accesso, infine cambia la grafica e mette la bacheca se corretti i dati
		observer.accesso(textUser.getText(), textPassword.getText());
		//forse ci vuole una piccola sleep per fare si che il pacchetto di risposta all'accesso torni indietro
		if(infoSessione.getRuolo()!=null) {
			//accesso con successo cambia la scena in homeBacheca
			//ma prima prepara tutte le tab
			tabs=new TabPane();
			Tab tImpostazioni=new Tab("Impostazioni");
			Tab tBacheca=new Tab("Bacheca");
			if(infoSessione.getRuolo()==Ruolo.AMMINISTRATORE) {
				Tab tGestioneGruppi=new Tab("GestioneGruppi");
				try {
					tImpostazioni.setContent(FXMLLoader.load(getClass().getResource("ImpostazioniAmministratore.fxml")));
					tGestioneGruppi.setContent(FXMLLoader.load(getClass().getResource("BestioneGruppi.fxml")));
					tBacheca.setContent(FXMLLoader.load(getClass().getResource("BachecaAmministratori.fxml")));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tabs.getTabs().add(tGestioneGruppi);
			}else {
				try {
					tImpostazioni.setContent(FXMLLoader.load(getClass().getResource("ImpostazioniUtente.fxml")));
					tBacheca.setContent(FXMLLoader.load(getClass().getResource("BachecaUtenti.fxml")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			tabs.getTabs().addAll(tBacheca,tImpostazioni);
			if(infoSessione.getGruppi()!=null)
				for(String nomeGruppo : infoSessione.getGruppi()) {
					tabs.getTabs().add(new Tab(nomeGruppo));//dopo preparo le tab dei gruppi
					try {
						tabs.getTabs().get(tabs.getTabs().size() - 1).setContent(FXMLLoader.load(getClass().getResource("Gruppo.fxml")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			stage.setScene((new Scene(tabs, Color.WHITE)));
		}else{
			//accesso fallito mostra di nuovo la scena di login
			textUser.clear();
			textPassword.clear();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
