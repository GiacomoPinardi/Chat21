package controllerServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dominioServer.Gruppo;
import dominioServer.Utente;

public class Utenti {
	private List<Utente> lista;
	private Lock lock;
	
	public Utenti() {
		lista = new ArrayList<>();
		lock = new ReentrantLock();
	}
	
	public void lockList() {
		lock.lock(); // mancano tecniche di priorità
	}
	
	public void unlockList() {
		lock.unlock();
	}
	
	public boolean aggiungi(Utente utente) {
		for (Utente u : lista)
			if (u.getUsername().equals(utente.getUsername())) 
				return false;
		return lista.add(utente);
	}
	
	public boolean rimuovi(String username) {
		Utente daEliminare = null;
		for (Utente u : lista)
			if (u.getUsername().equals(username)) 
				daEliminare = u;
		
		if (daEliminare != null) {			
			return lista.remove(daEliminare);
		}
		return false;
	}
	
	public Utente getByUsername(String username) { // returns NULL if not present
		for (Utente u : lista)
			if (u.getUsername().equals(username)) 
				return u;
		return null;
	}
	
	public Utente getUtenti(int index) { 
		return lista.get(index);
	}
	
	public List<String> getUsernames() { 
		List<String> res = new ArrayList<>();
		for (Utente u: lista) {
			res.add(u.getUsername());
		}
		return res;
	}
	
	public List<Utente> getUtentiOnline() { // AGGIUNTO
		List<Utente> res = new ArrayList<>();
		for (Utente u : lista)
			if(u.isConnesso())
				res.add(u);
		return res;
	}
	
	public List<String> getAltriUtenti(List<String> utentiIn) { // aggiunto
		//lista utenti non in gruppo
		List<String> res = new ArrayList<>();
		boolean found;
		for (Utente u: lista) {
			found = false;
			for(String username: utentiIn)
				if (u.getUsername().equals(username))
					found = true;
			if (!found)
				res.add(u.getUsername());
		}
		return res;
	}
}















