import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.sql.*;

import javax.swing.table.DefaultTableModel;

public class Grafik {

	private static String arrayLink[];
	private static JTextArea txtResult;

	/*
	 * |============================================================================
	 * === | [ 1 ] METODA VRACA NIZ SA NAZIVIMA OPERATERA
	 * |============================================================================
	 * === | |
	 */
	private static ArrayList<Map<String, String>> getOperators() {

		ArrayList<Map<String, String>> mapCountryCodes1 = new ArrayList<Map<String, String>>();
		for (int m = 0; m < 3; m++) {
			Map<String, String> mapCountryCodes = new HashMap<>();
			mapCountryCodes.put("id", "USA" + m);
			mapCountryCodes.put("Country", "United Kingdom" + m);
			mapCountryCodes.put("P3", "France" + m);
			mapCountryCodes.put("P4", "Japan" + m);
			mapCountryCodes1.add(mapCountryCodes);
		}

		System.out.println(mapCountryCodes1);

		/*
		 * Set<String> setCodes = mapCountryCodes1.keySet(); Iterator<String> iterator =
		 * setCodes.iterator();
		 * 
		 * while (iterator.hasNext()) { String code = iterator.next(); String country =
		 * mapCountryCodes1.get(code);
		 * 
		 * System.out.println(code + " => " + country); }
		 */

		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement getID = con.prepareStatement("SELECT id, name FROM operator");
			ResultSet result_getID = getID.executeQuery();

			ArrayList<Map<String, String>> operators = new ArrayList<Map<String, String>>();
			while (result_getID.next()) {
				Map<String, String> operator = new HashMap<String, String>();
				operator.put("id", result_getID.getString("id"));
				operator.put("operator", result_getID.getString("name"));
				operators.add(operator);
			}
			return operators;

		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * |============================================================================
	 * === | [ 1 ] METODA VRACA NIZ SA IMENIMA KORISNIKA
	 * |============================================================================
	 * === | |
	 */

	private static ArrayList<Map<String, String>> getUsers() {

		if (MysqlConn.conn() == null) {
			return null;
		}

		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement getID = con.prepareStatement("SELECT id, full_name FROM user");
			ResultSet result_getID = getID.executeQuery();

			ArrayList<Map<String, String>> users = new ArrayList<Map<String, String>>();
			while (result_getID.next()) {
				Map<String, String> user = new HashMap<String, String>();
				user.put("user_id", result_getID.getString("id"));
				user.put("user_name", result_getID.getString("full_name"));
				users.add(user);
			}
			return users;

		} catch (Exception e) {
			return null;

		}
	}

	/*
	 * |============================================================================
	 * === | [ 1 ] METODA VRACA NAZIV PRVOG CVORA NA OSNOVU POSLATOG ID-a
	 * |============================================================================
	 * === | |
	 */

	private static String getEdgeName(String id, String ind) {
		if (MysqlConn.conn() == null) {
			return null;
		}

		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			if (ind == "") {
				PreparedStatement getID = con.prepareStatement("SELECT id, name FROM operator WHERE id = ?");
				getID.setString(1, id);
				ResultSet result_getID = getID.executeQuery();

				if (result_getID.next()) {
					return result_getID.getString("name");

				}

			} else if (ind == "user") {
				PreparedStatement getIdUser = con.prepareStatement("SELECT id, full_name FROM user WHERE id = ?");
				getIdUser.setString(1, id);
				ResultSet result_getIdUser = getIdUser.executeQuery();

				if (result_getIdUser.next()) {
					return result_getIdUser.getString("full_name");

				}
			}

			return null;

		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * |============================================================================
	 * === | [ 2 ] METODA VRACA LISTU POVEZANOSTI ZA ODREDJENI CVOR
	 * |============================================================================
	 * === | |
	 */
	private static String getLink(String id, String ind) {
		txtResult = new JTextArea();
		// Ime prvog cvora
		String first_edge = "";
		// String koji oznacava listu povezanih cvorova
		String link_e = "";

		// Definisani uslovi ako se trazi veza do susjednog cvora koji
		// oznacava operatera, ind = ""(inicijalna vrijednost)
		String sql_select = " id, full_name ";
		String sql_from = " user ";
		String sql_where = " operator_id = ? ";
		first_edge = getEdgeName(id, "");

		// Definisani uslovi ako se trazi veza do susjednog cvora koji
		// oznacava korisnika, ind = "user"
		if (ind == "user") {
			sql_select = " DISTINCT user.number, user.full_name ";
			sql_from = " user, sms ";
			sql_where = " (user_sender_id = ? AND user.id = sms.user_receiver_id AND user_sender_id != user.id) OR "
					+ "(sms.user_receiver_id = ? AND user.id = sms.user_sender_id AND sms.user_receiver_id != user.id) ";

			first_edge = getEdgeName(id, "user");
		}

		link_e += first_edge + "  =  ";
		System.out.println("Prvi cvor1: " + link_e);

		try {
			// MySQLkonekcija
			Connection con = MysqlConn.conn();

			PreparedStatement getID = con
					.prepareStatement("SELECT" + sql_select + "FROM" + sql_from + "WHERE" + sql_where);

			getID.setString(1, id);

			if (ind == "user") {
				getID.setString(2, id);
			}

			ResultSet result_getID = getID.executeQuery();

			ArrayList<Map<String, String>> links = new ArrayList<Map<String, String>>();
			while (result_getID.next()) {
				link_e += result_getID.getString("full_name") + "  - >  ";

				Map<String, String> link = new HashMap<String, String>();
				link.put("edge_name", result_getID.getString("full_name"));
				links.add(link);

			}
			if (link_e.endsWith("  - >  ")) {
				int last_char = link_e.lastIndexOf("  - >  ");
				link_e = link_e.substring(0, last_char);
			}

			txtResult.setMargin(new Insets(10, 10, 10, 10));
			txtResult.setBorder(UIManager.getBorder("EditorPane.border"));
			txtResult.setColumns(1);
			txtResult.setText(link_e);

			txtResult.setMargin(new Insets(10, 10, 10, 10));
			txtResult.setBorder(UIManager.getBorder("EditorPane.border"));
			txtResult.setColumns(1);
			// txtResult.setText("5555555");

			// System.out.println("Linkovi : " + links);

			return result_getID.getString("full_name");

		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {

		if (MysqlConn.conn() == null) {
			return;
		}

		System.out.println(getLink("1", ""));
		System.out.println(getOperators());
		System.out.println(getUsers());

		JFrame prozor = new JFrame("APLIKACIJA - GRAFIK");
		prozor.setSize(1024, 700);
		prozor.setLocation(200, 200);
		prozor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Color maroon = new Color(128, 0, 0);

		JPanel panel = new JPanel();
		panel.setLayout(null); // new GridLayout(3,5, 2, 2)

		JLabel label = new JLabel("LISTA POVEZANIH ÈVOROVA");
		label.setBounds(0, 1, 1008, 108);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Calibri", Font.BOLD, 20));

		panel.add(label);

		txtResult.setEditable(false);
		txtResult.setLineWrap(true);
		// txtResult.setBounds(10, 95, 988, 509);
		panel.add(txtResult);

		System.out.println(arrayLink);

		prozor.getContentPane().add(panel);
		panel.setLayout(new GridLayout(3, 5, 2, 2));
		prozor.setVisible(true);
	}
}