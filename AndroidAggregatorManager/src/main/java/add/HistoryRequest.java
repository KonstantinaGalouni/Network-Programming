package add;

import java.io.BufferedInputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.NetworkChangeReceiver;
import utility.Selected;
import utility.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.AndroidAM.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import application.App;

/**
 * The Class HistoryRequest is the GET request, in order to get the history of jobs of a selected SA.
 */
public class HistoryRequest extends AsyncTask<Void, Void, Void> {	
	
	/** The uri. */
	final String URI = App.ip + "/history";
	
	/** The onetime jobs from history. */
	public LinkedHashMap<String,String[]> onetime;
	
	/** The periodic jobs from history. */
	public LinkedHashMap<String,String[]> periodic;
	
	/** The Activity. */
	private Activity act;
	
	/** The hash key of SA. */
	private String hashKey;
	
	/** The selected row. */
	private Selected selected;
	
	/** The history of previously assigned nmap jobs. */
	private TableLayout history;
	
	/**
	 * This is the constructor of the class where its fields are instantiated.
	 *
	 * @param act the Activity
	 * @param hashKey the hash key of SA
	 * @param selected the selected row
	 * @param addView the add view
	 * @param historyView the history view
	 */
	public HistoryRequest(Activity act, String hashKey, Selected selected,
														View addView,
														View historyView){
    	this.act = act;
    	this.selected = selected;
    	this.history = (TableLayout) historyView.findViewById(R.id.jobsList);  
    	this.hashKey = hashKey;
	}

	/**
	 * This is the GET request which is executed in background.
	 * The hashKey of the selected SA is sent as a parameter of the GET request.
	 * AM returns a json string which includes the onetime jobs' map and the periodic jobs' map.
	 */
	@Override
	protected Void doInBackground(Void... params) {	
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection"); 		
		}else{
	    	try {
	    		HttpClient client = new DefaultHttpClient();
	        	HttpGet request = new HttpGet(URI + "/" + hashKey);
	        	HttpResponse response = client.execute(request);
	
	        	BufferedInputStream in = new BufferedInputStream(response.getEntity().getContent());
				
				Gson gson = new GsonBuilder().create();
				Type type = new TypeToken<LinkedHashMap<String, String[]>>(){}.getType();
				
				String[] data = gson.fromJson(Utils.getResponseText(in),String[].class);
	            onetime  = gson.fromJson(data[0],type); 
	            periodic = gson.fromJson(data[1],type); 
			}catch (Exception e ) {
	       	 	Log.e("Error", "Connection to AM Server failed! (history) ");
	        }
		}
    			
		return null;
	}
	
	/**
	 * After the execution of doInBackground function, this function is executed
	 * and renders both the returned maps.
	 */
	protected void onPostExecute(Void unused) { 
		render(onetime);
		render(periodic);
	}
	
	/**
	 * Render the map entries in the table of the view.
	 *
	 * @param map the map to be rendered
	 */
	private void render(LinkedHashMap<String,String[]> map){		
		if(map != null){			
			for (Entry<String, String[]> entry : map.entrySet()) {				
				String[] job = entry.getValue();
				Utils.addRow(act, history, job, selected, 4);	
			}
		}
	}
}
