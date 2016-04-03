package gui.jobs.add;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import cache.Wrapper;
import db.QueriesDB;

/**
 * The JPanel consisting of the AddJob interface buttons.
 */
@SuppressWarnings("serial")
public class AddJobButtons extends JPanel{
	
	/** Add a job to list. */
	private JButton addBtn;
	
	/** Delete a job from list. */
	private JButton delBtn;
	
	/** Submit job list. */
	private JButton subBtn;
	
	/** The list of jobs to be assigned. */
	private JTable table;	
	
	/** The hash key. */
	private String hashKey;

	
	/**
	 * Instantiates the three JButtons and adds the respective ActionListeners.
	 *
	 * @param table the list of jobs to be assigned
	 * @param hashKey the hash key
	 */
	public AddJobButtons(JTable table, String hashKey) {
		
		this.table = table;	  
		this.hashKey = hashKey;
		
		String path;
		setBounds(61, 11, 81, 140);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		path = getClass().getClassLoader().getResource("add.png").getFile();
		ImageIcon addIcon = new ImageIcon(path);
		addBtn = new JButton("Add to list", addIcon);
		addBtn.setMargin(new Insets(0,10,0,50));
		addBtn.setIconTextGap(15);	  
		
		add(addBtn);
		add(Box.createVerticalStrut(30));
		
		path = getClass().getClassLoader().getResource("delete.png").getFile();
		ImageIcon delIcon = new ImageIcon(path);
		delBtn = new JButton("Remove from list", delIcon);
		delBtn.setMargin(new Insets(0,10,0,5));
		delBtn.setIconTextGap(15);
		
		add(delBtn);		
		add(Box.createVerticalStrut(30));
		
		path = getClass().getClassLoader().getResource("submit.png").getFile();
		ImageIcon subIcon = new ImageIcon(path);
		subBtn = new JButton("Submit jobs", subIcon);
		subBtn.setMargin(new Insets(0,10,0,40));
		subBtn.setIconTextGap(15);
		
		add(subBtn);
		
		Border current = this.getBorder();
		Border empty = new EmptyBorder(75, 50, 50, 50);
		
		if (current == null){
			this.setBorder(empty);
		}else{
			this.setBorder(new CompoundBorder(empty, current));
		}
		
		addBtn.setFocusPainted(false);
		delBtn.setFocusPainted(false);
		subBtn.setFocusPainted(false);		
		
		initListeners();		
	}
	
	/**
	 * Inits the listeners.
	 */
	private void initListeners(){
		
		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				
				JTextField flags = new JTextField();
				String[] bool = {"true", "false"};
				JComboBox<String> options = new JComboBox<>(bool);
				
				Object[] message = {
						"Flags", flags,
						"Periodic Flag", options,
						"Time in seconds"
				};
	    	  
				String time = JOptionPane.showInputDialog(
	    			  						null,
							    			message,
							    			"Add a job",
							    			JOptionPane.OK_CANCEL_OPTION);
				
				if(time != null){
					
					String[] row = new String[4]; 
			    	  
					row[0] = (table.getRowCount()+1) + "";
					row[1] = flags.getText();
					row[2] = (String) options.getSelectedItem();
					row[3] = time;		    	  
			    	  
					// Validation
		    	  
					int errors = 0;
					String errormsg = "";
					
					if(!(row[1].contains("-oX -"))){
						row[1]+= " -oX -";
	                }				
					
					try {
						Integer.parseInt(row[3]);
					}catch (NumberFormatException e) {
						errors++;
						errormsg += errors + ". Time is not an integer\n";
					} 					
					
					if(errors > 0){
						JOptionPane.showMessageDialog(null,errormsg);
					}else{
						((DefaultTableModel) table.getModel()).addRow(row);
					}
				}
			}
		});
		
		delBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  
		    	  int row = table.getSelectedRow();	
		    	  
		    	  if(row >= 0){
		    		  ((DefaultTableModel) table.getModel()).removeRow(row);
		    		  
		    		  for(int i=0; i<table.getRowCount();i++){
		    			  table.setValueAt("" + (i+1),i,0);
		    		  }		    		 
		    	  }
			  }
		});
		
		subBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  
		    	  int rows = table.getRowCount();
		    	  int cols = table.getColumnCount();		    	  
		    	  
		    	  Wrapper<String, String[]> jobs = new Wrapper<String, String[]>();
		    	  
		    	  for(int i=0;i<rows;i++){
		    		  String[] job = new String[cols];
		    		  for(int j=0;j<cols;j++){
		    			  job[j] = (String) table.getValueAt(i,j);
		    		  }
		    		  jobs.put(job[0], job);
		    	  }
		    	  
		    	  String msg = "Are you sure you want to add this" +
    				  	  " list of job(s)?";
		    	  
		    	  int ok = JOptionPane.showConfirmDialog(null, msg,
	    			        "Confirmed deletion", JOptionPane.OK_CANCEL_OPTION);
	    		  
	    		  if(ok == 0){
	    			  QueriesDB qdb = new QueriesDB();
	    			  qdb.insertJobs(hashKey, jobs);	

			    	  for(int i=0;i<rows;i++){
			    		  ((DefaultTableModel) table.getModel()).removeRow(0);
			    	  }
			    	  
	    			  msg = "All jobs have been submitted successfully!";
		    	  
	    			  JOptionPane.showMessageDialog (null, msg, "Success", 
		    			  					JOptionPane.INFORMATION_MESSAGE);
	    		  }
			  }
		});		
	}	  
}
