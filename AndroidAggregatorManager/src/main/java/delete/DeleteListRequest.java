package delete;

import java.io.BufferedInputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import utility.NetworkChangeReceiver;
import utility.Selected;
import utility.Utils;
import android.AndroidAM.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import application.App;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * The Class DeleteListRequest is the GET request to AM in order get 
 * the periodic nmap jobs of the selected SA.
 */
public class DeleteListRequest extends AsyncTask<Void, Void, Void> {	
	
	/** The uri. */
	String URI= App.ip + "/activelist";
	
	/** The periodic nmap jobs' map. */
	public LinkedHashMap<String,String[]> periodic;
	
	/** The Activity. */
	private Activity act;
	
	/** The agent. */
	private String[] agent;
	
	/** The selected row. */
	public Selected selected;
	
	/** The table history. */
	private TableLayout history;
	
	/**
	 * This is the constructor of the class and instantiates its fields.
	 *
	 * @param act the act
	 * @param agent the agent
	 * @param delView the del view
	 * @param selected the selected
	 */
	public DeleteListRequest(Activity act, String[] agent, View delView,
															Selected selected){
    	this.act = act;
    	this.history = (TableLayout) delView.findViewById(R.id.jobsList);  
    	this.agent = agent;
    	this.selected = selected;
    	selected.index = -1;
	}

	/**
	 * This is the GET request which is executed in background.
	 * A json string is the response of AM which contains the map of the periodic jobs 
	 * which can be deleted from this SA. 
	 * 
	 * The hashKey of the SA is sent as a parameter to this request.
	 */
	@Override
	protected Void doInBackground(Void... params) {

		HttpURLConnection urlConnection = null;
		
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection"); 		
		}else{   	
	        try {
	            URL url = new URL(URI + "/" + agent[0]);
	            urlConnection = (HttpURLConnection) url.openConnection();
	
	            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
	            Gson gson = new GsonBuilder().create();
	            Type type = new TypeToken<LinkedHashMap<String, String[]>>(){}.getType();
	           
	            periodic = gson.fromJson(Utils.getResponseText(in),type);
	         }catch (Exception e ) { 
	        	 Log.e("Error", "Connection to AM Server failed! (periodic) ");
	         }finally{
	        	 if(urlConnection!=null){ 	 urlConnection.disconnect();	}
	         }
		}
		
		return null;
	}
	
	/**
	 * After the execution of doInBackground function, this function is executed
	 * and renders the returned map (periodic nmap jobs).
	 */
	protected void onPostExecute(Void unused) { 
		render(periodic);
	}
	
	/**
	 * Render the map entries in the table of the view.
	 *
	 * @param map the map
	 */
	private void render(LinkedHashMap<String,String[]> map){		
		if(map != null){			
			for (Entry<String, String[]> entry : map.entrySet()) {				
				String[] job = entry.getValue();
				
				if(job[1].equals("exit(0)") || job[1].equals("Stop")){
					continue;
				}			
				
				Utils.addRow(act, history, job, selected, 4);	
			}
		}
	}
}
