package gui.jobs.results;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import cache.Cache;
import cache.WrapperList;

/**
 * The nmap Details Class.
 */
@SuppressWarnings("serial")
public class NmapDetails extends JFrame{
	/**
	 * The panel which contains the details of a specified nmap result 
	 * found on {@link Cache#xmlMap}
	 * @param idJobResult
	 */
	public NmapDetails(int idJobResult){
		
  		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
  		
  		JPanel columnPanel = new JPanel();
  		columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));
  		columnPanel.setBorder(new EmptyBorder(50, 50, 50, 10));
  		
  		JPanel dataPanel = new JPanel();
  		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
  		dataPanel.setBorder(new EmptyBorder(50, 10, 50, 50));
  		
  		WrapperList dataValues = Cache.xmlMap.get(String.valueOf(idJobResult));
  		
  		for(int i=0; i<dataValues.size(); i++){
  			JLabel columnLabel = new JLabel(dataValues.get(i)[0]);
  			columnPanel.add(columnLabel);
  			JLabel dataLabel = new JLabel(dataValues.get(i)[1]);
  			dataPanel.add(dataLabel);
  		}
  		mainPanel.add(columnPanel);
  		mainPanel.add(dataPanel);
  		
		int vertical = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;  
        int horizontal = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;  

        JScrollPane scrollPane = new JScrollPane(mainPanel, vertical, horizontal);
		add( scrollPane, BorderLayout.CENTER);
  		
  		setTitle("Results of idJobResult " + idJobResult);
  		setMinimumSize(new Dimension(550, 550));
  		setBackground( Color.black );
		getContentPane().add(scrollPane);		
		setLocationRelativeTo(null);
		setVisible( true );
	}
}
