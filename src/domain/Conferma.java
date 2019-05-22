package domain;

public class Conferma implements Info {

	private static final long serialVersionUID = 1L;

	private String messaggioConferma;
	
	public Conferma () {
		
	}
	
	public Conferma (String messaggioConferma) {
		this.messaggioConferma = messaggioConferma;
	}

	public String getMessaggioConferma() {
		return messaggioConferma;
	}

	public void setMessaggioConferma(String messaggioConferma) {
		this.messaggioConferma = messaggioConferma;
	}
	
}
