package domain;

import java.util.ArrayList;
import java.util.List;

public class ListaContenuti implements Info {

	private static final long serialVersionUID = 1L;

	private List<Contenuto> listaContenuti;
	
	public ListaContenuti () {
		listaContenuti = new ArrayList<>();
	}
	
	public ListaContenuti (List<Contenuto> listaContenuti) {
		this.listaContenuti = listaContenuti;
	}

	public List<Contenuto> getListaContenuti() {
		return listaContenuti;
	}

	public void setListaContenuti(List<Contenuto> listaContenuti) {
		this.listaContenuti = listaContenuti;
	}
	
}
