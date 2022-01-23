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
	 * |****************************************************************************
	 * *****************************************************************
	 * |============================================================================
	 * ================================================================= | [ - ]
	 * INBOX
	 * |============================================================================
	 * ================================================================= |
	 * |****************************************************************************
	 * ***************************************************************** |
	 */

	/*
	 * |============================================================================
	 * === | [ 1 ] ID PRIJAVLJENOG KORISNIKA
	 * |============================================================================
	 * === | Definise se promjenljiva "userId", koja je dostupna svim metodama u
	 * ovoj klasi |
	 */
	private static void setUserId() {


		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement getUserID = con.prepareStatement("SELECT id FROM user WHERE number = ?");
			getUserID.setString(1, UserPanel.userNumber);
			ResultSet result_GetUserId = getUserID.executeQuery();

			while (result_GetUserId.next()) {
				inbox_userId = result_GetUserId.getInt("id");
			}

		} catch (Exception e) {

		}

	}

	/*
	 * |============================================================================
	 * === | [ 2 ] KORISNICI KOJIMA SU POSLATE PORUKE
	 * |============================================================================
	 * === | Metoda vraca niz koji cine korisnici kojima su poslate poruke |
	 */
	public static String[][] getSendMsg() {
		
		setUserId();
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
	 * |============================================================================
	 * === | [ 2 ] KORISNICI OD KOJIH SU PORUKE PRIMLJENE
	 * |============================================================================
	 * === | Metoda vraca niz koji cine korisnici od kojih su poruke primljene |
	 */
	public static String[][] getReceivedMsg() {
		
		setUserId();
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
				// data[i][0] = result_msgSend.getString("dt");
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
	
	*/

	/*
	 * |============================================================================
	 * === | [ 3 ] UKUPAN BROJ POSLATIH PORUKA
	 * |============================================================================
	 * === | Metoda vraca niz koji cine korisnici od kojih su poruke primljene |
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
			// TODO: handle exception

			return -1;
		}
	}

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
			// TODO: handle exception

			return -1;
		}
	}

}
