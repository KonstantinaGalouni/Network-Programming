package gui.monitor;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This Class monitors the main tabbed Panel.
 */
@SuppressWarnings("serial")
public class MonitorTabs extends JPanel{
	/**
	 * Instantiates a JTabbedPane with three tabs.
	 * 
	 * @param panel1 the panel that the first tab contains
	 * @param panel2 the panel that the second tab contains 
	 * @param panel3 the panel that the third tab contains 
	 */
	public MonitorTabs(JPanel panel1, JPanel panel2, JPanel panel3){
		super(new GridLayout(1, 1));
		
	    JTabbedPane tabbedPane = new JTabbedPane();
	        
	    tabbedPane.addTab("Confirmed Software Agents", panel1);
	    tabbedPane.addTab("Pending Software Agents", panel2);
	    tabbedPane.addTab("Pending Android Clients", panel3);
	    
	    add(tabbedPane);
	}
}
