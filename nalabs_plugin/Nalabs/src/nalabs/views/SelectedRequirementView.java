package nalabs.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class SelectedRequirementView {
    
	private nalabs.core.Requirement requirement;
	private Composite composite;
	private Label labelTitle;
    private Text requirementText;

    public SelectedRequirementView(Composite parent) {
    	
    	composite = parent;
    	
    	GridData requirementViewerData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayout(new GridLayout());
		parent.setLayoutData(requirementViewerData);
		
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
    
    public Composite getComposite() {
    	return composite;
    }
    
    public void setRequirement(nalabs.core.Requirement requirement) {
    	this.requirement = requirement; 
    	requirementText.setText(this.requirement.Text);
    }
}
