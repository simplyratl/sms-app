import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.sql.*;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Models {

	private static int inbox_userId;
	public static int senderId;

	static {
		countReceived();
		countSender();
	}

	public static void main(String args[]) {

		getSendMsg();

	}


	/*
	 * |=============================================================================== 
	 * | [ 1 ] ID PRIJAVLJENOG KORISNIKA
	 * |=============================================================================== 
	 * | Definise se promjenljiva "userId", koja je dostupna svim metodama u
	 * | ovoj klasi 
	 * 
	 */
	
	private static void getUserData() {
		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement getUserID = con.prepareStatement("SELECT id FROM user WHERE number = ?");
			getUserID.setString(1, UserPanel.userNumber);
			ResultSet result_GetUserId = getUserID.executeQuery();

			//Trazenje id-a korisnika.
			
			while (result_GetUserId.next()) {
				inbox_userId = result_GetUserId.getInt("id");
			}

		} catch (Exception e) {

		}

	}

	/*
	 * |=============================================================================== 
	 * | [ 2 ] KORISNICI KOJIMA SU POSLATE PORUKE
	 * |=============================================================================== 
	 * | Metoda vraca niz koji cine korisnici kojima su poslate poruke |
	 */
	
	public static String[][] getSendMsg() {
		
		getUserData();
		
		/*
		 * Count received sluzi je metoda koja sluzi da izbroji koliko da napravi
		 * redova sa porukama, a 3 oznacava da postoje 3 kolone.
		 */
		String data[][] = new String[countReceived()][3];

		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement msgSend = con.prepareStatement("SELECT *, DATE_FORMAT(date, '%d-%m-%Y %H:%i:%s') as dt "
					+ "FROM sms, user "
					+ "WHERE sms.user_sender_id = ? AND sms.user_receiver_id = user.id AND sms.user_sender_id != user.id "
					+ "ORDER BY dt DESC");

			msgSend.setInt(1, inbox_userId);
			ResultSet result_msgSend = msgSend.executeQuery();

			int i = 0;
			
			//Ispisivanje kolona u slucaju da upit je u redu.
			
			while (result_msgSend.next()) {
				// data[i][0] = result_msgSend.getString("dt");
				data[i][0] = result_msgSend.getString("full_name");
				data[i][1] = result_msgSend.getString("content");
				data[i][2] = result_msgSend.getString("dt");
				i++;
			}
			return data;

		} catch (Exception e) {

		}

		return data;
	}

	/*
	 * |=============================================================================== 
	 * | [ 2 ] KORISNICI OD KOJIH SU PORUKE PRIMLJENE
	 * |=============================================================================== 
	 * | Metoda vraca niz koji cine korisnici od kojih su poruke primljene |
	 */
	
	public static String[][] getReceivedMsg() {
		
		getUserData();
		String data[][] = new String[countSender()][3];

		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement msgReceiver = con
					.prepareStatement("SELECT *, DATE_FORMAT(date, '%d-%m-%Y %H:%i:%s') as dt " + "FROM sms, user "
							+ "WHERE sms.user_receiver_id = ? AND sms.user_sender_id = user.id AND user.id != ? "
							+ "ORDER BY dt DESC");

			msgReceiver.setInt(1, inbox_userId);
			msgReceiver.setInt(2, inbox_userId);
			ResultSet result_msgReceiver = msgReceiver.executeQuery();

			int i = 0;
			while (result_msgReceiver.next()) {
				// data[i][0] = result_msgSend.getString("dt"); ???
				data[i][0] = result_msgReceiver.getString("full_name");
				data[i][1] = result_msgReceiver.getString("content");
				data[i][2] = result_msgReceiver.getString("dt");
				i++;
			}
			return data;

		} catch (Exception e) {

		}

		return data;
	}

/*
 * |============================================================================
 * === | [ 3 ] UKUPAN BROJ POSLATIH PORUKA ZA ODREDJENOG KORISNIKA
 * |============================================================================
 * === | Metoda vraca ukupan broj poslatih poruka |
 */

	public static int countReceived() {
		
		try {
			Connection con = MysqlConn.conn();

			PreparedStatement msgReceiver = con
					.prepareStatement("SELECT count(*) as ukupno FROM sms WHERE user_sender_id = ?");

			msgReceiver.setInt(1, Login.userId);

			ResultSet result_msgReceiver = msgReceiver.executeQuery();

			int returnValue = -1;

			while (result_msgReceiver.next()) {
				returnValue = result_msgReceiver.getInt("ukupno");

			}
			return returnValue;
		} catch (Exception e) {
			return -1;
		}
	}
	

