package domain;

import java.util.ArrayList;
import java.util.List;

public class ListaString implements Info {

	private static final long serialVersionUID = 1L;

	private List<String> list;
	
	public ListaString () {
		list = new ArrayList<>();
	}
	
	public ListaString (List<String> list) {
		this.list = list;
	}

	public List<String> getListaContenuti() {
		return list;
	}

	public void setListaContenuti(List<String> list) {
		this.list = list;
	}
	
}
