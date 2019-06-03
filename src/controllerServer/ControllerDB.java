package controllerServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import dominioPacchetto.Contenuto;
import dominioPacchetto.MessaggioTestuale;
import dominioPacchetto.TipoDestinatario;
import dominioServer.Gruppo;
import dominioServer.Ruolo;
import dominioServer.Utente;

public class ControllerDB {
	
	private static final String DBPATH = "jdbc:sqlite:chat21.db";
	
	public ControllerDB () {}
	
	private synchronized Connection getConnection () throws SQLException {
		// load the sqlite-JDBC driver using the current class loader
	    try {
			Class.forName("org.sqlite.JDBC");
		}
	    catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	    
	    return DriverManager.getConnection(DBPATH);
	}
	
	public synchronized void creaTabelle() {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.executeUpdate("DROP TABLE IF EXISTS utenti");
			statement.executeUpdate("DROP TABLE IF EXISTS gruppi");
			statement.executeUpdate("DROP TABLE IF EXISTS messaggitestuali");
			statement.executeUpdate("DROP TABLE IF EXISTS appartenenza");
			
			statement.executeUpdate("CREATE TABLE utenti (" +
					"username VARCHAR(50) NOT NULL PRIMARY KEY," +
					"password VARCHAR(20) NOT NULL," +
					"ruolo VARCHAR(20) NOT NULL" +
					")");
			
			statement.executeUpdate("CREATE TABLE gruppi (" +
					"nome VARCHAR(50) NOT NULL PRIMARY KEY" +
					")");
		
			statement.executeUpdate("CREATE TABLE messaggitestuali (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"dataora INTEGER NOT NULL," +
					"username VARCHAR(50) NOT NULL," +
					"gruppo VARCHAR(50) NULL," +
					"inBacheca BOOLEAN NOT NULL," +
					"testo VARCHAR(200) NOT NULL," +
					"FOREIGN KEY (username) REFERENCES utenti (username)," +
					"FOREIGN KEY (gruppo) REFERENCES gruppi (nome)" +
					")");
			
			statement.executeUpdate("CREATE TABLE appartenenza (" +
					"username VARCHAR(50) NOT NULL," +
					"nomeGruppo VARCHAR(50) NOT NULL," +
					"FOREIGN KEY (username) REFERENCES utenti (username)," +
					"FOREIGN KEY (nomeGruppo) REFERENCES gruppi (nome)," +
					"CONSTRAINT userGruppo UNIQUE (username, nomeGruppo)" +
					")");			
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
	}
	
	public synchronized void pulisciTutto() {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.executeUpdate("DELETE FROM utenti");
			statement.executeUpdate("DELETE FROM gruppi");
			statement.executeUpdate("DELETE FROM messaggitestuali");
			statement.executeUpdate("DELETE FROM appartenenza");			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
	}
	
