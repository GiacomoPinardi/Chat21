package controllerServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ControllerLog {
	
	private PrintWriter writer;
	private Utenti utenti;
	private FileInputStream fis;
	private BufferedReader reader;
	
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat dateTimeFormat;
	
	public ControllerLog () {
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
	}
	
	public synchronized void addEntry (String entry) {
		writer.println("[" + dateTimeFormat.format(Calendar.getInstance().getTime()) + "]: " + entry);
		writer.flush();
	}
	
	public synchronized List<String> getLog (Date date) {
		List<String> logs = null;
		
		try {
			// torno a leggere dall'inizio
			fis.getChannel().position(0);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			logs = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				String dateString = dateFormat.format(date);
				if (line.contains(dateString)) {
					logs.add(line);
				}
			}
						
		}
		catch (IOException e) {
			e.printStackTrace();
		}		
		return logs;
	}
	
	public synchronized void close() {
		try {
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		writer.close();
	}
	
	/*
	
	public String getLog(String executor) {
		String res = "";
		int x;
		char c;
		try {
			while ((x = reader.read()) > 0) {
				c = (char) x;
				res += c;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public void addEntry(String string, String executor, boolean esito) {
		String res = "";
		try {
			res += "[" + LocalDateTime.now().toString() + "] ";
			res += executor + "\t " + string;
			if (esito)
				res += " successo ";
			else 
				res += " fallito";
			writer.write('c');
			//writer.write(res + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addEntry(String string, String executor, String parametro1) {
		String res = "";
		try {
			res += "[" + LocalDateTime.now().toString() + "] ";
			res += executor + "\t " + string;
			res += " " + parametro1;
			writer.write(res + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addEntry(String string, String executor, String parametro1, String parametro2) {
		String res = "";
		try {
			res += "[" + LocalDateTime.now().toString() + "] ";
			res += executor + "\t " + string;
			res += " " + parametro1 + " " + parametro2;
			writer.write(res + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addEntry(String string, String executor) {
		String res = "";
		try {
			res += "[" + LocalDateTime.now().toString() + "] ";
			res += executor + "\t " + string;
			writer.write(res + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
