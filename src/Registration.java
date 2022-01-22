import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.desktop.UserSessionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.*;
import java.sql.*;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class Registration extends JFrame {

	private JPanel contentPane;
	private JTextField txtFullName;
	private JTextField txtNumber;
	private JTextField txtYears;
	private final ButtonGroup genderGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	
//	List<User> users = new LinkedList<User>();
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			
			public void run() {
				try {
					Registration frame = new Registration();
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	
	public Registration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(51, 0, 153));
		panel.setBounds(0, 0, 250, 480);
		contentPane.add(panel);
		panel.setLayout(null);
		
		Button loginButton = new Button("Uloguj se");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login l = new Login();
				l.setVisible(true);
				dispose();
			}
		});
		loginButton.setBounds(69, 173, 110, 33);
		panel.add(loginButton);
		loginButton.setForeground(Color.WHITE);
		loginButton.setBackground(new Color(51, 0, 153));
		
		JLabel lblRegistracija = new JLabel("REGISTRACIJA");
		lblRegistracija.setForeground(Color.WHITE);
		lblRegistracija.setFont(new Font("Dialog", Font.BOLD, 21));
		lblRegistracija.setBounds(41, 420, 173, 28);
		panel.add(lblRegistracija);
		
		JRadioButton selectMuski = new JRadioButton("Mu\u0161ki");
		genderGroup.add(selectMuski);
		selectMuski.setBounds(281, 216, 86, 23);
		contentPane.add(selectMuski);
		
		JRadioButton selectZenski = new JRadioButton("\u017Denski");
		genderGroup.add(selectZenski);
		selectZenski.setBounds(403, 216, 86, 23);
		contentPane.add(selectZenski);	
		
		JLabel outputISP = new JLabel("");
		outputISP.setBounds(281, 335, 318, 14);
		contentPane.add(outputISP);
		
		Button registerButton = new Button("Registruj se");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				
				String gender = null;
				
				if(selectMuski.isSelected()) {
					gender = "Muski";
				} else if(selectZenski.isSelected()){
					gender = "Zenski";
				}
				
				if(txtFullName.getText().equalsIgnoreCase("") || txtYears.getText().equalsIgnoreCase("") 
						 || txtNumber.getText().equalsIgnoreCase("")) {
					JOptionPane.showMessageDialog(null, "Morate unijeti svako polje vezano za registraciju registraciju");
					
					txtFullName.setText("");
					txtYears.setText("");
					txtNumber.setText("");
					gender = "";
					genderGroup.clearSelection();
					return;
				}
				
				if(outputISP.getText().equals("Taj provajder ne postoji.")) {
					JOptionPane.showMessageDialog(null, "Morate izabrati validnog provajdera.");
					txtFullName.setText("");
					txtYears.setText("");
					txtNumber.setText("");
					gender = "";
					genderGroup.clearSelection();
					return;
				}
				

				int years = Integer.parseInt(txtYears.getText());
				String fullName = txtFullName.getText();
				String number = txtNumber.getText();
				
				

				
				
				try {
					 Class.forName("com.mysql.cj.jdbc.Driver");
					// Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sms", "root","");
					
					// MySQLkonekcija	
					MysqlConn MysqlConn = new MysqlConn();
					Connection con = MysqlConn.conn();
										 
					PreparedStatement pstCheck = con.prepareStatement("select full_name, number from user where number='" + number + "'");
					
					ResultSet rsCheck = pstCheck.executeQuery();
					
					if(rsCheck.next()) {
						JOptionPane.showMessageDialog(null, "Ve� postoji korisnik sa brojem " + number + ".");
						return;
					}

					PreparedStatement pst = con.prepareStatement("insert into user(full_name, gender, number, years) values (?,?,?,?)");
					
					
					pst.setString(1, fullName);
					pst.setString(2, gender);
					pst.setString(3, number);
					pst.setInt(4, years);
							
					pst.executeUpdate();
					
					txtFullName.setText("");
					txtYears.setText("");
					txtNumber.setText("");
					gender = "";
					genderGroup.clearSelection();
					
					JOptionPane.showMessageDialog(null, "Uspje�na registracija " + fullName + ".");
					txtFullName.requestFocus();
					
					
				} catch (ClassNotFoundException ex) {
					JOptionPane.showMessageDialog(null, "ClassNotFound");
				} catch (SQLException el) {
					el.printStackTrace();
					JOptionPane.showMessageDialog(null, "SQL error");
				}				
			}
		});
		registerButton.setForeground(new Color(255, 255, 255));
		registerButton.setBackground(new Color(51, 0, 153));
		registerButton.setBounds(356, 375, 179, 43);
		contentPane.add(registerButton);
		
		JLabel lblImeIPrezime = new JLabel("Ime i Prezime *");
		lblImeIPrezime.setFont(new Font("Dialog", Font.BOLD, 15));
		lblImeIPrezime.setBounds(281, 36, 142, 18);
		contentPane.add(lblImeIPrezime);
		
		txtFullName = new JTextField();
		txtFullName.setBounds(281, 66, 318, 35);
		contentPane.add(txtFullName);
		txtFullName.setColumns(10);
		
		JLabel lblBrojTelefona = new JLabel("Broj Telefona *");
		lblBrojTelefona.setFont(new Font("Dialog", Font.BOLD, 15));
		lblBrojTelefona.setBounds(281, 263, 142, 18);
		contentPane.add(lblBrojTelefona);
		
		txtYears = new JTextField();
		txtYears.setColumns(10);
		txtYears.setBounds(281, 143, 318, 35);
		contentPane.add(txtYears);
		
		JLabel lblGodine = new JLabel("Godine *");
		lblGodine.setFont(new Font("Dialog", Font.BOLD, 15));
		lblGodine.setBounds(281, 113, 142, 18);
		contentPane.add(lblGodine);
		
		JLabel lblPol = new JLabel("Pol *");
		lblPol.setFont(new Font("Dialog", Font.BOLD, 15));
		lblPol.setBounds(281, 190, 142, 18);
		contentPane.add(lblPol);
		
		txtNumber = new JTextField();
		txtNumber.setColumns(10);
		txtNumber.setBounds(281, 293, 318, 35);
		contentPane.add(txtNumber);
		txtNumber.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
			if(txtNumber.getText().length() >= 3) {
				String operatorDigit = txtNumber.getText().substring(0,3);
				
				if(operatorDigit.equals("067")) {
					outputISP.setText("Va� provajder je Telekom.");
				} else if(operatorDigit.equals("068")) {
					outputISP.setText("Va� provajder je MTEL.");
				} else if(operatorDigit.equals("069")) {
					outputISP.setText("Va� provajder je Telekom.");
				} else {
					outputISP.setText("Taj provajder ne postoji.");
				}
			} else {
				outputISP.setText("");
			}
		});
		
			
	}
	
	private interface SimpleDocumentListener extends DocumentListener {
	    void update(DocumentEvent e);

	    default void insertUpdate(DocumentEvent e) {
	        update(e);
	    }
	    default void removeUpdate(DocumentEvent e) {
	        update(e);
	    }
	    default void changedUpdate(DocumentEvent e) {
	        update(e);
	    }
	}
}