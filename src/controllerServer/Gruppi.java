package controllerServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dominioServer.Gruppo;

public class Gruppi {
	private List<Gruppo> lista;
	private Lock lock;
	
	public Gruppi() {
		lista = new ArrayList<>();
		lock = new ReentrantLock();
	}
	
	public void lockList() {
		lock.lock(); // mancano tecniche di priorità
	}
	
	public void unlockList() {
		lock.unlock();
	}
	
	public boolean aggiungi(Gruppo gruppo) {
		for (Gruppo g : lista)
			if (g.getNome().equals(gruppo.getNome())) 
				return false;
		return lista.add(gruppo);
	}
	
	public boolean rimuovi(String nome) {
		Gruppo daEliminare = null;
		for (Gruppo g : lista)
			if (g.getNome().equals(nome)) 
				daEliminare = g;
		
		if (daEliminare != null) {			
			return lista.remove(daEliminare);
		}
		return false;
	}
	
	public Gruppo getByNome(String nome) { // returns NULL if not present
		for (Gruppo g : lista)
			if (g.getNome().equals(nome)) 
				return g;
		return null;
	}
	
	public Gruppo getGruppi(int index) {
		return lista.get(index);
	}
	
	public List<String> getGruppiCon(String username) { // AGGIUNTO
		// per smista
		List<String> res = new ArrayList<>();
		for(Gruppo gruppo: lista)
			if (gruppo.isUtenteInGruppo(username))
				res.add(gruppo.getNome());
		return res;
	}
	
	public List<String> getNomeGruppi() { // AGGIUNTO
		// per la lista UI
		List<String> res = new ArrayList<>();
		for(Gruppo gruppo: lista)
			res.add(gruppo.getNome());
		return res;
	}
	
}










