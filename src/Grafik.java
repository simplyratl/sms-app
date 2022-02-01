import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.sql.*;

import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.ArrayUtils; 

//import java.util.Arrays;

public class Grafik {

	private static String arrayLink[][];	
	private static String edge_links = "";	
	
	private static JTable tableGraph1;
	private static JTable tableGraph2;
	private static JTable tableGraph3;
	private static JFrame prozor;
	private static JFrame prozor2;
	
	private static Boolean btnOperateriClickEvent = true;
	private static Boolean btnKorisniciClickEvent = true;
	private static Boolean btnAllGraphClickEvent = true;
	private static Boolean btnCreateGraphClickEvent = true;
	
	// Operateri
	private static JScrollPane scrollPanel1;
	private static Boolean showSP1 = false;
	// Korisnici
	private static JScrollPane scrollPanel2;
	private static Boolean showSP2 = false;
	// Svi
	private static JScrollPane scrollPanel3;
	private static Boolean showSP3 = false;
	// Kreiranje Grafa
	private static JScrollPane scrollPanel4;
	private static Boolean showSP4 = false;
	
	private static String arrayRowOperateri[][] = new String[ getOperators().size() ][2];
	private static int br1 = 0;
	private static String arrayRowKorisnici[][] = new String[getUsers().size()][2];
	private static int br2 = 0;
	
	private static String column[] = {"POÈETNI ÈVOR","POVEZANI ÈVOROVI"};
		
/*
|===============================================================================
|   [ 1 ] METODA VRACA NIZ SA NAZIVIMA OPERATERA
|===============================================================================
| 
|
*/	
		private static ArrayList<String> getOperators()
		{
			ArrayList<String> arr_list = new ArrayList<String>();
			 						
			try {	
				// MySQLkonekcija	
				Connection con = MysqlConn.conn();
								
				PreparedStatement getID = con.prepareStatement("SELECT id, name FROM operator");
				ResultSet result_getID  = getID.executeQuery();
				
				while(result_getID.next()) {  
			
					arr_list.add( result_getID.getString("id") +"|"+ result_getID.getString("name"));
				}
				return arr_list;
								
			} catch (Exception e) {
				
				return null;
			}
			
			
			
		}	
		
/*
|===============================================================================
|   [ 1 ] METODA VRACA NIZ SA IMENIMA KORISNIKA
|===============================================================================
| 
|
*/	
				
		private static ArrayList<String> getUsers()
		{
			ArrayList<String> arr_list = new ArrayList<String>();
			try {	
				// MySQLkonekcija	
				Connection con = MysqlConn.conn();
				
				PreparedStatement getID = con.prepareStatement("SELECT id, full_name FROM user");
				ResultSet result_getID = getID.executeQuery();
					
				while(result_getID.next()) {  
					arr_list.add( result_getID.getString("id") +"|"+ result_getID.getString("full_name"));
					
				}
				return arr_list;
						
			} catch (Exception e) {
						
				return null;		
			}
		
					
		}	
				
	
/*
|===============================================================================
|   [ 1 ] METODA VRACA NAZIV PRVOG CVORA NA OSNOVU POSLATOG ID-a
|===============================================================================
| 
|
*/	
	
