package monitor;

import java.util.LinkedHashMap;

import results.AllResultsFragment;
import results.ResultsFragment;
import main.MainActivity;
import utility.Selected;
import utility.Utils;
import delete.DeleteFragment;
import add.AddFragment;
import android.os.Bundle;
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
import android.app.FragmentTransaction;
import android.content.DialogInterface;
 
/**
 * The Class MonitorFragment consists of a list all all Software Agents and their condition.
 * This is the main Fragment of the application.
 */
public class MonitorFragment extends Fragment {
	
	/** The Activity. */
	private Activity act;
	
	/** The add fragment. */
	private AddFragment addFrag;	
	
	/** The delete fragment. */
	private DeleteFragment delFrag;
	
	/** The view results fragment. */
	private ResultsFragment viewFrag;
	
	/** The view all results fragment. */
	private AllResultsFragment viewAllFrag;
	
	/** The latest data of the monitor. */
	public static LinkedHashMap<String, String[]> data = null;
	
	/** The suspend flag. */
	private boolean suspend;
	
	/** The lock object. */
	private final Object lock = new Object();
	
	/** The monitor table. */
	private TableLayout monitor;

	/** The selected row. */
	public static Selected selected = new Selected();
	
    /**
     * This is the constructor of the class and instantiates its fields.
     *
     * @param act the Activity
     */
    public MonitorFragment(Activity act) {
    	this.act = act;  	
    	this.addFrag = new AddFragment(act);
    	this.delFrag = new DeleteFragment(act);
    	this.viewFrag = new ResultsFragment(act);
    	this.viewAllFrag = new AllResultsFragment(act);
    }
	
    /**
     * This function is called when the view is created. The table monitor and 
     * the buttons are initiated, and the title of the view is set to "Monitor".  
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      	
        View rootView = inflater.inflate(R.layout.fragment_monitor, container, false);        
        
        monitor = (TableLayout) rootView.findViewById(R.id.monitor);
        
        if(data != null){
        	Utils.renderMonitor(data,monitor,act,selected);
        }
        
        initButtons(rootView);
        setTitle("Monitor");
        
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
				    		act.runOnUiThread(new Runnable() {
					    		public void run() {  render();	}
				    		});
				    		Thread.sleep(2000);
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
	 * Render the table, by filling it with data from {@link MonitorRequest}.
	 */
	private void render(){							
		new MonitorRequest(act,selected).execute();
	} 
	
	
	/**
	 * Inits the buttons of the main fragment (Add Jobs, Delete Jobs, View, View All, Terminate),
	 * by showing the appropriate fragment or dialog.
	 * 
	 * Every button, but the "View", needs a selected row of the table (SA) in order
	 * to be clickable.
	 *
	 * @param view the view
	 */
	private void initButtons(View view){
		
		Button addBtn = (Button) view.findViewById(R.id.addBtn);
		 
		addBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				TableRow row = null;
				
				if(selected.index > -1 && monitor != null){
					
					row = (TableRow) monitor.getChildAt(selected.index);					
					
					String[] agent = new String[2];
					
					for(int i=0; i<2; i++){
						agent[i] = (String) ((TextView) row.getChildAt(i)).getText();
					}

					addFrag.agent = agent;
                      	
					FragmentTransaction ft = getFragmentManager().beginTransaction();	
					ft.replace(R.id.fragment_container, addFrag);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		            ft.addToBackStack(null);
		            ft.commit();       
				}
			}
		});
		
		
		Button delBtn = (Button) view.findViewById(R.id.delBtn);
		 
		delBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				TableRow row = null;
				
				if(selected.index > -1 && monitor != null){
					
					row = (TableRow) monitor.getChildAt(selected.index);
					String[] agent = new String[2];
					
					for(int i=0; i<2; i++){
						agent[i] = (String) ((TextView) row.getChildAt(i)).getText();
					}

					delFrag.agent = agent;
                      	
					FragmentTransaction ft = getFragmentManager().beginTransaction();	
					ft.replace(R.id.fragment_container, delFrag);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		            ft.addToBackStack(null);
		            ft.commit();       
				}
			}
		});
		
		Button viewBtn = (Button) view.findViewById(R.id.viewBtn);
		 
		viewBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				TableRow row = null;
				
				if(selected.index > -1 && monitor != null){
					
					row = (TableRow) monitor.getChildAt(selected.index);					
					
					String[] agent = new String[2];
					
					for(int i=0; i<2; i++){
						agent[i] = (String) ((TextView) row.getChildAt(i)).getText();
					}

					viewFrag.agent = agent;
					row = (TableRow)monitor.getChildAt(selected.index);	
		    		viewFrag.hashKey = (String)((TextView) row.getChildAt(0)).getText();
                      	
					FragmentTransaction ft = getFragmentManager().beginTransaction();	
					ft.replace(R.id.fragment_container, viewFrag);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		            ft.addToBackStack(null);
		            ft.commit();       
				}
				
			}
		});
		
		Button viewAllBtn = (Button) view.findViewById(R.id.viewAllBtn);
		 
		viewAllBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if(monitor != null){
					FragmentTransaction ft = getFragmentManager().beginTransaction();	
					ft.replace(R.id.fragment_container, viewAllFrag);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		            ft.addToBackStack(null);
		            ft.commit();       
				}
				
			}
		});
		
		Button termBtn = (Button) view.findViewById(R.id.terminateBtn);
		 
		termBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(selected.index > -1 && monitor != null){					
					showTerminateDialog();
				}
			}
		});
	}	
	
	
	/**
	 * Show terminate dialog.
	 * Terminate the selected SA when the "Terminate" button is clicked.
	 * The user has to confirm this, by clicking "Yes" to the dialog which is shown,
	 * otherwise (the user clicks "No"), the termination is canceled.
	 */
	public void showTerminateDialog(){
		
		DialogInterface.OnClickListener dialogClickListener; 
		
		dialogClickListener = new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:		        	
		        	TableRow row = (TableRow)monitor.getChildAt(selected.index);	
		    		String hashKey = (String)((TextView) row.getChildAt(0)).getText();
		    		
		        	new TerminateRequest(hashKey).execute();
		        	
		            break;
		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};
		
		if(selected.index > -1){
			String msg = "Are you sure you want to terminate this Software Agent?";
			
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder	.setMessage(msg)
					.setPositiveButton("Yes", dialogClickListener)
			    	.setNegativeButton("No", dialogClickListener)
			    	.show();
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