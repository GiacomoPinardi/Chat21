
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoDestinatario;
import dominioPacchetto.TipoInfo;
import dominioServer.Ruolo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import dominioPacchetto.Conferma;
import dominioPacchetto.Contenuto;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.Operazione;

public class Observer {
	private ObjectOutputStream sockOut;
	private InformazioniSessione informazioniSessione;
	private TextField textUser;
	private PasswordField textPassword;
	private InterfacciaUtente ui;
	private Stage stage;
	private TabPane tabs;
	private TextArea corpoBacheca;
	private Map<String, TextArea> areeGruppi;
	private Scene sceneLogin;

	// public Observer(ObjectOutputStream outStream, client.InformazioniSessione
	// informazioniSess, InterfacciaUtente ui){
	// this.sockOut=outStream;
	// this.informazioniSessione = informazioniSess;
	// this.ui=ui;
	// }

	public Observer(TextField textUser, PasswordField textPassword, ObjectOutputStream sockOut) {
		// TODO Auto-generated method stub
		// solo per test grafica
		// try {
		// sockOut = new ObjectOutputStream(new FileOutputStream(new
		// File("prova.txt")));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		informazioniSessione = null;
		// ui = new InterfacciaUtente(this);
		ui = null;

		this.textUser = textUser;
		this.textPassword = textPassword;
		this.sockOut = sockOut;
		this.areeGruppi = new HashMap<>();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setUI(InterfacciaUtente ui) {
		this.ui = ui;
	}

	private void trasmettiPacchetto(Pacchetto pacchetto) {
		try {
			sockOut.writeObject(pacchetto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void modificaPassword(String vecchiaPassword, String nuovaPassword) {
		trasmettiPacchetto(
				new Pacchetto(new Operazione(vecchiaPassword, nuovaPassword, null), TipoInfo.CAMBIA_PASSWORD));
	}

	public void accesso(String username, String password) {
		trasmettiPacchetto(new Pacchetto(new Operazione(username, password, null), TipoInfo.ACCESSO));

		// solo per test simulo che threadServer riceva il pacchetto info
		// sessione e chiami setInfoSessione
		// ArrayList<String> gruppi=new ArrayList<String>();
		// gruppi.add("2");
		// gruppi.add("1");
		// gruppi.add("21");
		// List<Contenuto> contenutiBacheca=new ArrayList<Contenuto>();
		// contenutiBacheca.add(new MessaggioTestuale(TipoDestinatario.BACHECA,
		// LocalDateTime.now(), "Amminisratore1", "bacheca", "io spero che
		// vada"));
		// contenutiBacheca.add(new MessaggioTestuale(TipoDestinatario.BACHECA,
		// LocalDateTime.now(), "Amminisratore2", "bacheca", "casomai non al
		// primo colpo"));
		// contenutiBacheca.add(new MessaggioTestuale(TipoDestinatario.BACHECA,
		// LocalDateTime.now(), "Amminisratore1", "bacheca", "ma cazzo deve
		// ann�"));
		// this.setInfoSessione(Ruolo.AMMINISTRATORE, "pincopallo", gruppi,
		// true,contenutiBacheca);
	}

	public void setInfoSessione(Ruolo ruolo, String username, List<String> gruppi, Boolean esitoAccesso,
			List<Contenuto> contenutiBacheca) {
		informazioniSessione = new InformazioniSessione(username, gruppi, ruolo, contenutiBacheca);
		MyResourceBundle bundleInit = new MyResourceBundle(this, informazioniSessione);
		if (esitoAccesso) {
			// accesso con successo cambia la scena in homeBacheca
			// ma prima prepara tutte le tab
			tabs = new TabPane();
			Tab tImpostazioni = new Tab("Impostazioni");
			Tab tBacheca = new Tab("Bacheca");
			if (informazioniSessione.getRuolo() == Ruolo.AMMINISTRATORE) {
				Tab tGestioneGruppi = new Tab("GestioneGruppi");
				try {
					Node parent = FXMLLoader.load(getClass().getResource("ImpostazioniAmministratore.fxml"),
							bundleInit);
					tImpostazioni.setContent(parent);
					tGestioneGruppi
							.setContent(FXMLLoader.load(getClass().getResource("GestioneGruppi.fxml"), bundleInit));
					tBacheca.setContent(
							FXMLLoader.load(getClass().getResource("BachecaAmministratori.fxml"), bundleInit));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tabs.getTabs().add(tGestioneGruppi);
			} else {
				try {
					tImpostazioni
							.setContent(FXMLLoader.load(getClass().getResource("ImpostazioniUtente.fxml"), bundleInit));
					tBacheca.setContent(FXMLLoader.load(getClass().getResource("BachecaUtenti.fxml"), bundleInit));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			tabs.getTabs().addAll(tBacheca, tImpostazioni);
			if (informazioniSessione.getGruppi() != null)
				for (String nomeGruppo : informazioniSessione.getGruppi()) {
					tabs.getTabs().add(new Tab(nomeGruppo));// dopo preparo le
															// tab dei gruppi
					try {
						MyResourceBundleGruppo bundleInitGruppo = new MyResourceBundleGruppo(this, informazioniSessione,
								nomeGruppo);
						tabs.getTabs().get(tabs.getTabs().size() - 1)
								.setContent(FXMLLoader.load(getClass().getResource("Gruppo.fxml"), bundleInitGruppo));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			stage.setScene((new Scene(tabs, Color.WHITE)));
		} else {
			// accesso fallito mostra di nuovo la scena di login
			textUser.clear();
			textPassword.clear();
		}
	} // sono fatti prima o dopo la creazione del thread?

	public void disconnessione() {
		informazioniSessione = null;
		trasmettiPacchetto(new Pacchetto(null, TipoInfo.DISCONNETTI));
		stage.setScene(sceneLogin);
	}

	public void inviaContenuto(Contenuto contenuto) {
		trasmettiPacchetto(new Pacchetto(contenuto, TipoInfo.CONTENUTO));
	}

	public synchronized void alertWindow(String alertMess) {
		// Alert alert = new Alert(AlertType.WARNING, alertMess, ButtonType.OK);
		// Alert alert = new Alert(AlertType.CONFIRMATION, alert, ButtonType.OK,
		// ButtonType.NO, ButtonType.CANCEL);
		// alert.showAndWait();

		Alert info = new Alert(AlertType.INFORMATION, alertMess, ButtonType.OK);
		info.showAndWait();
	}

	public void doNothing() {
		System.out.println("nothing");
	}

	public synchronized void aggiungiContenuto(Contenuto contenuto) {
		switch (contenuto.getTipoDestinatario()) {
		case GRUPPO:

			String destinatario = contenuto.getDestinario();
			if (areeGruppi.containsKey(destinatario)) {
				MessaggioTestuale m = (MessaggioTestuale) contenuto;
				areeGruppi.get(destinatario).appendText(m.getMittente() + " : " + m.getMessaggio() + "\n");
			} else {
				System.out.println("NON C'è il gruppo!");
			}
			break;
		case BACHECA:
			MessaggioTestuale m = (MessaggioTestuale) contenuto;
			if (!m.getMittente().equals(informazioniSessione.getUsername()))
				corpoBacheca.appendText(m.getMittente() + " : " + m.getMessaggio() + "\n");
			break;

		case UTENTE:

			break;

		default:
			break;
		}
	}

	public void rimuoviGruppo(String nomeGruppo) {
		informazioniSessione.deleteGroup(nomeGruppo);
		tabs.getTabs().remove(new Tab(nomeGruppo));
	}

	public void aggiungiGruppo(String nomeGruppo) {
		informazioniSessione.addGroup(nomeGruppo);
		tabs.getTabs().add(new Tab(nomeGruppo));
		MyResourceBundleGruppo bundleInitGruppo = new MyResourceBundleGruppo(this, informazioniSessione, nomeGruppo);
		try {
			tabs.getTabs().get(tabs.getTabs().size() - 1)
					.setContent(FXMLLoader.load(getClass().getResource("Gruppo.fxml"), bundleInitGruppo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getUtenti() {
		trasmettiPacchetto(new Pacchetto(null, TipoInfo.LISTA_UTENTI));
	}

	public void getGruppi() {
		trasmettiPacchetto(new Pacchetto(null, TipoInfo.LISTA_GRUPPI));
	}

	public void getUtentiGruppo(String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo), TipoInfo.LISTA_UTENTI_GRUPPO));
	}

	public void getUtentiNonInGruppo(String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo), TipoInfo.LISTA_UTENTI_NON));
	}

	public void aggiungiUtente(String username, String password, String ruolo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(username, password, ruolo), TipoInfo.AGG_UTENTE));
	}

	public void eliminaUtente(String username) {
		trasmettiPacchetto(new Pacchetto(new Operazione(username), TipoInfo.ELIMINA_UTENTE));
	}

	public void creaGruppo(String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo), TipoInfo.CREA_GRUPPO));
	}

	public void eliminaGruppo(String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo), TipoInfo.ELIMINA_GRUPPO));
	}

	public void aggiungiUtenteGruppo(String username, String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo, username), TipoInfo.AGG_UTENTE_GRUPPO));
	}

	public void eliminaUtenteGruppo(String username, String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo, username), TipoInfo.ELIMINA_UTENTE_GRUPPO));
	}

	public void richiediContenutiGruppo(String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo), TipoInfo.RICHIESTA_CONTENUTI));
	}

	public void setUIGestioneGruppi(List<String> gruppi) {

	}

	public void setUIGestioneUtenti(List<String> utenti) {

	}

	public void setUIGestioneUtentiGruppo(List<String> utentiGruppo) {

	}

	public void setUIGestioneUtentiNonInGruppo(List<String> utentiNonInGruppo) {

	}

	public void setLog(String log) {

	}

	public void getLog(LocalDate localDate) {
		try {
			sockOut.writeObject(
					new Pacchetto(new Operazione(localDate.toString(), null, null), TipoInfo.VISUALIZZA_LOG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setAnomalie(String anomalie) {

	}

	public void getAnomalie(LocalDate localDate) {

	}

	public void setTextBacheca(TextArea corpoBacheca) {
		this.corpoBacheca = corpoBacheca;
	}

	public void setScene(Scene sceneLogin) {
		this.sceneLogin = sceneLogin;
	}

	public void setTextGruppo(String nomeGruppo, TextArea corpoGruppo) {
		areeGruppi.put(nomeGruppo, corpoGruppo);

	}

}