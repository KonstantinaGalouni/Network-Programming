package gui.monitor;

import gui.jobs.add.AddJob;
import gui.jobs.delete.DeleteJob;
import gui.jobs.results.MonitorAllNmapResults;
import gui.jobs.results.MonitorNmapResults;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import db.QueriesDB;

@SuppressWarnings("serial")
public class MonitorButtons extends JPanel {
	  
	private JButton addBtn;
	private JButton delBtn;
	private JButton viewBtn;
	private JButton viewAllBtn;
	private JButton termBtn;
	private JTable  table;
	
	public MonitorButtons(JTable table) {
		
		this.table = table;
		    
		String path;
		setBounds(61, 11, 81, 140);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		path = getClass().getClassLoader().getResource("add.png").getFile();
		ImageIcon addIcon = new ImageIcon(path);
		addBtn = new JButton("Add a job to an Agent", addIcon);
		addBtn.setMargin(new Insets(10,10,10,40));
		addBtn.setIconTextGap(15);	  
		addBtn.setHorizontalAlignment(SwingConstants.CENTER);

		add(addBtn);
		add(Box.createVerticalStrut(30));
		
		path = getClass().getClassLoader().getResource("delete.png").getFile();
		ImageIcon delIcon = new ImageIcon(path);
		delBtn = new JButton("Delete a job from an Agent", delIcon);
		delBtn.setMargin(new Insets(10,10,10,5));
		delBtn.setIconTextGap(15);
		delBtn.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(delBtn);		
		add(Box.createVerticalStrut(30));
		
		path = getClass().getClassLoader().getResource("view.png").getFile();
		ImageIcon viewIcon = new ImageIcon(path);
		viewBtn = new JButton("View results of an Agent", viewIcon);
		viewBtn.setMargin(new Insets(10,10,10,20));
		viewBtn.setIconTextGap(15);
		viewBtn.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(viewBtn);
		add(Box.createVerticalStrut(30));

		viewAllBtn = new JButton("View results of all Agents", viewIcon);
		viewAllBtn.setMargin(new Insets(10,10,10,15));
		viewAllBtn.setIconTextGap(15);
		viewBtn.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(viewAllBtn);
		add(Box.createVerticalStrut(30));
		
		path = getClass().getClassLoader().getResource("terminate.png").getFile();
		ImageIcon termIcon = new ImageIcon(path);
		termBtn = new JButton("Terminate an Agent", termIcon);
		termBtn.setMargin(new Insets(10,10,10,55));
		termBtn.setIconTextGap(15);
		termBtn.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(termBtn);		
		add(Box.createVerticalStrut(30));
		
		Border current = this.getBorder();
		Border empty = new EmptyBorder(75, 50, 30, 50);
		
		if (current == null){
			this.setBorder(empty);
		}else{
			this.setBorder(new CompoundBorder(empty, current));
		}
		
		addBtn.setFocusPainted(false);
		delBtn.setFocusPainted(false);
		viewBtn.setFocusPainted(false);
		viewAllBtn.setFocusPainted(false);
		termBtn.setFocusPainted(false);
		
		initListeners();		
	}
	
	private void initListeners(){
		
		addBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  
			      int row = table.getSelectedRow();		
			      
			      if(row >= 0){
			    	  String[] agent = new String[6];
			    	  for(int col=0 ; col < 6; col++){
			    		  agent[col] = (String) table.getValueAt(row,col);
			    	  }
			    	  new AddJob(agent);
			      }
			  }
		});
		
		delBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  
			      int row = table.getSelectedRow();		
			      
			      if(row >= 0){
			    	  String[] agent = new String[6];
			    	  for(int col=0 ; col < 6; col++){
			    		  agent[col] = (String) table.getValueAt(row,col);
			    	  }
			    	  new DeleteJob(agent);
			      }
			  }
		});
		
		viewBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {

			      int row = table.getSelectedRow();		
			      
			      if(row >= 0){
			    	  String[] agent = new String[6];
			    	  for(int col=0 ; col < 6; col++){
			    		  agent[col] = (String) table.getValueAt(row,col);
			    	  }
			    	  new MonitorNmapResults(agent);
			      }
			  }
		});	
		
		viewAllBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  new MonitorAllNmapResults();
			  }
		});
		
		termBtn.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent event) {
		    	  
		    	  int row = table.getSelectedRow();
			    	  
	  			  if(row >= 0){
	  				  String msg =  "Are you sure you want to terminate this" +
		    				  	  		" agent?";
		    				  
	  				  int ok = JOptionPane.showConfirmDialog(null, msg,
		    			        "Confirm Termination", JOptionPane.YES_NO_OPTION);
	  				  
	  				  if(ok == 0){
	  					  QueriesDB qdb = new QueriesDB();
		    			  String hashKey = (String)table.getValueAt(row,5);		    				  
		    			  qdb.terminateSoftwareAgent(hashKey);
	  				  }
	  			  }    			  
		     }
		});	
	}
}
