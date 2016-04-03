package login;

import android.AndroidAM.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * The Class LoginFragment is the main area of the LoginActivity.
 */
public class LoginFragment extends Fragment{
    
    /** The Activity. */
    private Activity act;

	/**
	 * This is the constructor of the class and instantiates the activity.
	 *
	 * @param act the act
	 */
	public LoginFragment(Activity act) {
    	this.act = act;
    }
	
    /**
     * This function is called when the view is created. There is a username and 
     * a password EditText where the user inserts their account info.
     * There is also a Login button which checks if the account is valid and a
     * link to the Register Activity.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      	
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
     
		setTitle("Login");
		
		Button login = (Button) rootView.findViewById(R.id.loginBtn);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				
				act.runOnUiThread(new Runnable() {
		    		public void run() {  validateLogin();	}
	    		});
			}
		});
		
		TextView register = (TextView) rootView.findViewById(R.id.link_register);
		
		Spannable span = new SpannableString(getResources().getString(R.string.register));	
		ClickableSpan clickableSpan = new ClickableSpan() {
		    @Override
		    public void onClick(View textView) {
		    	Intent i = new Intent(act.getApplicationContext(), RegisterActivity.class);
		    	startActivity(i);
		    	act.finish();
		    }
		};
		span.setSpan(clickableSpan, 12, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		register.setText(span);
		register.setMovementMethod(LinkMovementMethod.getInstance());
        
        return rootView;
    }
	
	/**
	 * Validate login by a {@link LoginRequest} to AM.
	 */
	private void validateLogin(){
		new LoginRequest(act).execute();
	}
	
	/**
	 * Sets the title of the ActionBar for the current Activity.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		((LoginActivity) getActivity()).setActionBarTitle(title);
	}
}
