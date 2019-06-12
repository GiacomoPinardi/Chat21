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
							ca.negaAccesso(o.getParametro1(), outSock);
						}
						cl.addEntry(executor + " prova ad accedere:", esito);
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
							cl.addEntry(executor + " ha creato il gruppo " + o.getParametro1());
						break;
					case ELIMINA_GRUPPO:
						o = (Operazione) pacchetto.getInformazione();
						if(cgg.eliminaGruppo(o.getParametro1(), executor))
							cl.addEntry(executor + " ha eliminato il gruppo " + o.getParametro1());
						break;
					case AGG_UTENTE_GRUPPO:
						o = (Operazione) pacchetto.getInformazione();
						// nomeGruppo, username
						if(cgg.aggiungiUtenteGruppo(o.getParametro1(), o.getParametro2(), executor)) {
							cl.addEntry(executor + " ha aggiunto al gruppo " + o.getParametro1() + " l'utente " + o.getParametro2());
						}
						break;
					case ELIMINA_UTENTE_GRUPPO:
						o = (Operazione) pacchetto.getInformazione();
						// nomeGruppo, username
						if(cgg.eliminaUtenteGruppo(o.getParametro1(), o.getParametro2(), executor))
							cl.addEntry(executor + " ha eliminato dal gruppo " + o.getParametro1() + " l'utente " + o.getParametro2());
						break;
					case LISTA_GRUPPI:
						// tutti i gruppi presenti nel sistema
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
						// username, password, ruolo, executor
						if(cgu.aggiungiUtente(o.getParametro1(), o.getParametro2(), Ruolo.valueOf(o.getParametro3()), executor))
							cl.addEntry(executor + " ha aggiunto l'utente " + o.getParametro1());
						break;
					case ELIMINA_UTENTE:
						o = (Operazione) pacchetto.getInformazione();
						if(cgu.eliminaUtente(o.getParametro1(), executor))
							cl.addEntry(executor + " ha eliminato l'utente " + o.getParametro1());
						break;
					case LISTA_UTENTI:
						cgu.inviaListaUtenti(executor); 
						break;
					case CAMBIA_PASSWORD:
						o = (Operazione) pacchetto.getInformazione();
						esito = ca.modicaPassword(executor, o.getParametro1(), o.getParametro2());
						cl.addEntry(executor + " ha modificato la password:", esito);
						break;
					case DISCONNETTI:
						ca.disconnetti(executor);
						cl.addEntry(executor + " si e' disconnesso");
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
			if (!executor.equals("UNKNOWN")) {
				System.out.println("Client " + executor + " disconnesso in modo inaspettato!");
				ca.disconnetti(executor);
				cl.addEntry(executor + " si e' disconnesso in modo inaspettato");
				executor = "UNKNOWN";
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
