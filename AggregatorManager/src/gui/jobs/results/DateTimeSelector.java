package gui.jobs.results;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

/**
 * This Class is about date and time selection spinner.
 */
@SuppressWarnings("serial")
public class DateTimeSelector extends JPanel {
	private DateEditor editor;
	private SpinnerDateModel spinnermodel;
	private JSpinner spinner;
	
	/**
	 * A spinner for date and time selection is created.
	 * 
	 * @param labelName	defines if the spinner is about lower or upper limit of results
	 */
	public DateTimeSelector(String labelName){
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(0, 0, 7, 30));

		JLabel label = new JLabel(labelName);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;		//first col
		c1.gridy = 0;		//first row
		c1.anchor = GridBagConstraints.WEST;
		c1.insets = new Insets(0, 0, 5, 0);
		label.setFont(new Font("Courier New", Font.BOLD, 16));
		label.setForeground(Color.RED);	
		add(label, c1);
		
		spinner = new JSpinner();
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;		//first col
		c2.gridy = 1;		//second row
		c2.anchor = GridBagConstraints.WEST;
  		spinnermodel = new SpinnerDateModel();
  		spinnermodel.setCalendarField(Calendar.SECOND);
  		spinner.setModel(spinnermodel);
		editor = new DateEditor(spinner, "yyyy-MM-dd HH:mm:ss");
		spinner.setEditor(editor);
		add(spinner, c2);
  		
		JLabel format = new JLabel("(yyyy-MM-dd HH:mm:ss)");
		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 0;		//first col
		c3.gridy = 2;		//third row
		c3.anchor = GridBagConstraints.WEST;
		c3.insets = new Insets(2, 0, 0, 0);;
		add(format, c3);
	}
	
	/**
	 * Checks if the time and date are represented by the suitable number of digits.
	 * 
	 * @return the selected date and time of spinner (if acceptable, otherwise null)
	 */
	public String getSelected(){
		String dateRegex = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
		if(editor.getTextField().getText().matches(dateRegex)){
			return editor.getTextField().getText();
		}
  		spinnermodel = new SpinnerDateModel();
  		spinnermodel.setCalendarField(Calendar.SECOND);
  		spinner.setModel(spinnermodel);
		editor = new DateEditor(spinner, "yyyy-MM-dd HH:mm:ss");
		spinner.setEditor(editor);
		return null;
	}
}
