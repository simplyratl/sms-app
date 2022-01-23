import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		panel.setBackground(new Color(login.backgroundColor[0], login.backgroundColor[1], login.backgroundColor[2]));
		panel.setBounds(0, -12, 360, 93);
		contentPane.add(panel);
		
		JLabel lblNewLabel_2 = new JLabel("");
		if(userpanel.userNumber.contains("067")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/telekomSmall.png")));
		} else if(userpanel.userNumber.contains("068")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/mtelSmall.png")));
		} else if(userpanel.userNumber.contains("069")) {
			lblNewLabel_2.setIcon(new ImageIcon(UserPanel.class.getResource("/images/telenorSmall.png")));
		}
		lblNewLabel_2.setBounds(128, 24, 105, 45);
		panel.add(lblNewLabel_2);
		
		JLabel lblDodavanjemodifikovanjebrisanjeKorisnika = new JLabel("Dodavanje/Modifikovanje/Brisanje korisnika");
		lblDodavanjemodifikovanjebrisanjeKorisnika.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDodavanjemodifikovanjebrisanjeKorisnika.setBounds(12, 192, 336, 25);
		contentPane.add(lblDodavanjemodifikovanjebrisanjeKorisnika);
		
		JLabel lblUkupanBrojPoruka = new JLabel("Ukupan broj poruka razlicitog pola");
		lblUkupanBrojPoruka.setFont(new Font("Dialog", Font.BOLD, 14));
		lblUkupanBrojPoruka.setBounds(12, 309, 336, 15);
		contentPane.add(lblUkupanBrojPoruka);
		
		JLabel lblNaj = new JLabel("Najmlaða i najstarija osoba u kontaktu");
		lblNaj.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNaj.setBounds(12, 449, 336, 15);
		contentPane.add(lblNaj);
		
		JLabel lblPodeavanja = new JLabel("Podešavanja");
		lblPodeavanja.setFont(new Font("Dialog", Font.BOLD, 21));
		lblPodeavanja.setBounds(97, 133, 164, 25);
		contentPane.add(lblPodeavanja);
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Dialog", Font.BOLD, 14));
		label.setBounds(135, 295, 70, 15);
		contentPane.add(label);
		
		Button button_1_1 = new Button("Nazad");
		button_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserPanel u = new UserPanel(UserPanel.userName, UserPanel.userNumber);
				u.setVisible(true);
				dispose();
			}
		});
		
		button_1_1.setForeground(Color.WHITE);
		button_1_1.setBackground(new Color(182, 57, 93));
		button_1_1.setActionCommand("PodeÅ¡avanja");
		button_1_1.setBounds(10, 87, 96, 33);
		contentPane.add(button_1_1);
		
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
		
	}
}
