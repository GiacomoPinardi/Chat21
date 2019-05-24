package dominioPacchetto;

import java.time.LocalDateTime;

public class MessaggioTestuale extends Contenuto {
	private static final long serialVersionUID = 1L;
	private String messaggio;

	public MessaggioTestuale (TipoDestinatario tipoDestinatario, LocalDateTime dateTime, String usernameMittente,
			String destinatario, String messaggio) {
		super(tipoDestinatario, dateTime, usernameMittente, destinatario);
		this.messaggio = messaggio;
	}
	
	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
}
