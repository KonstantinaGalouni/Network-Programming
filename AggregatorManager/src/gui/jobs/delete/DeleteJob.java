package gui.jobs.delete;

import gui.jobs.InfoSA;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The window frame for deleting jobs from a Software Agent.
 */
@SuppressWarnings("serial")
public class DeleteJob extends JFrame{
	
	
	/**
	 * Instantiates a new JFrame.
	 * <p> The JFrame is consisted of :
	 *	<ul>
	 *   <li>an {@link InfoSA} JPanel.</li>
	 *	 <li>a sub-panel containing the {@link DeleteJobTable} 
	 *		and {@link DeleteJobButtons} .</li>
	 *	</ul>
	 * @param agent the Software Agent's info
	 */
	public DeleteJob(String[] agent){  	
		
		InfoSA infoPanel = new InfoSA(agent);
		
  		DeleteJobTable table = new DeleteJobTable(agent);
  		DeleteJobButtons buttons = new DeleteJobButtons(table.getTable(), agent[5]);
  		
  		JPanel subPanel = new JPanel();
  		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
  		subPanel.add(table);
  		subPanel.add(buttons);  		
  		
  		JPanel mainPanel = new JPanel();
  		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
  		mainPanel.add(infoPanel);
  		mainPanel.add(subPanel);
  		
  		getContentPane().add(mainPanel);
  		
  		setTitle("Delete periodic jobs from " + agent[0]);
  		setMinimumSize(new Dimension(1100, 550));
  		setBackground( Color.black );				
		setLocationRelativeTo(null);
		setVisible( true );
	}
}
