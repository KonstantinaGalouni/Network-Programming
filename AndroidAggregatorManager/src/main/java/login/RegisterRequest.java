package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.NetworkChangeReceiver;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import application.App;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The Class RegisterRequest is the POST request to AM in order to register to the application.
 */
public class RegisterRequest extends AsyncTask<Void, Void, Void> {

	/** The StringBuilder sb. */
	private StringBuilder sb;
	
	/** The uri. */
	final String URI = App.ip + "/androidRegister";
	
	/** The Activity. */
	private Activity act;
	
	/** The username edit text. */
	private EditText usernameEditText = null;
	
	/** The password edit text. */
	private EditText passwordEditText = null;
	
	/** The password confirm edit text. */
	private EditText passwordConfirmEditText = null;
	
	/** The username. */
	private String username;
	
	/** The password. */
	private String password;
	
	/** The password confirm. */
	private String passwordConfirm;
	
	/** The response string. */
	private String responseString = null;
    
    /**
     * This is the constructor of the class and instantiates its fields.
     *
     * @param act the Activity
     * @param usernameEditText the username edit text
     * @param passwordEditText the password edit text
     * @param passwordConfirmEditText the password confirm edit text
     */
    public RegisterRequest(Activity act, EditText usernameEditText, 
    		EditText passwordEditText, EditText passwordConfirmEditText) {
    	this.act = act;
    	this.usernameEditText = usernameEditText;
    	this.passwordEditText = passwordEditText;
    	this.passwordConfirmEditText = passwordConfirmEditText;
    	this.username = usernameEditText.getText().toString();
    	this.password = passwordEditText.getText().toString();
    	this.passwordConfirm = passwordConfirmEditText.getText().toString();
    }
    
    /**
	 * This is the POST request which is executed in background.
	 * A json string is sent to AM which contains the AndroidInfo (username, password).
	 * A json string is returned as a response which describes the validation results of the registration.
	 * 
	 * The request is done only if the username and the password texts are acceptable,
	 * if there is a connection and if the password and its confirmation are the same.
	 */
    @Override
    public Void doInBackground(Void... params) {    
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection"); 		
		}else{
	        try {
	        	if(username.length()==0 || username.startsWith(" ")) {
	        		responseString = "usernameEmpty";
	        	} else if(password.length()==0 || password.startsWith(" ")) {
	        		responseString = "passwordEmpty";
	        	} else if(!password.equals(passwordConfirm)){
	        		responseString = "notMatch";
	        	} else {
		        	Gson gson = new GsonBuilder().create();
		        	
		        	AndroidInfo androidInfo = new AndroidInfo();
		        	androidInfo.setUsername(username);
		        	androidInfo.setPassword(password);
		        	
		        	String data = gson.toJson(androidInfo, AndroidInfo.class);
		        	
		            DefaultHttpClient httpclient = new DefaultHttpClient();
		            HttpPost httpost = new HttpPost(URI);
		            StringEntity se = new StringEntity(data);
		            httpost.setEntity(se);
		            httpost.setHeader("Content-type", "application/json");
		            HttpResponse response = httpclient.execute(httpost);
		            
		            sb = new StringBuilder();
		            try {
		                BufferedReader reader = new BufferedReader
		                		(new InputStreamReader(response.getEntity().getContent()));
		                String line = null;
		
		                while ((line = reader.readLine()) != null) {
		                    sb.append(line);
		                }
		            } catch (IOException e) { 
		            	e.printStackTrace(); 
		            } catch (Exception e) { 
		            	e.printStackTrace(); 
		            }
		       	 	responseString = sb.toString();
	        	}
	        }catch (Exception e ) {
	        	Log.e("Error", "Connection to AM Server failed!");
	        }
		}
        return null;
    }  
    
    
    /**
	 * After the execution of doInBackground function, this function is executed.
	 * In case of failure to register, the appropriate color and messages are set in EditTexts.
	 * In case of success to register, a dialog is shown in order to inform the user
	 * about the successful registration. The user is able to press "OK" button
	 * to proceed to the LoginActivity. 
     */
    protected void onPostExecute(Void unused) {
    	if(responseString != null) {
	    	if(responseString.equals("true")) {
	    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
	    		
	    		alertDialog.setTitle("Registration");
	    		alertDialog.setMessage(username + ", your request has been submitted!"
	    				+ "\nAdministrator will accept or decline your account!");
	    		alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int which) {
	    				dialog.dismiss();	
	    		        Intent i = new Intent(act.getApplicationContext(), 
	    		        		LoginActivity.class);
	    		        act.startActivity(i);
	    				act.finish();
    		        }
	    		});
	    		alertDialog.show();
			} else if(responseString.equals("false")) {
				setEditTexts(Color.RED, Color.BLACK, Color.BLACK, 
						"Username already in use", null, null);
			} else if(responseString.equals("usernameEmpty")) {
				setEditTexts(Color.RED, Color.BLACK, Color.BLACK, 
						"Do not start with space", null, null);
			} else if(responseString.equals("passwordEmpty")) {
				setEditTexts(Color.BLACK, Color.RED, Color.BLACK, 
						null, "Do not start with space", null);
			} else {
				setEditTexts(Color.BLACK, Color.RED, Color.RED, 
						null, "Passwords do not match", "Passwords do not match");
			}
    	} else {
    		setEditTexts(Color.BLACK, Color.BLACK, Color.BLACK, 
    				"No connection", "No connection", "No connection");
    	}
    }
    
    /**
     * Sets the edit texts.
     *
     * @param c1 the c1
     * @param c2 the c2
     * @param c3 the c3
     * @param e1 the e1
     * @param e2 the e2
     * @param e3 the e3
     */
    private void setEditTexts(int c1, int c2, int c3, String e1, String e2, String e3) {
    	if(usernameEditText != null) {
    		usernameEditText.setTextColor(c1);
    		usernameEditText.setError(e1);
    	}
    	if(passwordEditText != null) {
    		passwordEditText.setTextColor(c2);
    		passwordEditText.setError(e2);
    	}
    	if(passwordConfirmEditText != null) {
    		passwordConfirmEditText.setTextColor(c3);
			passwordConfirmEditText.setError(e3);
    	}
    }
}

