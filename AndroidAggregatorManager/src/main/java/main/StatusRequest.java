package main;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.NetworkChangeReceiver;
import android.os.AsyncTask;
import android.util.Log;
import application.App;

/**
 * The Class StatusRequest is responsible for checking the condition of AM.
 */
public class StatusRequest extends AsyncTask<Void, Void, Void> {

	/** The uri. */
	final String URI = App.ip + "/status";

	/**
	 * This is the GET request which is executed in background.
	 * If the request succeeds, the server status is set to 1 (online), otherwise to -1 (offline).
	 */
	@Override
	protected Void doInBackground(Void... params) {

		if(NetworkChangeReceiver.status == 0){
			NetworkChangeReceiver.updateServerStatus(0);
			Log.e("Connection" , "There is no connection"); 
		}else{	    	
	        try {
	        	HttpClient client = new DefaultHttpClient();
	        	HttpGet request = new HttpGet(URI);
	        	client.execute(request);
	        	NetworkChangeReceiver.updateServerStatus(1);
	         }catch (Exception e ) {        	
	        	NetworkChangeReceiver.updateServerStatus(-1); 
	        	Log.e("Error", "Connection to AM Server failed! (status)");
	         }
		}
		return null;
	}
}
