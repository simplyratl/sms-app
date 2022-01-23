import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;

public class Login extends JFrame{

	private JPanel contentPane;
	public JTextField txtFullName;
	public JTextField txtNumber;
	public static int[] backgroundColor = {182, 57, 93};
	
//---------------------------------- NOVO----------------------------------------------------------------
	public static String userFullName = "Gost";
	public static String userNumber   = UserPanel.userNumber; 
	public static Boolean userLoged   = false; 
	public static int userId = 9;
//-------------------------------------------------------------------------------------------------------	

	/*
	 * Launch the application.
	 */
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(MysqlConn.conn() == null) {
						System.exit(ERROR);
						return;
					}
					Login frame = new Login();
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
	
	public Login() {
		setResizable(false);
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
		panel.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
		panel.setBounds(0, 0, 250, 480);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(Login.class.getResource("/images/telekomlogo.png")));
		label.setBounds(27, 12, 194, 105);
		panel.add(label);
		
		Button registerButton = new Button("Registruj se");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Registration r = new Registration();
				r.setVisible(true);
				dispose();
			}
		});
		registerButton.setBounds(73, 173, 110, 33);
		panel.add(registerButton);
		registerButton.setForeground(Color.WHITE);
		registerButton.setBackground(new Color(182, 57, 93));
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setForeground(Color.WHITE);
		lblLogin.setFont(new Font("Dialog", Font.BOLD, 21));
		lblLogin.setBounds(83, 418, 88, 28);
		panel.add(lblLogin);
		
		
		Button button = new Button("Uloguj se");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
				   // Class.forName("com.mysql.cj.jdbc.Driver");
				   // Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sms", "root","");
				  
//---------------------------------- NOVO----------------------------------------------------------------
					// MySQLkonekcija	
					Connection con = MysqlConn.conn();
//---------------------------------- --------------------------------------------------------------------					
					
					String fullName = txtFullName.getText();  	 
					String number = txtNumber.getText();		
					
					PreparedStatement pstCheck = con.prepareStatement("select id, full_name, number from user where number='" + number + "' and full_name = '" + fullName + "'");
					ResultSet rsCheck = pstCheck.executeQuery();
					
					if(rsCheck.next()) {
//---------------------------------- NOVO----------------------------------------------------------------						
						userFullName = rsCheck.getString("full_name");
						userNumber = rsCheck.getString("number");
						userLoged = true;
						userId = rsCheck.getInt("id");
						
//--------------------------------------------------------------------------------------------------------
						JOptionPane.showInternalMessageDialog(null, "Uspješno ste se prijavili.");
						UserPanel up = new UserPanel(fullName, number);
						up.setVisible(true);
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Taj korisnik ne postoji. Pokušajte ponovo.");
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Greška, pokušajte za minut.");
				}
			}
		});
		button.setForeground(new Color(255, 255, 255));
		button.setBackground(new Color(182, 57, 93));
		button.setBounds(356, 375, 179, 43);
		contentPane.add(button);
		
		txtFullName = new JTextField();
		txtFullName.setBounds(281, 167, 318, 35);
		contentPane.add(txtFullName);
		txtFullName.setColumns(10);
		
		JLabel lblImeIPrezime = new JLabel("Ime i Prezime");
		lblImeIPrezime.setFont(new Font("Dialog", Font.BOLD, 15));
		lblImeIPrezime.setBounds(281, 137, 142, 18);
		contentPane.add(lblImeIPrezime);
		
		JLabel lblBrojTelefona = new JLabel("Broj Telefona");
		lblBrojTelefona.setFont(new Font("Dialog", Font.BOLD, 15));
		lblBrojTelefona.setBounds(281, 228, 142, 18);
		contentPane.add(lblBrojTelefona);
		
		txtNumber = new JTextField();
		txtNumber.setColumns(10);
		txtNumber.setBounds(281, 258, 318, 35);
		contentPane.add(txtNumber);
		
		JLabel serviceProviderOutput = new JLabel("");
		serviceProviderOutput.setBounds(281, 305, 318, 15);
		contentPane.add(serviceProviderOutput);
		txtNumber.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
			
			if(txtNumber.getText().length() >= 3) {
			String operatorDigit = txtNumber.getText().substring(0,3);
			
				if(operatorDigit.equals("067")) {
					backgroundColor = new int[]{182, 57, 93};
					panel.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					button.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					registerButton.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					
					label.setIcon(new ImageIcon(Login.class.getResource("/images/telekomlogo.png")));
					
					serviceProviderOutput.setText("Vaš servis provajder je Telekom.");
				} else if(operatorDigit.equals("068")) {
					backgroundColor = new int[] {237, 28, 36};
					panel.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					button.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					registerButton.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					
					label.setIcon(new ImageIcon(Login.class.getResource("/images/mtelLogo.png")));
					
					serviceProviderOutput.setText("Vaš½ servis provajder je MTEL.");
				} else if(operatorDigit.equals("069")) {
					backgroundColor = new int[] {42, 160, 215};
					panel.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					button.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					registerButton.setBackground(new Color(backgroundColor[0] ,backgroundColor[1] ,backgroundColor[2]));
					
					label.setIcon(new ImageIcon(Login.class.getResource("/images/telenorLogo.png")));
					
					serviceProviderOutput.setText("Vaš servis provajder je Telenor.");
				} else {
					serviceProviderOutput.setText("Taj servis provajder ne postoji.");
				}
			} else {
				serviceProviderOutput.setText("");
			}
		});
	}
	
	public interface SimpleDocumentListener extends DocumentListener {
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
