import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import controllerServer.ThreadClient;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainClient extends Application {

	private TextField textUser;
	private PasswordField textPassword;
	private Observer observer;
	private Stage stage;
	private InterfacciaUtente ui;

	@Override
	public void start(Stage stage) throws Exception {
		textPassword = new PasswordField();
		textUser = new TextField();		
		
		InetAddress address = null;
		try {
			//address = InetAddress.getByName("localhost");
			address = InetAddress.getByName("172.20.10.5");
		}
		catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		int port = 55555;		
		Socket socket = null;
		ObjectInputStream inSocket = null;
		ObjectOutputStream outSocket = null;
		try {					
			socket = new Socket(address, port);
			outSocket = new ObjectOutputStream(socket.getOutputStream());
			inSocket = new ObjectInputStream(socket.getInputStream());
			System.out.println("Terminata inizializzazione!");
		}
		catch (IOException e) {			
			e.printStackTrace();
			System.exit(2);
		}
		
		//infoSessione = new InformazioniSessione();
		
		observer = new Observer(textUser, textPassword, outSocket);
		ui = new InterfacciaUtente(this.observer);
		observer.setUI(ui);
		
		ThreadServer threadServer = new ThreadServer(observer, inSocket);
		Thread ts = new Thread(threadServer);
		ts.start();
		
		this.stage = stage;
		this.stage.setTitle("Chat21");
		VBox login = new VBox();
		Label chat21 = new Label("Chat21");
		login.getChildren().add(chat21);
		HBox user = new HBox();
		Label userName = new Label("Username:");
		user.getChildren().addAll(userName, textUser);
		login.getChildren().add(user);
		HBox psw = new HBox();
		Label password = new Label("Password:");
		psw.getChildren().addAll(password, textPassword);
		login.getChildren().add(psw);
		Button accedi = new Button("Accedi");
		accedi.setOnAction(this::accediHandle);
		login.getChildren().add(accedi);
		Scene scene = new Scene(login, Color.WHITE);
		observer.setStage(this.stage);
		observer.setScene(scene);
		this.stage.setScene(scene);
		this.stage.show();

	}

	private void accediHandle(ActionEvent event) {
		// parla con il server che andr� a verificare i dati dell'accesso
		observer.accesso(textUser.getText(), textPassword.getText());
		textUser.clear();
		textPassword.clear();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
