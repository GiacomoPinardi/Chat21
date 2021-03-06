package controllerServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

import dominioServer.Gruppo;
import dominioServer.Utente;

public class MainServer {

	public static void main(String[] args) {
		
		// cosi lo posso runnare da eclipse
		int port = 55555;
		
// 		int port = -1;
//		try {
//			// controllo argomenti
//			if (args.length == 1) {				
//				port = Integer.parseInt(args[0]);				
//				if (port < 1024 || port > 65535) {
//					System.out.println("Chat21Server: la porta deve essere compresa tra 1024 e 65535! Termino.");
//				    System.exit(1);
//				}
//			}
//			else {
//				System.out.println("Usage: java -jar Chat21Server.jar serverPort");
//				System.exit(2);
//			}
//		}
//		catch (Exception e) {
//			System.out.println("Chat21Server: problema con gli argomenti:");
//			e.printStackTrace();
//			System.exit(3);
//		}
		
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
					//clientSocket.setSoTimeout(30000); //timeout altrimenti server sequenziale si sospende
					clientSocket.setSoTimeout(900000000);
					System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress());
						
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
					
					Thread tc = new Thread(new ThreadClient(ca, cgg, cgu, cscg, cscb, cl, clientSocket));
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
		finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Utenti inizializzaUtenti(ControllerDB cdb) {
		Utenti utenti = new Utenti();
		
		List<Utente> lu = cdb.getUtenti();
		
		utenti.lockList();
		for (Utente u : lu) {
			utenti.aggiungi(u);
		}
		utenti.unlockList();
		
		return utenti;		
	}
	
	private static Gruppi inizializzaGruppi(ControllerDB cdb) {
		Gruppi gruppi = new Gruppi();
		
		List<Gruppo> lg = cdb.getGruppi();
		
		gruppi.lockList();
		for (Gruppo g : lg) {
			gruppi.aggiungi(g);
		}
		gruppi.unlockList();
		
		return gruppi;
	}
	
	
}
