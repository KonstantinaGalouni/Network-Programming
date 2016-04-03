package add;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;


/**
 * The Class Validator is responsible for checking the nmap job input.
 */
public abstract class Validator implements TextWatcher{
	
	/** The text view. */
	private final TextView textView;

    /**
     * Instantiates a new validator.
     *
     * @param textView the text view
     */
    public Validator(TextView textView) {
        this.textView = textView;
    }

    /**
     * Validate.
     *
     * @param textView the text view
     * @param text the text of the textView
     */
    public abstract void validate(TextView textView, String text);

    final public void afterTextChanged(Editable s) {}   
    
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    
    final public void onTextChanged(CharSequence s, int start, int before, int count) {
    	String text = textView.getText().toString();
        validate(textView, text);
    }
}
