package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import dominioPacchetto.Info;
import dominioPacchetto.InfoSessione;
import dominioPacchetto.Operazione;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;

public class MainClientMock {

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
			outSocket.writeObject(new Pacchetto(new Operazione("giacomo", "miapsw"), TipoInfo.ACCESSO));
			outSocket.flush();
			
			Pacchetto p = (Pacchetto) inSocket.readObject();
			if (p.getTipo().equals(TipoInfo.CONFERMA)) {
				InfoSessione infoSess = (InfoSessione) p.getInformazione();
				System.out.println(infoSess.isEsitoCredenziali());
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
