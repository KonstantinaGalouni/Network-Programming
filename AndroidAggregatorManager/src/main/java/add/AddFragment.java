package add;

import java.util.LinkedHashMap;

import main.MainActivity;
import utility.Selected;
import utility.Utils;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
 
/**
 * The Class AddFragment is responsible for adding nmap jobs for a selected SA.
 * It consists of the list to be inserted, info about the SA and the buttons:
 * add to list, add from history, delete from list, submit jobs.
 */
public class AddFragment extends Fragment {
	
	/** The Activity. */
	private Activity act;	
	
	/** The agent. */
	public String[] agent;
	
	/** The jobslist. */
	private TableLayout jobslist;
	
	/** The selected row. */
	public static Selected selected = new Selected();
	
	/**
	 * Instantiates the Activity of AddFragment.
	 *
	 * @param act the Activity
	 */
	public AddFragment(Activity act) {
		this.act = act;
	}	
	
    /** 
     * This function is called when the view is created. The joblist, the heading 
     * and the buttons are initiated, and the title of the view is set to "Add Jobs".  
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        
    	this.jobslist = (TableLayout) rootView.findViewById(R.id.jobsList);
    	
        initHeading(rootView);
        initButtons(rootView);
        setTitle("Add Jobs");
        
        return rootView;
    }
    
    /**
     * The super onCreate function is called.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }   
    
    
	/**
	 * Inits the heading which contains information about the selected SA.
	 *
	 * @param rootView the root view
	 */
	public void initHeading(View rootView){		
		TextView msg = (TextView) rootView.findViewById(R.id.addheading);
		String text = 	"<b> Hostname: &nbsp &nbsp </b> " 
						+ agent[1] + "<br> <b> HashKey: &nbsp &nbsp </b> " 						
						+ agent[0] + "<br>"; 
		msg.setText(Html.fromHtml(text));
		msg.setTextSize(12);
	}
	
	/**
	 * Inits the operational buttons, by calling the appropriate function when a button is clicked.
	 *
	 * @param addView the add view
	 */
	public void initButtons(final View addView){
		
		Button addListBtn = (Button) addView.findViewById(R.id.addListBtn);
		Button addHistBtn = (Button) addView.findViewById(R.id.addHistBtn);
		Button delListBtn = (Button) addView.findViewById(R.id.delListBtn);
		Button subListBtn = (Button) addView.findViewById(R.id.subListBtn);
		
		addListBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View rootView) { showAddDialog(addView); }
		});
		
		addHistBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View rootView) {	
				new AddHistory(act, agent, addView);	
			}
		});
		
		delListBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View rootView) {
				Utils.deleteSelected(act,jobslist,selected, true);
			}
		});
		
		subListBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View rootView) {	submitJobs();		}
		});
		
	} 	

	
	/**
	 * Show add dialog.
	 * This is the dialog where a new nmap job is added to the list.
	 * The user inserts the flags of the nmap job, the time to be repeated 
	 * and specifies if the job is periodic or not.
	 * 
	 * The dialog can be confirmed ("OK" button) or canceled ("Cancel" button).
	 *
	 * @param addView the add view
	 */
	public void showAddDialog(final View addView){
		LayoutInflater layoutInflater = LayoutInflater.from(act);
		final View v = layoutInflater.inflate(R.layout.dialog_add, null);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
		alertDialogBuilder.setView(v);
	
		alertDialogBuilder.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id) {
					addRow(v);					
				}
			})
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
		
		final AlertDialog alert = alertDialogBuilder.create();
		final EditText time = (EditText) v.findViewById(R.id.inputjobtime);	
		
		alert.show();
		alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);		
		
		
		time.addTextChangedListener(new Validator(time) {
		    @Override public void validate(TextView textView, String text) {
		    	try {		    		
					Integer.parseInt(text);
					textView.setTextColor(Color.BLACK);
					alert.getButton(AlertDialog.BUTTON_POSITIVE)
						 .setEnabled(true); 
				}catch (NumberFormatException e){
					textView.setTextColor(Color.RED);
					textView.setError("Time is not an integer");
					alert.getButton(AlertDialog.BUTTON_POSITIVE)
						 .setEnabled(false); 
				} 
		    }
		});
	}
	
	/**
	 * Adds the row with the new inserted nmap job to the list.
	 * If there is no -oX flag it is inserted automatically, in order to get the result in xml.
	 *
	 * @param v the View
	 */
	public void addRow(View v){
		
		String[] job = new String[4];		
		RadioGroup rg = (RadioGroup) v.findViewById(R.id.inputjobperiodic);  
		RadioButton rb = (RadioButton) v.findViewById(rg.getCheckedRadioButtonId());
		EditText jobflags = (EditText) v.findViewById(R.id.inputjobflags);
		EditText jobtime = (EditText) v.findViewById(R.id.inputjobtime);
		
		job[0] = Integer.toString(jobslist.getChildCount()-1);
		job[1] = jobflags.getText().toString();
		job[2] = rb.getText().toString();		
		job[3] = jobtime.getText().toString();			
		
		if(!(job[1].contains("-oX -"))){
			job[1]+= " -oX -";
        }
		
		Utils.addRow(act, jobslist, job, selected, 4);		

		Toast.makeText(act, "Job added!", Toast.LENGTH_SHORT).show();		
	}
	
	/**
	 * Submit joblist to AM when the "Submit list" button is clicked.
	 * The user has to confirm this, by clicking "Yes" to the dialog which is shown,
	 * otherwise (the user clicks "No"), the submission is canceled.
	 * 
	 * If user clicks "Yes" {@link AddRequest} is executed
	 */
	public void submitJobs(){		
		
		DialogInterface.OnClickListener dialogClickListener; 
		
		dialogClickListener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	
		    		TableRow row;
		    		TextView col;
		    		LinkedHashMap<String, String[]> jobs;
		    		
		    		jobs = new LinkedHashMap<String, String[]>();
		    		
		    		for(int i=2; i<jobslist.getChildCount(); i++){
		    			
		    			String[] job = new String[4];
		    			
		    			row = (TableRow) jobslist.getChildAt(i);
		    			for(int j=0; j<row.getChildCount(); j++){
		    				col = (TextView) row.getChildAt(j);
		    				job[j] = (String) col.getText();
		    			}
		    			jobs.put(job[0], job);
		    		}
		    		
		        	new AddRequest(agent[0], jobs).execute();	
		        	Utils.emptyTable(jobslist,2);
		        	
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};
		
		String msg = "Are you sure you want to add this " +
			  	  	 "list of jobs?";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder	.setMessage(msg)
				.setPositiveButton("Yes", dialogClickListener)
		    	.setNegativeButton("No", dialogClickListener)
		    	.show();
	}
	
	/**
	 * Sets the title in the ActionBar of the current Activity.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		((MainActivity) getActivity()).setActionBarTitle(title);
	}	
}