	private static String getEdgeName(String id, String ind)
	{
		try {	
			// MySQLkonekcija	
			Connection con = MysqlConn.conn();
			  
			if(ind == "") {  
				PreparedStatement getID = con.prepareStatement("SELECT id, name FROM operator WHERE id = ?");
				getID.setString(1, id);
				ResultSet result_getID = getID.executeQuery();
				
				if(result_getID.next()) {    					
					return result_getID.getString("name");
		
				} 
				
			} else if(ind == "user") {  
				PreparedStatement getIdUser = con.prepareStatement("SELECT id, full_name FROM user WHERE id = ?");
				getIdUser.setString(1, id);
				ResultSet result_getIdUser = getIdUser.executeQuery();
				
				if(result_getIdUser.next()) {
					return result_getIdUser.getString("full_name");
					
				} 
			}
			
		} catch (Exception e) {
			
			return "";
		}
		
		return "";
		
	}
	

/*
|===============================================================================
|   [ 2 ] METODA VRACA LISTU POVEZANOSTI ZA ODREDJENI CVOR
|===============================================================================
| 
|
*/
	private static String getLink(String id, String ind)
	{
				
		// Ime prvog cvora
		String first_edge = "";
		// String koji oznacava listu povezanih cvorova 
		String link_e = "";
		
		String sql_select = "";
		String sql_from   = "";
		String sql_where  = "";
		
		// Definisani uslovi ako se trazi veza do susjednog cvora koji
		// oznacava operatera, ind = ""(inicijalna vrijednost)
		if(ind == "") {
			sql_select = " id, full_name ";
			sql_from   = " user ";
			sql_where  = " operator_id = ? ";
			first_edge = getEdgeName(id, "").toUpperCase();  
		}
		
		// Definisani uslovi ako se trazi veza do susjednog cvora koji 
		// oznacava korisnika, ind = "user"
		if(ind == "user") {
			sql_select = " DISTINCT user.number, user.full_name ";
			sql_from   = " user, sms ";
			sql_where  = " (user_sender_id = ? AND user.id = sms.user_receiver_id AND user_sender_id != user.id) OR "
					   + "(sms.user_receiver_id = ? AND user.id = sms.user_sender_id AND sms.user_receiver_id != user.id) ";
			
			first_edge = getEdgeName(id, "user").toUpperCase();
			
		}
			
		//link_e += first_edge.toUpperCase() + "  =  ";
				
		try {	
			// MySQLkonekcija	
			Connection con = MysqlConn.conn();
			
			PreparedStatement getID = con.prepareStatement("SELECT" + sql_select + "FROM" + sql_from + "WHERE" + sql_where);
			
			getID.setString(1, id);
			
			if(ind == "user") { getID.setString(2, id); }
			
			ResultSet result_getID = getID.executeQuery();
				         												
			boolean bool= result_getID.isBeforeFirst();
			
			if( bool == false ) {
				link_e += "  X ";
			} 
					
			while(result_getID.next()) { 
			   link_e += " \n " + result_getID.getString("full_name") + "  - > ";
		      		       
			}
			
			if(link_e.endsWith("  - > ")) {
				int last_char = link_e.lastIndexOf("  - > ");
				link_e = link_e.substring(0, last_char);
				
			}
			if(ind == "") {
				edge_links += link_e + "\n\n";
				arrayRowOperateri[br1][0] = first_edge;
				arrayRowOperateri[br1][1] = link_e;
				br1++;
				
			} else if(ind == "user") {
				edge_links += link_e + "\n\n";
				arrayRowKorisnici[br2][0] = first_edge;
				arrayRowKorisnici[br2][1] = link_e;
				br2++;
			}
							
			
			return link_e;
			
		} catch (Exception e) {
			return "";
		}
				
	}
	
	public static void showGraphicOperateri() {
    	// System.out.println( getOperators() );
    	 for(int i=0; i < getOperators().size(); i++) {
    	     String str = getOperators().get(i).trim();
    	     String[] row_arr = str.split("[,|]+"); 
    	     // row_arr[0] - ID Operatera
    	     // row_arr[1] - Naziv Operatera
    	     getLink(row_arr[0], "");
    	 } 
    	  
 		String[][] data1 = arrayRowOperateri;  	
     	tableGraph1 = new JTable(data1, column);
     	tableGraph1.setFillsViewportHeight(true);
     	// Razmak izmedju teksta i lijeve i desne ivice(15px)
     	tableGraph1.setIntercellSpacing(new Dimension(15,0));
     	tableGraph1.setRowHeight(30);
     	tableGraph1.setMinimumSize(new Dimension(970, 700));
     	tableGraph1.setMaximumSize(new Dimension(970, 700));
     	tableGraph1.setPreferredSize(new Dimension(970, 700));
     	tableGraph1.setEnabled(false);
     	tableGraph1.getColumnModel().getColumn(0).setPreferredWidth(170);
     	tableGraph1.getColumnModel().getColumn(0).setMaxWidth(220);
     	
     	if (showSP1 == true ) {
			prozor.getContentPane().remove(scrollPanel1);
			
		}
     	if( showSP2 == true ) {
			prozor.getContentPane().remove(scrollPanel2);
			showSP2 = false;
		} 
     	if( showSP3 == true ) {
			prozor.getContentPane().remove(scrollPanel3);
			showSP3 = false;
		}
     	showSP1 = true;
		scrollPanel1 = new JScrollPane(); 
		scrollPanel1 = new JScrollPane(tableGraph1); 
		scrollPanel1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
 	//	scrollPanel1.setPreferredSize(new Dimension(964, 400));
 	//	scrollPanel1.setMaximumSize(new Dimension(964, 400));
 		scrollPanel1.setBounds(20,130,970,500); 
 		prozor.getContentPane().add(scrollPanel1);  
     	
	}
	
