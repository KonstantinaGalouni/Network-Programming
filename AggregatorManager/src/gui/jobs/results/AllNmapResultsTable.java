package gui.jobs.results;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map.Entry;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import cache.Cache;
import cache.WrapperList;

/**
 * This Class monitors the table of results for all SoftwareAgents.
 */
@SuppressWarnings("serial")
public class AllNmapResultsTable extends JPanel{
	/** Table of results. */
	private	JTable		table;
	
	/** ScrollPane where table is placed. */
	private	JScrollPane scrollPane;
	
	/** Limits for results' date and time. */
	String timeFrom, timeTo;
	
	/**
	 * The table contains information about all results and the time they 
	 * reached to the Aggregator Manager. 
	 * The table is being reloaded every 5 seconds. 
	 * 
	 * @param timeFrom	the lower limit of date for the presented results
	 * @param timeTo	the upper limit of date for the presented results
	 */
	public AllNmapResultsTable(String timeFrom, String timeTo){
		this.timeFrom = timeFrom;
		this.timeTo   = timeTo;		
		
		setLayout(new BorderLayout());

		// Set columns names
		
		String columnNames[] = { "Hostname", "IdNmapJob", "idJobResult", "Time"};
		
		// Create table based on model		
		
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public void setValueAt(Object oValue, int row, int nColumn){}
		};	
		 
		
		table = new JTable(tableModel)
		{
			public boolean getScrollableTracksViewportWidth()
	        {
				return getPreferredSize().width < getParent().getWidth();
	        }
	     };

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		table.setRowHeight(60);
		
		fillTable();
		
		// Set readonly mode on cell focus
		
		JTextField tf = new JTextField();
		tf.setEditable(false);
		DefaultCellEditor editor = new DefaultCellEditor( tf );
		table.setDefaultEditor(Object.class, editor);
		
		// Set headers' color and font		
		
		JTableHeader header = table.getTableHeader();
		header.setForeground(Color.blue);
		
		Font font = header.getFont();
    	font = font.deriveFont(Collections
    			   .singletonMap(TextAttribute.WEIGHT,
    					   		 TextAttribute.WEIGHT_BOLD));
    	header.setFont(font);
    	
		// Create custom cell renderer
		
		TableCellRenderer defaultRenderer = table.getDefaultRenderer(Object.class);
		TableCellRenderer r = new SimpleColumnBackgroundRenderer(defaultRenderer);
		
		// Color the cells 
		
		for(int i=0; i < table.getColumnCount(); i++){		
			table.getColumnModel().getColumn(i).setCellRenderer(r);
			table.getColumnModel().getColumn(i).setPreferredWidth(160);
		}		
	
		// Set panel bounds
		
		Border current = this.getBorder();
		Border empty = new EmptyBorder(50, 50, 50, 50);		
		
		if (current == null){
			setBorder(empty);
		}else{
			setBorder(new CompoundBorder(empty, current));
		}
		
		int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;  
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;  

        scrollPane = new JScrollPane(table, vertical, horizontal);
		add( scrollPane, BorderLayout.CENTER);
		
		Timer timer = new Timer(0, new ActionListener() {

			   @Override
			   public void actionPerformed(ActionEvent e) {
			      updateTable();
			   }
			});

		timer.setDelay(5000);
		timer.start();
	}
	
	/**
	 * The table is initially filled with the current results available 
	 * on {@link Cache#resultMap}.
	 * The results must satisfy the date limits.
	 */
	public void fillTable(){
		String[] row;
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		for (Entry<String, WrapperList> entry : Cache.resultMap.entrySet()) {
			for (String[] results : entry.getValue().getList()) {
				if(validateTime(results[2], timeFrom, timeTo)){
					row = new String[4];
					row[0] = Cache.acceptedMap.get(entry.getKey())[0];
					row[1] = results[0];
					row[2] = results[1];
					row[3] = results[2];
					model.addRow(row);
				}
			}
		}
	}

	/**
	 * The number of rows is set to 0 and the table is filled again with
	 * the available results on {@link Cache#resultMap}.
	 */
	public void updateTable(){		
		int selected = table.getSelectedRow();
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

		tableModel.setRowCount(0);
		
		String[] row;
		
		for (Entry<String, WrapperList> entry : Cache.resultMap.entrySet()) {
			for (String[] results : entry.getValue().getList()) {
				if(validateTime(results[2], timeFrom, timeTo)){
					row = new String[4];
					row[0] = Cache.acceptedMap.get(entry.getKey())[0];
					row[1] = results[0];
					row[2] = results[1];
					row[3] = results[2];
					tableModel.addRow(row);
				}
			}
		}
		
		tableModel.fireTableDataChanged();	
		table.changeSelection(selected, 0, false, false);
		table.repaint();
	}
	
	/**
	 * Time and Date of results must be between the given limits.
	 * 
	 * @param time		when the results reached to the server
	 * @param timeFrom	the lower limit of date for the presented results
	 * @param timeTo	the upper limit of date for the presented results
	 * @return			true if limits are satisfied, false otherwise
	 */
	private boolean validateTime(String time, String timeFrom, String timeTo){
		Timestamp entryTime, from, to;
		entryTime = Timestamp.valueOf(time);
		if(timeFrom != null){
			from = Timestamp.valueOf(timeFrom);
			if(!entryTime.after(from)){
				return false;
			}
		}
		if(timeTo != null){
			to = Timestamp.valueOf(timeTo);
			if(!entryTime.before(to)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This Class is responsible for the cells of the table.
	 */
	class SimpleColumnBackgroundRenderer implements TableCellRenderer{
		
		private TableCellRenderer delegate;
		  
		public SimpleColumnBackgroundRenderer(TableCellRenderer defaultRenderer){
			delegate = defaultRenderer;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, 
	             boolean isSelected, boolean hasFocus, int row, int column){			
			
			
			final boolean showFocusedCellBorder = false;  
		    Component c = delegate.getTableCellRendererComponent(
				table,
				value,
				isSelected, 
				hasFocus && showFocusedCellBorder,	// No border on focused cell
				row,
				column);  
		    
		    // Set cells center alignment 
		    
		    ((JLabel) delegate).setHorizontalAlignment( SwingConstants.CENTER);
		    
		    // Set colours for Online-Offline values
		    
		    Color color = null;
		    
		    c.setForeground(color);
		    
		    // Set font
		    
		    Font font = c.getFont();
		    font = font.deriveFont(Collections
	 			   .singletonMap(TextAttribute.WEIGHT,
	 					   		 TextAttribute.WEIGHT_BOLD));      
		   
		    if (isSelected){
		    	font = font.deriveFont(Collections.singletonMap(TextAttribute.SIZE,12));	    	
		    }else{
		    	font = font.deriveFont(Collections.singletonMap(TextAttribute.SIZE,11));
		    }
		    
		    c.setFont(font);
		    
		    return c;
		}
	} 
	
	/**
	 * Returns the table.
	 * @return table of results
	 */
	public JTable getTable(){
		return table;
	}
}

