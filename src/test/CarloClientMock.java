package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import dominioPacchetto.Conferma;
import dominioPacchetto.InfoSessione;
import dominioPacchetto.Operazione;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;

public class CarloClientMock {

	public static void main(String[] args) {
		
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
			outSocket.writeObject(new Pacchetto(new Operazione("Carlo", "carlo"), TipoInfo.ACCESSO));
			outSocket.flush();
			
			Pacchetto p = (Pacchetto) inSocket.readObject();
			if (p.getTipo().equals(TipoInfo.CONFERMA)) {
				InfoSessione infoSess = (InfoSessione) p.getInformazione();
				System.out.println(infoSess.isEsitoCredenziali());
			}
			
			System.out.println("Creo gruppo...");
			
			outSocket.writeObject(new Pacchetto(new Operazione("Gruppo1"), TipoInfo.ELIMINA_GRUPPO));
			outSocket.flush();
			
			p = (Pacchetto) inSocket.readObject();
			if (p.getTipo().equals(TipoInfo.CONFERMA)) {
				Conferma c = (Conferma) p.getInformazione();
				System.out.println(c.getMessaggioConferma());
			}
			
			outSocket.writeObject(new Pacchetto(new Operazione("GruppoXY"), TipoInfo.ELIMINA_GRUPPO));
			outSocket.flush();
			
			p = (Pacchetto) inSocket.readObject();
			if (p.getTipo().equals(TipoInfo.CONFERMA)) {
				Conferma c = (Conferma) p.getInformazione();
				System.out.println(c.getMessaggioConferma());
			}
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