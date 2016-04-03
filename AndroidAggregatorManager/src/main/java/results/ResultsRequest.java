package results;

import java.io.BufferedInputStream;
import java.lang.reflect.Type;
import java.util.List;

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

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import application.App;

/**
 * The Class ResultsRequest is the GET request to AM to get the nmap results of the selected SA.
 */
public class ResultsRequest extends AsyncTask<Void, Void, Void> {
	
	/** The uri. */
	final String URI = App.ip + "/results";
	
	/** The latest data. */
	private List<String[]> data = null;
	
	/** The Activity. */
	private Activity act;
	
	/** The results table. */
	private TableLayout resultsTable;
	
	/** The selected row. */
	public Selected selected ;	// index of last row selected
	
	/** The hash key of the selected SA. */
	private String hashKey;
	
    /**
     * This is the constructor of the class and instantiates its fields.
     *
     * @param act the act
     * @param selected the selected
     * @param hashKey the hash key
     * @param resultsTable the results table
     */
    public ResultsRequest(Activity act, Selected selected, String hashKey, TableLayout resultsTable) {
    	this.act = act;
    	this.resultsTable = resultsTable;
    	this.selected = selected;
    	this.hashKey = hashKey;
    }
    
    /**
     * This is the GET request which is executed in background.
     * The hashKey of the selected SA is sent as a parameter of the GET request.
	 * AM returns a json string which includes a list of nmap results related to
	 * the selected SA.
     */
    @Override
    public Void doInBackground(Void... params) {
    	
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection"); 		
		}else{  	
	        try {
	    		HttpClient client = new DefaultHttpClient();
	        	HttpGet request = new HttpGet(URI + "/" + hashKey);
	        	HttpResponse response = client.execute(request);
	
	        	BufferedInputStream in = new BufferedInputStream(response.getEntity().getContent());
	            Gson gson = new GsonBuilder().create();
	            Type type = new TypeToken<List<String[]>>(){}.getType();
	            
	            data = gson.fromJson(Utils.getResponseText(in), type); 	            
	         }catch (Exception e ) {        
	        	Log.e("Error", "Connection to AM Server failed!");
	         }
		}

        return null;
    }    
    
    /**
	 * After the execution of doInBackground function, this function is executed.
	 * There is examined if there is a number in the EditText, so as to render
	 * the number latest results, otherwise it renders 0 results.
	 * 
	 * It also stores the latest data, empties the currently displayed table and 
	 * renders the returned list, according to the number of the latest results.
     */
    protected void onPostExecute(Void unused) {   	   	
        
        // Fill the table with the data from server response
    	TableRow row = (TableRow) resultsTable.getChildAt(1);
    	row.getChildAt(1).clearFocus();
    	String num = (String) ((EditText) row.getChildAt(1)).getText().toString();
    	int latestResults = 0;

    	if(!num.equals("")){
    		latestResults = Integer.parseInt(num);
    	}
    	
        if(data != null){
        	ResultsFragment.data = data;
        	Utils.emptyTable(resultsTable, 3); // Clear the current contents of the table   
        	Utils.renderResults(data,resultsTable,act,selected,latestResults);
        }
    }
}
