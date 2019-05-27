package controllerServer;

import java.util.Calendar;
import java.util.List;

import dominioServer.Ruolo;
import dominioServer.Utente;

public class Main {

	public static void main(String[] args) {
		Utente matt = new Utente("matt_esse", Ruolo.AMMINISTRATORE);
		Utente jack = new Utente("jack_pino", Ruolo.AMMINISTRATORE);
		// lol nico e' un utente comune hehehe
		Utente nico = new Utente("nico_dona", Ruolo.UTENTE);
		
		// SINGOL-TON!
		ControllerLog cl = SingleLog.getControllerLog();
		
		cl.addEntry("accesso " + matt.getUsername() + " riuscito");
		cl.addEntry("accesso " + matt.getUsername() + " non riuscito");
		
		List<String> logs = cl.getLog(Calendar.getInstance().getTime());
		
		for (String log : logs) {
			System.out.println(log);
		}
		
		cl.close();
	}

}
