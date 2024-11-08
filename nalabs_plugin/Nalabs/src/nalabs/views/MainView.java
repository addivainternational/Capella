package nalabs.views;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
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
	
	@Override
	public void createPartControl(Composite parent) {
		// Create layout and composites
		parent.setLayout(new GridLayout(1, true));
		
		parent.addListener(SWT.Resize, arg0 -> {
			resizeListener(parent);
		});
		
		upperComposite = new Composite(parent, SWT.FILL);
		GridData upperCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		upperComposite.setLayout(new GridLayout(2, false));
		upperComposite.setLayoutData(upperCompositeData);
		
		lowerComposite = new Composite(parent, SWT.FILL);
		GridData lowerCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		lowerComposite.setLayout(new GridLayout());
		lowerComposite.setLayoutData(lowerCompositeData);

		statisticsComposite = new Composite(upperComposite, SWT.EMBEDDED | SWT.BORDER | SWT.FILL);
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
	public void setFocus() {
		requirementsTableView.setFocus();
	}

	public void setRequirementData(Collection<nalabs.core.Requirement> requirements) {
		statisticsView.setRequirementData(requirements);
		requirementsTableView.setRequirementData(requirements);
	}

	private void resizeListener(Composite parent) {
		org.eclipse.swt.graphics.Point size = parent.getSize();
		GridData upperCompositeData = (GridData) upperComposite.getLayoutData();
		GridData lowerCompositeData = (GridData) lowerComposite.getLayoutData();
		GridData statisticsCompositeData = (GridData) statisticsComposite.getLayoutData();
		GridData requirementViewerCompositeData = (GridData) requirementViewerComposite.getLayoutData();
		upperCompositeData.heightHint = (int) (size.y * 0.5);
		lowerCompositeData.heightHint = size.y - upperCompositeData.heightHint;
		statisticsCompositeData.widthHint = (int) (size.x * 0.5);
		requirementViewerCompositeData.widthHint = size.x - statisticsCompositeData.widthHint;
	}
}