	public static void showGraphicKorisnici() { 
	   	// System.out.println( getUsers() );
	   	for(int i=0; i < getUsers().size(); i++) {
	   	     String str = getUsers().get(i).trim();
	   	     String[] row_arr = str.split("[,|]+"); 
	   	     // row_arr[0] - ID Korisnika
	   	     // row_arr[1] - Ime Korisnika
	   	     getLink(row_arr[0], "user");
	   	     
	   	} 
	   	 
    	String[][] data2 = arrayRowKorisnici;  	
    	tableGraph2 = new JTable(data2, column);
    	// Razmak izmedju teksta i lijeve i desne ivice(15px)
    	tableGraph2.setIntercellSpacing(new Dimension(15,0));
    	tableGraph2.setRowHeight(30);
    	tableGraph2.setMinimumSize(new Dimension(970, 700));
    	tableGraph2.setMaximumSize(new Dimension(970, 700));
    	tableGraph2.setPreferredSize(new Dimension(970, 700));
    	tableGraph2.setEnabled(false);
    	tableGraph2.getColumnModel().getColumn(0).setPreferredWidth(170);
    	tableGraph2.getColumnModel().getColumn(0).setMaxWidth(220);
    //	tableGraph.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    	tableGraph2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	
    	if (showSP1 == true ) {
			prozor.getContentPane().remove(scrollPanel1);
			showSP1 = false;
		}  
    	if( showSP2 == true ) {
			prozor.getContentPane().remove(scrollPanel2);
	
		}
    	if( showSP3 == true ) {
			prozor.getContentPane().remove(scrollPanel3);
			showSP3 = false;
		}
    	showSP2 = true; 			
		scrollPanel2 = new JScrollPane(tableGraph2); 
		scrollPanel2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
 		scrollPanel2.setBounds(20,130,970,500); 
 		prozor.getContentPane().add(scrollPanel2);
 			 		
 		
	} 
	
	
	public static void showGraphicAll() {
		
		for(int i=0; i < getOperators().size(); i++) {
	   	     String str = getOperators().get(i).trim();
	   	     String[] row_arr = str.split("[,|]+"); 
	   	     // row_arr[0] - ID Operatera
	   	     // row_arr[1] - Naziv Operatera
	   	     getLink(row_arr[0], "");
	   	} 
		
		for(int i=0; i < getUsers().size(); i++) {
	   	     String str = getUsers().get(i).trim();
	   	     String[] row_arr = str.split("[,|]+"); 
	   	     // row_arr[0] - ID Korisnika
	   	     // row_arr[1] - Ime Korisnika
	   	     getLink(row_arr[0], "user");
	   	     
	   	} 
				
		String[][] data3 = ArrayUtils.addAll(arrayRowOperateri, arrayRowKorisnici);
		
     	tableGraph3 = new JTable(data3, column);
     	tableGraph3.setFillsViewportHeight(true);
     	// Razmak izmedju teksta i lijeve i desne ivice(15px)
     	tableGraph3.setIntercellSpacing(new Dimension(15,0));
     	tableGraph3.setRowHeight(30);
     	tableGraph3.setMinimumSize(new Dimension(970, 700));
     	tableGraph3.setMaximumSize(new Dimension(970, 700));
     	tableGraph3.setPreferredSize(new Dimension(970, 700));
     	tableGraph3.setEnabled(false);
     	tableGraph3.getColumnModel().getColumn(0).setPreferredWidth(170);
     	tableGraph3.getColumnModel().getColumn(0).setMaxWidth(220);
		
     	if (showSP1 == true ) {
			prozor.getContentPane().remove(scrollPanel1);	
			showSP1 = false;
		}
     	if( showSP2 == true ) {
			prozor.getContentPane().remove(scrollPanel2);	
			showSP2 = false;
		} 
     	if (showSP3 == true ) {
			prozor.getContentPane().remove(scrollPanel1);	
     	
     	}	
     	showSP3 = true;
		scrollPanel3 = new JScrollPane(tableGraph3); 
		scrollPanel3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
 	//	scrollPanel1.setPreferredSize(new Dimension(964, 400));
 	//	scrollPanel1.setMaximumSize(new Dimension(964, 400));
 		scrollPanel3.setBounds(20,130,970,500); 
 		prozor.getContentPane().add(scrollPanel3);
	
	}
	
