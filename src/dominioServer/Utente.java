package dominioServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import dominioPacchetto.Pacchetto;

public class Utente {

	private String username;
	private ObjectOutputStream oos;
	private Ruolo ruolo;
	private boolean connesso;

	public Utente(String username, Ruolo ruolo) {
		this.username = username;
		this.ruolo = ruolo;
		this.connesso = false;
		this.oos = null;
	}

	public boolean isConnesso() {
		return connesso;
	}

	public void setConnesso(boolean connesso) {
		this.connesso = connesso;
		if (!connesso)
			this.oos = null;
	}

	public String getUsername() {
		return username;
	}

	public void setObjectOutputStream(ObjectOutputStream oos) {
		this.oos = oos;		
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public synchronized boolean invia(Pacchetto pacchetto) {
		if (connesso) {
			try {
				oos.writeObject(pacchetto);
				oos.flush();
				return true;
			}
			catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			return false;
		}
	}

}
