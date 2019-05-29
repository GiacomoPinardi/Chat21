package controllerServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainServer {

	public static void main(String[] args) {
		
		int port = -1;
		
		try {
			// controllo argomenti
			if (args.length == 1) {				
				port = Integer.parseInt(args[0]);				
				if (port < 1024 || port > 65535) {
					System.out.println("Chat21Server: la porta deve essere compresa tra 1024 e 65535! Termino.");
				    System.exit(1);
				}
			}
			else {
				System.out.println("Usage: java -jar Chat21Server.jar serverPort");
				System.exit(2);
			}
		}
		catch (Exception e) {
			System.out.println("Chat21Server: problema con gli argomenti:");
			e.printStackTrace();
			System.exit(3);
		}
		
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);
			
			System.out.println("Chat21Server: avviato!");
		}
		catch (Exception e) {
			System.out.println("Chat21Server: problemi nella creazione della server socket: ");
			e.printStackTrace();
			System.exit(4);
		}
		
		ControllerDB cdb = new ControllerDB();
		
		Utenti utenti = inizializzaUtenti(cdb);
		Gruppi gruppi = inizializzaGruppi(cdb);
		
		ControllerAutenticazione ca = new ControllerAutenticazione(utenti, gruppi, cdb);
		ControllerGestioneGruppi cgg = new ControllerGestioneGruppi(utenti, gruppi, cdb);
		ControllerGestioneUtenti cgu = new ControllerGestioneUtenti(utenti, gruppi, cdb);
		ControllerScambiaContenutiGruppi cscg = new ControllerScambiaContenutiGruppi(utenti, gruppi, cdb);
		ControllerScambiaContenutiBacheca cscb = new ControllerScambiaContenutiBacheca(utenti, cdb);
		ControllerLog cl = new ControllerLog(utenti);
		
		Socket clientSocket = null;
		
		try {
			while (true) {				
				try {
						
					clientSocket = serverSocket.accept(); // bloccante
					clientSocket.setSoTimeout(30000); //timeout altrimenti server sequenziale si sospende
						
				}
				catch (SocketTimeoutException te) {
					System.out.println("Chat21Server: non ho ricevuto nulla dal client per 30 secondi, interrompo la comunicazione e accetto nuove richieste.");
					continue; // il server continua a fornire il servizio ricominciando dall'inizio
				}
				catch (Exception e) {
					System.out.println("Chat21Server: problemi nell'accettazione della connessione: ");
					e.printStackTrace();
					continue; // il server continua a fornire il servizio ricominciando dall'inizio
				}
				
				try {
					
					Thread tc = new Thread(new ThreadClient(ca, cgg, cgu, cscg, cscb, cl, clientSocket.getInputStream()));
					tc.start();
					
				}
				catch (Exception e) {
					System.err.println("Chat21Server: problemi nel server thread: ");
	    			e.printStackTrace();
	    			continue; // il server continua a fornire il servizio ricominciando dall'inizio
				}					
			} // while	
		}		
		// qui catturo le eccezioni non catturate all'interno del while in seguito alle quali il server termina l'esecuzione
	    catch (Exception e) {
	    	e.printStackTrace();
	    	// chiusura di stream e socket
	    	System.out.println("Chat21Server: termino...");
	    	System.exit(2);
	    }
	}

	private static Utenti inizializzaUtenti(ControllerDB cdb) {
		// TODO
		return null;		
	}
	
	private static Gruppi inizializzaGruppi(ControllerDB cdb) {
		// TODO
		return null;
	}
	
	
}
