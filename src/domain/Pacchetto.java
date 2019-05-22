package domain;

import java.io.Serializable;

public class Pacchetto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Info info;
	private TipoInfo tipoInfo;
	
	public Pacchetto () {
		
	}
	
	public Pacchetto (Info info, TipoInfo tipoInfo) {
		this.info = info;
		this.tipoInfo = tipoInfo;
	}
	
	public void setInformazione (Info info) {
		this.info = info;
	}
	
	public Info getInformazione () {
		return info;
	}
	
	public void setTipo (TipoInfo tipoInfo) {
		this.tipoInfo = tipoInfo;
	}
	
	public TipoInfo getTipo () {
		return tipoInfo;
	}
	
}
