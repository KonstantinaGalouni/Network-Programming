package main;

import utility.NetworkChangeReceiver;
import login.LoginActivity;
import monitor.MonitorFragment;
import android.AndroidAM.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {

	/** The monitor fragment. */
	private Fragment monitorFragment = new MonitorFragment(this);
	
	/** The header fragment. */
	private Fragment headerFragment  = new HeaderFragment(this);
	
	/** The main Activity. */
	public static Activity main ;
	
	/**
	 * This function is executed when the view is created.
	 * The activity creates two fragments:
	 * 	-  the header fragment which is a bar showing the connectivity condition
	 * 	   and the condition of AM,
	 *  -  the monitor fragment, where the table of the Software Agents and their condition is displayed. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		 
		NetworkChangeReceiver.act = this;
		
		if (monitorFragment != null) {
			
			if (savedInstanceState != null) {
                return;
            }
			
			FragmentManager fm = getFragmentManager();
	        FragmentTransaction ft = fm.beginTransaction();
	        ft.add(R.id.fragment_header,headerFragment,"STATUS_TAG");
	        ft.add(R.id.fragment_container,monitorFragment,"MONITOR_TAG");
	        ft.commit();
	    }			
	}
	
    /**
     * This function creates the menu of the mainActivity where the logout option is placed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * When the logout option is selected, the account info is deleted by the sharedpreferences
     * and the LoginActivity is launched.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_settings) {
            SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(i);
			finish();
        }
        return true;
    }

    /**
     * When the back button is pressed, if there are more fragments in the BackStack,
     * the most recent is displayed.
     */
    @Override
    public void onBackPressed() { 
    	int count = getFragmentManager().getBackStackEntryCount();

	    if (count == 0) {
	        super.onBackPressed();
	    } else {
	        getFragmentManager().popBackStack();
	    }
    }
    
    /**
     * When the activity is onDestroy condition the data saved in MonitorFragment is set to null.
     */
    @Override
    public void onDestroy(){
    	MonitorFragment.data = null;    	    	
    	super.onDestroy();
    }
    
    /**
     * Sets the action bar title.
     *
     * @param title the new action bar title
     */
    public void setActionBarTitle(String title) {
        getActionBar().setTitle(title);
    }
    
}
