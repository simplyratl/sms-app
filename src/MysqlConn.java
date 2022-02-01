import java.sql.*;


import javax.swing.JOptionPane;

public class MysqlConn {
	
	public static Connection conn() throws ClassNotFoundException {
	    Connection con = null;

	    String url 		= "jdbc:mysql://localhost/sms";
	    String username = "root";
	    String password = "";
	    
	    try {
	    	
	      Class.forName("com.mysql.cj.jdbc.Driver");
	      con = DriverManager.getConnection(url, username, password);
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
