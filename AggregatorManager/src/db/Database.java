package db;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * The Database
 */
public class Database {
	
	private String dbURL = "jdbc:mysql://127.0.0.1:3306/";
	private String dbUser = "root";
	private String dbPass = "root";

	
	/**
	 * Creates the MySQL database by executing the script 
	 * ( does nothing if already exists ).
	 */
	public Database() {
		
		String str = new String(); 
	    StringBuffer strBuff = new StringBuffer();
	    
		Connection con = null;
	    ResultSet rs = null;
	    Statement stmt = null;
	    PreparedStatement prepStmt = null;
	    
        try{ 
        	
        	Class.forName("com.mysql.jdbc.Driver");		
   	 		con = DriverManager.getConnection(dbURL ,dbUser,dbPass);
   	 		
   	 		rs = con.getMetaData().getCatalogs();
   	 		Boolean exists = false;
   	 		
		   	while (rs.next()) {
		   	  String dbName = rs.getString(1);
		   	  if(dbName.equals("mydb")){
		   		  exists = true;
		   	  }
		   	}
		   	
		   	rs.close();   	 		
   	 		
		   	if(!exists){
		   		
		   		System.out.print("Creating database ... ");		   		
		   		
	       	 	stmt = con.createStatement();
	
	       	 	// Read db.sql file  
	       	 	
	       	 	FileReader fileReader = new FileReader(new File("db.sql"));  
	       	 	BufferedReader buffReader = new BufferedReader(fileReader);  
	
	       	 	while((str = buffReader.readLine()) != null){   
	       	 		strBuff.append(str);   
	       	 	}   
	       	 	buffReader.close(); 
	       	 	String[] inst = strBuff.toString().split(";");  
	
	       	 	// Create DataBase mydb
	       	 	
	       	 	for(int i = 0; i<inst.length; i++) {   
	       	 		if( !inst[i].trim().equals("") ){   
	       	 			stmt.executeUpdate(inst[i]); 
	       	 		}   
	       	 	}
	       	 	       	        	 		       	 	
	       	 	// Put admin in Database
	       	 	
	       	 	String password = "root";
	       	 	MessageDigest md = MessageDigest.getInstance("SHA-256");
	       	 	md.update(password.getBytes("UTF-8"));
	       	 	byte[] digest = md.digest();
	       	
	       	 	String insertStmt = "INSERT INTO Users VALUES (1, 'root', ?, false, true)";
	       	 	prepStmt = con.prepareStatement(insertStmt);
	       	 	prepStmt.setBytes(1, digest);
	       	 	prepStmt.executeUpdate();
	       	 	
	       	 	System.out.println("successfully!");	       	 	
		   	}
       }catch(Exception exeption_error){ 
           System.err.println(exeption_error); 
       }finally{
    	   	try {
    	   		if(rs   != null) { 	rs.close(); 		}
    	   		if(stmt != null) {	stmt.close();		}
    	    	if(stmt != null) {	prepStmt.close();	}
    	    	if(con  != null) {	con.close();		}			
			} catch (SQLException e) {
				e.printStackTrace();
			}
       }
	}
	
}
