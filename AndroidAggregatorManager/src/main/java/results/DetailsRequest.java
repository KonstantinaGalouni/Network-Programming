package results;

import java.io.BufferedInputStream;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.AndroidAM.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import application.App;

/**
 * The Class DetailsRequest is the GET request to AM about the details of a specific nmap result.
 */
public class DetailsRequest extends AsyncTask<Void, Void, Void> {
	
	/** The uri. */
	final String URI = App.ip + "/details";
	
	/** The latest data. */
	private List<String[]> data = null;
	
	/** The Activity. */
	private Activity act;
	
	/** The id job result. */
	private String idJobResult;
	
	/** The view. */
	private View view;
	
	/** The details table. */
	private TableLayout detailsTable;
	
    /**
     * This is the constructor of the class and instantiates its fields.
     *
     * @param act the act
     * @param idJobResult the id job result
     * @param view the view
     */
    public DetailsRequest(Activity act, String idJobResult, View view) {
    	this.act = act;
    	this.idJobResult = idJobResult;
    	this.view = view;
    }
    
    /**
     * This is the GET request which is executed in background.
     * The idjobResult of the selected nmap result is sent as a parameter of the GET request.
	 * AM returns a json string which includes a list of details about the selected nmap result.
     */
    @Override
    public Void doInBackground(Void... params) {
    	
        try {
    		HttpClient client = new DefaultHttpClient();
        	HttpGet request = new HttpGet(URI + "/" + idJobResult);
        	HttpResponse response = client.execute(request);

        	BufferedInputStream in = new BufferedInputStream(response.getEntity().getContent());
            Gson gson = new GsonBuilder().create();
            Type type = new TypeToken<List<String[]>>(){}.getType();
            
            data = gson.fromJson(Utils.getResponseText(in), type); 
         }catch (Exception e ) {        	
        	 Log.e("Error", "Connection to AM Server failed!");
         }

        return null;
    }    
    
    /**
	 * After the execution of doInBackground function, this function is executed,
	 * it empties the currently displayed table and renders the returned list.
     */
    protected void onPostExecute(Void unused) {   	   	
        
        // Fill the table with the data from server response
        if(data != null){
    		LayoutParams layout = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
    		detailsTable = (TableLayout) view.findViewById(R.id.detailsTable);
    		
        	for(int i=detailsTable.getChildCount()-1;i>=0;i--){
        		detailsTable.removeViewAt(i); // Clear the current contents of the table           	
        	}
        	     		
	    	for (String[] res : data) {
    		
	    		TableRow row = new TableRow(act);
	    		TextView col;
	
	    		col = new TextView(act);
	    		col.setLayoutParams(layout);
	    		col.setTextColor(Color.BLACK);
	    		col.setSingleLine(true);
	    		col.setTextSize(11);  
	    		col.setText(res[0]);
	    		col.setGravity(Gravity.START);
	    		row.addView(col);
	    		
	    		col = new TextView(act);
	    		col.setLayoutParams(layout);
	    		col.setTextColor(Color.BLACK);
	    		col.setSingleLine(true);
	    		col.setTextSize(11);  
	    		col.setText(res[1]);
	    		col.setGravity(Gravity.START);
	    		row.addView(col);	
	    		
	    		row.setId(detailsTable.getChildCount());
	    		detailsTable.addView(row);	    		
	 		} 
        }
    }
}
