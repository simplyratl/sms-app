import java.sql.*;


import javax.swing.JOptionPane;

/**
 * @author Pc-200
 *
 */
public class MysqlConn {
	
	public static Connection conn() {
	    Connection con = null;

	    String url 		= "jdbc:mysql://localhost/sms";
	    String username = "root";
	    String password = "";
	    
	    try {
	    	
	     // Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);
//	      System.out.println("Uspješna konekcija!");
	      return con;

	    } catch (SQLException ex) {
//	        throw new Error("Error ", ex);
	    	JOptionPane.showMessageDialog(null, "Baza nije uključena, ili je došlo do greške u konekciji.");
	        return null;
	       
	    } 
	    
	}
	
	public static void main(String[] args) { 
		 
	
	}

}
