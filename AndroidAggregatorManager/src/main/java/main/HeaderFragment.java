package main;

import utility.NetworkChangeReceiver;
import utility.NetworkUtils;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
/**
 * The Class HeaderFragment.
 */
public class HeaderFragment extends Fragment {
	
	/** The Activity. */
	private Activity act;
	
	/** The thread. */
	private Thread t;
	
    /**
     * This is the constructor of the class and instantiates the activity.
     *
     * @param act the act
     */
    public HeaderFragment(Activity act) {
    	this.act = act;  	
    }
    
    /**
     * This function is called when the view is created and instantiates the rootView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      	
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);        
                
        return rootView;
    }
    
    /**
     * This function is called when the Activity is created.
     * It instantiates the connectivity status and starts the thread responsible for the refresh.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);    
        
        int status = NetworkUtils.getConnectivityStatus(act);
        NetworkChangeReceiver.status = status;
        NetworkChangeReceiver.updateConnectionStatus();
        
        refreshServerStatus();
    }
    
    /**
     * If the fragment gets onStop condition, interrupt the thread responsible for the refresh. 
     */
    @Override
    public void onStop() {
    	t.interrupt();
        super.onStop();        
    }  
    
    /**
     * Refresh server status every two seconds.
     */
    private void refreshServerStatus(){				
		Thread t = new Thread() {
			@Override
			public void run() {
			    try{			    	
			    	while(!isInterrupted()) {
			    		new StatusRequest().execute();	
			    		Thread.sleep(2000);
			    	}
			    }catch (InterruptedException e) {}		    
			}
		};
		this.t = t;
		t.start();		
    }
}
