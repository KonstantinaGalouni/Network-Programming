package delete;

import main.MainActivity;
import utility.Selected;
import utility.Utils;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * The Class DeleteFragment is responsible for deleting periodic nmap jobs from a selected SA.
 * It consists of the list to be deleted, info about the SA and the button "Delete from list".
 */
public class DeleteFragment extends Fragment {
	
	/** The Activity. */
	private Activity act;	
	
	/** The agent. */
	public String[] agent;
	
	/** The jobslist to be deleted. */
	private TableLayout jobslist;
	
	/** The selected row. */
	public static Selected selected = new Selected();
	
	/**
	 * This is the constructor of the class and instantiated the activity.
	 *
	 * @param act the Activity
	 */
	public DeleteFragment(Activity act) {
		this.act = act;
	}	
	
    /**
	 *  This function is called when the view is created. The joblist, the heading and the button
     * 	are initiated, and the title of the view is set to "Delete Jobs". 
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delete, container, false);
        
    	this.jobslist = (TableLayout) rootView.findViewById(R.id.jobsList); 
    	
    	initHeading(rootView);
    	initTable(rootView);
    	initButton(rootView);
    	setTitle("Delete Jobs");
    	
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
		TextView msg = (TextView) rootView.findViewById(R.id.delheading);
		String text = 	"<b> Hostname: &nbsp &nbsp </b> " 
				+ agent[1] + "<br> <b> HashKey: </b> &nbsp &nbsp " 						
				+ agent[0] + "<br>"; 
		msg.setText(Html.fromHtml(text));
		msg.setTextSize(12);
	}
	
    
    /**
     * Inits the table of potential jobs to be deleted from a new DeleteRequest to AM.
     * 
     * In order to fill the table {@link DeleteListRequest} is executed
     *
     * @param rootView the root view
     */
    public void initTable(View rootView){
    	new DeleteListRequest(act,agent,rootView,selected).execute();
    }
    
    /**
     * Inits the button "Delete from list" and calls the deleteJob function when it is clicked.
     *
     * @param delView the del view
     */
    public void initButton(View delView){
    	Button delListBtn = (Button) delView.findViewById(R.id.delListBtn);
    	
		delListBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View rootView) {
				deleteJob();
			}
		});
    }
    
    /**
	 * Show delete dialog.
	 * This is the dialog where the user needs to confirm the deletion of a periodic nmap job or not.
	 * 
	 * If the user clicks the positive button {@link DeleteRequest} is executed for the deletion
     */
    public void deleteJob(){		
		
		DialogInterface.OnClickListener dialogClickListener; 
		
		dialogClickListener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:		        	
		    		
		        	TableRow row = (TableRow)jobslist.getChildAt(selected.index);
		        	TextView col = (TextView) row.getChildAt(0);
		        	String jobid = (String)col.getText();

		        	Utils.deleteSelected(act, jobslist, selected, false);
		        	new DeleteRequest(agent[0], jobid).execute();	
		        	
		            break;
		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};
		
		if(selected.index > -1){
			String msg = "Are you sure you want to delete this job? ";
			
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder	.setMessage(msg)
					.setPositiveButton("Yes", dialogClickListener)
			    	.setNegativeButton("No", dialogClickListener)
			    	.show();
		}
	} 
    
	/**
	 * Sets the title of the ActionBar of the current Activity.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		((MainActivity) getActivity()).setActionBarTitle(title);
	}	
}
