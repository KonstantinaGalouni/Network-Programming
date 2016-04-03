package gui.monitor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import cache.Cache;


/**
 * The JPanel for monitoring the Software Agents and their status.
 */
@SuppressWarnings("serial")
public class MonitorTable extends JPanel{

	/** The table. */
	private	JTable		table;
	
	/** The scroll pane. */
	private	JScrollPane scrollPane;
	
	/** The wait time. */
	private int waitTime;
	
	/**
	 * Instantiates a new JTable and attaches a JScrollPane.
	 *<p> The JTable :
	 *	<ul>
	 *	 <li>sets a predefined time period ( waitTime ), used for checking 
	 *		if a Software Agent is offline</li>
	 *   <li>updates the status of all Software Agents every 1 second.</li>
	 *	 <li>shows information about "Hostname", "Ip Address", "Mac Address",
				 "OS Version", "Nmap Version", "Hash Key", "Status"</li>
	 *	</ul>
	 */
	public MonitorTable(){
		
		setWaitTime();
		setLayout(new BorderLayout());

		// Set columns names
		
		String columns[] = { "Hostname", "Ip Address", "Mac Address",
				 "OS Version", "Nmap Version", "Hash Key", "Status"};
		
		
		// Create table based on model		
		
		DefaultTableModel tableModel = new DefaultTableModel(null, columns) {
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
		
		Border current = getBorder();
		Border empty = new EmptyBorder(50, 50, 50, 0);		
		
		if (current == null){
			setBorder(empty);
		}else{
			setBorder(new CompoundBorder(empty, current));
		}
		
		scrollPane = new JScrollPane(table);
		add( scrollPane, BorderLayout.CENTER);	
		
		Timer timer = new Timer(0, new ActionListener() {

		   @Override
		   public void actionPerformed(ActionEvent e) {
		      updateTable();
		   }
		});

		timer.setDelay(1000);
		timer.start();		
	}
	
	/**
	 * Fills the table.
	 */
	public void fillTable(){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		for (Entry<String, String[]> entry : Cache.acceptedMap.entrySet()) {			
			model.addRow(entry.getValue());
		}
	}
	
	/**
	 * Update table.
	 */
	public void updateTable(){	
		
		int selected = table.getSelectedRow();
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		
		for(Entry<String, String[]> entry : Cache.acceptedMap.entrySet()){
			
			String[] data = entry.getValue();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Date parsedDate = null;
		    Date current = new Date();
		    
			try {
				parsedDate = dateFormat.parse(data[7]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			long diff = (current.getTime() - parsedDate.getTime()) / 1000;

			if(diff > waitTime){
				data[6] = "Offline";
			}else{
				data[6] = "Online";
			}
			
			tableModel.addRow(data);
		}
		
		tableModel.fireTableDataChanged();	
		table.changeSelection(selected, 0, false, false);
	}
	
	/**
	 * Sets the wait time.
	 */
	@SuppressWarnings("resource")
	public void setWaitTime(){
		try {
			String path = getClass()
						 .getClassLoader()
					     .getResource("property.dat")
						 .getFile();
			
			Scanner scanner = new Scanner(new File(path));			
		    Random rand = new Random();

	        if(scanner.hasNextInt()){	        	
	        	waitTime = scanner.nextInt();
	        }else{
	        	waitTime = rand.nextInt(10) + 1;
	        }	        
	        
	        System.out.println("Wait time is " + waitTime + " seconds");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
		    
		    if(value.equals("Online")){
		    	color = new Color (60,175,60);   // Green color
	    	}else if(value.equals("Offline")){
	    		color = Color.RED;
	    	}
		    
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
