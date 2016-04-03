package utility;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;

import android.AndroidAM.R;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

/**
 * The Class Utils contains function for the appropriate rendering of TableLayout
 * and HeaderFragment of the application.
 */
public class Utils {

	/** The layout. */
	public static LayoutParams layout;
    
    /**
     * Adds the row in the TableLayout (tb) given as parameter.
     *
     * @param act the act
     * @param tb the tb
     * @param data the data
     * @param selected the selected
     * @param colbound the colbound
     */
    public static void addRow(Activity act, final TableLayout tb, String[] data,
    							final Selected selected, int colbound){
    	
    	layout = new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
		TableRow row = new TableRow(act);
		TextView col;
		
		
		for(int j=0; j<data.length; j++){
			col = new TextView(act);
			col.setLayoutParams(layout);
 			col.setBackgroundResource(R.drawable.border);
 			col.setTextColor(Color.BLACK);
 			col.setGravity(Gravity.CENTER);
 			col.setSingleLine(true);
 			col.setTextSize(11);  
 			col.setText(data[j]);
 			row.addView(col);
 			
 			if(	j==(colbound-1)){ 	break;	}
		}
		
		row.setId(tb.getChildCount());
 		row.setOnClickListener(new OnClickListener() {
 			public void onClick(View v) {	
 				Utils.renderRow(tb, v, selected );    }   
 		});
 		
 		tb.addView(row); 		
    }
    
    
    /**
     * Delete the selected row from the TableLayout (tb) given as parameter.
     *
     * @param act the act
     * @param tb the tb
     * @param selected the selected
     * @param flagIndex the flag index
     */
    public static void deleteSelected(Activity act, final TableLayout tb,
    												final Selected selected,
    												boolean flagIndex){
    	if(selected.index > -1){
			tb.removeViewAt(selected.index);
			selected.index = -1;					
			
			for(int i=0; i<tb.getChildCount(); i++){
				TableRow row = (TableRow) tb.getChildAt(i);
				row.setId(i); 							// Fix index IDs
				
				if(flagIndex == true && i > 1){
					((TextView)row.getChildAt(0)).setText("" + (i-1));
				}
			}	
		}
    }    
    
    
    /**
     * Render monitor.
     * This function renders the TableLayout (monitor) given as a parameter, which
     * is the table of the MonitorFragment.
     *
     * @param data the data
     * @param monitor the monitor
     * @param act the act
     * @param selected the selected
     */
    public static void renderMonitor(LinkedHashMap<String,String[]> data,
    												 TableLayout monitor,
    												 		Activity act,
    													Selected selected){
    	int i = 2;
 		
    	for (Entry<String, String[]> entry : data.entrySet()) {	
    		
    		String[] agent = entry.getValue();
    		
    		Utils.addRow(act, monitor, agent, selected, 5);
    		
    		TableRow row = (TableRow) monitor.getChildAt(monitor.getChildCount()-1);
    		
    		((TextView) row.getChildAt(0)).setText(agent[5]);
    		((TextView) row.getChildAt(1)).setText(agent[0]);
    		((TextView) row.getChildAt(2)).setText(agent[1]);
    		((TextView) row.getChildAt(3)).setText(agent[2]);
    		((TextView) row.getChildAt(4)).setText(agent[6]);
    		
    		if(agent[6].equals("Online")){
    			((TextView) row.getChildAt(4)).setTextColor(Color.GREEN);
			}else{
				((TextView) row.getChildAt(4)).setTextColor(Color.RED);
			}
    		if(i == selected.index){
				Utils.renderSelected(row);
			}
    		i++;	
 		} 
    }
    
    /**
     * Render results.
     * This function renders the TableLayout (results) given as a parameter, which
     * is the table of the ResultsFragment.
     *
     * @param data the data
     * @param results the results
     * @param act the act
     * @param selected the selected
     * @param latestResults the latest results
     */
    public static void renderResults(List<String[]> data, TableLayout results,
    		Activity act, Selected selected, int latestResults){
    	int i = 3;
    	TableRow row;
 		
    	for (String[] res : data) {
    		if(latestResults == 0){
    			break;
    		}
    		
    		Utils.addRow(act, results, res, selected, res.length);
    		
    		row = (TableRow) results.getChildAt(results.getChildCount()-1);
    		
    		((TextView) row.getChildAt(0)).setText(res[0]);
    		((TextView) row.getChildAt(1)).setText(res[1]);
    		((TextView) row.getChildAt(2)).setText(res[2]);

    		if(i == selected.index){
				Utils.renderSelected(row);
			}
    		
    		i++;
    		latestResults--;
 		}
    }
    
    /**
     * Render all results.
     * This function renders the TableLayout (results) given as a parameter, which
     * is the table of the AllResultsFragment.
     *
     * @param data the data
     * @param results the results
     * @param act the act
     * @param selected the selected
     * @param latestResults the latest results
     */
    public static void renderAllResults(List<String[]> data, 
    		TableLayout results, Activity act, Selected selected, int latestResults){
    	int i = 2;
    	TableRow row;
    	
    	for(String[] res : data){
    		if(latestResults == 0){
    			break;
    		}
    		
    		Utils.addRow(act, results, res, selected, res.length);
    		
    		row = (TableRow) results.getChildAt(results.getChildCount()-1);
    		
    		((TextView) row.getChildAt(0)).setText(res[0]);
    		((TextView) row.getChildAt(1)).setText(res[1]);
    		((TextView) row.getChildAt(2)).setText(res[2]);
    		((TextView) row.getChildAt(3)).setText(res[3]);

    		if(i == selected.index){
				Utils.renderSelected(row);
			}
    		
    		i++;
    		latestResults--;
    	}
    }
     
    /**
     * Empty table tb given as a parameter, without removing the header rows.
     *
     * @param tb the TableLayout
     * @param limit the limit of the header rows
     */
    public static void emptyTable(TableLayout tb, int limit){    	
    	for(int i=tb.getChildCount(); i>=limit; i--){
    		tb.removeView(tb.getChildAt(i));
    	}    	
    }
      
	/**
	 * Render row.
	 *
	 * @param tb the TableLayout
	 * @param v the View
	 * @param selected the selected row
	 */
	public static void renderRow(TableLayout tb, View v, Selected selected){		
		if(selected.index > -1){
			TableRow row = (TableRow) tb.getChildAt(selected.index);
			renderUnSelected(row);
		}
		
		TableRow row = (TableRow)v;
		renderSelected(row);
		selected.index = row.getId();	
    }
    
    /**
     * Render which row is selected.
     *
     * @param row the selected row
     */
    public static void renderSelected(TableRow row){
    	for(int i=0; i< row.getChildCount(); i++){
    		TextView col = (TextView) row.getChildAt(i);
    		col.setBackgroundResource(R.drawable.selected);
        	col.setTypeface(null, Typeface.BOLD_ITALIC);
    	}  
	} 
    
    /**
     * Render unselected row.
     *
     * @param row the unselected row
     */
    public static void renderUnSelected(TableRow row){
        for(int i=0; i< row.getChildCount(); i++){
        	TextView col = (TextView) row.getChildAt(i);       	  
        	col.setBackgroundResource(R.drawable.border);
        	col.setTypeface(null, Typeface.NORMAL);
        }  
	} 
    
    /**
     * Gets the response text.
     *
     * @param inStream the in stream
     * @return the response text
     */
    @SuppressWarnings("resource")
	public static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
