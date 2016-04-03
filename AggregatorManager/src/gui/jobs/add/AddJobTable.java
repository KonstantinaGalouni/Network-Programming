package gui.jobs.add;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.Vector;

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



/**
 * The JPanel consisting of the jobs list to be assigned to a Software Agent.
 */
@SuppressWarnings("serial")
public class AddJobTable extends JPanel{
	
	/** The table. */
	private	JTable		table;
	
	/** The scroll pane. */
	private	JScrollPane scrollPane;

	/**
	 * Instantiates a new JTable and attaches a JScrollPane.
	 *<p> The JTable :
	 *	<ul>
	 *	 <li>shows the jobs to be assigned.</li>
	 *   <li>shows information about  "ID", "Flags", "Periodic Flag","Time"</li>
	 *	</ul>
	 */
	public AddJobTable(){
				
		setLayout(new BorderLayout());

		// Set columns names
		
		String columnNames[] = { "#", "Flags", "Periodic Flag","Time"};
				
		// Create table based on model		
		
		DefaultTableModel tableModel = new DefaultTableModel(null, columnNames) {
			@SuppressWarnings("unchecked")
			@Override
			public void setValueAt(Object value, int row, int col){				
				Vector<Object> rowVector = (Vector<Object>)getDataVector().elementAt(row);
			    rowVector.setElementAt(value, col);
			    fireTableCellUpdated(row, col);
			}

		    @Override
		    public boolean isCellEditable(int row, int column) { 
		    	return  true;		    	
		    }
		};	 
		
		table = new JTable(tableModel);	
		table.setRowHeight(60);
		
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
		
		Border current = getBorder();
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
