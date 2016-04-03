package monitor;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import sqlite.SQLiteDB;
import utility.NetworkChangeReceiver;
import android.os.AsyncTask;
import android.util.Log;
import application.App;

/**
 * The Class TerminateRequest is the GET request to AM for termination of a SA.
 */
public class TerminateRequest extends AsyncTask<Void, Void, Void> {	
	
	/** The uri. */
	final String URI = App.ip + "/terminate";
	
	/** The hash key of the selected SA. */
	private String hashKey;
	
	/**
	 * This is the constructor of the class and instantiates the hashKey field.
	 *
	 * @param hashKey the hash key
	 */
	public TerminateRequest (String hashKey){
		this.hashKey = hashKey;
	}

	/**
	 * This is the GET request which is executed in background.
	 * The hashKey of the selected SA is sent as a parameter of the GET request.
	 */
	@Override
	protected Void doInBackground(Void... params) {   
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection, adding jobs to database");
			String[] job = {null,"exit(0)", "", ""};
			SQLiteDB.db.insertJob(hashKey,job);			
		}else{	
	        try {
	        	HttpClient client = new DefaultHttpClient();
	        	HttpGet request = new HttpGet(URI + "/" + hashKey);
	        	client.execute(request);
	        }catch (Exception e ) { 
	        	 Log.e("Error", "Connection to AM Server failed! (terminate) ");
	        }
		}
        return null;
	}
}