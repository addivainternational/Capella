package nalabs.views;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.swt.layout.*;

public class RequirementsTableView {

	private Collection<se.addiva.nalabs_core.Requirement> nalabRequirements;
	private TableViewer tableViewer;
	private SelectedRequirementView selectedRequirementView;
	private ISelectionChangedListener selectionChangedListener = null;
	private GridData tableViewerData;

	public RequirementsTableView(Composite parent, SelectedRequirementView requirementView) {
		
		selectedRequirementView = requirementView;
		tableViewerData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayout(new GridLayout());
		parent.setLayoutData(tableViewerData);
		
		parent.addListener(SWT.Resize, arg0 -> {
			resizeListener(parent);
		});

		// Create requirements table
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createColumns();
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		// Handle initial sizing
		resizeListener(parent);
	}
	
	private void resizeListener(Composite parent) {
		org.eclipse.swt.graphics.Point size = parent.getSize();
		Composite requirementViewerComposite = selectedRequirementView.getComposite();
		GridData requirementViewerData = (GridData) requirementViewerComposite.getLayoutData();
		tableViewerData.widthHint = (int) (size.x * 0.6);
		requirementViewerData.widthHint = size.x - tableViewerData.widthHint;
		tableViewer.getTable().setLayoutData(tableViewerData);
		requirementViewerComposite.setLayoutData(requirementViewerData);
	}
	
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}
	
	public void setRequirementData(Collection<se.addiva.nalabs_core.Requirement> requirements) {
		if (selectionChangedListener != null) {
			tableViewer.removeSelectionChangedListener(selectionChangedListener);
		}
		nalabRequirements = requirements;
		tableViewer.setInput(nalabRequirements);
		selectionChangedListener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
				se.addiva.nalabs_core.Requirement requirement = (se.addiva.nalabs_core.Requirement) structuredSelection
						.getFirstElement();
				selectedRequirementView.setRequirement(requirement);
			}
		};
		tableViewer.addSelectionChangedListener(selectionChangedListener);
		if (nalabRequirements.size() > 0) {
			tableViewer.setSelection(new StructuredSelection(tableViewer.getElementAt(0)),true);
		}
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private void createColumns() {
		// Define column names and widths
		String[] titles = { "Id", "Text", "Ari Score", "Conjunctions", "Vague Phrases", "Optionality", "Subjectivity",
				"References", "References2", "Weakness", "Imperatives", "Continuances" };
		int[] bounds = { 50, 400, 80, 100, 100, 80, 80, 80, 80, 80, 100, 80 };

		// Id
		TableViewerColumn colId = createTableViewerColumn(titles[0], bounds[0]);
		colId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return r.Id;
			}
		});

		// Text
		TableViewerColumn colText = createTableViewerColumn(titles[1], bounds[1]);
		colText.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return r.Text;
			}
		});

		// AriScore
		TableViewerColumn colAriScore = createTableViewerColumn(titles[2], bounds[2]);
		colAriScore.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return String.format("%.2f", r.AriScore);
			}
		});

		// Conjunctions
		TableViewerColumn colConjunctions = createTableViewerColumn(titles[3], bounds[3]);
		colConjunctions.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Conjunctions.totalCount);
			}
		});

		// VaguePhrases
		TableViewerColumn colVaguePhrases = createTableViewerColumn(titles[4], bounds[4]);
		colVaguePhrases.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.VaguePhrases.totalCount);
			}
		});

		// Optionality
		TableViewerColumn colOptionality = createTableViewerColumn(titles[5], bounds[5]);
		colOptionality.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Optionality.totalCount);
			}
		});

		// Subjectivity
		TableViewerColumn colSubjectivity = createTableViewerColumn(titles[6], bounds[6]);
		colSubjectivity.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Subjectivity.totalCount);
			}
		});

		// References
		TableViewerColumn colReferences = createTableViewerColumn(titles[7], bounds[7]);
		colReferences.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.References.totalCount);
			}
		});
		
		// References2
		TableViewerColumn colReferences2 = createTableViewerColumn(titles[11], bounds[11]);
		colReferences2.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.References2.totalCount);
			}
		});

		// Weakness
		TableViewerColumn colWeakness = createTableViewerColumn(titles[8], bounds[8]);
		colWeakness.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Weakness.totalCount);
			}
		});

		// Imperatives
		TableViewerColumn colImperatives = createTableViewerColumn(titles[9], bounds[9]);
		colImperatives.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Imperatives.totalCount);
			}
		});

		// Continuances
		TableViewerColumn colContinuances = createTableViewerColumn(titles[10], bounds[10]);
		colContinuances.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Continuances.totalCount);
			}
		});
	}
}