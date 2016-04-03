package delete;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import sqlite.SQLiteDB;
import utility.NetworkChangeReceiver;
import android.os.AsyncTask;
import android.util.Log;
import application.App;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The Class DeleteRequest is the POST request to AM in order to delete a nmap job from a SA.
 */
public class DeleteRequest extends AsyncTask<Void, Void, Void> {	
	
	/** The uri. */
	final String URI = App.ip + "/deletejob";
	
	/** The jobid. */
	private String jobid;
	
	/** The hash key of the SA. */
	private String hashKey;
	
	/**
	 * This is the constructor of this class and instantiates its fields.
	 *
	 * @param hashKey the hash key
	 * @param jobid the jobid
	 */
	public DeleteRequest (String hashKey, String jobid){
		this.jobid = jobid; 
		this.hashKey = hashKey;
	}

	/**
	 * This is the POST request which is executed in background.
	 * A json string is sent to AM which contains the hashKey of the selected SA
	 * and the jobid of nmap job to be deleted from this SA.
	 * 
	 * If there is no connection to the Internet, the job to be deleted is inserted 
	 * in the SQLite database
	 */
	@Override
	protected Void doInBackground(Void... params) {			
	
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection, adding jobs to database"); 
			String[] job = {jobid,"Stop", "", ""};
			SQLiteDB.db.insertJob(hashKey,job);			
		}else{			
			Gson gson = new GsonBuilder().create();
			
			String[] wrapper = new String[2];	
			wrapper[0] = gson.toJson(hashKey);
			wrapper[1] = gson.toJson(jobid);
			
			String data = gson.toJson(wrapper);	
	        try {
	        	DefaultHttpClient httpclient = new DefaultHttpClient();
	            HttpPost httpost = new HttpPost(URI);
	            StringEntity se = new StringEntity(data);
	            httpost.setEntity(se);
	            httpost.setHeader("Content-type", "application/json");
	            httpclient.execute(httpost);
	        }catch (Exception e ) { 
	        	Log.e("Error", "Connection to AM Server failed! (delete) ");	        	 
	        }
		}
        
        return null;
	}
}