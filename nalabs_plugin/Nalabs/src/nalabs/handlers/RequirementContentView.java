package nalabs.handlers;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;

public class RequirementContentView {
    
	private se.addiva.nalabs.Requirement requirement;
	private Label labelTitle;
    private Text requirementText;

    public RequirementContentView(Composite parent) {
    	
    	labelTitle = new Label(parent, SWT.TITLE | SWT.BOLD);
    	labelTitle.setText("Requirement");
    	GridData labelGridData = new GridData();
        labelGridData.widthHint = 100; 
        labelGridData.heightHint = 20; 
        labelTitle.setLayoutData(labelGridData);
    	requirementText = new Text(parent, SWT.WRAP);
    	GridData textGridData = new GridData();
    	textGridData.widthHint = 500; 
    	textGridData.heightHint = 40; 
    	requirementText.setLayoutData(textGridData);
    }
    
    public void setRequirement(se.addiva.nalabs.Requirement requirement) {
    	this.requirement = requirement; 
    	requirementText.setText(this.requirement.Text);
    }
}
