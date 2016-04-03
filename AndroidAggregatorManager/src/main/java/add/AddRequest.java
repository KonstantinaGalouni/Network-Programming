package add;

import java.util.LinkedHashMap;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import sqlite.SQLiteDB;
import utility.NetworkChangeReceiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.AsyncTask;
import android.util.Log;
import application.App;

/**
 * The Class AddRequest is the POST request to AM in order to add nmap jobs of the list jobs to the SA.
 */
public class AddRequest extends AsyncTask<Void, Void, Void> {	
	
	/** The uri. */
	final String URI = App.ip + "/addjobs";
	
	/** The list jobs. */
	private LinkedHashMap<String,String[]> jobs;
	
	/** The hash key of SA. */
	private String hashKey;
	
	/**
	 * Instantiates the POST request to AM for adding the jobs.
	 *
	 * @param hashKey the hash key of SA
	 * @param jobs the nmap jobs list
	 */
	public AddRequest (String hashKey, LinkedHashMap<String,String[]> jobs){
		this.jobs = jobs; 
		this.hashKey = hashKey;
	}

	/**
	 * This is the POST request which is executed in background.
	 * A json string is sent to AM which contains the hashKey of the selected SA
	 * and the list of nmap jobs to be assigned to this SA.
	 */
	@Override
	protected Void doInBackground(Void... params) {
			
		Gson gson = new GsonBuilder().create();
		
		String[] wrapper = new String[2];	
		wrapper[0] = gson.toJson(hashKey);
		wrapper[1] = gson.toJson(jobs);
		String data = gson.toJson(wrapper);		
		
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection, adding jobs to database");
			SQLiteDB.db.insertJobs(hashKey,jobs);			
		}else{		
	        try {
	        	DefaultHttpClient httpclient = new DefaultHttpClient();
	            HttpPost httpost = new HttpPost(URI);
	            StringEntity se = new StringEntity(data);
	            httpost.setEntity(se);
	            httpost.setHeader("Content-type", "application/json");
	            httpclient.execute(httpost);
	        }catch (Exception e ) { 
	        	Log.e("Error", "Connection to AM Server failed! (add) ");	        	
	        }
		}
        
        return null;
	}
}
