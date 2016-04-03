package gui.jobs;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * The JPanel for showing the data of a Software Agent.
 */
@SuppressWarnings("serial")
public class InfoSA extends JPanel {
	
	
	/**
	 * Instantiates a new JPanel with the Software Agent's information.
	 *
	 * @param agent the Software Agent's info
	 */
	public InfoSA(String[] agent){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(new EmptyBorder(0, 110, 0, 0));
		
		String headers = 	"<html>  <div style=\"text-align: right;\"> "  +
										 " Hostname : "  +
								"<br>    IP Address : "  +
								"<br>   Mac Address : "  +
								"<br>    OS Version : "  +
								"<br>  Nmap Version : "  +
								"<br>      Hash Key : "  +
								"</html>";
	
		JLabel headerLabel = new JLabel(headers);
		add(headerLabel);		
		
		String info = 	"<html>  <div style=\"text-align: left;\"> "  +
				 					  agent[0]  +
							"<br>"  + agent[1] +
							"<br>"  + agent[2] +
							"<br>"  + agent[3] +
							"<br>"  + agent[4] +
							"<br>"  + agent[5] + "</html>";
		

		JLabel infoLabel = new JLabel(info);
		add(infoLabel);
		
		Border current = infoLabel.getBorder();
		Border empty = new EmptyBorder(10, 10, 10, 0);		
	
		if (current == null){
			infoLabel.setBorder(empty);
		}else{
			infoLabel.setBorder(new CompoundBorder(empty, current));
		}
		
	}
	
}
