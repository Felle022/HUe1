package Bsp2mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Bestellung {
	
	static void createTable(Connection c, String tableName) {
		try {
			Statement stmt = c.createStatement();
			String sql = "create table if not exists " + tableName +		// in Zukunft keine ID einbauen(KundenID&ArtikelID zum PrimaryKey machen)
			" (id integer primary key auto_increment, kundeID integer, " +
			"artikelID integer, anzahl integer, foreign key (kundeID) " +
			"references Kunde (id), foreign key (artikelID) references " +
			"Artikel (id));";
			System.out.println("Tabelle " + tableName + " wurde erstellt.");
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void insertInto(Connection c, String tableName, int kundeID, int artikelID, int anzahl) {
		try {
			Statement stmt = c.createStatement();
			
				String sql = "insert into " + tableName + " (kundeID, artikelID, anzahl) values " +
				"(" + kundeID + ", " + artikelID + ", " + anzahl + ");";
				System.out.println("insert --> Bestellung.");
				stmt.executeUpdate(sql);
				String sql2="update Artikel set lagerbestand=((select lagerbestand from Artikel where id="+artikelID+")-"+anzahl+") where id="+artikelID+";";
				System.out.println(sql2);
				stmt.execute(sql2);
//				
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	static void showBestellung(Connection c, String tableName, int id) {
		try {
			
			Statement stmt = c.createStatement();
			
			ResultSet rs = stmt.executeQuery("select kundeID from Bestellung where id = " + id + ";");
			int xKunde = rs.getInt("kundeID");
			rs.close();
			
			ResultSet rs2 = stmt.executeQuery("select artikelID from Bestellung where id = " + id + ";");
			int xArtikel = rs2.getInt("artikelID");
			
			rs2.close();
			ResultSet rs3 = stmt.executeQuery("select bezeichnung from Artikel where id = " + xArtikel + ";");
			String strBezeichnung = rs3.getString("bezeichnung");
			rs3.close();
			ResultSet rs4 = stmt.executeQuery("select name from Kunde where id = " + xKunde + ";");
			String strName = rs4.getString("name");
			rs4.close();
			
			ResultSet rs5 =stmt.executeQuery("select anzahl from Bestellung where id = " + id + ";");
			int stranzahl = rs5.getInt("anzahl");
			rs5.close();
			
			ResultSet rs6 = stmt.executeQuery("select lagerbestand from Artikel where id = " +xArtikel  + ";");
			int lagerbestand = rs6.getInt("lagerbestand");
			//System.out.println(lagerbestand);
			rs6.close();
			
			ResultSet rs7 = stmt.executeQuery("select anzahl from Bestellung where id = " +id  + ";");
			int anzahl = rs7.getInt("anzahl");
			rs7.close();
			System.out.println("\n");
			System.out.println("Bestellung " + id + ":\nName: " + strName + "\nBezeichnung: " + strBezeichnung+"\nAnzahl:"+stranzahl+"");
			System.out.println();
			
			if(lagerbestand>=0)
			{
			System.out.println("Die Bestellung kann verschickt werden");
			System.out.printf("Lagerbestand:%d \n",lagerbestand);
			System.out.println();
			
			}else{
			System.out.printf("Bestellung kann noch nicht versendet werden!\nEs fehlen %d Artikel im Lager!",-lagerbestand);
		}
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void deleteBestellung(Connection c, String tableName, int id) {
		try {
			Statement stmt = c.createStatement();
			String sql = "delete from Bestellung where id = " + id + ";";
			System.out.println("Bestellung " + id + " wurde gelöscht.");
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	static void updateBestellung(Connection c, String tableName, int id, int artikelid, int anzahl) {
		try {
			Statement stmt = c.createStatement();
			ResultSet rs=stmt.executeQuery("select anzahl from Bestellung where id="+id+";");//Anzahl vor dem update wird berechnet
			int anzahlAlt=rs.getInt("anzahl");
			rs.close();
			
			ResultSet rs1= stmt.executeQuery("select artikelid from Bestellung where id="+id+";");// Die Artikelid vor dem Update wird gespeichert
			int artikelIdalt=rs1.getInt("artikelid");
			rs.close();
			
			ResultSet l= stmt.executeQuery("select lagerbestand from Artikel where id="+artikelIdalt+";");
			int lagerbestand= l.getInt("lagerbestand");
			l.close();
			
			String update= "update Artikel set lagerbestand=("+lagerbestand+"+"+anzahlAlt+") where id="+artikelIdalt+";";
			stmt.executeUpdate(update); //Lagerbestand wird wieder hoch gerechnet da die Bestellung verändert wurde
			
			String sql = "update " + tableName + " set anzahl="+ anzahl +", artikelID="+ artikelid+" where id=" +id+";"; // Bestellung wird geupdated
			stmt.executeUpdate(sql);

			String sql2="update Artikel set lagerbestand=((select lagerbestand from Artikel where id="+artikelIdalt+")-"+anzahl+") where id="+artikelid+";";
			stmt.execute(sql2);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
