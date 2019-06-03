package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import dominioPacchetto.InfoSessione;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.Operazione;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoDestinatario;
import dominioPacchetto.TipoInfo;

public class MarioClientMock {

	public static void main(String[] args) throws InterruptedException {
		
		InetAddress address = null;
		try {
			address = InetAddress.getByName("localhost");
		}
		catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		int port = 4321;
				
		Socket socket = null;
		ObjectInputStream inSocket = null;
		ObjectOutputStream outSocket = null;
		
		
		// creazione della socket
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
		
		try {
			outSocket.writeObject(new Pacchetto(new Operazione("Mario", "mario"), TipoInfo.ACCESSO));
			outSocket.flush();
			
			Pacchetto p = (Pacchetto) inSocket.readObject();
			if (p.getTipo().equals(TipoInfo.CONFERMA)) {
				InfoSessione infoSess = (InfoSessione) p.getInformazione();
				System.out.println(infoSess.isEsitoCredenziali());
			}
			
			// Mario scrive nel gruppo con Carlo
			outSocket.writeObject(new Pacchetto(new MessaggioTestuale(TipoDestinatario.GRUPPO, LocalDateTime.now(),
					"Mario", "Gruppo1", "Ciao a tutti!"), TipoInfo.CONTENUTO));
			outSocket.flush();
			outSocket.writeObject(new Pacchetto(new MessaggioTestuale(TipoDestinatario.GRUPPO, LocalDateTime.now(),
					"Mario", "Gruppo1", "Messaggio di prova"), TipoInfo.CONTENUTO));
			outSocket.flush();
			
			Thread.sleep(2000);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			socket.shutdownOutput();
			socket.shutdownInput();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
