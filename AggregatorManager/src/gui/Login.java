package gui;

import gui.monitor.Monitor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import db.QueriesDB;


/**
 * The Class Login.
 */
@SuppressWarnings("serial")
public class Login extends JFrame {
	
	private JLabel labelFail, labelUsername, labelPassword;
	private JTextField fieldUsername;
	private JPasswordField fieldPassword;
	private JButton cancel, ok;
	
	/**
	 * Instantiates a new login.
	 * In case of wrong input, there is an error message.
	 */
	public Login() {
		
	    JPanel mainPanel = new JPanel();
	    
	    mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		GridBagConstraints c2 = new GridBagConstraints();
		GridBagConstraints c3 = new GridBagConstraints();
		GridBagConstraints c4 = new GridBagConstraints();
		GridBagConstraints c5 = new GridBagConstraints();
		GridBagConstraints c6 = new GridBagConstraints();
		GridBagConstraints c7 = new GridBagConstraints();
		
		labelFail = new JLabel("Invalid Input. Please try again");
		c1.gridx = 0;		//first col
		c1.gridy = 0;		//first row
		c1.gridwidth = 3;	//3 columns wide
		c1.insets = new Insets(0,0,10,0);	//top, left, bottom, right
		labelFail.setForeground(Color.RED);
		labelFail.setVisible(false);
		mainPanel.add(labelFail, c1);
		
	    labelUsername = new JLabel("Username");
		c2.gridx = 0;		//first col
		c2.gridy = 1;		//second row
		c2.anchor = GridBagConstraints.WEST;
		c2.insets = new Insets(0, 0, 5, 5);	//top, left, bottom, right
		mainPanel.add(labelUsername, c2);
	    fieldUsername = new JTextField(15);
	    fieldUsername.setText("");
		c3.gridx = 1;		//second col
		c3.gridy = 1;		//second row
		c3.gridwidth = 2;	//2 columns wide
		c3.insets = new Insets(0, 20, 5, 0);	//top, left, bottom, right
		mainPanel.add(fieldUsername, c3);        
	    labelPassword = new JLabel("Password");
		c4.gridx = 0;		//first col
		c4.gridy = 2;		//third row
		c4.gridwidth = 1;
		c4.insets = new Insets(5, 0, 5, 5);	//top, left, bottom, right
		mainPanel.add(labelPassword, c4);
	    fieldPassword = new JPasswordField(15);
		c5.gridx = 1;		//second col
		c5.gridy = 2;		//third row
		c5.gridwidth = 2;	//2 columns wide
		c5.insets = new Insets(5, 20, 5, 0);	//top, left, bottom, right
		mainPanel.add(fieldPassword, c5);
	    
		cancel = new JButton("Cancel");
		c6.gridx = 1;		//second col
		c6.gridy = 3;		//fourth raw
		c6.gridwidth = 1;
		c6.insets = new Insets(20, 20, 0, 5);	//top, left, bottom, right
		mainPanel.add(cancel, c6);
	    ok = new JButton("OK");
		c7.gridx = 2;		//third col
		c7.gridy = 3;		//fourth raw
		c7.anchor = GridBagConstraints.EAST;
		c7.insets = new Insets(20,0,0,0);	//top, left, bottom, right
		ok.setPreferredSize(cancel.getPreferredSize());
		mainPanel.add(ok, c7);
		
		ok.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e)
		    {
		        //Execute when "ok" or enter is pressed
		    	String username = fieldUsername.getText();
		    	String password = new String(fieldPassword.getPassword());

		    	QueriesDB qdb = new QueriesDB();
		    	
				if(qdb.connectUser(username, password)){
		   	 		dispose();	            
		   	 		new Monitor();
			 	}else{
			 		fieldUsername.setText("");
		            fieldPassword.setText("");
		            labelFail.setVisible(true);
			 	}
		    }
	    });
		
		cancel.addActionListener(new ActionListener() {
			 
	        public void actionPerformed(ActionEvent e)
	        {
	            //Execute when "cancel" is pressed
	            fieldUsername.setText("");
	            fieldPassword.setText("");
	        }
	    });
		
		getRootPane().setDefaultButton(ok);
		
		setTitle("Login");
	    setMinimumSize(new Dimension(300,160));
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);
	    setVisible(true);
		
	    Container pane = getContentPane();
	    pane.add(mainPanel);
	}
}
