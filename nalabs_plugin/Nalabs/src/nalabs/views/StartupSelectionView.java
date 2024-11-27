package nalabs.views;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

public class StartupSelectionView extends Dialog {

	private boolean useDefaultTextField;
	private Button textButton;
	private Font labelTitleFont;
	
    public StartupSelectionView(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout(1, false); 
        gridLayout.marginHeight = 20;
        gridLayout.marginWidth = 30;
        container.setLayout(gridLayout);

        Label labelTitle = new Label(container, SWT.NONE | SWT.TOP);
		GridData labelGridData = new GridData();
		labelGridData.heightHint = 40;
		labelTitle.setLayoutData(labelGridData);
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(labelTitle.getFont()).setStyle(SWT.BOLD).setHeight(16);
		labelTitleFont = boldDescriptor.createFont(labelTitle.getDisplay());
		labelTitle.setFont(labelTitleFont);
		labelTitle.setText("Welcome to the Requirement Smell Detector");
		
		Label infoLabel = new Label(container, SWT.NONE);
		infoLabel.setText("Which requirement field do you want to use as the requirement text?");
		
		Composite radioButtonsComposite = new Composite(container, SWT.NONE);
		radioButtonsComposite.setLayout(new GridLayout(1, false));

	    Button longNameButton = new Button(radioButtonsComposite, SWT.RADIO);
	    longNameButton.setText("Long Name");
	    longNameButton.setSelection(false);

	    textButton = new Button(radioButtonsComposite, SWT.RADIO);
	    textButton.setText("Text field");
	    textButton.setSelection(true);
	    
	    Label recommendedChoiceLabel = new Label(container, SWT.NONE);
	    recommendedChoiceLabel.setText("Please note that 'Text field' is the recommended choice.");
	    
	    Label elseLongNameLabel = new Label(container, SWT.NONE);
	    elseLongNameLabel.setText("However, if 'Long Name' has been used for the set of requirements, then use that.");

        return container;
    }
    
    @Override 
    public boolean close() {
    	useDefaultTextField = textButton.getSelection();
    	if (labelTitleFont != null) {
    		labelTitleFont.dispose();
    	}
    	return super.close();
    }
    
    public boolean getUseDefaultTextField() {
    	return useDefaultTextField;
    }

}