	public static void createGraphic() { 
		
	 	prozor.setVisible(false);
	 	
	 	 prozor2 = new JFrame ("APLIKACIJA - GRAFOVI");
		   prozor2.getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		   prozor2.setSize(1024, 700);
		   prozor2.setLocation(200, 200);
		   prozor2.setResizable(false);
		   prozor2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   prozor2.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		    	
		   JLabel lblGraphTitle = new JLabel("GRAF");
		   lblGraphTitle.setPreferredSize(new Dimension(1920, 80));
		   lblGraphTitle.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		   lblGraphTitle.setFont(new Font("Segoe Script", Font.PLAIN, 20));
		   lblGraphTitle.setHorizontalAlignment(SwingConstants.CENTER);
		   prozor2.getContentPane().add(lblGraphTitle);
		    	
		   JButton btnFirstEdge = new JButton("DODAJ POÈETNI ÈVOR");
		   btnFirstEdge.addMouseListener(new MouseAdapter() {
			   @Override
		   		public void mouseClicked(MouseEvent e) {
		   			//if(btnAllGraphClickEvent == true) { showGraphicAll(); }
		    			
		   		}
		   });
		       	
		   btnFirstEdge.setPreferredSize(new Dimension(200, 30));
		   btnFirstEdge.setMaximumSize(new Dimension(200, 30));
		   btnFirstEdge.setMinimumSize(new Dimension(200, 30));
		   prozor2.getContentPane().add(btnFirstEdge);
		    	
		   JButton btnCreateLink = new JButton("DODAJ SUSJEDNI ÈVOR");
		   btnCreateLink.addMouseListener(new MouseAdapter() {
		   		@Override
		   		public void mouseClicked(MouseEvent e) {
		   			//if(btnOperateriClickEvent == true) { showGraphicOperateri(); }
		   		}
		   });
		   btnCreateLink.addActionListener(new ActionListener() {
		   		public void actionPerformed(ActionEvent e) {
		   		}
		   });
		   btnCreateLink.setPreferredSize(new Dimension(200, 30));
		   btnCreateLink.setMinimumSize(new Dimension(200, 30));
		   btnCreateLink.setMaximumSize(new Dimension(200, 30));
		   prozor2.getContentPane().add(btnCreateLink);
		    	
		   JButton btnKorisnici = new JButton("KORISNICI");
		   		btnKorisnici.addMouseListener(new MouseAdapter() {
		    	@Override
		    	public void mouseClicked(MouseEvent e) {
		    		
		    		//if(btnKorisniciClickEvent == true) { showGraphicKorisnici(); }
		    	}
		   });
		   btnKorisnici.setPreferredSize(new Dimension(200, 30));
		   btnKorisnici.setMinimumSize(new Dimension(200, 30));
		   btnKorisnici.setMaximumSize(new Dimension(200, 30));
		   prozor2.getContentPane().add(btnKorisnici);
	 	/*
	 	scrollPanel4 = new JScrollPane(); 
		scrollPanel4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	 	scrollPanel4.setBounds(20,130,970,500); 
	 	prozor.getContentPane().add(scrollPanel4);
		*/
	 	prozor2.setVisible(true);
	}
		
	
    public static void main(String[] args)
    { 	
    	    		  	
	   prozor = new JFrame ("APLIKACIJA - GRAFOVI");
	   prozor.getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	   prozor.setSize(1024, 700);
	   prozor.setLocation(200, 200);
	   prozor.setResizable(false);
	   prozor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   prozor.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	    	
	   JLabel lblGraphTitle = new JLabel("LISTA POVEZANIH \u010CVOROVA");
	   lblGraphTitle.setMaximumSize(new Dimension(1920, 80));
	   lblGraphTitle.setMinimumSize(new Dimension(800, 80));
	   lblGraphTitle.setPreferredSize(new Dimension(1920, 80));
	   lblGraphTitle.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	   lblGraphTitle.setFont(new Font("Segoe Script", Font.PLAIN, 20));
	   lblGraphTitle.setHorizontalAlignment(SwingConstants.CENTER);
	   prozor.getContentPane().add(lblGraphTitle);
	    	
	   JButton btnAllGraph = new JButton("PRIKAŽI SVE");
	   btnAllGraph.addMouseListener(new MouseAdapter() {
		   @Override
	   		public void mouseClicked(MouseEvent e) {
	   			if(btnAllGraphClickEvent == true) { showGraphicAll(); }
	    			
	   		}
	   });
	       	
	   btnAllGraph.setPreferredSize(new Dimension(200, 30));
	   btnAllGraph.setMaximumSize(new Dimension(200, 30));
	   btnAllGraph.setMinimumSize(new Dimension(200, 30));
	   prozor.getContentPane().add(btnAllGraph);
	    	
	   JButton btnOperateri = new JButton("OPERATERI");
	   btnOperateri.addMouseListener(new MouseAdapter() {
	   		@Override
	   		public void mouseClicked(MouseEvent e) {
	   			if(btnOperateriClickEvent == true) { showGraphicOperateri(); }
	   		}
	   });
	   btnOperateri.addActionListener(new ActionListener() {
	   		public void actionPerformed(ActionEvent e) {
	   		}
	   });
	   btnOperateri.setPreferredSize(new Dimension(200, 30));
	   btnOperateri.setMinimumSize(new Dimension(200, 30));
	   btnOperateri.setMaximumSize(new Dimension(200, 30));
	   prozor.getContentPane().add(btnOperateri);
	    	
	   JButton btnKorisnici = new JButton("KORISNICI");
	   		btnKorisnici.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		
	    		if(btnKorisniciClickEvent == true) { showGraphicKorisnici(); }
	    	}
	   });
	   btnKorisnici.setPreferredSize(new Dimension(200, 30));
	   btnKorisnici.setMinimumSize(new Dimension(200, 30));
	   btnKorisnici.setMaximumSize(new Dimension(200, 30));
	   prozor.getContentPane().add(btnKorisnici);
	   
	   JButton btnCreateGraph = new JButton("KREIRAJ GRAF");
	   btnCreateGraph.addMouseListener(new MouseAdapter() {
		   @Override
	   		public void mouseClicked(MouseEvent e) {
			  /*
			   btnOperateri.setEnabled(false);
			   btnOperateriClickEvent = false;
			   btnKorisnici.setEnabled(false);
			   btnKorisniciClickEvent = false;
			   btnAllGraph.setEnabled(false);
			   btnAllGraphClickEvent = false;
			   btnCreateGraph.setEnabled(false);
			   btnCreateGraphClickEvent = false;
			   */
			   createGraphic();
	    			
	   		}
	   });
	       	
	   btnCreateGraph.setPreferredSize(new Dimension(200, 30));
	   btnCreateGraph.setMaximumSize(new Dimension(200, 30));
	   btnCreateGraph.setMinimumSize(new Dimension(200, 30));
	   prozor.getContentPane().add(btnCreateGraph);
	    /*	
	   JComboBox comboBox_1 = new JComboBox();
	   comboBox_1.setPreferredSize(new Dimension(200, 30));
	   comboBox_1.setMinimumSize(new Dimension(200, 30));
	   comboBox_1.setMaximumSize(new Dimension(200, 30));
	   prozor.getContentPane().add(comboBox_1);
    	*/  
	   
	   
	   
	   prozor.setVisible(true);
    
	  Models.Method2();  
	  
	  int last_el = Models.Method3().size() - 1;
	  
	 // System.out.println( Models.Method3().get(last_el)  );
    
    }
    
    
    
}