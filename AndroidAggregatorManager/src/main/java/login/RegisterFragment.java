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
import android.widget.EditText;
import android.widget.TextView;

/**
 * The Class RegisterFragment is the main area of the RegisterActivity.
 */
public class RegisterFragment extends Fragment {

    /** The Activity. */
    private Activity act;
    
    /** The username. */
    private EditText username;
    
    /** The password. */
    private EditText password;
    
    /** The password confirm. */
    private EditText passwordConfirm;

	/**
	 * Instantiates a new register fragment.
	 *
	 * @param act the act
	 */
	public RegisterFragment(Activity act) {
    	this.act = act;
    }

    /**
     * This function is called when the view is created. There is a username, 
     * a password and a confirm password EditText where the user inserts the info
     * about the new requested account.
     * There is also a Register button which checks if the account is properly
     * registered and a link to the Login Activity.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      	
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        
    	this.username = (EditText) rootView.findViewById(R.id.newUsername);
    	this.password = (EditText) rootView.findViewById(R.id.newPassword);
    	this.passwordConfirm = (EditText) rootView.findViewById(R.id.newPasswordConfirm);
        
		setTitle("Register");
		
		Button register = (Button) rootView.findViewById(R.id.registerBtn);
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				validateRegister();
			}
		});
		
		TextView login = (TextView) rootView.findViewById(R.id.link_login);
		Spannable span = new SpannableString(getResources().getString(R.string.login));	
		ClickableSpan clickableSpan = new ClickableSpan() {
		    @Override
		    public void onClick(View textView) {
		    	Intent i = new Intent(act.getApplicationContext(), LoginActivity.class);
		    	startActivity(i);
		    	act.finish();
		    }
		};
		span.setSpan(clickableSpan, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		login.setText(span);
		login.setMovementMethod(LinkMovementMethod.getInstance());
        
		return rootView;
    }
	
	/**
	 * Validate register by a {@link RegisterRequest} to AM..
	 */
	private void validateRegister(){
		act.runOnUiThread(new Runnable() {
			public void run(){
				new RegisterRequest(act, username, password, passwordConfirm).execute();
			}
		});
	}
	
	/**
	 * Sets the title of the ActionBar for the current Activity.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		((RegisterActivity) getActivity()).setActionBarTitle(title);
	}
}
