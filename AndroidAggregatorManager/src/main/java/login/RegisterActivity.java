package login;

import main.HeaderFragment;
import utility.NetworkChangeReceiver;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * The Class RegisterActivity represents the activity of creating an account.
 */
public class RegisterActivity extends Activity {
	
	/** The register fragment. */
	private Fragment registerFragment = new RegisterFragment(this);
	
	/** The header fragment. */
	private Fragment headerFragment  = new HeaderFragment(this);
	
	/**
	 * This function is executed when the view is created.
	 * The activity creates two fragments:
	 * 	-  the header fragment which is a bar showing the connectivity condition
	 * 	   and the condition of AM,
	 *  -  the register fragment, where users can create a new account.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);	
		
		NetworkChangeReceiver.act = this;
		
		if (registerFragment != null) {
			
			if (savedInstanceState != null) {
                return;
            }
			
			FragmentManager fm = getFragmentManager();
	        FragmentTransaction ft = fm.beginTransaction();
	        ft.add(R.id.fragment_header,headerFragment,"STATUS_TAG");
	        ft.add(R.id.fragmentRegister_container,registerFragment,"LOGIN_TAG");
	        ft.commit();
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
