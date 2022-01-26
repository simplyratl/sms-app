
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mysql.cj.protocol.Resultset;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JTextArea;
import java.awt.Button;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

//---------------------------- NOVO -----------------------------------
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.util.*;




public class Inbox extends JFrame{
	
	static {
		addMessages();
	}

	
	private JPanel contentPane;
//	public static int receiver;
	private static int messageCounter = -1;
	private JPanel message_panel;
		
	
		
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
					Inbox frame = new Inbox();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	static UserPanel userpanel = new UserPanel();
	Login login = new Login();

	/**
	 * Create the frame.
	 */
	public Inbox() {		
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
			user_number = UserPanel.userinboxMsg;
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
		button_1_1.setForeground(Color.WHITE);
		button_1_1.setBackground(new Color(Login.backgroundColor[0], Login.backgroundColor[1], Login.backgroundColor[2]));
		button_1_1.setActionCommand("Podešavanja");
		button_1_1.setBounds(10, 558, 96, 33);
		contentPane.add(button_1_1);

		
		JLabel lblPoruke = new JLabel("INBOX");
		lblPoruke.setBounds(0, 93, 360, 33);
		lblPoruke.setBackground(Color.lightGray);
		lblPoruke.setForeground(Color.WHITE);
		lblPoruke.setOpaque(true);
		lblPoruke.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPoruke.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblPoruke);
		
/*
		message_panel = new JPanel();
	//	message_panel.setBackground(Color.WHITE);
		message_panel.setBorder(new EmptyBorder(5, 5, 5, 5));
	//	message_panel.setBorder( new LineBorder(new Color(128, 128, 128), 2) );
		message_panel.setBounds(10, 141, 324, 400);
		//message_panel.setLayout(null);
		//getContentPane().add(message_panel);
	  //  contentPane.add(message_panel);
		
*/
		
				
		
/*-- NOVO---------------------------------------------------------------------------------*/		
		
	//	if(Login.userLoged == true) {
						
		
			String[][] data_sent_msg = Models.getSendMsg();
			String column[] = {"KORISNIK","PORUKA","DATUM"};   
			JTable jt = new JTable(data_sent_msg,column);        
			JScrollPane sent_msg = new JScrollPane(jt);  
			jt.getColumnModel().getColumn(1).setPreferredWidth(250);
			jt.getColumnModel().getColumn(0).setPreferredWidth(150);
			jt.setRowHeight(jt.getRowHeight() + 15);
			
			String[][] data_received_msg = Models.getReceivedMsg();
			JTable jt2 = new JTable(data_received_msg,column);    
			//jt.setBounds(0,0,200,300);          
			JScrollPane received_msg = new JScrollPane(jt2);  
			jt2.getColumnModel().getColumn(1).setPreferredWidth(250);
			jt2.getColumnModel().getColumn(0).setPreferredWidth(150);
			jt2.setRowHeight(jt2.getRowHeight() + 15);
			
		    JTabbedPane tp = new JTabbedPane();  
		    tp.setBounds(10,141,320,400); 
		    tp.addTab("Primljene", received_msg);  
		    tp.addTab("Poslate", sent_msg);   
		    contentPane.add(tp);
//		} 
	    
/*-----------------------------------------------------------------------------------------*/	  	    
	    
	    
	   	
		button_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserPanel u = new UserPanel(UserPanel.userName, UserPanel.userNumber);
				u.setVisible(true);
				dispose();
			}
		});
				
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);				
	}
	
	static void addMessages() {
		try {
			
			// MySQLkonekcija	
			Connection con = MysqlConn.conn();
			
			//Class.forName("com.mysql.cj.jdbc.Driver");
			//Connection con = DriverManager.getConnection("jdbc:mysql://localhost/sms", "root","");
			
/*	
			PreparedStatement pstReceiverID = con.prepareStatement("select id from user where number = ?");
			PreparedStatement pstFetch = con.prepareStatement("select * from sms\n"
					+ "inner join user on sms.user_sender_id = user.id\n"
					+ "where sms.user_receiver_id = ?\n"
					+ "group by sms.id");
			
			pstReceiverID.setString(1,userpanel.userNumber);
			ResultSet rsReceiverID = pstReceiverID.executeQuery();
						
			
			while(rsReceiverID.next()) {
				receiver = rsReceiverID.getInt("id");
				System.out.println("ID Primaoca: " + receiver);
			}
			
			if(receiver == -1) {
				return;
			}
			
			pstFetch.setInt(1, receiver);
			ResultSet rsFetch = pstFetch.executeQuery();
			
			while(rsFetch.next()) {
				System.out.println("Poslao: " + rsFetch.getString("full_name"));
				System.out.println("Sadrzaj: " + rsFetch.getString("content"));
				messageCounter++;
			}
			

			System.out.println("Ukupan broj poruka: " + messageCounter);
			
			if(messageCounter == -1) {
				return;
			}
*/
			


			
			
			
		
			
			
			
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
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
