package dominioPacchetto;

import java.time.LocalDateTime;

public abstract class Contenuto implements Info {
	private static final long serialVersionUID = 1L;
	
	private TipoDestinatario tipoDestinatario;
	private LocalDateTime dateTime;	
	private String usernameMittente;
	private String destinatario;
	
	public Contenuto(TipoDestinatario tipoDestinatario, LocalDateTime dateTime, String usernameMittente,
			String destinatario) {
		super();
		this.tipoDestinatario = tipoDestinatario;
		this.dateTime = dateTime;
		this.usernameMittente = usernameMittente;
		this.destinatario = destinatario;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}
	
	public String getMittente() {
		return usernameMittente;
	}
	
	public String getDestinario() {
		return destinatario;
	}
	
}
