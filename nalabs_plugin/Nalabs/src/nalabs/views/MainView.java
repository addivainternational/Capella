package nalabs.views;

import se.addiva.nalabs_core.*;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import nalabs.helpers.ProjectInfo;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class MainView extends ViewPart {

	public static final String ID = "nqdin29hbfwpifgpnpw09fgew30"; // Unique ID for your view

	private StatisticsView statisticsView;
	private SelectedRequirementView selectedRequirementView;
	private RequirementsTableView requirementsTableView;
	private Composite upperComposite;
	private Composite lowerComposite;
	private Composite statisticsComposite;
	private Composite requirementViewerComposite;
	
	private Font labelTitleFont;
	
	@Override
	public void createPartControl(Composite parent) {
		// Create layout and composites
		parent.setLayout(new GridLayout(1, true));
		
		parent.addListener(SWT.Resize, arg0 -> {
			resizeListener(parent);
		});
		
		Label labelTitle = new Label(parent, SWT.NONE | SWT.TOP);
		GridData labelGridData = new GridData();
		labelGridData.heightHint = 40;
		labelGridData.horizontalIndent = 10;
		labelGridData.verticalIndent = 10;
		labelTitle.setLayoutData(labelGridData);
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(labelTitle.getFont()).setStyle(SWT.BOLD).setHeight(16);
		labelTitleFont = boldDescriptor.createFont(labelTitle.getDisplay());
		labelTitle.setFont(labelTitleFont);
		labelTitle.setText("Requirement Smell Detector");
		
		upperComposite = new Composite(parent, SWT.FILL);
		GridData upperCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		upperComposite.setLayout(new GridLayout(2, false));
		upperComposite.setLayoutData(upperCompositeData);
		
		lowerComposite = new Composite(parent, SWT.FILL);
		GridData lowerCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		lowerComposite.setLayout(new GridLayout());
		lowerComposite.setLayoutData(lowerCompositeData);

		statisticsComposite = new Composite(upperComposite, SWT.BORDER | SWT.FILL);
		requirementViewerComposite = new Composite(upperComposite, SWT.BORDER | SWT.FILL);
		Composite tableViewerComposite = new Composite(lowerComposite, SWT.BORDER | SWT.FILL);
		
		// Create view for statistics data
		statisticsView = new StatisticsView(statisticsComposite);

		// Create view for selected requirement
		selectedRequirementView = new SelectedRequirementView(requirementViewerComposite);
		
		// Create view for the table of requirements
		requirementsTableView = new RequirementsTableView(tableViewerComposite, selectedRequirementView);
	}
	
	@Override 
	public void dispose() {
		if (labelTitleFont != null) {
    		labelTitleFont.dispose();
    	}
		if (statisticsView != null) {
			statisticsView.dispose();
		}
		if (selectedRequirementView != null) {
			selectedRequirementView.dispose();
		}
		if (requirementsTableView != null) {
			requirementsTableView.dispose();
		}
		super.dispose();
	}


	@Override
	public void setFocus() {
		requirementsTableView.setFocus();
	}

	public void setRequirementData(Collection<Requirement> requirements, ProjectInfo projectInfo) throws ExecutionException {
		statisticsView.setRequirementData(requirements, projectInfo);
		requirementsTableView.setRequirementData(requirements);
	}

	private void resizeListener(Composite parent) {
		org.eclipse.swt.graphics.Point size = parent.getSize();
		GridData upperCompositeData = (GridData) upperComposite.getLayoutData();
		GridData lowerCompositeData = (GridData) lowerComposite.getLayoutData();
		GridData statisticsCompositeData = (GridData) statisticsComposite.getLayoutData();
		GridData requirementViewerCompositeData = (GridData) requirementViewerComposite.getLayoutData();
		upperCompositeData.heightHint = (int) (size.y * 0.7);
		lowerCompositeData.heightHint = size.y - upperCompositeData.heightHint;
		statisticsCompositeData.widthHint = (int) (size.x * 0.5);
		requirementViewerCompositeData.widthHint = size.x - statisticsCompositeData.widthHint;
	}
}