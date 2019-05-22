package domain;

public class MessaggioTestuale extends Contenuto {

	private static final long serialVersionUID = 1L;
	
	private String messaggio;

	public MessaggioTestuale (String messaggio) {
		this.messaggio = messaggio;
	}
	
	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	
	
	
}
