package domain;

import java.util.ArrayList;
import java.util.List;

public class Bacheca {

	private List<Contenuto> contenuti;
	
	public Bacheca () {
		contenuti = new ArrayList<>();
	}
	
	public List<Contenuto> getContenuti () {
		return contenuti;
	}
	
	public void aggiungiContenuto (Contenuto contenuto) {
		contenuti.add(contenuto);		
	}
	
}
