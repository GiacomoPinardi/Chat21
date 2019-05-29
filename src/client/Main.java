package client;

import java.io.ObjectOutputStream;

import dominioPacchetto.InfoSessione;
import dominioServer.Ruolo;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	
	private TabPane tabs;
	private TextField textUser;
	private PasswordField textPassword;
	private Observer observer;
	private InfoSessione infoSessione;
	private Stage stage;
	private InterfacciaUtente interfacciaUtente;

	@Override
	public void start(Stage stage) throws Exception {
		//uso costruttori vuoti solo per test
		observer=new Observer();
		interfacciaUtente=new InterfacciaUtente();
		infoSessione=new InfoSessione();
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
		if(observer.accesso(textUser.getText(), textPassword.getText()) == null) {
			//accesso con successo cambia la scena in homeBacheca
			tabs=new TabPane();
			
			Tab tImpostazioni=new Tab("Impostazioni");
			tabs.getTabs().add(tImpostazioni);
			if(infoSessione.getRuolo()==Ruolo.AMMINISTRATORE) {
				Tab tGestioneGruppi=new Tab("GestioneGruppi");
				tabs.getTabs().add(tGestioneGruppi);
			}
			Tab tBacheca=new Tab("Bacheca");
			Tab tChatUtenti=new Tab("ChatUtenti");
			tabs.getTabs().addAll(tBacheca,tChatUtenti);
			if(infoSessione.getGruppi()!=null)
				for(String nomeGruppo : infoSessione.getGruppi()) {
					tabs.getTabs().add(new Tab(nomeGruppo));//dopo preparo le tab dei gruppi
					tabs.getTabs().get(tabs.getTabs().size() - 1).setContent(new TextArea());;
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
