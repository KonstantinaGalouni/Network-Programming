package results;

import java.io.BufferedInputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
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
 * The Class AllResultsRequest is the GET request to AM to get all the results of nmap jobs.
 */
public class AllResultsRequest extends AsyncTask<Void, Void, Void> {
	
	/** The uri. */
	final String URI = App.ip + "/allresults";
	
	/** The latest data. */
	private List<String[]> data = null;
	
	/** The Activity. */
	private Activity act;
	
	/** The results table. */
	private TableLayout resultsTable;
	
	/** The selected row. */
	public Selected selected ;	// index of last row selected
	
    /**
     * This is the constructor of the class and instantiates its fields.
     *
     * @param act the act
     * @param selected the selected
     * @param resultsTable the results table
     */
    public AllResultsRequest(Activity act, Selected selected, TableLayout resultsTable) {
    	this.act = act;
    	this.resultsTable = resultsTable;
    	this.selected = selected;
    }
    
    /**
     * This is the GET request which is executed in background.
	 * AM returns a json string which includes a list of all nmap results.
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
	 * 
	 * Before the results are rendered they have to be sorted by date.
     */
    protected void onPostExecute(Void unused) {   	   	
        
        // Fill the table with the data from server response
    	TableRow row = (TableRow) resultsTable.getChildAt(0);
    	row.getChildAt(1).clearFocus();
    	String num = (String) ((EditText) row.getChildAt(1)).getText().toString();
    	int latestResults = 0;

    	if(!num.equals("")){
    		latestResults = Integer.parseInt(num);
    	}
    	
        if(data != null) {
        	Collections.sort(data, new Comparator<String[]>(){
    		     public int compare(String[] res1, String[] res2){
    		         return res1[3].compareTo(res2[3]);
    		     }
    		});
    		Collections.reverse(data);
        	
        	AllResultsFragment.data = data;
        	Utils.emptyTable(resultsTable, 2); // Clear the current contents of the table      
        	Utils.renderAllResults(data,resultsTable,act,selected,latestResults);
        } else if(AllResultsFragment.data != null) {
        	Utils.emptyTable(resultsTable, 2); // Clear the current contents of the table
        	Utils.renderAllResults(AllResultsFragment.data,resultsTable,act,selected,latestResults);
        }
    }
}
