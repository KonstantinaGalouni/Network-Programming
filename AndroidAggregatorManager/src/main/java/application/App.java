package application;
import sqlite.SQLiteDB;
import android.app.Application;
import android.util.Log;

/**
 * The Class App creates and initiates the SQLite database of the application as soon as it starts.
 */
public class App extends Application {
	
	/** The ip for the services. */
	public static String ip = "http://83.212.113.45:8080/AggregatorManager/services";
	//public static String ip = "http://10.0.2.2:8080/AggregatorManager/services";
	
	/**
	 * The SQLite database is created. 
	 */
	@Override
	public void onCreate() {
		deleteDatabase("jobs.db");
		Log.i("Database","\n\n======= PREVIOUS DATABASE DESTROYED ========\n\n"); 
		
		SQLiteDB.db = new SQLiteDB(this);	
		super.onCreate();
	}
}
