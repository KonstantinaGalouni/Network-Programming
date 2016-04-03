package login;

import utility.NetworkChangeReceiver;
import main.HeaderFragment;
import main.MainActivity;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * The Class LoginActivity represents the initial activity of the application.
 */
public class LoginActivity extends Activity {	
	
	/** The login fragment. */
	private Fragment loginFragment = new LoginFragment(this);
	
	/** The header fragment. */
	private Fragment headerFragment  = new HeaderFragment(this);
	
	/**
	 * This function is executed when the view is created.
	 * The activity creates two fragments:
	 * 	-  the header fragment which is a bar showing the connectivity condition
	 * 	   and the condition of AM,
	 *  -  the login fragment, where users can insert their usernames and passwords.  
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);	
		
		NetworkChangeReceiver.act = this;
		
		SharedPreferences sharedpreferences = getSharedPreferences
				("MyPrefs", Context.MODE_PRIVATE);
		if(sharedpreferences.contains("username")) {
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
			finish();
		} else{
			if (loginFragment != null) {
				
				if (savedInstanceState != null) {
	                return;
	            }
				
				FragmentManager fm = getFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        ft.add(R.id.fragment_header,headerFragment,"STATUS_TAG");
		        ft.add(R.id.fragmentLogin_container,loginFragment,"LOGIN_TAG");
		        ft.commit();
		    }			
		}
	}
	
    /**
     * Sets the action bar title of the Activity.
     *
     * @param title the new action bar title
     */
    public void setActionBarTitle(String title) {
        getActionBar().setTitle(title);
    }
}
