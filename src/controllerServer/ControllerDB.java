package controllerServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dominioPacchetto.Contenuto;

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
	
	public synchronized boolean verificaPassword(String username, String hashPassword) {
		return false;		
	}
	
	public synchronized void creaTabelle() {
		Connection connection = null;
		
		try {
			connection = getConnection();
		
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.executeUpdate("DROP TABLE IF EXISTS utenti");
			statement.executeUpdate("DROP TABLE IF EXISTS gruppi");
			statement.executeUpdate("DROP TABLE IF EXISTS messaggiotestuale");
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
					"id INT NOT NULL PRIMARY KEY," +
					"dataora DATETIME NOT NULL," +
					"username VARCHAR(50) NOT NULL," +
					"gruppo VARCHAR(50) NULL," +
					"inBacheca BOOLEAN NOT NULL," +
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
	
	public synchronized List<Contenuto> getContenutiGruppo(String gruppo) {
		return null;
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
	
	/*
	public synchronized List<String> getGruppi() {
		Connection connection = null;
		List<String> gruppi = new ArrayList<>();
		
		try {
			connection = getConnection();
		
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM gruppi");
			statement.clearParameters();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
						
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				gruppi.add(rs.getString("nome"));
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
	*/

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
			statement.setString(3, ruolo);
			
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
