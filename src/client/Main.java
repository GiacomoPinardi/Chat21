package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Chat21");
		VBox login=new VBox();
		Label chat21=new Label("Chat21");
		login.getChildren().add(chat21);
		HBox user=new HBox();
		Label userName=new Label("Username:");
		TextField textUser=new TextField();
		user.getChildren().addAll(userName,textUser);
		login.getChildren().add(user);
		HBox psw=new HBox();
		Label password=new Label("Password:");
		PasswordField textPassword=new PasswordField();
		psw.getChildren().addAll(password,textPassword);
		login.getChildren().add(psw);
		Button accedi=new Button("Accedi");
		accedi.setOnAction(this::accediHandle);
		BorderPane base=new BorderPane();// sopra hbox con le tab che aggiornano il pane centrale
		
		
	}
	
	private void accediHandle(ActionEvent event){ 
		//parla con il server che andrà a verificare i dati dell'accesso, infine cambia la grafica e mette la bacheca se corretti i dati
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
