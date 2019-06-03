package controllerServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
	
	Socket socketClient;
	
	ObjectOutputStream outSock;
	ObjectInputStream inSock;
	
	public ThreadClient(ControllerAutenticazione ca, ControllerGestioneGruppi cgg, ControllerGestioneUtenti cgu,
			ControllerScambiaContenutiGruppi cscg, ControllerScambiaContenutiBacheca cscb, ControllerLog cl, Socket socketClient) {
		this.ca = ca;
		this.cgg = cgg;
		this.cgu = cgu;
		this.cscg = cscg;
		this.cscb = cscb;
		this.cl = cl;
		
		this.executor = "UNKNOWN";
		
		this.socketClient = socketClient;
		
		try {
			this.outSock = new ObjectOutputStream(socketClient.getOutputStream());
			this.inSock = new ObjectInputStream(socketClient.getInputStream());
		}
		catch (IOException e) {
			try {
				socketClient.close();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
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
				System.out.println("In attesa del pacchetto...");
				pacchetto = (Pacchetto) inSock.readObject();
				System.out.println("Pacchetto arrivato!");
				switch (pacchetto.getTipo()) {
					case ACCESSO:
						o = (Operazione) pacchetto.getInformazione();
						
						esito = ca.verificaPassword(o.getParametro1(), o.getParametro2());
						
						System.out.println(o.getParametro1() + "[" + o.getParametro2() + "] esito accesso: " + esito);
						
						if (esito) {
							executor = o.getParametro1();
							ca.connetti(executor, outSock);
							ca.confermaAccesso(executor);
						}
						else {
							ca.negaAccesso(executor, outSock);
						}
						cl.addEntry("accesso " + executor, esito);
						break; 
					case RICHIESTA_CONTENUTI:
						o = (Operazione) pacchetto.getInformazione();
						cscg.getContenutiGruppo(o.getParametro1(), executor); // nessun controllo sull'appartenenza
						break;
					case CONTENUTO:
						c = (Contenuto) pacchetto.getInformazione();
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
						cl.addEntry("modifica password " + executor, esito);
						break;
					case DISCONNETTI:
						o = (Operazione) pacchetto.getInformazione();
						ca.disconnetti(executor);
						cl.addEntry("disconnessione " + executor);
						executor = "UNKNOWN";
						break;
					case VISUALIZZA_LOG:
						o = (Operazione) pacchetto.getInformazione();
						cl.getLog(executor, o.getParametro1());					
						break;
					default:
						break;
				}

			}
		}
		catch (EOFException e) {
			System.out.println("Client " + executor + " disconnesso in modo inaspettato!");
			ca.disconnetti(executor);
			cl.addEntry("disconnessione " + executor);
			executor = "UNKNOWN";
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
