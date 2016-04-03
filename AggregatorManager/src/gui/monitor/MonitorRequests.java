package gui.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import cache.Cache;
import db.QueriesDB;

/**
 * This Class monitors the table of pending SoftwareAgents.
 */
@SuppressWarnings("serial")
public class MonitorRequests extends JPanel{

	private	JTable table;
	private	JScrollPane scrollPane;
	private Map<Integer, Color> rowsSelected = new HashMap<Integer, Color>();
	
	private AcceptButtonEditor accBtnEditor;
	private DeclineButtonEditor declBtnEditor;

	/**
	 * The table contains information about pending Software Agents 
	 * and is being reloaded every 5 seconds.
	 * <p>
	 * Every SoftwareAgent in the table can be accepted or declined,
	 * using the appropriate buttons on every row.
	 * <p>
	 * If a SoftwareAgent is accepted, the row turns to green, 
	 * otherwise the row turns to red. 
	 */
	public MonitorRequests(){
		this.setLayout(new BorderLayout());
		
		// Set columns names
		
		String columnNames[] = { "Hostname", "Ip Address", "Mac Address",
								 "OS Version", "Nmap Version", "HashKey", 
								 "Accept", "Decline" };
		
		// Create table based on model		
		
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public void setValueAt(Object oValue, int row, int nColumn){}
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
    	font = font.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT,
    			TextAttribute.WEIGHT_BOLD));
    	header.setFont(font);
    	
		// Create custom cell renderer
		
		TableCellRenderer defaultRenderer = table.getDefaultRenderer(Object.class);
		TableCellRenderer r = new SimpleColumnBackgroundRenderer(defaultRenderer);
		
		// Color the cells 
		
		for(int i=0; i < table.getColumnCount()-2; i++){		
			table.getColumnModel().getColumn(i).setCellRenderer(r);
		}
		
		accBtnEditor  = new AcceptButtonEditor(new JCheckBox());
		declBtnEditor = new DeclineButtonEditor(new JCheckBox());
		table.getColumn("Accept").setCellRenderer(new AcceptButtonRenderer());
	    table.getColumn("Accept").setCellEditor(accBtnEditor);
	    table.getColumn("Decline").setCellRenderer(new DeclineButtonRenderer());
	    table.getColumn("Decline").setCellEditor(declBtnEditor);
	
		// Set panel bounds
		
		Border current = this.getBorder();
		Border empty = new EmptyBorder(50, 50, 50, 50);		
		
		if (current == null){
			this.setBorder(empty);
		}else{
			this.setBorder(new CompoundBorder(empty, current));
		}
		
		scrollPane = new JScrollPane( table );
		this.add( scrollPane, BorderLayout.CENTER );
		
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
	 * The table is initially filled with the current pending SoftwareAgents 
	 * found on {@link Cache#pendingMap}.
	 * The results must satisfy the date limits.
	 */
	public void fillTable(){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		for (Entry<String, String[]> entry : Cache.pendingMap.entrySet()) {			
			model.addRow(entry.getValue());
		}
	}
	
	/**
	 * The number of rows is set to 0 and the table is filled again with
	 * the SoftwareAgents found on {@link Cache#pendingMap}.
	 * Buttons are set to their initial state and colors. 
	 */
	public void updateTable(){	
		
		int selected = table.getSelectedRow();
		
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

		tableModel.setRowCount(0);
		
		rowsSelected.clear();
		
		for(Entry<String, String[]> entry : Cache.pendingMap.entrySet()){
			String[] data = entry.getValue();
			tableModel.addRow(data);
		}
		
		accBtnEditor.getAcceptButton().setForeground(new Color (60,175,60));
		accBtnEditor.getAcceptButton().setBackground(UIManager.getColor("Button.background"));
		declBtnEditor.getDeclineButton().setForeground(Color.RED);
		declBtnEditor.getDeclineButton().setBackground(UIManager.getColor("Button.background"));
		
		tableModel.fireTableDataChanged();	
		table.changeSelection(selected, 0, false, false);
		table.repaint();
	}
	
	/**
	 * This Class is responsible for the cells of the table 
	 * except for the button cells.
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
		    
		    // Set colours for accepted-declined rows
		    
		    Color color = null;
		    
		    if(rowsSelected.containsKey(row) == false){
		    	c.setForeground(color);
		    	if(isSelected){
		    		c.setBackground(c.getBackground());
		    	}else{
		    		c.setBackground(color);
		    	}
			}else{
				c.setForeground(rowsSelected.get(row));
				c.setBackground(Color.LIGHT_GRAY);				
			}		    
		    // Set font
		    
		    Font font = c.getFont();
		    font = font.deriveFont(Collections.singletonMap
		    		(TextAttribute.WEIGHT,TextAttribute.WEIGHT_BOLD));      
		   
		    if (isSelected){
		    	font = font.deriveFont(Collections.singletonMap
		    			(TextAttribute.SIZE,12));	    	
		    }else{
		    	font = font.deriveFont(Collections.singletonMap
		    			(TextAttribute.SIZE,11));
		    }
		    
		    c.setFont(font);
		    
		    return c;
		}
	} 
	
	/**
	 * This Class is responsible for the accept button cells.
	 */
	class AcceptButtonRenderer implements TableCellRenderer {
		JButton button;
		
		public AcceptButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(final JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			button = new JButton();
			if(rowsSelected.containsKey(row) == false){				
				if (isSelected) {
					button.setForeground(new Color (60,175,60));
					button.setBackground(table.getSelectionBackground());
				}else {
					button.setForeground(new Color (60,175,60));
					button.setBackground(UIManager.getColor("Button.background"));
				}
			}else{
				button.setForeground(Color.WHITE);
				button.setBackground(Color.LIGHT_GRAY);
			}
		    button.setText("Accept");

		    return button;
		}

	}

	/**
	 * Accept buttons are created and an actionListener is set 
	 * in case they are pushed. 
	 */
	class AcceptButtonEditor extends DefaultCellEditor {
		private JButton button;

		public AcceptButtonEditor(final JCheckBox checkBox) {
		    super(checkBox);
		    button = new JButton("Accept");
		    button.setOpaque(true);
			button.setForeground(new Color (60,175,60));
			button.setBackground(UIManager.getColor("Button.background"));
			
		    button.addActionListener(new ActionListener() {			 
		    	public void actionPerformed(ActionEvent e)
		    	{
		    		if(rowsSelected.containsKey(table.getSelectedRow()) == false){
		    			rowsSelected.put(table.getSelectedRow(), new Color (60,175,60));
		    			button.setForeground(Color.WHITE);
		    			button.setBackground(Color.LIGHT_GRAY);

		    			table.repaint();
	    			
		    			QueriesDB qdb = new QueriesDB();
		    			qdb.acceptORdeclineSoftwareAgent(true, Integer.parseInt((String)
		    					table.getValueAt(table.getSelectedRow(), 5)));
		    		}
		    	}
		    });
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, 
				boolean isSelected, int row, int column) {
			return button;
		}
		
		public JButton getAcceptButton(){
			return button;
		}
	}
	
	/**
	 * This Class is responsible for the decline button cells.
	 */
	class DeclineButtonRenderer extends JButton implements TableCellRenderer {
		public DeclineButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(final JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if(rowsSelected.containsKey(row) == false){
				if (isSelected) {
					setForeground(Color.RED);
					setBackground(table.getSelectionBackground());
				}else {
					setForeground(Color.RED);
					setBackground(UIManager.getColor("Button.background"));
				}
			}else{
				setForeground(Color.WHITE);
				setBackground(Color.LIGHT_GRAY);
			}
		    setText("Decline");
		    
		    return this;
		}

	}

	/**
	 * Decline buttons are created and an actionListener is set 
	 * in case they are pushed. 
	 */
	class DeclineButtonEditor extends DefaultCellEditor {
		protected JButton button;
	
		public DeclineButtonEditor(JCheckBox checkBox) {
		    super(checkBox);
		    button = new JButton("Decline");
		    button.setOpaque(true);
			button.setForeground(Color.RED);
			button.setBackground(UIManager.getColor("Button.background"));
			
		    button.addActionListener(new ActionListener() {			 
		    	public void actionPerformed(ActionEvent e)
		    	{
		    		if(rowsSelected.containsKey(table.getSelectedRow()) == false){
		    			rowsSelected.put(table.getSelectedRow(), Color.RED);
		    			button.setForeground(Color.WHITE);
		    			button.setBackground(Color.LIGHT_GRAY);

		    			table.repaint();
	    			
		    			QueriesDB qdb = new QueriesDB();
		    			qdb.acceptORdeclineSoftwareAgent(false, Integer.parseInt((String) 
		    					table.getValueAt(table.getSelectedRow(), 5)));
		    		}
		    	}
		    });
		}
	  
	  	public Component getTableCellEditorComponent(JTable table, Object value,
	  	      boolean isSelected, int row, int column) {
	  		return button;
	  	}
	  	
		public JButton getDeclineButton(){
			return button;
		}
	}
}
