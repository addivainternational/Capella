package nalabs.views;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.swt.layout.GridLayout;

public class MainView extends ViewPart {

	public static final String ID = "nqdin29hbfwpifgpnpw09fgew30"; // Unique ID for your view

	private Collection<se.addiva.nalabs.Requirement> nalabRequirements;
	private RequirementsTableView requirementsTableView;
	private SelectedRequirementView selectedRequirementView;

	@Override
	public void createPartControl(Composite parent) {
		createTableViewer(parent);
	}

	private void createTableViewer(Composite parent) {

		// Create layout and composites
		parent.setLayout(new GridLayout(2, false));

		Composite tableViewerComposite = new Composite(parent, SWT.BORDER | SWT.FILL);
		Composite requirementViewerComposite = new Composite(parent, SWT.BORDER | SWT.FILL);

		// Create view for selected requirement
		selectedRequirementView = new SelectedRequirementView(requirementViewerComposite);
		
		// Create view for the table of requirements
		requirementsTableView = new RequirementsTableView(tableViewerComposite, selectedRequirementView);
	}

	@Override
	public void setFocus() {
		requirementsTableView.setFocus();
	}

	public void setRequirementData(Collection<se.addiva.nalabs.Requirement> requirements) {
		nalabRequirements = requirements;
		requirementsTableView.setRequirementData(requirements);
	}

}