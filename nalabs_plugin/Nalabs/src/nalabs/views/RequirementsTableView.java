package nalabs.views;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import se.addiva.nalabs_core.Requirement;

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

	private <T> TableViewerColumn createTableViewerColumn(String title, int bound, Function<se.addiva.nalabs_core.Requirement, T> valueProvider, 
            Comparator<T> comparator) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(new SelectionAdapter() {
            private boolean ascending = true;

            @Override
            public void widgetSelected(SelectionEvent e) {
            	tableViewer.setComparator(new ViewerComparator() {
                    @Override
                    public int compare(Viewer v, Object e1, Object e2) {
                        T value1 = valueProvider.apply((se.addiva.nalabs_core.Requirement) e1);
                        T value2 = valueProvider.apply((se.addiva.nalabs_core.Requirement) e2);
                        return ascending ? comparator.compare(value1, value2) : comparator.compare(value2, value1);
                    }
                });
                ascending = !ascending;
                tableViewer.refresh();
            }
        });
		return viewerColumn;
	}

	private void createColumns() {
		// Define column names and widths
		String[] titles = { "Id", "Text", "Ari Score", "Word Count", "Conjunctions", "Vague Phrases", "Optionality", "Subjectivity",
				"References", "References2", "Weakness", "Imperatives", "Continuances" };
		int[] bounds = { 50, 400, 80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };
		// Id
		TableViewerColumn colId = createTableViewerColumn(titles[0], bounds[0], (se.addiva.nalabs_core.Requirement req) -> req.Id, 
				Comparator.comparing(reqId -> reqId));
		colId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return r.Id;
			}
		});

		// Text
		TableViewerColumn colText = createTableViewerColumn(titles[1], bounds[1], (se.addiva.nalabs_core.Requirement req) -> req.Text, 
				Comparator.comparing(reqText -> reqText));
		colText.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return r.Text;
			}
		});

		// AriScore
		TableViewerColumn colAriScore = createTableViewerColumn(titles[2], bounds[2], (se.addiva.nalabs_core.Requirement req) -> req.AriScore, 
				Comparator.comparingDouble(reqAriScore -> reqAriScore));
		colAriScore.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return String.format("%.2f", r.AriScore);
			}
		});
		
		// Word Count
		TableViewerColumn colWordCount = createTableViewerColumn(titles[3], bounds[3], (se.addiva.nalabs_core.Requirement req) -> req.WordCount.totalCount, 
				Comparator.comparingInt(reqWordCount -> reqWordCount));
		colWordCount.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.WordCount.totalCount);
			}
		});

		// Conjunctions
		TableViewerColumn colConjunctions = createTableViewerColumn(titles[4], bounds[4], (se.addiva.nalabs_core.Requirement req) -> req.Conjunctions.totalCount, 
				Comparator.comparingInt(c -> c));
		colConjunctions.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Conjunctions.totalCount);
			}
		});

		// VaguePhrases
		TableViewerColumn colVaguePhrases = createTableViewerColumn(titles[5], bounds[5], (se.addiva.nalabs_core.Requirement req) -> req.VaguePhrases.totalCount, 
				Comparator.comparingInt(c -> c));
		colVaguePhrases.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.VaguePhrases.totalCount);
			}
		});

		// Optionality
		TableViewerColumn colOptionality = createTableViewerColumn(titles[6], bounds[6], (se.addiva.nalabs_core.Requirement req) -> req.Optionality.totalCount, 
				Comparator.comparingInt(c -> c));
		colOptionality.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Optionality.totalCount);
			}
		});

		// Subjectivity
		TableViewerColumn colSubjectivity = createTableViewerColumn(titles[7], bounds[7], (se.addiva.nalabs_core.Requirement req) -> req.Subjectivity.totalCount, 
				Comparator.comparingInt(c -> c));
		colSubjectivity.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Subjectivity.totalCount);
			}
		});

		// References Internal
		TableViewerColumn colReferencesInternal = createTableViewerColumn(titles[8], bounds[8], (se.addiva.nalabs_core.Requirement req) -> req.ReferencesInternal.totalCount, 
				Comparator.comparingInt(c -> c));
		colReferencesInternal.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.ReferencesInternal.totalCount);
			}
		});
		
		// References External
		TableViewerColumn colReferencesExternal = createTableViewerColumn(titles[9], bounds[9], (se.addiva.nalabs_core.Requirement req) -> req.ReferencesExternal.totalCount, 
				Comparator.comparingInt(c -> c));
		colReferencesExternal.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.ReferencesExternal.totalCount);
			}
		});

		// Weakness
		TableViewerColumn colWeakness = createTableViewerColumn(titles[10], bounds[10], (se.addiva.nalabs_core.Requirement req) -> req.Weakness.totalCount, 
				Comparator.comparingInt(c -> c));
		colWeakness.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Weakness.totalCount);
			}
		});

		// Imperatives
		TableViewerColumn colImperatives = createTableViewerColumn(titles[11], bounds[11], (se.addiva.nalabs_core.Requirement req) -> req.Imperatives.totalCount, 
				Comparator.comparingInt(c -> c));
		colImperatives.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Imperatives.totalCount);
			}
		});

		// Continuances
		TableViewerColumn colContinuances = createTableViewerColumn(titles[12], bounds[12], (se.addiva.nalabs_core.Requirement req) -> req.Continuances.totalCount, 
				Comparator.comparingInt(c -> c));
		colContinuances.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				se.addiva.nalabs_core.Requirement r = (se.addiva.nalabs_core.Requirement) element;
				return Integer.toString(r.Continuances.totalCount);
			}
		});
	}
}