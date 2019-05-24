package dominioServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import dominioPacchetto.Pacchetto;

public class Utente {

	private String username;
	private ObjectOutputStream socket;
	private Ruolo ruolo;
	private boolean connesso;

	public Utente(String username, Ruolo ruolo) {
		this.username = username;
		this.ruolo = ruolo;
		this.connesso = false;
		this.socket = null;
	}

	public boolean isConnesso() {
		return connesso;
	}

	public void setConnesso(boolean connesso) {
		this.connesso = connesso;
		if (!connesso)
			this.socket = null;
	}

	public String getUsername() {
		return username;
	}

	public void setSocket(OutputStream socket) {
		try {
			this.socket = new ObjectOutputStream(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public synchronized boolean invia(Pacchetto pacchetto) {
		if (connesso) {
			try {
				socket.writeObject(pacchetto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
