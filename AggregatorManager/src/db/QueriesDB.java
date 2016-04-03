package db;


import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import jaxb.*;
import cache.Cache;
import cache.Wrapper;
import cache.WrapperList;
import services.RegisterInfo;


/**
 * Contains the queries needed for the application to communicate with
 *  the database.
 * <p>
 * Contains the methods needed for the initialization of the cache memory.
 * <p>
 * Methods updating the database also update the cache memory.
 */
public class QueriesDB {
	
	private String dbURL = "jdbc:mysql://127.0.0.1:3306/mydb";
	private String dbUser = "root";
	private String dbPass = "root";
	private Connection con = null;
	
	/**
	 * Gets the JDBC connection.
	 *
	 * @return the JDBC connection
	 */
	public Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");		
   	 		con = DriverManager.getConnection(dbURL ,dbUser,dbPass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * Free JDBC resources.
	 *
	 * @param con 	the JDBC connection
	 * @param stmt 	the prepared statement
	 * @param rs 	the result set
	 */
	public void freeResources(Connection con, PreparedStatement stmt, ResultSet rs){
		try {
			if(con  != null)		{	con.close();	}
			if(stmt != null)		{	stmt.close();	}	
			if(rs   != null)		{ 	rs.close();		} 
		}catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Inserts the Software Agent into the database and cache memory
	 * ( does nothing if already exists ) 
	 * <p>
	 * The new agent:
	 *  <ul>
	 *   <li>is considered unconfirmed.</li>
	 *	 <li>is inserted in the {@link Cache#pendingMap} .</li>
	 *	</ul>
	 * 
	 * @param regInfo the registration info of Software Agent
	 */
	public void insertSoftwareAgent(RegisterInfo regInfo){
		Connection con = getConnection();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = null;
		
		try {	
			if( !Cache.acceptedMap.containsKey(regInfo.getHash() + "") 	&& 
				!Cache.pendingMap .containsKey(regInfo.getHash() + "")){
				
				Date today = new Date();
	   	 		Timestamp ts = new Timestamp(today.getTime());
	   	 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   	 		String str = dateFormat.format(ts);
	   	 		
	   	 		String[] agent = new String[9];
	   	 		agent[0] = regInfo.getHostname();
	   	 		agent[1] = regInfo.getIp();
	   	 		agent[2] = regInfo.getMac();
			   	agent[3] = regInfo.getOsVersion();
			   	agent[4] = regInfo.getNmapVersion();
			   	agent[5] = String.valueOf(regInfo.getHash());
			   	agent[6] = "Online";
			   	agent[7] = str;
			   	agent[8] = "dummy";
			   	
		   	 	Cache.pendingMap.put(agent[5],agent);
		   	 
				query = 					
					" INSERT INTO SoftwareAgents " 						+
					" (deviceName, ipAddress, macAddress, osVersion, "	+
					" nmapVersion, hashKey, confirmed, lastRequest) "	+
					" VALUES (?, ?, ?, ?, ?, ? ,?, ?)";	
				
		   	 	stmt = con.prepareStatement(query);
		   	 	
		   	 	stmt.setString(1, agent[0]);
		   	 	stmt.setString(2, agent[1]);
		   	 	stmt.setString(3, agent[2]);
		   	 	stmt.setString(4, agent[3]);
		   	 	stmt.setString(5, agent[4]); 
		   	 	stmt.setInt(6, Integer.parseInt(agent[5]));
		   	 	stmt.setBoolean(7, false); // false indicates pending confirmation
		   	 	stmt.setTimestamp(8,ts);
		   	 	stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, rs);		
		}
	}
	
	/**
	 * Checks if Software Agent indicated by the hashkey is accepted.
	 * ( by checking the {@link Cache#acceptedMap} )
	 * 
	 * @param hashKey the Software Agent's hash key
	 * @return true if is accepted, otherwise false
	 */
	public Boolean checkAccepted(String hashKey){		
		if(Cache.acceptedMap.containsKey(hashKey)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Fills the cache memory with confirmed Software Agents from the database.
	 * <p> The results are stored in {@link Cache#acceptedMap}
	 */
	public void getSoftwareAgents(){
		
		Connection con = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = null;
		
		try {
			
			query = "SELECT * FROM SoftwareAgents WHERE confirmed = ?" ;		
	   	 	stmt = con.prepareStatement(query);
	   	 	stmt.setBoolean(1, true);
	   	 	rs = stmt.executeQuery();

	   	 	while(rs.next()){	   	 		
	   	 		
	   	 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   	 		Timestamp ts = rs.getTimestamp("lastRequest");
	   	 		String str = dateFormat.format(ts);
	   	 		
	   	 		String[] agent = new String[9];
	   	 		agent[0] = rs.getString("deviceName");
	   	 		agent[1] = rs.getString("ipAddress");
	   	 		agent[2] = rs.getString("macAddress");
			   	agent[3] = rs.getString("osVersion");
			   	agent[4] = rs.getString("nmapVersion");
			   	agent[5] = rs.getString("hashKey");
			   	agent[6] = "Offline";
			   	agent[7] = str;
			   	agent[8] = rs.getString("idSoftwareAgents");
 			   	
			   	Cache.acceptedMap.put(agent[5],agent);
	   	 	}	   	 	   	 	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, rs);
		}		
	}
	
	
	/**
	 * Fills the cache memory with the sent jobs from the database.
	 * <p> Periodic jobs are stored in {@link Cache#periodicMap}
	 * <p> One time jobs are stored in {@link Cache#onetimeMap}
	 */
	public void getSentJobs(){
		
		Connection con = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = null;
		
		try {
			query = "SELECT * FROM SoftwareAgents s, NmapJobs n "	
						 + "WHERE n.idSoftwareAgents = s.idSoftwareAgents "
						 + "AND sent = ?";								
			
	   	 	stmt = con.prepareStatement(query);
	   	 	stmt.setBoolean(1, true);   	 	
	   	 	rs = stmt.executeQuery();
	   	 	
	   	 	while(rs.next()){	
	   	 		
	   	 		String[] job = new String[4];
	   	 		String stopped = Boolean.toString(rs.getBoolean("stopped"));
	   	 		
	   	 		job[0] = rs.getString("idNmapJobs");
	   	 		job[1] = rs.getString("flags");
	   	 		job[2] = Boolean.toString(rs.getBoolean("periodic"));
	   	 		job[3] = rs.getString("periodicTime");		   	 		
	   	 		
	   	 		String id = rs.getString("hashKey");
	   	 		Wrapper<String, String[]> jobmap;

	   	 		if(job[2] == "true"){	   	 		
		   	 		if(!Cache.periodicMap.containsKey(id)){
		   	 			jobmap = new Wrapper<>();
		   	 			Cache.periodicMap.put(id,jobmap);
		   	 		}else{
		   	 			jobmap = Cache.periodicMap.get(id);
		   	 		}
		   	 		if(stopped == "false"){
			   	 		if(!Cache.activeMap.containsKey(id)){
			   	 			Cache.activeMap.put(id,new Wrapper<String,String[]>());
			   	 		}
			   	 		Cache.activeMap.get(id).put(job[0], job); 
		   	 		}		   	 		
	   	 		}else{
		   	 		if(!Cache.onetimeMap.containsKey(id)){
		   	 			jobmap = new Wrapper<>();
		   	 			Cache.onetimeMap.put(id,jobmap);
		   	 		}else{
		   	 			jobmap = Cache.onetimeMap.get(id);
		   	 		}
	   	 		}
	   	 		jobmap.put(job[0],job);
	   	 	}	   	 	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, rs);
		}		
	}
	
	/**
	 * Inserts a map of jobs into the database and cache memory.
	 * <p> Jobs are also stored in {@link Cache#addMap}
	 * 
	 * @param hashKey the Software Agent's hash key
	 * @param jobs a map representing the jobs
	 */
	@SuppressWarnings("resource")
	public void insertJobs(String hashKey, Wrapper<String, String[]> jobs){
		
		Connection con = getConnection();		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = null;
		
		try{			
	 		if(Cache.acceptedMap.containsKey(hashKey)){
 			
	 			String id = (Cache.acceptedMap.get(hashKey))[8];
	 			int jobid;
	 			
	 			query = "SELECT MAX(idNmapJobs) as last_id FROM NmapJobs";
	 			stmt = con.prepareStatement(query);	
	 			rs = stmt.executeQuery();
	 			
	 			if(rs.next()){ 
	 				jobid = rs.getInt("last_id");			
	 			}else{
	 				jobid = 1;
	 			}
	 			
	 			query = "INSERT INTO NmapJobs " 			
	 					+ "(idSoftwareAgents, idNmapJobs, flags, periodic,"
	 					+ " periodicTime, sent, stopped) "
	 					+ "VALUES (?, ?, ?, ?, ?, ?,?)";
	 			
	 	 		stmt = con.prepareStatement(query);	
	 	 		
	 	 		for(Entry<String, String[]> entry : jobs.entrySet()){	
					String[] job = entry.getValue();
					
					stmt.setString(1,id);
					stmt.setInt(2,++jobid);					
					stmt.setString(3,job[1]);
					stmt.setBoolean(4,Boolean.parseBoolean(job[2]));
					stmt.setInt(5,Integer.parseInt(job[3]));
					stmt.setBoolean(6,false);
					stmt.setBoolean(7,false);
					stmt.addBatch();
					
					job[0] = Integer.toString(jobid);
					
					if(Cache.addMap.containsKey(hashKey)){
						Cache.addMap.get(hashKey).put(job[0],job);
					}					
				}	
				
				if(!Cache.addMap.containsKey(hashKey)){
					Cache.addMap.put(hashKey, jobs);
				}	
				
				stmt.executeBatch();
				stmt.close();				
	 		}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, rs);
		}
	}
	
	/**
	 * Fills the cache memory with the newly added jobs from the database.
	 * <p> Jobs are stored in {@link Cache#addMap}
	 */
	public void getAddJobs(){
		
		Connection con = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = null;
		
		try {
			query = "SELECT *  FROM SoftwareAgents s, NmapJobs n "	
						 + "WHERE n.idSoftwareAgents = s.idSoftwareAgents " 	
						 + "AND sent = ?";
			
	   	 	stmt = con.prepareStatement(query);
	   		stmt.setBoolean(1, false);
	   		rs = stmt.executeQuery();
		   	 	 	 
	   	 	while(rs.next()){	
		   	 	String hashKey = rs.getString("hashKey");
		   	 	Wrapper<String, String[]> jobmap;
	   	 		
	   	 		if(!Cache.addMap.containsKey(hashKey)){
	   	 			jobmap = new Wrapper<String, String[]> ();
	   	 			Cache.addMap.put(hashKey,jobmap);
	   	 		}else{
	   	 			jobmap = Cache.addMap.get(hashKey);
	   	 		}
	   	 		
	   	 		String[] job = new String[4];
	   	 		job[0] = rs.getString("idNmapJobs");
	   	 		job[1] = rs.getString("flags");
	   	 		job[2] = Boolean.toString(rs.getBoolean("periodic"));
	   	 		job[3] = rs.getString("periodicTime");
	   	 		jobmap.put(job[0], job);
	   	 	} 
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, rs);
		}
	}
	
	/**
	 * Updates the jobs' sent status to "true into database and cache."
	 * <p>
	 * The updated jobs:
	 *  <ul>
	 *   <li>are removed from {@link Cache#addMap}.</li>
	 *	 <li>are inserted in the {@link Cache#periodicMap}
	 *			and {@link Cache#onetimeMap} respectively.</li>
	 *	</ul>
	 * @param hashKey the Software Agent's hash key
	 */
	public void updateJobs(String hashKey){
		
		Connection con = getConnection();
		PreparedStatement stmt = null;
		String query = null;
		
		try{	 		
			if(Cache.acceptedMap.containsKey(hashKey)){
	 			
				String id = Cache.acceptedMap.get(hashKey)[8];
	 			
	 			query = "UPDATE NmapJobs " 			+
	 						" SET sent = ? WHERE idSoftwareAgents = ? " +
	 						" AND idNmapJobs = ?";

	 	 		stmt = con.prepareStatement(query);		 	 		
	 	 		
	 	 		Wrapper <String,String[]> jobs = Cache.addMap.get(hashKey);	 	 		
	 	 		Wrapper <String,String[]> pmap ;
	 	 		Wrapper <String,String[]> omap ;
	 	 		Wrapper <String,String[]> actmap ;
	 	 		
	 	 		if(!Cache.periodicMap.containsKey(hashKey)){
	 	 			pmap = new Wrapper <String,String[]>();
	   	 			Cache.periodicMap.put(hashKey,pmap);
	   	 		}else{
	   	 			pmap = Cache.periodicMap.get(hashKey);
	   	 		}
	 	 		
	 	 		if(!Cache.onetimeMap.containsKey(hashKey)){
	 	 			omap = new Wrapper <String,String[]>();
	   	 			Cache.onetimeMap.put(hashKey,omap);
	   	 		}else{
	   	 			omap = Cache.onetimeMap.get(hashKey);
	   	 		}
	 	 		
	 	 		if(!Cache.activeMap.containsKey(hashKey)){
	 	 			actmap = new Wrapper <String,String[]>();
	   	 			Cache.activeMap.put(hashKey,actmap);
				}else{
					actmap = Cache.activeMap.get(hashKey);
				}
	 	 		
	 	 		boolean exit = false;
	 	 		
				for(Entry<String, String[]> entry : jobs.entrySet()){						
					String[] job = entry.getValue();
					
					if(job[1].equals("Stop")){
						continue;
					}
					
					if(job[1].equals("exit(0)")){
						exit = true; 
						continue;
					}
					
					stmt.setBoolean(1,true);
					stmt.setString(2,id);
					stmt.setInt(3,Integer.parseInt(job[0]));					
					stmt.addBatch();
					
					if(job[2].equals("true")){	
						pmap.put(job[0], job);
						actmap.put(job[0], job);
					}else{
						omap.put(job[0], job);
					}					
				}	
				
				stmt.executeBatch();
				stmt.close();
				Cache.addMap.remove(hashKey);
				
				if(exit){ 				// agent has no active jobs after exit	
					query = "UPDATE NmapJobs SET stopped = ? WHERE idNmapJobs = ? ";
					stmt = con.prepareStatement(query);
					for(Entry<String, String[]> entry : actmap.entrySet()){						
						String[] job = entry.getValue();						
						stmt.setBoolean(1,true);
						stmt.setInt(2,Integer.parseInt(job[0]));
						stmt.addBatch();
					}
					stmt.executeBatch();
					Cache.activeMap.remove(hashKey); 
				}
	 		}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, null);
		}
	}
	
	
	/**
	 * Deletes a job based on its ID from database and cache memory.
	 * <p> The job's flags field is updated to "Stop".
	 * <p> The job is deleted from {@link Cache#periodicMap}.
	 * <p> The job is added to {@link Cache#addMap}.
	 * 
	 * @param hashKey the Software Agent's hash key
	 * @param idJob the job id
	 */
	public void deleteJob(String hashKey, String idJob){
		
		Connection con = getConnection();
		PreparedStatement stmt = null;
		String query = null;
		
		try {							
			query = "UPDATE NmapJobs SET stopped = ? WHERE idNmapJobs = ? ";
			stmt = con.prepareStatement(query);
			stmt.setBoolean(1,true);
			stmt.setInt(2,Integer.parseInt(idJob));
 	 		stmt.executeUpdate();
		
 	 		Wrapper<String,String[]> addmap = Cache.addMap.get(hashKey);
			
			String[] job = Cache.periodicMap.get(hashKey).get(idJob);
			
			if(Cache.activeMap.containsKey(hashKey)){
				if(Cache.activeMap.get(hashKey).containsKey(job[0])){
					Cache.activeMap.get(hashKey).remove(job[0]);	
				}
			}					
			
			if(addmap == null){
				addmap = new Wrapper<String,String[]>();
				Cache.addMap.put(hashKey,addmap);
			}			
			
			if(addmap.containsKey(idJob)){
				job = addmap.get(idJob);
				job[1] = "Stop";
			}else{
				job = new String[4];
				job[0] = idJob;
				job[1] = "Stop";
				job[2] = "true";
				job[3] = "0";
				addmap.put(job[0],job);
			}		
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, null);
		}
	}
	
