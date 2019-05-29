package controllerServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dominioPacchetto.Conferma;
import dominioPacchetto.ListaString;
import dominioPacchetto.Pacchetto;
import dominioPacchetto.TipoInfo;

public class ControllerLog {
	
	private PrintWriter writer;
	
	private FileInputStream fis;
	private BufferedReader reader;
	
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateTimeFormat;
	
	private Utenti utenti;
	
	public ControllerLog (Utenti utenti) {
		try {
			this.writer = new PrintWriter(new FileWriter("log.txt"));
			
			this.fis = new FileInputStream(new File("log.txt"));
			this.reader = new BufferedReader(new InputStreamReader(fis));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		this.dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		this.dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd-MM:hh");
		
		this.utenti = utenti;
	}
	
	public synchronized void addEntry (String entry) {
		writer.println("[" + dateTimeFormat.format(Calendar.getInstance().getTime()) + "]: " + entry);
		writer.flush();
	}
	
	public synchronized void addEntry (String entry, boolean e) {		
		writer.println("[" + dateTimeFormat.format(Calendar.getInstance().getTime()) + "]: " + entry + " " + (e ? "successo" : "fallito"));
		writer.flush();
	}
	
	public synchronized void getLog (String executor, String date) {		
		try {
			// torno a leggere dall'inizio
			fis.getChannel().position(0);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			List<String> logs = new ArrayList<>();
			
			if (date == null || date == "") {
				// ottengo tutti i log
				String line;
				while ((line = reader.readLine()) != null) {
					logs.add(line);					
				}				
			}
			else {
				// ottengo i log relativi alla data specificata
				
				String line;
				while ((line = reader.readLine()) != null) {					
					if (line.contains(date)) {
						logs.add(line);
					}
				}
			}
			
			utenti.lockList();
			utenti.getByUsername(executor).invia(new Pacchetto(new ListaString(logs) , TipoInfo.VISUALIZZA_LOG));
			utenti.unlockList();			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
