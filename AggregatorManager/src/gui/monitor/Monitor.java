package gui.monitor;

import gui.monitor.MonitorTabs;

import java.awt.*;
import javax.swing.*;

/**
 * The administration main window frame.
 */
@SuppressWarnings("serial")
public class Monitor extends JFrame{

	/**
	 * Instantiates a new JFrame.
	 * <p> The JFrame is consisted of :
	 *	<ul>
	 *		<li>a {@link MonitorTabs} JTabbedPane.</li>
	 *	 	<li>a sub-panel containing the {@link MonitorTable} 
	 *				and {@link MonitorButtons} .</li>
	 *   	<li>a panel containing the {@link MonitorRequests} .</li>
	 *	</ul>
	 */
	public Monitor(){
		JPanel confirmedPanel = new JPanel();
		JPanel pendingPanel = new JPanel();
		JPanel androidPanel = new JPanel();
		
		MonitorTable monitor = new MonitorTable();
		MonitorRequests requests = new MonitorRequests();
		MonitorAndroid androidRequests = new MonitorAndroid();
		
		confirmedPanel.add(monitor);
		confirmedPanel.add(new MonitorButtons(monitor.getTable()));
		confirmedPanel.setLayout(new BoxLayout(confirmedPanel, BoxLayout.X_AXIS));

		pendingPanel.add(requests);
		pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.X_AXIS));
		
		androidPanel.add(androidRequests);
		androidPanel.setLayout(new BoxLayout(androidPanel, BoxLayout.X_AXIS));
		
		MonitorTabs tabs = new MonitorTabs(confirmedPanel, pendingPanel, androidPanel);
		getContentPane().add(tabs);

		setTitle("Software Agents Panel");
		setMinimumSize(new Dimension(1300, 650));				
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible( true );		
	}
}
