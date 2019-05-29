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
			ControllerScambiaContenutiGruppi cscg, ControllerScambiaContenutiBacheca cscb, ControllerLog cl, InputStream inSock) {
		this.ca = ca;
		this.cgg = cgg;
		this.cgu = cgu;
		this.cscg = cscg;
		this.cscb = cscb;
		this.cl = cl;
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
			boolean esito;
			while (true) {
				pacchetto = (Pacchetto) inSock.readObject();
				switch (pacchetto.getTipo()) {
				case ACCESSO:
					o = (Operazione) pacchetto.getInformazione();
					esito = ca.confermaAccesso(o.getParametro1(), o.getParametro2());
					if (esito) {
						executor = o.getParametro1();
					}
					cl.addEntry("accesso " + executor, esito);
					break; 
				case RICHIESTA_CONTENUTI:
					o = (Operazione) pacchetto.getInformazione();
					cscg.getContenutiGruppo(o.getParametro1(), executor); // nessun controllo sull'appartenenza
					break;
				case CONTENUTO:
					c = (Contenuto) inSock.readObject();
					if (c.getTipoDestinatario().equals(TipoDestinatario.BACHECA)) {
						cscb.smista(c);
					}
					else if (c.getTipoDestinatario().equals(TipoDestinatario.GRUPPO)) {
						cscg.smista(c);
					}
					break;
				case CREA_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					if(cgg.creaGruppo(o.getParametro1(), executor))
						cl.addEntry("creazione gruppo " + executor + " " + o.getParametro1());
					break;
				case ELIMINA_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					if(cgg.eliminaGruppo(o.getParametro1(), executor))
						cl.addEntry("eliminazione gruppo " + executor + " " + o.getParametro1());
					break;
				case AGG_UTENTE_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					if(cgg.aggiungUtenteGruppo(o.getParametro1(), o.getParametro2(), executor))
						cl.addEntry("aggiunta utente a gruppo " + executor + " " + o.getParametro1() + " " + o.getParametro2());
					break;
				case ELIMINA_UTENTE_GRUPPO:
					o = (Operazione) pacchetto.getInformazione();
					if(cgg.eliminaUtenteGruppo(o.getParametro1(), o.getParametro2(), executor))
						cl.addEntry("eliminazione utente da gruppo " + executor + " " + o.getParametro1() + " " + o.getParametro2());
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
					if(cgu.aggiungiUtente(o.getParametro1(), Ruolo.valueOf(o.getParametro2()), o.getParametro3(), executor))
						cl.addEntry("aggiungiunta utente " + executor + " " + o.getParametro1());
					break;
				case ELIMINA_UTENTE:
					o = (Operazione) pacchetto.getInformazione();
					if(cgu.eliminaUtente(o.getParametro1(), executor))
						cl.addEntry("eliminazione utente " + executor + " " + o.getParametro1());
					break;
				case LISTA_UTENTI:
					cgu.inviaListaUtenti(executor); 
					break;
				case CAMBIA_PASSWORD:
					o = (Operazione) pacchetto.getInformazione();
					esito = ca.modicaPassword(executor, o.getParametro1(), o.getParametro2());
					cl.addEntry("modifica password" + executor, esito);
					break;
				case DISCONNETTI:
					o = (Operazione) pacchetto.getInformazione();
					ca.disconnetti(executor);
					cl.addEntry("disconnessione" + executor);
					
					// TODO
					// SE MI DISCONNETTO DOVRO' FERMARE IL THREAD? IN REALTA NO -> NUOVA CONNESSIONE
					// MA QUINDI QUANDO TERMINA QUESTO THREAD?
						
					break;
				case VISUALIZZA_LOG:
					o = (Operazione) pacchetto.getInformazione();
					cl.getLog(executor, o.getParametro1());					
					break;
				default:
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
