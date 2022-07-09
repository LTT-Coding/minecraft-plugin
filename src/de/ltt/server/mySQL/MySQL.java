package de.ltt.server.mySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQL {
	
	private String host;
	private String database;
	private String user;
	private String password;
	
	private Connection con;
	
	public MySQL(String host, String database, String user, String password) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		connect();
	}
	
	public void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true", user, password);
			System.out.println("[MilePlay] Verbindung zu MySQL wurde hergestellt!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[MilePlay] Verbindung zu MySQL ist fehlgeschlagen! Fehler:" + e.getMessage());
		}
	}
	
	public void close() {
		try {
			if (con != null) {
				con.close();
				System.out.println("[MilePlay] Verbindung zu MySQL wurde geschlossen!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[MilePlay] Fehler beim Schlieﬂen der Verbindung zu MySQL! Fehler:" + e.getMessage());
		}
	}
	
	public void update(String qry) {
		try {
			Statement st = con.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
	}
	
	public ResultSet query(String qry) {
		ResultSet rs = null;
		
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(qry);
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
		return rs;
	}

}
