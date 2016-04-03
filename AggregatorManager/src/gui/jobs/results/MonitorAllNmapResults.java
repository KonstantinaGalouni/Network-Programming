package gui.jobs.results;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 * A panel for the results of all Software Agents.
 */
@SuppressWarnings("serial")
public class MonitorAllNmapResults extends JFrame{
	private AllNmapResultsTable table;
	private MoreNmapButton button;
	private JPanel subPanel, subPanelHeader, mainPanel;
	private DateTimeSelector selectorFrom;
	private DateTimeSelector selectorTo;
	
	/**
	 * Instantiates:
	 *  <ul>
	 *   <li>the table of results.</li>
	 *	 <li>the spinner of upper limit for date and time selection of results.</li>
	 *	 <li>the spinner of lower limit for date and time selection of results.</li>
	 *	 <li>the button for showing details about the selected result.</li>
	 *	</ul>
	 */
	public MonitorAllNmapResults(){
		selectorFrom = new DateTimeSelector("From:");
		selectorTo = new DateTimeSelector("To:");
		JButton ok = new JButton("OK");
		ok.setMargin(new Insets(10, 60, 10, 60));
  		
  		subPanelHeader = new JPanel();
  		subPanelHeader.setLayout(new FlowLayout(FlowLayout.LEFT));
  		subPanelHeader.setBorder(new EmptyBorder(10, 0, 0 ,0));
  		subPanelHeader.add(selectorFrom);
  		subPanelHeader.add(selectorTo);
  		subPanelHeader.add(ok);
  		
  		table = new AllNmapResultsTable(null, null);
  		button = new MoreNmapButton(table.getTable(), 2);
  		
  		subPanel = new JPanel();
  		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
  		subPanel.add(table);
  		subPanel.add(button);  
  		
  		mainPanel = new JPanel();
  		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
  		mainPanel.add(subPanelHeader);
  		mainPanel.add(subPanel);
  		
  		setTitle("Results of nmapJobs for all SoftwareAgents");
  		setMinimumSize(new Dimension(1100, 550));
  		setBackground( Color.black );
		getContentPane().add(mainPanel);		
		setLocationRelativeTo(null);
		setVisible( true );
		
		getRootPane().setDefaultButton(ok);
		
		ok.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
		    {
		        //Execute when "ok" or enter is pressed
				String selectedFrom = selectorFrom.getSelected();
				String selectedTo = selectorTo.getSelected();
				
				if(selectedFrom != null	&& selectedTo != null){
					subPanel.removeAll();
					table = new AllNmapResultsTable(selectedFrom, selectedTo);
					button = new MoreNmapButton(table.getTable(), 2);
					subPanel.add(table);
					subPanel.add(button);
					subPanel.revalidate();
				}else{
					JOptionPane.showMessageDialog(null, 
							"Invalid selection of Date and Time. "
							+ "There are no changes.");
				}
		    }
	    });
	}
}
