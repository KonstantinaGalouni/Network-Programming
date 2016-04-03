package gui.jobs.delete;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.Map.Entry;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import cache.Cache;
import cache.Wrapper;

/**
 * The JPanel consisting of the periodic jobs list that can be deleted( stopped )
 * 	 of a Software Agent.
 */
@SuppressWarnings("serial")
public class DeleteJobTable extends JPanel{
	
	/** The list of periodic jobs that can be deleted( stopped ). */
	private	JTable		table;
	
	/** The scroll pane. */
	private	JScrollPane scrollPane;
	
	/** The hash key. */
	private String hashKey;

	/**
	 * Instantiates a new JTable and attaches a JScrollPane.
	 *<p> The JTable :
	 *	<ul>
	 *	 <li>shows the jobs that can be deleted.</li>
	 *   <li>shows information about  "ID", "Flags", "Periodic Flag","Time"</li>
	 *	</ul>
	 */
	public DeleteJobTable(String[] agent){
				
		setLayout(new BorderLayout());
		hashKey = agent[5];
		
		// Set columns names
		
		String columnNames[] = { "ID", "Flags", "Periodic", "Periodic Time"};
		
		// Create table based on model		
		
		DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
			@Override
			public void setValueAt(Object oValue, int row, int nColumn){}

		    @Override
		    public boolean isCellEditable(int row, int column) { 
		    	return  (column < 6) ? true : false;		    	
		    }
		};	
		 
		
		table = new JTable(tableModel);	
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
		}		
	
		// Set panel bounds
		
		Border current = this.getBorder();
		Border empty = new EmptyBorder(0, 50, 50, 0);		
		
		if (current == null){
			setBorder(empty);
		}else{
			setBorder(new CompoundBorder(empty, current));
		}
		
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER );
	}
	
	/**
	 * Fill table.
	 */
	public void fillTable(){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Wrapper<String, String[]> jobs = Cache.periodicMap.get(hashKey);
		
		if(jobs != null){
			for (Entry<String, String[]> entry : jobs.entrySet()) {
				
				String[] job = entry.getValue();
				
				if(Cache.activeMap.containsKey(hashKey) 	&&
				   Cache.activeMap.get(hashKey).containsKey(job[0])){ 	
					
					model.addRow(job);	// check if job is active
				}
			}
		}
	}
	
	/**
	 * The Class SimpleColumnBackgroundRenderer.
	 */
	class SimpleColumnBackgroundRenderer implements TableCellRenderer{
		
		/** The delegate. */
		private TableCellRenderer delegate;
		  
		/**
		 * Instantiates a new simple column background renderer.
		 *
		 * @param defaultRenderer the default renderer
		 */
		public SimpleColumnBackgroundRenderer(TableCellRenderer defaultRenderer){
			delegate = defaultRenderer;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
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
	 * Gets the table.
	 *
	 * @return the table
	 */
	public JTable getTable(){
		return table;
	}
}
