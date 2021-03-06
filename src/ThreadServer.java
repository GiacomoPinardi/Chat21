

import java.io.IOException;
import java.io.ObjectInputStream;

import dominioPacchetto.Conferma;
import dominioPacchetto.Contenuto;
import dominioPacchetto.Inizializzazione;
import dominioPacchetto.ListaContenuti;
import dominioPacchetto.ListaString;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.Pacchetto;
import javafx.application.Platform;


public class ThreadServer implements Runnable{
	 
	 private ObjectInputStream inStream;
	 private Observer observer;

	 public ThreadServer(Observer observer, ObjectInputStream inStream){
	 	this.inStream = inStream;
	 	this.observer = observer;
	 }

	 public void run() {
	 	while(true){
	 		try{
	 			//System.out.println("ready to spacket");
	 			Pacchetto p = (Pacchetto) inStream.readObject();	 			
	 			spacchetta(p);
	 		}
	 		catch(IOException e){
	 		}
	 		catch(ClassNotFoundException e2){
	 		}
	 	}
	 }

	 public void spacchetta(Pacchetto pacchetto){
		// System.out.println("spacketing");
		 switch (pacchetto.getTipo()) {
			 case INFO_SESSIONE:
				Inizializzazione initPack = (Inizializzazione) pacchetto.getInformazione();
								
				Platform.runLater( () -> {
					observer.setInfoSessione(initPack.getRuolo(), initPack.getUsername(), 
							initPack.getGruppi(), initPack.isEsitoCredenziali(), initPack.getContenutiBacheca());
				});
				
				break;
			 case LISTA_GRUPPI:
				ListaString gruppi=(ListaString) pacchetto.getInformazione();
				Platform.runLater( () -> {
					observer.setUIGestioneGruppi(gruppi.getListaContenuti());
				});
				break;
			 case CONTENUTO:
				Contenuto contenuto=(Contenuto) pacchetto.getInformazione();
				Platform.runLater( () -> {
					observer.aggiungiContenuto(contenuto);
				});
				break;
			 case CONFERMA:
				 Conferma conferma = (Conferma) pacchetto.getInformazione();
				 Platform.runLater( () -> {
						observer.alertWindow(conferma.getMessaggioConferma());
					});
				 break;
			 case DISCONNETTI:
				 Platform.runLater( () -> {
						observer.disconnessione();
					});
				 break;
			 case CREA_GRUPPO:
				 Platform.runLater( () -> {
						observer.aggiungiGruppo(((MessaggioTestuale)pacchetto.getInformazione()).getDestinario());
					});
				 break;
			 case VISUALIZZA_LOG:
				 Platform.runLater( () -> {
						observer.setLog(((ListaString) pacchetto.getInformazione()).getListaContenuti());
					});
				 break;
			 case LISTA_UTENTI:
				 Platform.runLater( () -> {
						observer.setUIGestioneUtenti(((ListaString) pacchetto.getInformazione()).getListaContenuti());
					});
				 break;
			 case LISTA_UTENTI_GRUPPO:
			 case LISTA_UTENTI_NON:
				 Platform.runLater( () -> {
					 	ListaString ls =  (ListaString) pacchetto.getInformazione();
					 	
					 	/*for (String s : ls.getListaContenuti()) {
					 		System.out.println(s);
					 	}
					 	System.out.println("");*/
					 	
						observer.setUIGestioneUtentiGruppo(ls.getListaContenuti());
					});
				 break;
			 case CONTENUTI_GRUPPO:
				 Platform.runLater( () -> {
					 	ListaContenuti lc =  (ListaContenuti) pacchetto.getInformazione();
						observer.aggiornaGruppo(lc.getListaContenuti());
					});
				 break;
			default:
				System.out.println("pacchetto incognito: " + pacchetto.getTipo().toString());
				break;
		}
	 }
}