	/**
	 * Terminates a Software Agent by assigning an exiting job.
	 * <p> If the job does not exit, creates a nmap job 
	 * 	    with id = -1, flags = "exit(0), periodic = true, periodicTime = -1".
	 * <p> If the job exists, the flags field is updated to "exit(0)".
	 * <p> The job is deleted from {@link Cache#periodicMap}.
	 * <p> The job is added to {@link Cache#addMap}.
	 *
	 * @param hashKey the Software Agent's hash key
	 */	
	public void terminateSoftwareAgent(String hashKey){		
		Wrapper<String,String[]> addmap = Cache.addMap.get(hashKey);

		if(addmap == null){
			addmap = new Wrapper<String,String[]>();
			Cache.addMap.put(hashKey,addmap);
		}		
		if(!addmap.containsKey("-1")){
			String[] job = new String[4];
			job[0] = "-1";
			job[1] = "exit(0)";
			job[2] = "true";
			job[3] = "0";
			addmap.put(job[0],job);
		}
	}
	
	/**
	 * Updates the last request time of a Software Agent with the current
	 * timestamp into the database and cache memory.
	 * <p> Agent in {@link Cache#acceptedMap} is also updated.
	 *
	 * @param hashKey the Software Agent's hash key
	 */
	public void updateLastRequest(String hashKey){

		Connection con = getConnection();
		PreparedStatement stmt = null;
		String query = null;
		
		try{	 		
			if(Cache.acceptedMap.containsKey(hashKey)){
	 			
				String id = Cache.acceptedMap.get(hashKey)[8];
	 			
	 			Date today = new Date();
				Timestamp ts = new Timestamp(today.getTime());
	 			
	 			query = "UPDATE SoftwareAgents SET lastRequest = ? " + 
	 					"WHERE idSoftwareAgents = ? ";

	 	 		stmt = con.prepareStatement(query);	
	 	 		stmt.setTimestamp(1,ts);
	 	 		stmt.setString(2,id);	 	 		
	 	 		stmt.executeUpdate();
	 	 		
	 	 		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 			String[] agent = Cache.acceptedMap.get(hashKey);
	 			agent[7] = dateFormat.format(ts);
	 		}
	 	}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, null);
		}
	}
	
	/**
	 * Validates the login info of a user.
	 *
	 * @param username the username
	 * @param password the password
	 * @return true if info is valid, otherwise false
	 */
	@SuppressWarnings("resource")
	public boolean connectUser(String username, String password){
		Connection con = this.getConnection();

		boolean connected = false;
		MessageDigest md;
		String query = null;
		PreparedStatement stmt = null;
		ResultSet data = null;
		
		try {
			md = MessageDigest.getInstance("SHA-256");
	 		md.update(password.getBytes("UTF-8"));
	 		byte[] digest = md.digest();

	 		query = "SELECT COUNT(*) FROM Users u "				+ 
	 				"WHERE u.username = ? AND u.password = ? AND u.admin = ?"; 
		
   	 		stmt = con.prepareStatement(query);
   	 		stmt.setString(1, username);
   	 		stmt.setBytes(2, digest);
   	 		stmt.setBoolean(3, true);
   	 	
   	 		data = stmt.executeQuery();
   	 		if(data.next()){
				if(data.getInt(1) == 1){
					query = "UPDATE Users SET active = ? "		+ 
							"WHERE username = ? AND password = ?";  
					
			   	 	stmt = con.prepareStatement(query);
			   	 	stmt.setBoolean(1, true);
			   	 	stmt.setString(2, username);
			   	 	stmt.setBytes(3, digest);
			   	 	
			   	 	stmt.executeUpdate();
			   	 	connected = true;
				}
   	 		}
		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{	
			freeResources(con, stmt, data);
		}
		return connected;
	}
	
	/**
	 * Fills the cache memory with the unconfirmed Software Agents.
	 * <p> The results are stored in {@link Cache#pendingMap}.
	 */
	public void getUnconfirmedSA(){
		
		Connection con = getConnection();
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			query = "SELECT * FROM SoftwareAgents WHERE confirmed = ?" ;		
	   	 	stmt = con.prepareStatement(query);
	   	 	stmt.setBoolean(1, false);
	   	 	rs = stmt.executeQuery();

	   	 	while(rs.next()){	   	 		
	   	 		
	   	 		String[] agent = new String[9];
	   	 		agent[0] = rs.getString("deviceName");
	   	 		agent[1] = rs.getString("ipAddress");
	   	 		agent[2] = rs.getString("macAddress");
			   	agent[3] = rs.getString("osVersion");
			   	agent[4] = rs.getString("nmapVersion");
			   	agent[5] = rs.getString("hashKey");
			   	agent[6] = "Online";
			   	agent[7] = rs.getString("lastRequest");
			   	agent[8] = rs.getString("idSoftwareAgents");
			   	
			   	Cache.pendingMap.put(agent[5],agent);
	   	 	}	   	 	   	 	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, rs);
		}		
	}
	
	/**
	 * Accept or decline a Software Agent.
	 *<p>
	 * The agent:
	 *  <ul>
	 *   	<li>gets deleted from {@link Cache#pendingMap}.</li>
	 *	 	<li>if accepted, becomes confirmed and stored in 
	 *											{@link Cache#acceptedMap}.</li>
	 *		<li>if declined, gets deleted from database.</li>
	 *	</ul>
	 * 
	 * @param accept flag indicating if accepted or not
	 * @param hashKey the Software Agent's hash key
	 */
	@SuppressWarnings("resource")
	public void acceptORdeclineSoftwareAgent(boolean accept, int hashKey){
		
		Connection con = this.getConnection();
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			if(accept == true){
				
				query = "SELECT * FROM SoftwareAgents WHERE hashKey = ?"; 
				stmt = con.prepareStatement(query);		   	 	
		   	 	stmt.setInt(1, hashKey);
		   	 	
		   	 	rs = stmt.executeQuery();
		   	 	rs.next();
		   	 	
		   	 	String id = rs.getString("idSoftwareAgents");
		   	 	Cache.pendingMap.get(String.valueOf(hashKey))[8] = id;
		   	 	
				query = "UPDATE SoftwareAgents SET confirmed = true "		+ 
							"WHERE hashKey = ?"; 
				Cache.acceptedMap.put(String.valueOf(hashKey), 
						Cache.pendingMap.get(String.valueOf(hashKey)));
				Cache.pendingMap.remove(String.valueOf(hashKey));
			}else{
				query = "DELETE FROM SoftwareAgents WHERE hashKey = ?";
				Cache.pendingMap.remove(String.valueOf(hashKey));
			}
	   	 	stmt = con.prepareStatement(query);
	   	 	stmt.setInt(1, hashKey);
	   	 	
	   	 	stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, rs);
		}
	}

	/**
	 * Fills the cache memory with the nmap results.
	 * <p> The results are stored in {@link Cache#resultMap}.
	 */
	public void getNmapResults(){

		Connection con = this.getConnection();
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			WrapperList jobsList;
	   	 	
		   	query = "SELECT s.hashKey, jr.idNmapJobs, jr.idJobResults, jr.time "
		   			+ "FROM JobResults jr, NmapJobs n, SoftwareAgents s "
		   			+ "WHERE n.idSoftwareAgents = s.idSoftwareAgents "
		   			+ "AND n.idNmapJobs = jr.idNmapJobs";		
		   	
	   		stmt = con.prepareStatement(query);
		 	rs = stmt.executeQuery();

		 	while(rs.next()){
		 		if(Cache.acceptedMap.containsKey(String.valueOf(rs.getInt("hashKey")))){
		   	 		String[] results = new String[3];
			 		results[0] = String.valueOf(rs.getInt("idNmapJobs"));
			 		results[1] = String.valueOf(rs.getInt("idJobResults"));
			 		results[2] = String.valueOf(rs.getTimestamp("time"));
			 		
					if(!Cache.resultMap.containsKey(String.valueOf(rs.getInt("hashKey")))){
			 			jobsList = new WrapperList();
			 			Cache.resultMap.put(String.valueOf(
			 					String.valueOf(rs.getInt("hashKey"))), jobsList);
			 		}else{
			 			jobsList = Cache.resultMap.get(String.valueOf(rs.getInt("hashKey")));
			 		}
			 		jobsList.add(results);
			 		Collections.sort(jobsList.getList(), new Comparator<String[]>(){
			 		     public int compare(String[] res1, String[] res2){
			 		         return res1[2].compareTo(res2[2]);
			 		     }
			 		});
			 		Collections.reverse(jobsList.getList());
		 		}
		 	}
	   	 	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, rs);
		}
	}
	
	/**
	 * Fills the cache memory with the xml results.
	 * <p> The results are stored in {@link Cache#xmlMap}.
	 */
	public void getXmlResults(){

		Connection con = this.getConnection();
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {	   	 	
		   	query = "SELECT idJobResults, result FROM JobResults jr";		   	

	   		stmt = con.prepareStatement(query);
		 	rs = stmt.executeQuery();

		 	while(rs.next()){
		 		Cache.xmlMap.put(String.valueOf(rs.getInt("idJobResults")), 
		 				unmarshallXml(rs.getString("result")));
		 	}
	   	 	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, rs);
		}
	}
	
	/**
	 * Gets the all the nmap results filtered by the received time range.
	 *
	 * @param timeFrom 	the lower bound of time range
	 * @param timeTo 	the upper bound of time range
	 * @return all the nmap results
	 */
	public String[][] getAllNmapResults(String timeFrom, String timeTo){
		
		Connection con = this.getConnection();
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		String[][] results = null;
		
		try {
			int rows;
	   	 	
		   	query = "SELECT n.idSoftwareAgents,	jr.idJobResults, "
		   			+ "n.idNmapJobs, jr.time "
		   			+ "FROM JobResults jr, NmapJobs n	"
		   			+ "WHERE n.idNmapJobs=jr.idNmapJobs";
		
		 	if(timeFrom != null && timeTo != null){
		   		query += " AND jr.time > ? AND jr.time < ?";
		   		stmt = con.prepareStatement(query);
			 	stmt.setTimestamp(1, Timestamp.valueOf(timeFrom));
			 	stmt.setTimestamp(2, Timestamp.valueOf(timeTo));
		   	}else if(timeFrom != null){
		   		query += " AND jr.time > ?";
		   		stmt = con.prepareStatement(query);
			 	stmt.setTimestamp(1, Timestamp.valueOf(timeFrom));
		   	}else if(timeTo != null){
		   		query += " AND jr.time < ?";
		   		stmt = con.prepareStatement(query);
			 	stmt.setTimestamp(1, Timestamp.valueOf(timeTo));
		   	}else{
		   		stmt = con.prepareStatement(query);
		   	}
		 	
		 	rs = stmt.executeQuery();
		 	
	   	 	if(rs.last()){
		   	 	rows = rs.getRow();
		   	 	rs.first();
		   	 	
		   	 	results = new String[rows][4];
		   	 	rows = 0;
			 	
		   	 	do{	
		   	 		results[rows][0] = rs.getString("idSoftwareAgents");
		   	 		results[rows][1] = rs.getString("idNmapJobs");
		   	 		results[rows][2] = rs.getString("idJobResults");
			   	 	results[rows][3] = rs.getTimestamp("time").toString();
			   	 	
			   	 	rows++;
		   	 	}while(rs.next());
	   	 	}	   	 	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, rs);
		}

		return results;	
	}
	
	/**
	 * Inserts the nmap result into the database and cache memory.
	 *
	 * @param idNmapJob the nmap job id
	 * @param xml the result in XML
	 * @param time the time which the result was received
	 */
	@SuppressWarnings("resource")
	public void insertNmapResult(int idNmapJob, String xml, Timestamp time){
		Connection con = this.getConnection();

		String query = null;
		PreparedStatement stmt = null;
		ResultSet data = null;
		int hashKey;
		WrapperList jobsList;
		
		try {
	 		query = "SELECT s.hashKey FROM SoftwareAgents s, NmapJobs n "
	 				+ "WHERE n.idNmapJobs = ? "
	 				+ "AND n.idSoftwareAgents = s.idSoftwareAgents"; 
		
   	 		stmt = con.prepareStatement(query);
   	 		stmt.setInt(1, idNmapJob);
   	 	
   	 		data = stmt.executeQuery();
   	 		data.next();
   	 		hashKey = data.getInt(1);
	 		
 	 		query = "INSERT INTO JobResults (time, result, idNmapJobs)"		+
 					"VALUES (?, ?, ?)"; 
	 	 		
 	 		stmt = con.prepareStatement(query);
 	 		
 	 		stmt.setTimestamp(1, time);
 	 		stmt.setString(2, xml);
 	 		stmt.setInt(3, idNmapJob);
	 	 		
	 	 	stmt.executeUpdate();
	 	 	
	 		query = "SELECT idJobResults FROM JobResults "
	 				+ "WHERE idNmapJobs = ? AND result = ? AND time = ?"; 
		
   	 		stmt = con.prepareStatement(query);
   	 		stmt.setInt(1, idNmapJob);
 	 		stmt.setString(2, xml);
 	 		stmt.setTimestamp(3, time);
 	 		 	 	
   	 		data = stmt.executeQuery();
   	 		data.next();
   	 		
   	 		String[] results = new String[3];
	 		results[0] = String.valueOf(idNmapJob);
	 		results[1] = String.valueOf(data.getInt(1));
	 		results[2] = String.valueOf(time);
	 		
	 		if(!Cache.resultMap.containsKey(String.valueOf(hashKey))){
	 			jobsList = new WrapperList();
	 			Cache.resultMap.put(String.valueOf(String.valueOf(hashKey)), jobsList);
	 		}else{
	 			jobsList = Cache.resultMap.get(String.valueOf(hashKey));
	 		}
	 		jobsList.add(0,results);
	 		Cache.xmlMap.put(String.valueOf(results[1]), 
	 				unmarshallXml(xml));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, data);
		}
	}	
	
	
	/**
	 * Adds the to list.
	 *
	 * @param columnName the column name
	 * @param data the data
	 * @param results the results
	 * @return the list
	 */
	public WrapperList addToList(String columnName, String data, WrapperList results){
		String[] row = new String[2];
		row[0] = columnName;
		row[1] = data;
		results.add(row);

		return results;
	}
	
	/**
	 * Unmarshall xml result.
	 *
	 * @param xml the XML result for unmarshalling.
	 * @return the results list
	 */
	public WrapperList unmarshallXml(String xml){
		WrapperList results = new WrapperList();
		int i1, i2, i3, i4;
		
 		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Nmaprun.class);
	 		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	 		StringReader reader = new StringReader(xml);
	 		Nmaprun nmap = (Nmaprun) jaxbUnmarshaller.unmarshal(reader);
	
	 		addToList("Verbose Level", ": "+nmap.getVerbose().getLevel(), results);
	 		addToList("Debugging Level", ": "+nmap.getDebugging().getLevel(), results);
	 		addToList("Up Hosts", ": "+nmap.getRunstats().getHosts().getUp(), results);
	 		addToList("Down Hosts", ": "+nmap.getRunstats().getHosts().getDown(), results);
	
	 		List<Scaninfo> scanInfoList = nmap.getScaninfo();
	 		for(i1=0; i1<scanInfoList.size(); i1++){
	 			Scaninfo scanInfo = scanInfoList.get(i1);
	 			
	 			addToList("Num Services ("+i1+")", ": "+scanInfo.getNumservices(), results);
	 		}
	 		
	 		List<Object> objectListOut = nmap.getTargetOrTaskbeginOrTaskprogressOrTaskendOrPrescriptOrPostscriptOrHostOrOutput();
	 		for(i1=0; i1<objectListOut.size(); i1++){
	
	 			Object objOut = objectListOut.get(i1);
	 			if(objOut instanceof Host){
	 				Host host = (Host) objOut;
	 				List<Object> objectListIn = host.getStatusOrAddressOrHostnamesOrSmurfOrPortsOrOsOrDistanceOrUptimeOrTcpsequenceOrIpidsequenceOrTcptssequenceOrHostscriptOrTraceOrTimes();
	    	 		for(i2=0; i2<objectListIn.size(); i2++){
	
	    	 			Object objIn = objectListIn.get(i2);
	    	 			if(objIn instanceof Status){
	    	 				Status status = (Status) objIn;
	
	    	 				addToList("State ("+i2+")", ": "+status.getState(), results);
	    	 				addToList("Reason ("+i2+")", ": "+status.getReason(), results);
	    				}else if(objIn instanceof Address){
	    	 				Address addr = (Address) objIn;
	
	    	 				addToList("Address ("+i2+")", ": "+addr.getAddr(), results);
	    	 				addToList("Address Type ("+i2+")", ": "+addr.getAddrtype(), results);
	    	 			}else if(objIn instanceof Hostnames){
	    	 				Hostnames hostnames = (Hostnames) objIn;
	
	    		   	 		List<Hostname> hostnamesList = hostnames.getHostname();
	    	    	 		for(i3=0; i3<hostnamesList.size(); i3++){
	    	    	 			Hostname hostname = hostnamesList.get(i3);
	
	    	    	 			addToList("Hostname ("+i3+")", ": "+hostname.getName(), results);
	    	    	 			addToList("Hostname Type ("+i3+")", ": "+hostname.getType(), results);
	    	    	 		}
	    	 			}else if(objIn instanceof Ports){
	    	 				Ports ports = (Ports) objIn;
	    	 				
	    	 				List<Extraports> extraPortsList = ports.getExtraports();
	    	 				for(i3=0; i3<extraPortsList.size(); i3++){
		    	 			Extraports extraports = extraPortsList.get(i3);
	
		    	 			addToList("ExtraPortsNum ("+i3+")", ": "+extraports.getCount(), results);
	    	 				}
	    	 				
	    	 				List<Port> portsList = ports.getPort();
		 				for(i3=0; i3<portsList.size(); i3++){
		 					Port port = portsList.get(i3);
	
		 					addToList("PortId ("+i3+")", ": "+port.getPortid(), results);
		 					addToList("Port Protocol ("+i3+")", ": "+port.getProtocol(), results);
		 					
	    		   	 		Service service = port.getService();
	
	    		   	 		addToList("Conf ("+i3+")", ": "+service.getConf(), results);
	    		   	 		addToList("Device Type ("+i3+")", ": "+service.getDevicetype(), results);
	    		   	 		addToList("ExtraInfo ("+i3+")", ": "+service.getExtrainfo(), results);
	    		   	 		addToList("Highver ("+i3+")", ": "+service.getHighver(), results);
	    		   	 		addToList("Hostname ("+i3+")", ": "+service.getHostname(), results);
	    		   	 		addToList("Lowver ("+i3+")", ": "+service.getLowver(), results);
	    		   	 		addToList("Method ("+i3+")", ": "+service.getMethod(), results);
	    		   	 		addToList("Name ("+i3+")", ": "+service.getName(), results);
	    		   	 		addToList("Os Type ("+i3+")", ": "+service.getOstype(), results);
	    		   	 		addToList("Product ("+i3+")", ": "+service.getProduct(), results);
	    		   	 		addToList("Proto ("+i3+")", ": "+service.getProto(), results);
	    		   	 		addToList("Rpc num ("+i3+")", ": "+service.getRpcnum(), results);
	    		   	 		addToList("Servicefp ("+i3+")", ": "+service.getServicefp(), results);
	    		   	 		addToList("Tunnel ("+i3+")", ": "+service.getTunnel(), results);
	    		   	 		addToList("Version ("+i3+")", ": "+service.getVersion(), results);
		 				}
	    	 			}else if(objIn instanceof Os){
	    	 				Os os = (Os) objIn;
	    	 				
	    	 				List<Osmatch> osmatchList = os.getOsmatch();
	    	 				for(i3=0; i3<osmatchList.size(); i3++){
		    	 			Osmatch osmatch = osmatchList.get(i3);
	
		    	 			addToList("Accuracy ("+i3+")", ": "+osmatch.getAccuracy(), results);
	   	    		   			addToList("Line ("+i3+")", ": "+osmatch.getLine(), results);
	   	    		   			addToList("Name ("+i3+")", ": "+osmatch.getName(), results);
	   	    		   	 		
	   	    		   	 		List<Osclass> osclassList = osmatch.getOsclass();
	   	    		   	 		for(i4=0; i4<osclassList.size(); i4++){
	   	    		   	 			Osclass osclass = osclassList.get(i4);
	
	   	    		   	 			addToList("Accuracy ("+i4+")", ": "+osclass.getAccuracy(), results);
	   	    		   	 			addToList("Osfamily ("+i4+")", ": "+osclass.getOsfamily(), results);
	   	    		   	 			addToList("Osgen ("+i4+")", ": "+osclass.getOsgen(), results);
	   	    		   	 			addToList("Type ("+i4+")", ": "+osclass.getType(), results);
	   	    		   	 			addToList("Vendor ("+i4+")", ": "+osclass.getVendor(), results);
	   	    		   	 		}
	    	 				}
	    	 			}else if(objIn instanceof Uptime){
	    	 				Uptime uptime = (Uptime) objIn;
	
	    	 				addToList("Lastboot", ": "+uptime.getLastboot(), results);
	    	 				addToList("Seconds", ": "+uptime.getSeconds(), results);
	    	 			}else if(objIn instanceof Tcpsequence){
	    	 				Tcpsequence tcpsequence = (Tcpsequence) objIn;
	
	    	 				addToList("Difficulty", ": "+tcpsequence.getDifficulty(), results);
	    	 				addToList("Index", ": "+tcpsequence.getIndex(), results);
	    	 				addToList("Values", ": "+tcpsequence.getValues(), results);
	    	 			}else if(objIn instanceof Ipidsequence){
	    	 				Ipidsequence ipidsequence = (Ipidsequence) objIn;
	    	 				
	    		   	 		addToList("Clazz", ": "+ipidsequence.getClazz(), results);
	    		   	 		addToList("Values", ": "+ipidsequence.getValues(), results);
	    	 			}else if(objIn instanceof Tcptssequence){
	    	 				Tcptssequence tcptssequence = (Tcptssequence) objIn;
	    	 				
	    		   	 		addToList("Clazz", ": "+tcptssequence.getClazz(), results);
	    		   	 		addToList("Values", ": "+tcptssequence.getValues(), results);
	    	 			}
	    	 		}
	 			}
	 		}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
 		return results;
	}

	/**
	 * Accept or decline an Android user account
	 *<p>
	 * The account:
	 *  <ul>
	 *   	<li>gets deleted from {@link Cache#pendingAndroid}.</li>
	 *	 	<li>if accepted, becomes active and stored in 
	 *											{@link Cache#acceptedAndroid}.</li>
	 *		<li>if declined, gets deleted from database.</li>
	 *	</ul>
	 * 
	 * @param accept flag indicating if accepted or not
	 * @param username the username of the user account
	 */
	public void acceptORdeclineAndroid(boolean accept, String username){
		
		Connection con = this.getConnection();
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			if(accept == true){  	 	
				query = "UPDATE Users SET active = true WHERE username = ?"; 
				Cache.acceptedAndroid.put(String.valueOf(username), 
						Cache.pendingAndroid.get(String.valueOf(username)));
			}else{
				query = "DELETE FROM Users WHERE username = ?";
			}
			Cache.pendingAndroid.remove(String.valueOf(username));
			
	   	 	stmt = con.prepareStatement(query);
	   	 	stmt.setString(1, username);
	   	 	
	   	 	stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			freeResources(con, stmt, rs);
		}
	}
	
	/**
	 * Fills the cache memory with the Android user accounts.
	 * <p> The user accounts are stored in {@link Cache#pendingAndroid} or 
	 * in {@link Cache#acceptedAndroid} depending on the condition of the account 
	 * (active or not).
	 */
	public void getAndroid(){
		
		Connection con = getConnection();
		String query = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			query = "SELECT * FROM Users WHERE admin = ?" ;		
	   	 	stmt = con.prepareStatement(query);
	   	 	stmt.setBoolean(1, false);
	   	 	rs = stmt.executeQuery();

	   	 	while(rs.next()){	   	 		
	   	 		
				String[] android = new String[2];
				android[0] = rs.getString("username");
	 			android[1] = Arrays.toString(rs.getBytes("password"));
				
		 		if(rs.getBoolean("active") == false) {
		 			Cache.pendingAndroid.put(android[0],android);
		 		} else {
		 			Cache.acceptedAndroid.put(android[0],android);
		 		}
	   	 	}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			freeResources(con, stmt, rs);
		}		
	}
	
	/**
	 * Inserts the Android user account into the database and cache memory
	 * <p>
	 * The new account:
	 *  <ul>
	 *   <li>is considered inactive.</li>
	 *	 <li>is inserted in the {@link Cache#pendingAndroid} .</li>
	 *	</ul>
	 *
	 * @param username the username of the user account
	 * @param password the password of the user account
	 * @return connected a flag indicating whether the registration was successful or not
	 */
	public boolean registerUser(String username, String password){
		boolean connected = false;
		Connection con = this.getConnection();
		MessageDigest md;

		PreparedStatement stmt = null;
		ResultSet data = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
	 		md.update(password.getBytes("UTF-8"));
	 		byte[] digest = md.digest();

	 		if(!Cache.acceptedAndroid.containsKey(username) && 
					!Cache.pendingAndroid.containsKey(username) && 
					!username.equals("root")) {
	 			connected = true;
	 			String insertStmt = "INSERT INTO Users (username, password, active, admin)"
	 					+	"VALUES (?, ?, ?, ?)";
	       	 	stmt = con.prepareStatement(insertStmt);
	       	 	stmt.setString(1, username);
	   	 		stmt.setBytes(2, digest);
	   	 		stmt.setBoolean(3, false);
	   	 		stmt.setBoolean(4, false);
	       	 	stmt.executeUpdate();
	       	 	String[] user = new String[2];
	       	 	user[0] = username;
	       	 	user[1] = Arrays.toString(digest);
	       	 	Cache.pendingAndroid.put(username, user);

	       	 	return true;
	 		}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{	
			freeResources(con, stmt, data);
		}
		return connected;
	}
}
