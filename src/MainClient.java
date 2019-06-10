import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainClient extends Application {

	private TextField textUser;
	private PasswordField textPassword;
	private Observer observer;
	private Stage stage;
	//private InterfacciaUtente ui;

	@Override
	public void start(Stage stage) throws Exception {
		textPassword = new PasswordField();
		textUser = new TextField();		
		textUser.setFont(new Font(14));
		textPassword.setFont(new Font(14));
		
		InetAddress address = null;
		try {
			address = InetAddress.getByName("localhost");
			//address = InetAddress.getByName("192.168.43.144");
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
		//ui = new InterfacciaUtente(this.observer);
		//observer.setUI(ui);
		
		ThreadServer threadServer = new ThreadServer(observer, inSocket);
		Thread ts = new Thread(threadServer);
		ts.start();
		
		this.stage = stage;
		this.stage.setTitle("Chat21");
		VBox login = new VBox();
		login.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, null, null)));
		login.setMinHeight(400);
		login.setMinWidth(600);
		login.setAlignment(Pos.CENTER);
		login.setSpacing(10);
		Label chat21 = new Label("Chat21");
		chat21.setFont(new Font(50));
		chat21.setPadding(new Insets(-30, 0, 0, 0));
		login.getChildren().add(chat21);
		HBox user = new HBox();
		user.setAlignment(Pos.CENTER);
		user.setSpacing(10);
		Label userName = new Label("Username:");
		userName.setFont(new Font(14));
		user.getChildren().addAll(userName, textUser);
		login.getChildren().add(user);
		HBox psw = new HBox();
		psw.setAlignment(Pos.CENTER);
		psw.setSpacing(12);
		Label password = new Label("Password:");
		password.setFont(new Font(14));
		psw.getChildren().addAll(password, textPassword);
		login.getChildren().add(psw);
		Button accedi = new Button("Accedi");
		accedi.setFont(new Font(14));
		accedi.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, null)));
		accedi.setStyle("-fx-border-color: black;");
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
