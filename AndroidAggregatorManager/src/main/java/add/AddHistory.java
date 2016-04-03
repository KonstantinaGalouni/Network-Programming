package add;

import utility.Selected;
import utility.Utils;
import android.AndroidAM.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


/**
 * The Class AddHistory is responsible for adding nmap jobs from the history (previous nmap jobs).
 */
public class AddHistory {	
	
	/** The Activity. */
	private Activity act;
	
	/** The jobslist. */
	private TableLayout jobslist;
	
	/** The historylist. */
	private TableLayout historylist;
	
	/** The selected row. */
	private Selected selected = new Selected();
	
	/**
	 * This is the constructor of the class.
	 * A new dialog is created where there is a list of all nmap jobs which were
	 * previously assigned to this SA. 
	 * 
	 * In order to get the history of the jobs {@link HistoryRequest} is executed
	 *
	 * @param act the Activity
	 * @param agent the agent
	 * @param addView the View
	 */
	public AddHistory(Activity act, String[] agent, View addView){		
    	
		LayoutInflater inflater = LayoutInflater.from(act);
		final View historyView = inflater.inflate(R.layout.dialog_addhistory,null);
		
		this.act = act;
		this.jobslist = (TableLayout) addView.findViewById(R.id.jobsList);
		this.historylist = (TableLayout) historyView.findViewById(R.id.jobsList);
				
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
		alertDialogBuilder.setView(historyView);
		
		TextView heading = (TextView) historyView.findViewById(R.id.addheading);
		String text = "History of <b> " + agent[1] +"</b>" ; 
		heading.setText(Html.fromHtml(text));
		heading.setTextSize(12);

		alertDialogBuilder.setCancelable(false)
			.setNegativeButton("Back",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();	
		
		new HistoryRequest(act, agent[0],selected, addView, historyView).execute();
		initButton(historyView);		
	}
	
	
	/**
	 * Inits the "Add to list" button.
	 *
	 * @param v the View
	 */
	public void initButton(View v){
		Button addBtn = (Button) v.findViewById(R.id.addListBtn);
		addBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { 
				moveSelected();   
			}
		});
	}
	
	/**
	 * Move selected row to the joblist of AddFragment, which will be sent to the SA.
	 */
	public void moveSelected(){		
		if(selected.index > -1){				
			TableRow row = (TableRow) historylist.getChildAt(selected.index);
			
			String[] data = new String[4];			
			data[0] = (jobslist.getChildCount()-1) + "";
			data[1] = (String) ((TextView)row.getChildAt(1)).getText();
			data[2] = (String) ((TextView)row.getChildAt(2)).getText();
			data[3] = (String) ((TextView)row.getChildAt(3)).getText();			
			
			Utils.addRow(act, jobslist, data, AddFragment.selected, 4);
			Toast.makeText(act, "Job added!", Toast.LENGTH_SHORT).show();
		}
	}	
}