/*
 * |============================================================================
 * === | [ 4 ] UKUPAN BROJ POSLATIH PORUKA ZA ODREDJENOG KORISNIKA
 * |============================================================================
 * === | Metoda vraca ukupan broj primljenih poruka |
 */		

	public static int countSender() {
		
		try {
			Connection con = MysqlConn.conn();

			PreparedStatement msgReceiver = con
					.prepareStatement("SELECT count(*) as ukupno FROM sms WHERE user_receiver_id = ?");

			msgReceiver.setInt(1, Login.userId);

			ResultSet result_msgReceiver = msgReceiver.executeQuery();

			int returnValue = -1;

			while (result_msgReceiver.next()) {
				returnValue = result_msgReceiver.getInt("ukupno");
			}
			return returnValue;
		} catch (Exception e) {
			return -1;
		}
	}
	
	
	/*
	 * |****************************************************************************
	 * *****************************************************************
	 * |============================================================================
	 * ================================================================= | [ - ]
	 * PODESAVANJA
	 * |============================================================================
	 * ================================================================= |
	 * |****************************************************************************
	 * ***************************************************************** |
	 */
	
	/*
	 * |=============================================================================== 
	 * | [ 5 ] UKUPAN BROJ PORUKA KOJE SU RAZMIJENILE OSOBE RAZLICITOG POLA 
	 * |=============================================================================== 
	 * |
	 */
	
	public static String getMessagesGender() {
	
		int i = 0;
		int rb = 1;
		String str = "";

		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement msg = con.prepareStatement("SELECT user.full_name as user1, user2.full_name as user2 "
					+ "FROM sms, user, sms as sms2, user as user2 "
					+ "WHERE sms.id = sms2.id AND user.gender != user2.gender AND ("
					+ "(user.id = sms.user_sender_id AND user2.id = sms2.user_receiver_id) OR "
					+ "(user.id = sms.user_receiver_id AND user2.id = sms2.user_sender_id AND sms2.user_receiver_id = sms.user_sender_id) )"
			);

			ResultSet result_msg = msg.executeQuery();
			
			while (result_msg.next()) {
				i++;
				str += rb + ". " + result_msg.getString("user1") + " - " + result_msg.getString("user2") + "\n\n";
				rb++;
			}
			
			str = "Ukupan broj poruka: " + i + "\n\n" + str;
			
			return str;
			
		} catch (Exception e) {
			return "";
		}

	}
	
	
/*
 * |=============================================================================== 
 * | [ 6 ] NAJMLADJA I NAJSTARIJA OSOBA U KONTAKTU
 * |===============================================================================
 * | Metoda vraca najmladju i najstariju osobu sa kojom je korisnik 
 * | razmjenjivao poruke(obostrana komunikacija)
 */
	public static ArrayList<String> getMessagesOldYoung() {
		
		getUserData();
		String str = "";
		ArrayList<String> user_list = new ArrayList<String>();
				
		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement query = con.prepareStatement("SELECT DISTINCT(user.full_name), user.years "
					+ "FROM sms, user, sms as sms2, user as user2 "
					+ "WHERE (sms.user_sender_id = ? AND sms.user_receiver_id = user.id AND sms.user_sender_id != user.id AND "
					+ "sms.user_receiver_id = sms2.user_sender_id AND sms2.user_receiver_id = ?) OR  "
					+ "(sms.user_receiver_id = ? AND sms.user_sender_id = user.id AND sms.user_receiver_id != user.id AND "
					+ "sms.user_sender_id = sms2.user_receiver_id AND sms2.user_sender_id = ? ) "
					+ "ORDER BY user.years DESC "
			);

			query.setInt(1, inbox_userId);
			query.setInt(2, inbox_userId);
			query.setInt(3, inbox_userId);
			query.setInt(4, inbox_userId);
			ResultSet result_query = query.executeQuery();

			while (result_query.next()) {
				str = result_query.getString("full_name") +" ("+ result_query.getString("years") + " god.)"; 
				user_list.add(str);
			}
			
			return user_list;

		} catch (Exception e) {
			return user_list;
		}

		
	}
}
