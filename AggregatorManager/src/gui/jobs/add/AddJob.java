package gui.jobs.add;

import gui.jobs.InfoSA;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * The window frame for assigning jobs to a Software Agent.
 */
@SuppressWarnings("serial")
public class AddJob extends JFrame{
	
	
	/**
	 * Instantiates a new JFrame.
	 * <p> The JFrame is consisted of :
	 *	<ul>
	 *   <li>an {@link InfoSA} JPanel.</li>
	 *	 <li>a sub-panel containing the {@link AddJobTable} 
	 *		and {@link AddJobButtons} .</li>
	 *	</ul>
	 * @param agent the Software Agent's info
	 */
	public AddJob(String[] agent){  	
		
		InfoSA infoPanel = new InfoSA(agent);
		
  		AddJobTable table = new AddJobTable();
  		AddJobButtons buttons = new AddJobButtons(table.getTable(), agent[5]);
  		
  		JPanel subPanel = new JPanel();
  		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
  		subPanel.add(table);
  		subPanel.add(buttons);  		
  		
  		JPanel mainPanel = new JPanel();
  		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
  		mainPanel.add(infoPanel);
  		mainPanel.add(subPanel);
  		
  		setTitle("Add jobs to " + agent[0]);
  		setMinimumSize(new Dimension(1100, 550));
  		setBackground( Color.black );
		getContentPane().add(mainPanel);		
		setLocationRelativeTo(null);
		setVisible( true );
	}
}
