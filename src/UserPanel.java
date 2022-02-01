import java.awt.BorderLayout;
import java.sql.*;
import java.util.Date;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.Result;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserPanel extends JFrame {
	
	
	private JPanel contentPane;
	public static String userFullName = "Gost";
	public static String userName 	  = "Gost";
	public static String userYears    = null;
	public static String userGender   = null;
	private int findSpace = -1;
	public static String userNumber   = "069123456";  
	public static String usersentMsg  = "Morate biti registrovani da biste poslali poruku!";
	public static String userinboxMsg = "Morate biti registrovani da biste vidjeli poruke!";  
	public static String userSettingsMsg = "<html>Podešavanja su dozvoljena samo registrovanim korisnicima!</html>";
	private JTextField txtNumberTo;
	public static int userReceiverID  = -1;
	public static int userSenderID    = -1;
	private String userReceiverIDName = null;
	private Date getDate 			  = new Date();
	private Timestamp date 			  = new Timestamp(getDate.getTime());
	
	public static int fontSizeSM 	  = 12;
	public static int fontSizeMD 	  = 16;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(MysqlConn.conn() == null) {
						return;
					}
					UserPanel frame = new UserPanel(userName, userNumber);
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
	public UserPanel(String name, String number) {
		
/* ???	
		userFullName = name;
		userNumber = number;
		findSpace = userFullName.indexOf(" ");
		Login login = new Login();
		
		if(findSpace != -1) {
			userName = userFullName.substring(0, findSpace);
		}
*/		

// NOVO
		
//----------------------------------Uzimanje od promjenjive od Login panela.----------------------------------
		
		Login login = new Login();
		userFullName = Login.userFullName;
		userNumber = Login.userNumber;
//
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 360, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
		
		JLabel lblNewLabel = new JLabel(userFullName);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		lblNewLabel.setBounds(12, 12, 240, 33);
		contentPane.add(lblNewLabel);
		
//---------------------------------- NOVO----------------------------------------------------------------	
		int numFontSize = 16;
		if(Login.userLoged == false) {
			numFontSize = UserPanel.fontSizeSM;
			userNumber = usersentMsg;
		}
//---------------------------------- --------------------------------------------------------------------	
		
		JLabel lblNewLabel_1 = new JLabel(userNumber);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, numFontSize));
		lblNewLabel_1.setBounds(12, 42, 320, 30);
		contentPane.add(lblNewLabel_1);
		

		JPanel panel = new JPanel();
		panel.setBackground(new Color(login.backgroundColor[0], login.backgroundColor[1], login.backgroundColor[2]));
		panel.setBounds(0, 0, 360, 93);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("");
		if(userNumber.contains("067")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/telekomSmall.png")));
		} else if(userNumber.contains("068")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/mtelSmall.png")));
		} else if(userNumber.contains("069")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/telenorSmall.png")));
		}
		
		lblNewLabel_2.setBounds(233, 37, 105, 45);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Pošaljite poruku");
		lblNewLabel_3.setFont(new Font("Dialog", Font.PLAIN, 21));
		lblNewLabel_3.setBounds(99, 163, 192, 25);
		contentPane.add(lblNewLabel_3);
		
		txtNumberTo = new JTextField();
		txtNumberTo.setBounds(12, 238, 322, 40);
		contentPane.add(txtNumberTo);
		txtNumberTo.setColumns(10);
		
		
		JLabel lblKome = new JLabel("Kome (unesite broj)");
		lblKome.setFont(new Font("Dialog", Font.BOLD, 16));
		lblKome.setBounds(12, 202, 322, 25);
		contentPane.add(lblKome);
		
		JLabel lblKome_1 = new JLabel("Sadr\u017Eaj");
		lblKome_1.setFont(new Font("Dialog", Font.BOLD, 16));
		lblKome_1.setBounds(12, 314, 102, 15);
		contentPane.add(lblKome_1);
		
		JTextArea txtContent = new JTextArea();
		txtContent.setLineWrap(true);
		txtContent.setBounds(12, 341, 322, 207);
		contentPane.add(txtContent);
		
		Button button = new Button("Pošalji");
		button.setActionCommand("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String toNumber = txtNumberTo.getText();
				String content = txtContent.getText();
				
				
				/*
				 * Provjera da korisnik ne moze da posalje poruku samom sebi
				 */
				
				if(toNumber.equals(Login.userNumber)) {
					JOptionPane.showMessageDialog(null, "Nemoguće je poslati poruku samom sebi.");
					return;
				}
				
				try {
										
/* ??? */				//	Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sms", "root","");
					
					// MySQLkonekcija	
					//MysqlConn MysqlConn = new MysqlConn();
					//Connection con = MysqlConn.conn();
					
					
					
					PreparedStatement pstCheck = con.prepareStatement("select * from user where number = ?");
					PreparedStatement pstReceiver = con.prepareStatement("select id, full_name from user where number = ?");
					PreparedStatement pstSender = con.prepareStatement("select id from user where number = ?");
					
					pstCheck.setString(1, toNumber);
					pstReceiver.setString(1, toNumber);
					pstSender.setString(1, userNumber);
					
					
					ResultSet rsCheck = pstCheck.executeQuery();
					ResultSet rsReciever = pstReceiver.executeQuery();
					ResultSet rsSender = pstSender.executeQuery();
					
					
					while(rsReciever.next()) {
						userReceiverID = rsReciever.getInt("id");	
						userReceiverIDName = rsReciever.getString("full_name");
					}
					
					while(rsSender.next()) {
						userSenderID = rsSender.getInt("id");
					}
					
					if(rsCheck.next()) {
						
						PreparedStatement pst = con.prepareStatement("insert into sms(content, user_sender_id, user_receiver_id) values(?,?,?)");
						
						pst.setString(1, content);
						pst.setInt(2, userSenderID);
						pst.setInt(3, userReceiverID);
												
						pst.executeUpdate();
						
						if(pst.getConnection() != null) {
							JOptionPane.showMessageDialog(null, "Poruka uspješno poslata korisniku " + userReceiverIDName);
						} else {
							JOptionPane.showMessageDialog(null, "Poruka nije poslata!");
						}
						
					} else {
//---------------------------------- NOVO----------------------------------------------------------------
						if(Login.userLoged == false) {
							JOptionPane.showMessageDialog(null, userNumber);
						} else if(toNumber.length() == 0) {
							JOptionPane.showMessageDialog(null, "Morate unijeti broj da biste poslali poruku!");
//---------------------------------- --------------------------------------------------------------------
						} else {
							JOptionPane.showMessageDialog(null, "Broj koji ste unijeli ne nalazi se u našoj bazi");
						}
					}
					
					
				} catch (SQLException e2) {
					JOptionPane.showMessageDialog(null, "Samo registrovani korisnici mogu da šalju poruke!");
				}
			}
		});
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(Login.backgroundColor[0], Login.backgroundColor[1], Login.backgroundColor[2]));
		button.setBounds(85, 608, 179, 43);
		contentPane.add(button);
		
		Button button_1 = new Button("Podešavanja");
		button_1.setActionCommand("Pode\u0161avanja");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings s = new Settings();
				s.setVisible(true);
				dispose();
			}
		});
				
		button_1.setForeground(Color.WHITE);
		button_1.setBackground(new Color(Login.backgroundColor[0], Login.backgroundColor[1], Login.backgroundColor[2]));
		button_1.setBounds(238, 99, 96, 33);
		
		if(Login.userLoged == false) {
			button_1.setEnabled(false);
			button_1.setBackground(Color.WHITE);
			
		}
		
		contentPane.add(button_1);
		
		
		Button button_1_1 = new Button("Inbox");
		button_1_1.setForeground(Color.WHITE);
		button_1_1.setBackground(new Color(Login.backgroundColor[0], Login.backgroundColor[1], Login.backgroundColor[2]));
		button_1_1.setActionCommand("Podešavanja");
		button_1_1.setBounds(10, 99, 96, 33);
		contentPane.add(button_1_1);
		button_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Inbox inbox = new Inbox();
				inbox.setVisible(true);
				dispose();
			}
		});
	}


	public UserPanel() {
		
	}
	

}
