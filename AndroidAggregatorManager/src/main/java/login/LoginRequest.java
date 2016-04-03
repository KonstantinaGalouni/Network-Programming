package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import main.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.NetworkChangeReceiver;
import android.AndroidAM.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.SharedPreferences;
import android.content.Context;
import application.App;

/**
 * The Class LoginRequest is the POST request to AM in order to login to the application.
 */
public class LoginRequest extends AsyncTask<Void, Void, Void> {

	/** The StringBuilder. */
	private StringBuilder sb;
	
	/** The uri. */
	final String URI = App.ip + "/androidLogin";

	/** The Activity. */
	private Activity act;
	
	/** The username. */
	private EditText username;
	
	/** The password. */
	private EditText password;
	
	/** The response string. */
	private String responseString = null;
	
    /**
     * This is the constructor of the class and instantiates its fields.
     *
     * @param act the Activity
     */
    public LoginRequest(Activity act) {
    	this.act = act;
    	this.username = (EditText) act.findViewById(R.id.username);
    	this.password = (EditText) act.findViewById(R.id.password);
    }
	    
    /**
	 * This is the POST request which is executed in background.
	 * A json string is sent to AM which contains the AndroidInfo (username, password).
	 * A json string is returned as a response which describes the validation results of the account.
	 */
    @Override
    public Void doInBackground(Void... params) {
		if(NetworkChangeReceiver.status == 0){
			Log.e("Connection" , "There is no connection"); 		
		}else{
    	
	        try {
	        	if(username.length()==0) {
	        		responseString = "usernameEmpty";
	        	} else if(password.length()==0) {
	        		responseString = "passwordEmpty";
	        	} else{
	        	
		        	Gson gson = new GsonBuilder().create();
		        	
		        	AndroidInfo androidInfo = new AndroidInfo();
		        	androidInfo.setUsername(username.getText().toString());
		        	androidInfo.setPassword(password.getText().toString());
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
	 * In case of failure to login, the appropriate color and messages are set in EditTexts.
	 * In case of success to login, the account info is stored in SharedPreferences,
	 * so as the user remains connected until the logout action and the MainActivity is launched. 
     */
    protected void onPostExecute(Void unused) {
    	if(responseString != null) {
    		
	    	if(responseString.equals("usernameEmpty")) {
	    		setEditTexts(Color.RED, Color.BLACK, "Empty Username", null);
	    	} else if(responseString.equals("passwordEmpty")) {
	    		setEditTexts(Color.BLACK, Color.RED, null, "Empty Password");
	    	} else if(responseString.equals("connected")) {
	    		setEditTexts(Color.BLACK, Color.BLACK, null, null);
	    		
	    		SharedPreferences sharedpreferences = act.getSharedPreferences
	    				("MyPrefs", Context.MODE_PRIVATE);
	            SharedPreferences.Editor editor = sharedpreferences.edit();
	            
	            editor.putString("username", username.getText().toString());
	            editor.commit();
	
				Intent i = new Intent(act.getApplicationContext(), MainActivity.class);
				act.startActivity(i);
				act.finish();
			} else if(responseString.equals("wrongUsername")) {
				setEditTexts(Color.RED, Color.BLACK, "Username does not exist "
						+ "or has been declined", null);
			} else if(responseString.equals("pending")) {
				setEditTexts(Color.RED, Color.RED, "This account has not been confirmed yet", null);
			} else {
				setEditTexts(Color.BLACK, Color.RED, null, "Wrong Password");
			}	    	
    	} else {
    		setEditTexts(Color.BLACK, Color.BLACK, "No connection", "No connection");
    	}
    }	    
    
    /**
     * Sets the color and the error of edit texts.
     *
     * @param c1 the c1
     * @param c2 the c2
     * @param e1 the e1
     * @param e2 the e2
     */
    private void setEditTexts(int c1, int c2, String e1, String e2) {
		username.setTextColor(c1);
		password.setTextColor(c2);
		username.setError(e1);
		password.setError(e2);
    }
}