	public synchronized boolean verificaPassword(String username, String password) {
		Connection connection = null;
		boolean result = false;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM utenti WHERE username = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
						
			statement.setString(1, username);
			
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				if (rs.getString("password").equals(password)) {
					result = true;
				}
			}			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
		return result;	
	}
	
	public synchronized void addContenutoGruppo (MessaggioTestuale contenuto) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("INSERT INTO messaggitestuali " +
											"(dataora, username, gruppo, inBacheca, testo) VALUES (?, ?, ?, ?, ?)");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setLong(1, contenuto.getDateTime().toEpochSecond(ZoneOffset.UTC));
			statement.setString(2, contenuto.getMittente());
			statement.setString(3, contenuto.getDestinario());
			statement.setBoolean(4, false);
			statement.setString(5, contenuto.getMessaggio());
			
			statement.execute();
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }	
	}
	
	public synchronized List<Contenuto> getContenutiGruppo(String gruppo) {
		Connection connection = null;
		List<Contenuto> contenuti = new ArrayList<>();
		
		Contenuto c;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM messaggitestuali WHERE gruppo = ? AND inBacheca = FALSE");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
						
			statement.setString(1, gruppo);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				LocalDateTime dateTime = LocalDateTime.ofEpochSecond(rs.getLong("dataora"), 0, ZoneOffset.UTC);						
				String mittente = rs.getString("username");
				String destinatario = gruppo;
				String messaggio = rs.getString("testo");
				
				c = new MessaggioTestuale(TipoDestinatario.GRUPPO, dateTime, mittente, destinatario, messaggio);
				
				contenuti.add(c);
			}			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
		return contenuti;
	}
	
	public synchronized void addContenutoBacheca (MessaggioTestuale contenuto) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("INSERT INTO messaggitestuali " +
											"(dataora, username, gruppo, inBacheca, testo) VALUES (?, ?, NULL, ?, ?)");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setLong(1, contenuto.getDateTime().toEpochSecond(ZoneOffset.UTC));
			statement.setString(2, contenuto.getMittente());
			statement.setBoolean(3, true);
			statement.setString(4, contenuto.getMessaggio());
			
			statement.execute();
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }	
	}
	
	public synchronized List<Contenuto> getContenutiBacheca() {
		Connection connection = null;
		List<Contenuto> contenuti = new ArrayList<>();
		
		Contenuto c;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM messaggitestuali WHERE gruppo = NULL AND inBacheca = TRUE");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
						
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				LocalDateTime dateTime = LocalDateTime.ofEpochSecond(rs.getLong("dataora"), 0, ZoneOffset.UTC);						
				String mittente = rs.getString("username");
				String destinatario = "Bacheca";
				String messaggio = rs.getString("testo");
				
				c = new MessaggioTestuale(TipoDestinatario.BACHECA, dateTime, mittente, destinatario, messaggio);
				
				contenuti.add(c);
			}			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
		return contenuti;
	}

	public synchronized void aggiungiGruppo(String nome) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("INSERT INTO gruppi (nome) VALUES (?)");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, nome);
			
			statement.execute();
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }		
	}
	
	public synchronized List<Utente> getUtenti () {
		Connection connection = null;
		List<Utente> utenti = new ArrayList<>();
		
		Utente u;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM utenti");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
						
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String username = rs.getString("username");
				String r = rs.getString("ruolo");				
				Ruolo ruolo = Ruolo.valueOf(r.toUpperCase());
				
				u = new Utente(username, ruolo);
				
				utenti.add(u);
			}			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
		return utenti;
	}
	
	public synchronized List<Gruppo> getGruppi () {
		Connection connection = null;
		List<Gruppo> gruppi = new ArrayList<>();
		
		Gruppo g;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM gruppi");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
						
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String nomeGruppo = rs.getString("nome");
				
				g = new Gruppo(nomeGruppo);
				
				List<String> usernameUtentiInGruppo = getUsernameUtentiInGruppo(nomeGruppo);
				for (String username : usernameUtentiInGruppo) {
					g.aggiungiUtente(username);
				}
				
				gruppi.add(g);
			}			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
		return gruppi;
	}
	
	private List<String> getUsernameUtentiInGruppo (String nomeGruppo) {
		Connection connection = null;
		List<String> usernames = new ArrayList<>();
				
		try {
			connection = getConnection();
		
			/*
			PreparedStatement statement = connection.prepareStatement("SELECT utenti.username FROM gruppi " +
															"INNER JOIN appartenenza ON gruppi.nome = appartenenza.nomeGruppo " +
															"INNER JOIN utenti ON utenti.username = appartenenza.username " +
															"WHERE gruppi.nome = ?");
			*/
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM appartenenza WHERE nomeGruppo = ?");
			
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
						
			statement.setString(1, nomeGruppo);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String username = rs.getString("username");				
				usernames.add(username);
			}			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	      try {
	        if(connection != null)
	          connection.close();
	      }
	      catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	    }
		return usernames;
	}

	public synchronized void eliminaGruppo(String nome) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("DELETE FROM appartenenza WHERE nomeGruppo = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, nome);
			
			statement.execute();
			
			statement = connection.prepareStatement("DELETE FROM messaggitestuali WHERE gruppo = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, nome);
			
			statement.execute();
			
			statement = connection.prepareStatement("DELETE FROM gruppi WHERE nome = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, nome);
			
			statement.execute();
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }
	}

	public synchronized void aggiungiUtenteGruppo(String nomeGruppo, String username) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("INSERT INTO appartenenza (username, nomeGruppo) VALUES (?, ?)");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, username);
			statement.setString(2, nomeGruppo);
			
			statement.execute();
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }		
	}

	public synchronized void eliminaUtenteGruppo(String nomeGruppo, String username) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("DELETE FROM appartenenza WHERE nomeGruppo = ? AND username = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, nomeGruppo);
			statement.setString(2, username);
			
			statement.execute();
			
			statement = connection.prepareStatement("DELETE FROM messaggitestuali WHERE gruppo = ? AND username = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, nomeGruppo);
			statement.setString(2, username);
			
			statement.execute();			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }		
	}

	public synchronized void aggiungiUtente(String username, String password, String ruolo) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("INSERT INTO utenti (username, password, ruolo) VALUES (?, ?, ?)");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, username);
			statement.setString(2, password);
			statement.setString(3, ruolo.toUpperCase());
			
			statement.execute();
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }		
	}

	public synchronized void eliminaUtente(String username) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("DELETE FROM appartenenza WHERE username = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, username);
			
			statement.execute();
			
			statement = connection.prepareStatement("DELETE FROM messaggitestuali WHERE username = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, username);
			
			statement.execute();
			
			statement = connection.prepareStatement("DELETE FROM utenti WHERE username = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, username);
			
			statement.execute();
			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }		
	}
	
	public synchronized void modificaPassoword(String username, String nuovaPassword) {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("UPDATE utenti SET password = ? WHERE username = ?");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.setString(1, nuovaPassword);
			statement.setString(2, username);
			
			statement.execute();			
		}
	    catch(SQLException e) {
	      e.printStackTrace();
	    }
	    finally {
	    	try {
	    		if(connection != null)
	    			connection.close();
	    	}
	    	catch(SQLException e) {
	    		e.printStackTrace();
	    	}
	    }
	}
}
