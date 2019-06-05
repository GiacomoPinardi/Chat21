

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoDestinatario;
import dominioPacchetto.TipoInfo;
import dominioServer.Ruolo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import dominioPacchetto.Contenuto;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.Operazione;

public class Observer{
	private ObjectOutputStream sockOut;
	private InformazioniSessione informazioniSessione;
	private TextField textUser;
	private PasswordField textPassword;
	private InterfacciaUtente ui;
	private Stage stage;
	private TabPane tabs;
	private TextArea corpoBacheca;

//	public Observer(ObjectOutputStream outStream, client.InformazioniSessione informazioniSess, InterfacciaUtente ui){
//		this.sockOut=outStream;
//		this.informazioniSessione = informazioniSess;
//		this.ui=ui;
//	}
	
	public Observer(TextField textUser, PasswordField textPassword, ObjectOutputStream sockOut) {
		// TODO Auto-generated method stub
		//solo per test grafica
//		try {
//			sockOut = new ObjectOutputStream(new FileOutputStream(new File("prova.txt")));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.sockOut = sockOut;
		informazioniSessione = null;
		//ui = new InterfacciaUtente(this);
		ui = null;
	}
	
	public void setStage(Stage stage) {
		this.stage=stage;
	}
	
	public void setUI(InterfacciaUtente ui) {
		this.ui = ui; 
	}

	public void trasmettiPacchetto(Pacchetto pacchetto){
		try {
			sockOut.writeObject(pacchetto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void modificaPassword(String vecchiaPassword, String nuovaPassword){
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(vecchiaPassword, nuovaPassword, null), TipoInfo.CAMBIA_PASSWORD));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void accesso(String username, String password){
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, password, null), TipoInfo.ACCESSO));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//solo per test simulo che threadServer riceva il pacchetto info sessione e chiami setInfoSessione
//		ArrayList<String> gruppi=new ArrayList<String>();
//		gruppi.add("2");
//		gruppi.add("1");
//		gruppi.add("21");
//		List<Contenuto> contenutiBacheca=new ArrayList<Contenuto>();
//		contenutiBacheca.add(new MessaggioTestuale(TipoDestinatario.BACHECA, LocalDateTime.now(), "Amminisratore1", "bacheca", "io spero che vada"));
//		contenutiBacheca.add(new MessaggioTestuale(TipoDestinatario.BACHECA, LocalDateTime.now(), "Amminisratore2", "bacheca", "casomai non al primo colpo"));
//		contenutiBacheca.add(new MessaggioTestuale(TipoDestinatario.BACHECA, LocalDateTime.now(), "Amminisratore1", "bacheca", "ma cazzo deve ann�"));
//		this.setInfoSessione(Ruolo.AMMINISTRATORE, "pincopallo", gruppi, true,contenutiBacheca);
	}

	public void setInfoSessione(Ruolo ruolo, String username, List<String> gruppi, Boolean esitoAccesso, List<Contenuto> contenutiBacheca){
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tabs.getTabs().add(tGestioneGruppi);
			} else {
				try {
					tImpostazioni
							.setContent(FXMLLoader.load(getClass().getResource("ImpostazioniUtente.fxml"), bundleInit));
					tBacheca.setContent(FXMLLoader.load(getClass().getResource("BachecaUtenti.fxml"), bundleInit));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			tabs.getTabs().addAll(tBacheca, tImpostazioni);
			if (informazioniSessione.getGruppi() != null)
				for (String nomeGruppo : informazioniSessione.getGruppi()) {
					tabs.getTabs().add(new Tab(nomeGruppo));// dopo preparo le
															// tab dei gruppi
					try {
						MyResourceBundleGruppo bundleInitGruppo=new MyResourceBundleGruppo(this, informazioniSessione,nomeGruppo);
						tabs.getTabs().get(tabs.getTabs().size() - 1)
								.setContent(FXMLLoader.load(getClass().getResource("Gruppo.fxml"), bundleInitGruppo));
					} catch (IOException e) {
						// TODO Auto-generated catch block
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

	public void disconnessione(){
		informazioniSessione = null;
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.DISCONNETTI));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// GRAFICA
	}

	public void  inviaContenuto(Contenuto contenuto){
		System.out.println("hee\n");
		try {
			sockOut.writeObject(new Pacchetto(contenuto, TipoInfo.CONTENUTO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void allertWindow(String allert){

	}

	public synchronized void aggiungiContenuto(Contenuto contenuto){
		switch (contenuto.getTipoDestinatario()) {
		case GRUPPO:
			
			break;
		case BACHECA:
			MessaggioTestuale m = (MessaggioTestuale) contenuto;
			if(!m.getMittente().equals(informazioniSessione.getUsername()))
				corpoBacheca.appendText(m.getMittente()+" : "+m.getMessaggio()+"\n");
			break;
		
		case UTENTE:
			
			break;

		default:
			break;
		}
	}
	
	public void rimuoviGruppo(String nomeGruppo) {
		informazioniSessione.deleteGroup(nomeGruppo);
	}

	public void aggiungiGruppo(String nomeGruppo) {
		informazioniSessione.addGroup(nomeGruppo);
	}

	public void getUtenti(){
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getGruppi(){
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_GRUPPI));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUtentiGruppo(String nomeGruppo){
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getUtentiNonInGruppo(String nomeGruppo){
		try {
			sockOut.writeObject(new Pacchetto(null, TipoInfo.LISTA_UTENTI_NON));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aggiungiUtente(String username, String password, String ruolo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, ruolo, password), TipoInfo.AGG_UTENTE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminaUtente(String username) {

	}
	
	public void creaGruppo(String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(nomeGruppo, null, null), TipoInfo.CREA_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminaGruppo(String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(nomeGruppo, null, null), TipoInfo.ELIMINA_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void aggiungiUtenteGruppo(String username, String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, nomeGruppo, null), TipoInfo.AGG_UTENTE_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void eliminaUtenteGruppo(String username, String nomeGruppo) {
		try {
			sockOut.writeObject(new Pacchetto(new Operazione(username, nomeGruppo, null), TipoInfo.ELIMINA_UTENTE_GRUPPO));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			sockOut.writeObject(new Pacchetto(new Operazione(localDate.toString(), null, null), TipoInfo.VISUALIZZA_LOG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setAnomalie(String anomalie) {
		
	}
	
	public void getAnomalie(LocalDate localDate) {

	}

	public void setBacheca(TextArea corpoBacheca) {
		this.corpoBacheca=corpoBacheca;
	}

}