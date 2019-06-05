

import java.io.IOException;
import java.io.ObjectInputStream;

import dominioPacchetto.Contenuto;
import dominioPacchetto.InfoSessione;
import dominioPacchetto.ListaString;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoDestinatario;


public class ThreadServer implements Runnable{
	 
	 private ObjectInputStream inStream;
	 private Observer observer;

	 public ThreadServer(Observer observer, ObjectInputStream inStream){
	 	this.inStream=inStream;
	 	this.observer=observer;
	 }

	 public void run() {
	 	while(true){
	 		try{
	 			spacchetta((Pacchetto)inStream.readObject());
	 		}
	 		catch( IOException e){

	 		}
	 		catch(ClassNotFoundException e2){

	 		}
	 	}
	 }

	 public void spacchetta(Pacchetto pacchetto){
		 switch (pacchetto.getTipo()) {
		case INFO_SESSIONE:
			InfoSessione infoSessione=(InfoSessione) pacchetto.getInformazione();
			this.observer.setInfoSessione(infoSessione.getRuolo(), infoSessione.getUsername(), 
					infoSessione.getGruppi(),infoSessione.isEsitoCredenziali(),infoSessione.getContenutiBacheca());
			break;
		case LISTA_GRUPPI:
			ListaString gruppi=(ListaString) pacchetto.getInformazione();
			observer.setUIGestioneGruppi(gruppi.getListaContenuti());
			break;
		case CONTENUTO:
			Contenuto contenuto=(Contenuto) pacchetto.getInformazione();
			observer.aggiungiContenuto(contenuto);
			break;
		default:
			break;
		}
	 }
}