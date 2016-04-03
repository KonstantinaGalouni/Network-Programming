package sqlite;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * The Class SQLiteDB is the database "jobs.db" of the application.
 */
public class SQLiteDB {
	
	/** The db. */
	public static SQLiteDB db;
	
	/** The database. */
	private SQLiteDatabase database;
	
	/** The db helper. */
	private SQLiteHelper dbHelper;
	
	/** All the columns of table jobs in "jobs.db". */
	private String[] allColumns = { 
									SQLiteHelper.COLUMN_ID,
								    SQLiteHelper.COLUMN_AGENT,
								    SQLiteHelper.COLUMN_FLAGS,
								    SQLiteHelper.COLUMN_PERIODIC,
								    SQLiteHelper.COLUMN_TIME	};

	/**
	 * Instantiates a new SQLite database.
	 *
	 * @param context the context
	 */
	public SQLiteDB(Context context) {
	    dbHelper = new SQLiteHelper(context);
	}

	/**
	 * Open the SQLite database.
	 *
	 * @throws SQLException the SQL exception
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close the SQLite database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Insert a job into the table jobs of the database "jobs.db".
	 *
	 * @param agent the agent
	 * @param job the job to be inserted
	 */
	public void insertJob(String agent, String[] job) {		
	    ContentValues values = new ContentValues();	   
	    
	    if(job[0] != null){					// for exit
	    	values.put(SQLiteHelper.COLUMN_ID, job[0]);
	    }
	    
	    values.put(SQLiteHelper.COLUMN_AGENT, agent);
	    values.put(SQLiteHelper.COLUMN_FLAGS, job[1]);
	    values.put(SQLiteHelper.COLUMN_PERIODIC, job[2]);
	    values.put(SQLiteHelper.COLUMN_TIME, job[3]); 
	    
	    open();	    
	    database.insert(SQLiteHelper.TABLE_NAME, null,values);	    
	    close();	    
	}
	
	/**
	 * Insert multiple jobs into the table jobs of the database "jobs.db".
	 *
	 * @param agent the agent
	 * @param jobs the jobs to be inserted
	 */
	public void insertJobs(String agent, LinkedHashMap<String, String[]> jobs) {	
		open();			
		for (Entry<String, String[]> entry : jobs.entrySet()) {
			String[] job = entry.getValue();
			
		    ContentValues values = new ContentValues();	    
		    values.put(SQLiteHelper.COLUMN_AGENT, agent);
		    values.put(SQLiteHelper.COLUMN_FLAGS, job[1]);
		    values.put(SQLiteHelper.COLUMN_PERIODIC, job[2]);
		    values.put(SQLiteHelper.COLUMN_TIME, job[3]); 
		    database.insert(SQLiteHelper.TABLE_NAME, null,values);
		}		
	    close();	    
	}	
	
	/**
	 * Delete a job from the table jobs of the database "jobs.db".
	 *
	 * @param id the id of the job to be deleted
	 */
	public void deleteJob(String id) {
		open();
		
		Log.i("Database", "Job deleted with id: " + id);
		database.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.COLUMN_ID
	        + " = " + id, null);
		
	    close();
	}

	/**
	 * Gets the all jobs from the table jobs of the database "jobs.db".
	 *
	 * @param flags the flags
	 * @return the all jobs
	 */
	public Map<String,LinkedHashMap<String, String[]>> getAllJobs(String flags) {	
		
		open();
		
		Map<String,LinkedHashMap<String, String[]>> jobmap;
		jobmap = new LinkedHashMap<String,LinkedHashMap<String, String[]>>();
		
		String where ;
		String[] args;
		
		
		if(flags == null){
			where = "flags != ? and flags != ?";
			args = new String[] {"Stop", "exit(0)"};
		}else{			
			where = "flags = ? ";
			args = new String[] { flags };
		}
		
	    Cursor cursor = database.query(SQLiteHelper.TABLE_NAME,
	    							allColumns, where, args , null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {	    	
	    	String[] job  = cursorToJob(cursor); 
	    	String agent = getAgent(cursor);
	    	
	    	if(!jobmap.containsKey(agent)){
	    		jobmap.put(agent, new LinkedHashMap<String, String[]>());
	    	}
	    	LinkedHashMap<String, String[]> jobs = jobmap.get(agent);
	    	jobs.put(job[0], job );

	    	cursor.moveToNext();
	    }
	    cursor.close();
	    
	    close();
	    
	    return jobmap;
	}

	/**
	 * Cursor to job.
	 *
	 * @param cursor the cursor
	 * @return the job that the cursor shows
	 */
	private String[] cursorToJob(Cursor cursor) {
		String[] job = new String[4];	    
		job[0] = cursor.getLong(0) + "" ;	// id
	    job[1] = cursor.getString(2);		// flags
	    job[2] = cursor.getString(3);		// periodic
	    job[3] = cursor.getString(4);		// time		 
	    
	    return job;
	}
	  
	/**
	 * Gets the agent where the job, that shows the cursor, belongs.
	 *
	 * @param cursor the cursor
	 * @return the agent where the job, that shows the cursor, belongs
	 */
	private String getAgent(Cursor cursor){
		return cursor.getString(1);
	}
	
	/**
	 * Clear table.
	 */
	public void clearTable(){
		open();
	    database.delete(SQLiteHelper.TABLE_NAME, null,null);
	    close();
	}
	
}
