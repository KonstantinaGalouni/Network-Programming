package utility;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import delete.DeleteRequest;
import sqlite.SQLiteDB;
import monitor.TerminateRequest;
import add.AddRequest;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The Class NetworkChangeReceiver is responsible to send the lost jobs when the
 * connectivity to Internet is restored and to update the HeaderFragment.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {     
	
	/** The Constant lock. */
	private static final Object lock = new Object();
	
	/** The status. */
	public static int status = -1;
	
	/** The Activity. */
	public static Activity act;
	
    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * The HeaderFragment is updated and if there is connection to the Internet,
     * the lost jobs are sent to AM, by calling the appropriate request.
     */
    @Override
    public void onReceive(final Context context, final Intent intent) { 
    	String msg = null;
    	
    	synchronized(lock){
    		status = NetworkUtils.getConnectivityStatus(context); 
    		
    		updateConnectionStatus();
    		
	        if(status > 0){
	        	Map<String, LinkedHashMap<String, String[]>> addjobs;
				Map<String, LinkedHashMap<String, String[]>> deljobs;
				Map<String, LinkedHashMap<String, String[]>> termjobs;
	        	SQLiteDB db = SQLiteDB.db;
	        	
	        	addjobs  = db.getAllJobs(null);
	        	deljobs  = db.getAllJobs("Stop");
	        	termjobs = db.getAllJobs("exit(0)");
	        	
	        	for(Entry<?, ?> entry: addjobs.entrySet()){
	        		String hashKey = (String) entry.getKey();	        		
	        		LinkedHashMap<String, String[]> jobs = addjobs.get(hashKey);
	        		
	        		new AddRequest(hashKey, jobs).execute();
	        	}
	        	
	        	for(Entry<?, ?> entry: deljobs.entrySet()){
	        		String hashKey = (String) entry.getKey();	        		
	        		LinkedHashMap<String, String[]> jobs = deljobs.get(hashKey);
	        		
	        		for(Entry<?, ?> row: jobs.entrySet()){
	        			String jobid = (String) row.getKey();
		        		new DeleteRequest(hashKey, jobid).execute();
	        		}
	        	}
	        	
	        	for(Entry<?, ?> entry: termjobs.entrySet()){
	        		String hashKey = (String) entry.getKey();
	        		new TerminateRequest(hashKey).execute();
	        	}

	        	db.clearTable();
	        }
    		
            if(status == 1) {
            	msg = "Wifi enabled";
            }else if(status == 2) {
            	msg = "Mobile 3G enabled";
            }else if(status == 0) {            	
            	msg = "No connection to Internet";
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Update connection status.
     */
    public static void updateConnectionStatus(){

    	Fragment frag = act.getFragmentManager().findFragmentById(R.id.fragment_header);
    	
    	if(frag != null) {
	        TextView connection = (TextView) frag.getView().findViewById(R.id.constatus);        
	        
	        updateServerStatus(0);
	        
	   		String text = null; 
	    	
	        if(status == 1) {
	        	text = "<b>Internet Status : "
						+ "<font color='#00b700'>Wifi</font> </b>";
	        }else if(status == 2) {
	        	text = "<b>Internet Status : "
						+ "<font color='#00b700'>Mobile 3G</font> </b>";
	        }else if(status == 0) {
	        	text = "<b>Internet Status : "
						+ "<font color='#EE0000'>No Connection</font> </b>";
	        }
	        
	        connection.setText(Html.fromHtml(text));
    	}
    }
    
    /**
     * Update server status.
     *
     * @param status the status
     */
    public static void updateServerStatus(int status){
    	
    	String text = null;   		
    	Fragment frag = act.getFragmentManager().findFragmentById(R.id.fragment_header);
    	if(frag!=null){
	    	final TextView server = (TextView) frag.getView().findViewById(R.id.serverstatus);
	    	
	    	if(status == 1){
		    	text = "<b>Server Status : "
						+ "<font color='#00b700'>Online</font></b>";
	    	}else if(status == 0){
	    		text = "<b>Server Status : "
	    				+ "<font color='#00bfff'>Unknown</font></b>";
	    	}else{
	    		text = "<b>Server Status : "
						+ "<font color='#EE0000'>Offline</font></b>";
	    	}
	    	
	    	final String msg = text;
	   	 	act.runOnUiThread(new Runnable() {
	   	 		public void run() {
	   	 			server.setText(Html.fromHtml(msg));
	   	 		}
	   	 	});
    	}
    }
    
}
