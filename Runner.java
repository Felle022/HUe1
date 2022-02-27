package Bsp2mysql;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Runner {

	static String url = "jdbc:mysql://localhost:3306/infi";
	static String user = "root";
	static String pass = "";
    
	static Connection getConnection(String tableName) throws ClassNotFoundException {
		try {
			return DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		Connection c = getConnection("Kunde_Artikel.db");
		dropTable(c, "Bestellung");
		dropTable(c, "Artikel");
		dropTable(c, "Kunde");
		
		Kunde.createTable(c, "Kunde");
		Artikel.createTable(c, "Artikel");
		Bestellung.createTable(c, "Bestellung");
		
		Kunde.insertInto(c, "Kunde", "Valentin", "valle@gmail.com");
		Kunde.insertInto(c, "Kunde", "Marco", "marco@gmail.com");
		
		Artikel.insertInto(c, "Artikel", "Buch", 25,1);
		Artikel.insertInto(c, "Artikel", "Fußball", 50,10);
		
		Bestellung.insertInto(c, "Bestellung", 1, 1, 1);
		Bestellung.insertInto(c, "Bestellung", 2, 2, 3);
		
		Bestellung.deleteBestellung(c, "Bestellung", 2);
		
		Bestellung.showBestellung(c, "Bestellung", 1);
		
		Bestellung.updateBestellung(c, "Bestellung", 1, 1, 3);
		
		
		Bestellung.showBestellung(c, "Bestellung", 1);
//		Bestellung.showBestellung(c, "Bestellung", 2);
//		Bestellung.updateBestellung(c, "Bestellung", 2, 2, 3);
//		Bestellung.showBestellung(c, "Bestellung", 2);
	}
	
	static void dropTable(Connection c, String tableName) {
		try {
			Statement stmt = c.createStatement();
			String sql = "drop table if exists " + tableName + ";";
			stmt.executeUpdate(sql);
			System.out.println("Tabelle " + tableName + " wurde gelöscht.");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
