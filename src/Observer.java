
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dominioPacchetto.Contenuto;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.Operazione;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;
import dominioServer.Ruolo;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Observer {
	private ObjectOutputStream sockOut;
	private InformazioniSessione informazioniSessione;
	private TextField textUser;
	private PasswordField textPassword;
	//private InterfacciaUtente ui;
	private Stage stage;
	private TabPane tabs;
	private TextArea corpoBacheca;
	private Map<String, TextArea> areeGruppi;
	private TextArea areaLog;
	private ListView<String> listaUtenti;
	private ListView<String> listaGruppi;
	private ListView<String> listaUtentiGruppo;
	private boolean aggEli = false;
	
	private Scene sceneLogin;
	private DateTimeFormatter dateTimeFormatter;

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
		//ui = null;

		this.textUser = textUser;
		this.textPassword = textPassword;
		this.sockOut = sockOut;
		this.areeGruppi = new HashMap<>();
		this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	}

	public void setStage(Stage stage) {
		this.stage = stage;
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
			tImpostazioni.setClosable(false);
			Tab tBacheca = new Tab("Bacheca");
			tBacheca.setClosable(false);
			
			tabs.getTabs().add(tImpostazioni);
			if (informazioniSessione.getRuolo() == Ruolo.AMMINISTRATORE) {
				
				Tab tGestioneGruppi = new Tab("GestioneGruppi");
				tGestioneGruppi.setClosable(false);
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
				tGestioneGruppi.setOnSelectionChanged(new EventHandler<Event>() {	
					@Override
					public void handle(Event event) {
						richiediGruppi();
					}
				});
				tImpostazioni.setOnSelectionChanged(new EventHandler<Event>() {	
					@Override
					public void handle(Event event) {
						richiediUtenti();
					}
				});
				richiediUtenti();
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
			String res = "";
			for(Contenuto c : contenutiBacheca) {
				MessaggioTestuale m = (MessaggioTestuale) c;
				res += m.getMittente() + " : " + m.getMessaggio() + "\n";
				//areeGruppi.get(destinatario).appendText(m.getMittente() + " : " + m.getMessaggio() + "\n");
			}
			corpoBacheca.setText(res);
			
			tabs.getTabs().add(tBacheca);
			if (informazioniSessione.getGruppi() != null)
				for (String nomeGruppo : informazioniSessione.getGruppi()) {
					Tab tGruppo = new Tab(nomeGruppo);
					tGruppo.setClosable(false);
					tabs.getTabs().add(tGruppo);
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
			alertWindow("le credenziali non sono corrette");
			
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
	
	public void aggiornaGruppo(List<Contenuto> listaContenuti) {
	String res = "";
	if (!listaContenuti.isEmpty()) {
			String destinatario = listaContenuti.get(0).getDestinario();
			if (areeGruppi.containsKey(destinatario)) {
				for (Contenuto c : listaContenuti) {
					MessaggioTestuale m = (MessaggioTestuale) c;
					areeGruppi.get(destinatario).clear();
					res += m.getMittente() + " : " + m.getMessaggio() + "\n";
					//areeGruppi.get(destinatario).appendText(m.getMittente() + " : " + m.getMessaggio() + "\n");
				}
				areeGruppi.get(destinatario).setText(res);
			} else {
				System.out.println("NON C'� il gruppo!");
			}
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
	
	public void richiediGruppi() {
		trasmettiPacchetto(new Pacchetto(new Operazione(), TipoInfo.LISTA_GRUPPI));
	}

	public void setUIGestioneGruppi(List<String> gruppi) {
		this.listaGruppi.getItems().clear();
		this.listaGruppi.getItems().addAll(gruppi);
	}
	
	public void richiediUtenti() {
		trasmettiPacchetto(new Pacchetto(new Operazione(), TipoInfo.LISTA_UTENTI));
	}
	
	public void setUIGestioneUtenti(List<String> utenti) {
		this.listaUtenti.getItems().clear();
		this.listaUtenti.getItems().addAll(utenti);
	}
	
	public void richiediUtentiGruppo(String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo), TipoInfo.LISTA_UTENTI_GRUPPO));
		aggEli = false;
	}

	public void setUIGestioneUtentiGruppo(List<String> utentiGruppo) {		
		this.listaUtentiGruppo.getItems().clear();
		this.listaUtentiGruppo.getItems().addAll(utentiGruppo);
	}
	
	public void richiediUtentiNonInGruppo (String nomeGruppo) {
		trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo), TipoInfo.LISTA_UTENTI_NON));
		aggEli = true;
	}

	public void setLog(List<String> list) {
		String res = "";
		for (String s : list) 
			res += s + "\n";
		System.out.println("res: " + res);
		areaLog.setText(res);;
	}

	public void getLog(LocalDate localDate) {
		
		try {
			sockOut.writeObject(
					new Pacchetto(new Operazione(dateTimeFormatter.format(localDate)), TipoInfo.VISUALIZZA_LOG));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public void setTextLog(TextArea areaLog) {
		this.areaLog = areaLog;
	}

	public void setListaUtenti(ListView<String> listaUtenti) {
		this.listaUtenti = listaUtenti;
		
	}
	
	public void setListaGruppi(ListView<String> listaGruppi) {
		this.listaGruppi = listaGruppi;
	}
		
	public void setListaUtentiGruppo (ListView<String> lug) {
		this.listaUtentiGruppo = lug;
		this.listaUtentiGruppo.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2) {
					String nomeGruppo = listaGruppi.getSelectionModel().getSelectedItem();
					String username = listaUtentiGruppo.getSelectionModel().getSelectedItem();
					if (aggEli) {
						trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo, username), TipoInfo.AGG_UTENTE_GRUPPO));
						richiediUtentiNonInGruppo(nomeGruppo);
					}
					else {
						trasmettiPacchetto(new Pacchetto(new Operazione(nomeGruppo, username), TipoInfo.ELIMINA_UTENTE_GRUPPO));
						richiediContenutiGruppo(nomeGruppo);
					}
				}
			}
		});
	}
}