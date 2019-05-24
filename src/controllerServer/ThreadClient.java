package controllerServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import dominioPacchetto.Contenuto;
import dominioPacchetto.Operazione;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoDestinatario;
import dominioServer.Ruolo;

public class ThreadClient implements Runnable {
	ControllerAutenticazione ca;
	ControllerGestioneGruppi cgg;
	ControllerGestioneUtenti cgu;
	ControllerScambiaContenutiGruppi cscg;
	ControllerScambiaContenutiBacheca cscb;
	ControllerLog cl;
	String executor;
	ObjectInputStream inSock;
	
	public ThreadClient(ControllerAutenticazione ca, ControllerGestioneGruppi cgg, ControllerGestioneUtenti cgu,
			ControllerScambiaContenutiGruppi cscg, ControllerScambiaContenutiBacheca cscb, ControllerLog cl, 
			String executor, InputStream inSock) { // CONTROLLER LOG??? SINGLE TONE
		this.ca = ca;
		this.cgg = cgg;
		this.cgu = cgu;
		this.cscg = cscg;
		this.cscb = cscb;
		this.cl = cl;
		this.executor = executor;
		try {
			this.inSock = new ObjectInputStream(inSock);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Pacchetto pacchetto;	
			Operazione o;
			Contenuto c;
			while (true) {
				pacchetto = (Pacchetto) inSock.readObject();
				switch (pacchetto.getTipo()) {
				case ACCESSO:
					o = (Operazione) pacchetto.getInformazione();
					ca.confermaAccesso(o.getParametro1(), o.getParametro2());
					break;
				case RICHIESTA_CONTENUTI:
					o = (Operazione) pacchetto.getInformazione();
					cscg.getContenutiGruppo(o.getParametro1(), executor); // nessun controllo sull'appartenenza
					break;
				case CONTENUTO:
					c = (Contenuto) inSock.readObject();
					if (c.getTipoDestinatario().equals(TipoDestinatario.BACHECA)) {
						cscb.smista(c);
					} else if (c.getTipoDestinatario().equals(TipoDestinatario.GRUPPO)) {
						cscg.smista(c);
					}
					break;
				case CREA_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					cgg.creaGruppo(o.getParametro1(), executor);
					break;
				case ELIMINA_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					cgg.eliminaGruppo(o.getParametro1(), executor);
					break;
				case AGG_UTENTE_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					cgg.aggiungUtenteGruppo(o.getParametro1(), o.getParametro2(), executor);
					break;
				case ELIMINA_UTENTE_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					cgg.eliminaUtenteGruppo(o.getParametro1(), o.getParametro2(), executor);
					break;
				case LISTA_GRUPPI:
					cgg.invioListaGruppi(executor);
					break;
				case LISTA_UTENTI_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					cgg.invioListaUtentInGruppo(o.getParametro1(), executor);
					break;
				case LISTA_UTENTI_NON:
					o = (Operazione) pacchetto.getInformazione();
					cgg.invioListaUtentiNonInGruppo(o.getParametro1(), executor);
					break;
				case AGG_UTENTE:
					o = (Operazione) pacchetto.getInformazione();
					cgu.aggiungiUtente(o.getParametro1(), Ruolo.valueOf(o.getParametro2()), executor); 
					break;
				case ELIMINA_UTENTE:
					o = (Operazione) pacchetto.getInformazione();
					cgu.eliminaUtente(o.getParametro1(), executor);
					break;
				case LISTA_UTENTI:
					cgu.inviaListaUtenti(executor); 
					break;
				case CAMBIA_PASSWORD:
					o = (Operazione) pacchetto.getInformazione();
					ca.modicaPassword(executor, o.getParametro1(), o.getParametro2());
					break;
				case DISCONNETTI:
					o = (Operazione) pacchetto.getInformazione();
					ca.disconnetti(executor);
					break;
				case VISUALIZZA_LOG:
					
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
