package gui.jobs.delete;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import db.QueriesDB;

/**
 * The JPanel consisting of the DeleteJob interface button.
 */
@SuppressWarnings("serial")
public class DeleteJobButtons extends JPanel{
	
	/** The Delete selected job button. */
	private JButton delBtn;
	
	/** The list of periodic jobs that can be deleted( stopped ). */
	private JTable table;	
	
	/** The hash key. */
	private String hashKey;
	
	/**
	 * Instantiates a new delete job button and adds an ActionListener.
	 *
	 * @param table the list of periodic jobs that can be deleted ( stopped ).
	 * @param hashKey the hash key
	 */
	public DeleteJobButtons(JTable table, String hashKey) {
		
		this.table = table;	  
		this.hashKey = hashKey;
		
		String path;
		setBounds(61, 11, 81, 140);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		path = getClass().getClassLoader().getResource("delete.png").getFile();
		ImageIcon delIcon = new ImageIcon(path);
		delBtn = new JButton("Delete selected job", delIcon);
		delBtn.setMargin(new Insets(0,10,0,50));
		delBtn.setIconTextGap(15);	  
		
		add(delBtn);		
		
		Border current = this.getBorder();
		Border empty = new EmptyBorder(75, 50, 50, 50);
		
		if (current == null){
			this.setBorder(empty);
		}else{
			this.setBorder(new CompoundBorder(empty, current));
		}
		
		delBtn.setFocusPainted(false);
		
		initListeners();		
	}
	
	/**
	 * Inits the listeners.
	 */
	private void initListeners(){		
		
		delBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  
		    	  int row = table.getSelectedRow();	
		    	  
		    	  if(row >= 0){
		    		  String msg = "Are you sure you want to delete this" +
		    				  	  " periodic job?";
		    				  
		    		  int ok = JOptionPane.showConfirmDialog(null, msg,
		    			        "Confirmed deletion", JOptionPane.OK_CANCEL_OPTION);
		    		  
		    		  if(ok == 0){				    	  
		    			  QueriesDB qdb = new QueriesDB();
		    			  qdb.deleteJob(hashKey, (String) table.getValueAt(row,0));
		    			  ((DefaultTableModel) table.getModel()).removeRow(row);
		    		  }
		    	  }
			  }
		});		
	}	  
}
