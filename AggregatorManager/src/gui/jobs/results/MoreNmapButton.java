package gui.jobs.results;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;


/**
 * Class about the button for showing details about a selected nmap result.
 */
@SuppressWarnings("serial")
public class MoreNmapButton extends JPanel{
	private JTable table;
	private int column;
	private JButton moreBtn;
	
	/**
	 * The "nmap Details" button is created.
	 * 
	 * @param table	 the table where the nmap results are 
	 * @param column the column of idJobResult
	 */
	public MoreNmapButton(JTable table, int column){
		this.table 	= table;
		this.column = column;
		String path;
		setBounds(61, 11, 81, 140);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		path = getClass().getClassLoader().getResource("nmap.png").getFile();
		ImageIcon delIcon = new ImageIcon(path);
		moreBtn = new JButton("nmap Details", delIcon);
		moreBtn.setMargin(new Insets(0,10,0,15));
		moreBtn.setIconTextGap(15);	  
		
		add(moreBtn);		
		
		Border current = this.getBorder();
		Border empty = new EmptyBorder(75, 0, 50, 50);
		
		if (current == null){
			this.setBorder(empty);
		}else{
			this.setBorder(new CompoundBorder(empty, current));
		}
		
		moreBtn.setFocusPainted(false);
		
		initListeners();		
	}
	
	/**
	 * The actionListener for when the button is pushed.
	 * A new panel for details is instantiated by calling {@link NmapDetails}.
	 */
	private void initListeners(){		
		
		moreBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  
		    	  int row = table.getSelectedRow();	
		    	  
		    	  if(row >= 0){
		    		  new NmapDetails(Integer.parseInt((String) 
		    				  table.getValueAt(row,column)));
		    	  }
			  }
		});		
	}
}
