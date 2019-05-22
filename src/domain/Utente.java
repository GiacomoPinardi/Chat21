package domain;

import java.io.OutputStream;

public class Utente {

	private String username;
	private OutputStream socket;
	private Ruolo ruolo;
	private boolean connesso;
	
	public Utente (String username, Ruolo ruolo, boolean connesso, OutputStream socket) {
		this.username = username;
		this.ruolo = ruolo;
		this.connesso = connesso;
		this.socket = socket;
	}
	
	public Utente (String username, Ruolo ruolo, boolean connesso) {
		this.username = username;
		this.ruolo = ruolo;
		this.connesso = connesso;
	}
	
	public boolean isConnesso() {
		return connesso;
	}
	
	public void setConnesso (boolean connesso) {
		this.connesso = connesso;
	}
	
	public void setSocket (OutputStream socket) {
		this.socket = socket;
	}
	
	public Ruolo getRuolo () {
		return ruolo;
	}
	
	public synchronized boolean invia (Pacchetto pacchetto) {
		return false;
		// TODO
	}
	
}
