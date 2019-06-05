

import java.io.IOException;
import java.io.ObjectInputStream;

import dominioPacchetto.Contenuto;
import dominioPacchetto.inizializzazione;
import javafx.application.Platform;
import dominioPacchetto.ListaString;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoDestinatario;


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
	 			System.out.println("ready to spacket");
	 			spacchetta((Pacchetto)inStream.readObject());
	 		}
	 		catch( IOException e){

	 		}
	 		catch(ClassNotFoundException e2){

	 		}
	 	}
	 }

	 public void spacchetta(Pacchetto pacchetto){
		 System.out.println("spacketing");
		 switch (pacchetto.getTipo()) {
		 case INFO_SESSIONE:
			inizializzazione initPack = (inizializzazione) pacchetto.getInformazione();
			Platform.runLater( () -> {
				observer.setInfoSessione(initPack.getRuolo(), initPack.getUsername(), 
						initPack.getGruppi(), initPack.isEsitoCredenziali(),initPack.getContenutiBacheca());
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
		default:
			System.out.println("pacchetto incognito");
			break;
		}
	 }
}