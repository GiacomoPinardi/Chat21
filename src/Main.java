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

public class Main extends Application {

	private TextField textUser;
	private PasswordField textPassword;
	private Observer observer;
	private Stage stage;
	private InterfacciaUtente ui;

	@Override
	public void start(Stage stage) throws Exception {
		// uso costruttori vuoti solo per test
		textPassword = new PasswordField();
		textUser = new TextField();
		observer = new Observer(textUser,textPassword);
		ui = new InterfacciaUtente(this.observer);
		observer.setUI(ui);
		//infoSessione = new InformazioniSessione();
		
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
		this.stage.setScene(scene);
		this.stage.show();

	}

	private void accediHandle(ActionEvent event) {
		// parla con il server che andr� a verificare i dati dell'accesso
		observer.accesso(textUser.getText(), textPassword.getText());
	}

	public static void main(String[] args) {
		launch(args);
	}

}
