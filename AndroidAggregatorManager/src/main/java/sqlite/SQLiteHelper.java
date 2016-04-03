package sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The Class SQLiteHelper contains the helper functions for the creation of SQLite database.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	/** The Constant TABLE_NAME. */
	public static final String TABLE_NAME 		= 	"jobs";
	
	/** The Constant COLUMN_ID. */
	public static final String COLUMN_ID 		= 	"id";
	
	/** The Constant COLUMN_AGENT. */
	public static final String COLUMN_AGENT		= 	"agent";
	
	/** The Constant COLUMN_FLAGS. */
	public static final String COLUMN_FLAGS 	= 	"flags";
	
	/** The Constant COLUMN_PERIODIC. */
	public static final String COLUMN_PERIODIC 	= 	"periodic";
	
	/** The Constant COLUMN_TIME. */
	public static final String COLUMN_TIME 		= 	"time";

	/** The Constant DATABASE_NAME. */
	private static final String DATABASE_NAME = "jobs.db";
	
	/** The Constant DATABASE_VERSION. */
	private static final int DATABASE_VERSION = 1;


	/** The Constant DATABASE_CREATE. */
	private static final String DATABASE_CREATE = 
			 "create table " + TABLE_NAME 	      + "("
		+ COLUMN_ID			+ " integer primary key autoincrement, " 
	    + COLUMN_AGENT		+ " text not null, " 
	    + COLUMN_FLAGS 		+ " text not null, " 
	    + COLUMN_PERIODIC 	+ " text not null, "
	    + COLUMN_TIME 		+ " text not null)";

	/**
	 * This is the constructor of the class and instantiates a new SQLite helper.
	 *
	 * @param context the context
	 */
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i("Database","\n\n========== DATABASE CREATED =========\n\n"); 
	}

	/**
	 * The database is created.
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);		  
	}

	/**
	 * The database is upgraded.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(SQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to " + newVersion 
        								+ ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}
} 

