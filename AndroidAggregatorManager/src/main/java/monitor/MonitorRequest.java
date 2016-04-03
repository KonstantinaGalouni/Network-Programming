package monitor;

import java.io.BufferedInputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

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
import android.widget.TableLayout;
import application.App;

/**
 * The Class MonitorRequest is the GET request to AM to get the condition of 
 * current page, as it is shown in AM.
 * .
 */
public class MonitorRequest extends AsyncTask<Void, Void, Void> {
	
	/** The uri. */
	final String URI = App.ip + "/monitor";
	
	/** The latest data. */
	private LinkedHashMap<String,String[]> data = null;
	
	/** The Activity. */
	private Activity act;
	
	/** The monitor. */
	private TableLayout monitor;
	
	/** The selected. */
	public Selected selected ;	// index of last row selected
	
    /**
     * This is the constructor of class and instantiates its fields.
     *
     * @param act the act
     * @param selected the selected
     */
    public MonitorRequest(Activity act, Selected selected) {
    	this.act = act;
    	this.monitor = (TableLayout) act.findViewById(R.id.monitor); 
    	this.selected = selected;
    }
    
    /**
     * This is the GET request which is executed in background.
	 * AM returns a json string which includes a map describing the condition 
	 * and the info about every SA related to AM.
     */
    @Override
    public Void doInBackground(Void... params) {      	
    	
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection"); 
		}else{	    	
	        try {
	        	HttpClient client = new DefaultHttpClient();
	        	HttpGet request = new HttpGet(URI);
	        	HttpResponse response = client.execute(request);
	
	        	BufferedInputStream in = new BufferedInputStream(response.getEntity().getContent());
	            Gson gson = new GsonBuilder().create();
	            Type type = new TypeToken<LinkedHashMap<String, String[]>>(){}.getType();
	            
	            data = gson.fromJson(Utils.getResponseText(in), type); 	            
	         }catch (Exception e ) {        	
	        	Log.e("Error", "Connection to AM Server failed!");
	         }
		}
        return null;
    }    
    
    /**
	 * After the execution of doInBackground function, this function is executed,
	 * it stores the latest data, empties the currently displayed table and 
	 * renders the returned map.
     */
    protected void onPostExecute(Void unused) {   	   	
        
        // Fill the table with the data from server response
        
        if(data != null){
        	MonitorFragment.data = data;
        	Utils.emptyTable(monitor, 2); // Clear the current contents of the table      
        	Utils.renderMonitor(data,monitor,act,selected);
        }
    }
}