package results;

import java.util.List;

import main.MainActivity;
import utility.Selected;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
 
/**
 * The Class ResultsFragment is responsible for showing all nmap results of 
 * the selected SA.
 */
public class ResultsFragment extends Fragment {
	
	/** The Activity. */
	private Activity act;
	
	/** The agent. */
	public String[] agent;
	
	/** The hash key of selected SA. */
	public String hashKey;
	
	/** The latest data. */
	public static List<String[]> data;
	
	/** The suspend flag. */
	private boolean suspend;
	
	/** The lock object. */
	private final Object lock = new Object();
	
	/** The results table. */
	private TableLayout resultsTable;
	
	/** The selected row. */
	public static Selected selected = new Selected();
	
    /**
     * This is the constructor of the class and instantiates the activity.
     *
     * @param act the act
     */
    public ResultsFragment(Activity act) {
    	this.act = act;  	
    }
	
    /**
     * This function is called when the view is created. The results table, the
     * heading and the button are initiated, and the title of the view is set to "Results".
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);        
        
        this.resultsTable = (TableLayout) rootView.findViewById(R.id.resultsTable);
       
        initHeading(rootView);
        initButton(rootView);
        setTitle("Results");
        
        return rootView;
    }
    
    /**
     * When the fragment is onCreate condition, the thread responsible for refresh is started.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        refresh();
    }
    
    /**
     * When the fragment is onPause condition, it is suspended and there is no selected row.
     */
    @Override
    public void onPause(){
    	suspend = true;
    	selected.index = -1;
    	super.onPause();
    }
    
    /**
     * When the fragment is onResume condition, it is not suspended any more and
     * the thread is notified.
     */
    @Override
    public void onResume(){    	
    	super.onResume();
    	suspend = false;
    	synchronized (lock){    lock.notify();    }
    }   

    
	/**
	 * Refresh the table data every two seconds, while the thread is not interrupted or suspended.
	 */
	private void refresh(){		
		suspend = false;
		
		Thread t = new Thread() {
			@Override
			public void run() {
			    try{			    	
			    	while(!isInterrupted()) {
			    		while(!suspend){
				    		Thread.sleep(2000);
				    		act.runOnUiThread(new Runnable() {
					    		public void run() { render();	}
				    		});
			    		}
		                synchronized (lock){
		                    try {
		                        lock.wait();
		                    } catch (InterruptedException e) {
		                        Thread.currentThread().interrupt();
		                        return;
		                    }
		                }			    		
			    	}
			    }catch (InterruptedException e) {}		    
			}
		};
		t.start();
    }			
	
	/**
	 * Render the table, by filling it with data from {@link ResultsRequest}.
	 */
	private void render(){		
		new ResultsRequest(act,selected,hashKey,resultsTable).execute();
	} 
	
	/**
	 * Inits the heading which contains information about the selected SA.
	 *
	 * @param rootView the root view
	 */
	public void initHeading(View rootView){		
		TextView msg = (TextView) rootView.findViewById(R.id.resultHeading);

		String text = 	"View Results of Software Agent with "
						+ "<br><b>Hostname: &nbsp &nbsp </b>" 
						+ agent[1] + "<br> <b> HashKey: &nbsp &nbsp </b>" 						
						+ agent[0];
		msg.setText(Html.fromHtml(text));
		msg.setTextSize(12);
	}
	
	/**
	 * Inits the button "Nmap Details". When this button is clicked, the function
	 * showDetailsDialog is called.
	 *
	 * @param resultView the result view
	 */
	private void initButton(final View resultView){
		Button nmapDetailsBtn = (Button) resultView.findViewById(R.id.nmapDetailsBtn);
		 
		nmapDetailsBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDetailsDialog(resultView);
			}
		});		 
	}
	
	/**
	 * Show details dialog.
	 * This dialog consists of a table with two columns.
	 * The first one describes the information type which is accessed after the
	 * unmarshalling of xml result in AM and the second one is the value of the 
	 * information for the specific result.
	 * 
	 * The dialog dismisses when the button "OK" is pressed.
	 * 
	 * In order to fill the table the {@link DetailsRequest} is called.
	 *
	 * @param resultView the result view
	 */
	public void showDetailsDialog(final View resultView){
		if(selected.index > -1 && resultsTable != null){
		
			TableRow row = (TableRow) resultsTable.getChildAt(selected.index);
			String idNmapJob = (String)((TextView) row.getChildAt(1)).getText();
		
			LayoutInflater layoutInflater = LayoutInflater.from(act);
			final View v = layoutInflater.inflate(R.layout.dialog_details, null);
			
			new DetailsRequest(act,idNmapJob,v).execute();
	
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
			alertDialogBuilder.setView(v);
			
			alertDialogBuilder.setCancelable(false)
			.setNegativeButton("OK",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
						
			final AlertDialog alert = alertDialogBuilder.create();
			
			alert.show();
			alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		}
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
