import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.sql.*;

public class Settings extends JFrame {

	private JPanel contentPane;

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
					Settings frame = new Settings();
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
	public Settings() {
		Login login = new Login();
		UserPanel userpanel = new UserPanel();
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 360, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(Login.backgroundColor[0], Login.backgroundColor[1], Login.backgroundColor[2]));
		panel.setBounds(0, 0, 360, 93);
		contentPane.add(panel);
		
		JLabel lblNewLabel = new JLabel(Login.userFullName);
		lblNewLabel.setBounds(12, 12, 240, 33);
		panel.add(lblNewLabel);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 20));
	
//---------------------------------- NOVO----------------------------------------------------------------			
		int numFontSize = 16;
		String user_number = Login.userNumber;
		
		if(Login.userLoged == false) {
			numFontSize = UserPanel.fontSizeSM;
			user_number = UserPanel.userSettingsMsg;
		}
//-------------------------------------------------------------------------------------------------------
		
		JLabel lblPhoneNumber = new JLabel(user_number);
		lblPhoneNumber.setForeground(Color.WHITE);
		lblPhoneNumber.setFont(new Font("Dialog", Font.BOLD, numFontSize));
		lblPhoneNumber.setBounds(12, 42, 320, 30);
		panel.add(lblPhoneNumber);
		
		
		JLabel lblNewLabel_2 = new JLabel("");
		if(UserPanel.userNumber.contains("067")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/telekomSmall.png")));
		} else if(UserPanel.userNumber.contains("068")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/mtelSmall.png")));
		} else if(UserPanel.userNumber.contains("069")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/telenorSmall.png")));
		}
		
		lblNewLabel_2.setBounds(233, 37, 105, 45);
		panel.add(lblNewLabel_2);
		
				
		Button button_1_1 = new Button("Nazad");
		button_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserPanel u = new UserPanel(UserPanel.userName, UserPanel.userNumber);
				u.setVisible(true);
				dispose();
			}
		});
		button_1_1.setForeground(Color.WHITE);
		button_1_1.setBackground(new Color(Login.backgroundColor[0], Login.backgroundColor[1], Login.backgroundColor[2]));
		button_1_1.setActionCommand("Podešavanja");
		button_1_1.setBounds(10, 100, 96, 33);
		contentPane.add(button_1_1);
		
		JLabel lblDodavanjemodifikovanjebrisanjeKorisnika = new JLabel("Ažuriraj ime i prezime");
		lblDodavanjemodifikovanjebrisanjeKorisnika.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDodavanjemodifikovanjebrisanjeKorisnika.setBounds(12, 170, 336, 20);
		contentPane.add(lblDodavanjemodifikovanjebrisanjeKorisnika);
		
		JTextField changeName = new JTextField(Login.userFullName);
		changeName.setBounds(12, 190, 322, 30);
		changeName.setBorder(new EmptyBorder(0,5,0,0));
		changeName.setColumns(10);
		contentPane.add(changeName);
		
		JLabel lblChangeYears = new JLabel("Ažuriraj godine");
		lblChangeYears.setFont(new Font("Dialog", Font.BOLD, 12));
		lblChangeYears.setBounds(12, 230, 336, 20);
		contentPane.add(lblChangeYears);
		
		JTextField changeYears = new JTextField(UserPanel.userYears);
		changeYears.setBounds(12, 250, 322, 30);
		changeYears.setColumns(10);
		changeYears.setBorder(new EmptyBorder(0,5,0,0));
		contentPane.add(changeYears);
		
		Button btnSacuvaj = new Button("Sačuvaj izmjene");
		btnSacuvaj.setActionCommand("");
		btnSacuvaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String txtYear = changeYears.getText();
				String txtName = changeName.getText();
				
				try {
							
					// MySQLkonekcija	
					Connection con = MysqlConn.conn();  
								
					PreparedStatement pstUpdate = con.prepareStatement("UPDATE user SET full_name = ?, years = ? WHERE id = ?");  
					
					pstUpdate.setString(1, txtName);
					pstUpdate.setString(2, txtYear);
					pstUpdate.setInt(3, Login.userId);
					
					//ResultSet rsCheck = pstUpdate.executeQuery();
					pstUpdate.executeUpdate();
											
						if(pstUpdate.getConnection() != null) {
							JOptionPane.showMessageDialog(null, "Podaci su uspješno ažurirani");
						} else {
							JOptionPane.showMessageDialog(null, "Došlo je do greške, pokušajte ponovo!");
						}
						

				} catch (SQLException e2) {
					JOptionPane.showMessageDialog(null, "Došlo je do greške!");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		btnSacuvaj.setBounds(12, 290, 322, 30);
		btnSacuvaj.setForeground(Color.WHITE);
		btnSacuvaj.setBackground(new Color(Login.backgroundColor[0], Login.backgroundColor[1], Login.backgroundColor[2]));
		contentPane.add(btnSacuvaj);
		
		if(Login.userAdmin) {
		
			JLabel lblUkupanBrojPoruka = new JLabel("<html>Ukupan broj poruka koje su razmijenile osobe različitog pola</html>"); //html??? -------------------!!!!
			lblUkupanBrojPoruka.setFont(new Font("Dialog", Font.BOLD, 14));
			lblUkupanBrojPoruka.setBounds(12, 335, 336, 36);
			contentPane.add(lblUkupanBrojPoruka);
						
			String str2 = Models.getMessagesGender();
			
			JTextArea txtContent2 = new JTextArea(str2);
			txtContent2.setLineWrap(true);
			txtContent2.setEditable(false);
			
			txtContent2.setBorder(new EmptyBorder(10, 10, 10, 10));
			
			
			JScrollPane ScrollPanel2 = new JScrollPane(txtContent2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			ScrollPanel2.setBounds(12, 381, 322, 140);			
			contentPane.add(ScrollPanel2);
		}
		
		JLabel lblNaj = new JLabel("Najmlaða i najstarija osoba u kontaktu");
		lblNaj.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNaj.setBounds(12, 541, 336, 15);
		contentPane.add(lblNaj);

/*- NOVO */
		String min_max;
		if(Models.getMessagesOldYoung().size() > 0) {
			 int last_el = Models.getMessagesOldYoung().size() - 1;
			 String user_max_y = Models.getMessagesOldYoung().get(0);
			 String user_min_y = Models.getMessagesOldYoung().get(last_el);
			 min_max = "- Najstarija osoba: " + user_max_y + "\n\n- Najmlaða osoba: " + user_min_y;
			 
		} else { min_max = "Ne postoji obostrana komunikacija sa korisnicima!"; }
		
		JTextArea txtContent = new JTextArea(min_max);
		txtContent.setLineWrap(true);
		txtContent.setBounds(12, 566, 322, 76);
		txtContent.setBorder(new EmptyBorder(10, 10, 10, 10));
		txtContent.setEditable(false);
		contentPane.add(txtContent);
/*- NOVO ----*/		
		
		JLabel lblPodeavanja = new JLabel("Podešavanja");
		lblPodeavanja.setFont(new Font("Dialog", Font.BOLD, 21));
		lblPodeavanja.setBounds(97, 133, 164, 25);
		contentPane.add(lblPodeavanja);
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		label.setBounds(135, 295, 70, 15);
		contentPane.add(label);
		

		
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
		
	}
}
