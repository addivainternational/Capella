package nalabs.views;

import se.addiva.nalabs_core.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.StyleRange;

import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.swt.layout.*;

public class RequirementsTableView {

	private Collection<Requirement> nalabRequirements;
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
		
		// Text searcher component
        Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
        searchText.setMessage("Search requirement text...");
        searchText.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));

		// Create requirements table
		tableViewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createColumns();
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		RequirementTextFilter requirementTextFilter = new RequirementTextFilter(searchText::getText);
        tableViewer.addFilter(requirementTextFilter);
        
        searchText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                tableViewer.refresh();
                tableViewer.setSelection(new StructuredSelection(tableViewer.getElementAt(0)),true);
            }
        });
		
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
	
	public void setRequirementData(Collection<Requirement> requirements) {
		if (selectionChangedListener != null) {
			tableViewer.removeSelectionChangedListener(selectionChangedListener);
		}
		nalabRequirements = requirements;
		tableViewer.setInput(nalabRequirements);
		selectionChangedListener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection structuredSelection = (IStructuredSelection) event.getSelection();
				Requirement requirement = (Requirement) structuredSelection
						.getFirstElement();
				selectedRequirementView.setRequirement(requirement);
			}
		};
		tableViewer.addSelectionChangedListener(selectionChangedListener);
		if (nalabRequirements.size() > 0) {
			tableViewer.setSelection(new StructuredSelection(tableViewer.getElementAt(0)),true);
		}
	}

	private <T> TableViewerColumn createTableViewerColumn(String title, int bound, Function<Requirement, T> valueProvider, 
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
                        T value1 = valueProvider.apply((Requirement) e1);
                        T value2 = valueProvider.apply((Requirement) e2);
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
		String[] titles = { "Id", "Text", "Ari Score", "Word Count", "Total Smells", "Severity", "#Conjunctions", "#Vague Phrases", "#Optionalities", "#Subjectivities",
				"#Internal References", "#External References", "#Weaknesses", "#Imperatives", "#Continuances" };
		int[] bounds = { 50, 400, 80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };
		// Id
		TableViewerColumn colId = createTableViewerColumn(titles[0], bounds[0], (Requirement req) -> req.id, 
				Comparator.comparing(reqId -> reqId));
		colId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(r.id);
			}
		});

		// Text
		TableViewerColumn colText = createTableViewerColumn(titles[1], bounds[1], (Requirement req) -> req.text, 
				Comparator.comparing(reqText -> reqText));
		colText.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(r.text);
			}
		});

		// AriScore
		TableViewerColumn colAriScore = createTableViewerColumn(titles[2], bounds[2], (Requirement req) -> req.ariScore, 
				Comparator.comparingDouble(reqAriScore -> reqAriScore));
		colAriScore.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(String.format("%.2f", r.ariScore));
			}
		});
		
		// Word Count
		TableViewerColumn colWordCount = createTableViewerColumn(titles[3], bounds[3], (Requirement req) -> req.wordCount.totalCount, 
				Comparator.comparingInt(reqWordCount -> reqWordCount));
		colWordCount.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.wordCount.totalCount));
			}
		});
		
		// Total smells 
		TableViewerColumn colTotalSmells = createTableViewerColumn(titles[4], bounds[4], (Requirement req) -> req.totalSmells, 
				Comparator.comparingInt(c -> c));
		colTotalSmells.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.totalSmells));
			}
		});
		
		// Severity
		TableViewerColumn colSeverity = createTableViewerColumn(titles[5], bounds[5], (Requirement req) -> req.severityLevel, 
				Comparator.comparing(c -> c));
		colSeverity.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(r.severityLevel.toString());
				cell.setBackground(nalabs.helpers.Util.getSeverityColor(r.severityLevel));
				if (r.severityLevel == SeverityLevel.Critical) {
					cell.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				} else {
					cell.setForeground(null);
				}
			}
		});

		// Conjunctions
		TableViewerColumn colConjunctions = createTableViewerColumn(titles[6], bounds[6], (Requirement req) -> req.conjunctions.totalCount, 
				Comparator.comparingInt(c -> c));
		colConjunctions.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.conjunctions.totalCount));
			}
		});

		// VaguePhrases
		TableViewerColumn colVaguePhrases = createTableViewerColumn(titles[7], bounds[7], (Requirement req) -> req.vaguePhrases.totalCount, 
				Comparator.comparingInt(c -> c));
		colVaguePhrases.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.vaguePhrases.totalCount));
			}
		});

		// Optionality
		TableViewerColumn colOptionality = createTableViewerColumn(titles[8], bounds[8], (Requirement req) -> req.optionality.totalCount, 
				Comparator.comparingInt(c -> c));
		colOptionality.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.optionality.totalCount));
			}
		});

		// Subjectivity
		TableViewerColumn colSubjectivity = createTableViewerColumn(titles[9], bounds[9], (Requirement req) -> req.subjectivity.totalCount, 
				Comparator.comparingInt(c -> c));
		colSubjectivity.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.subjectivity.totalCount));
			}
		});

		// References Internal
		TableViewerColumn colReferencesInternal = createTableViewerColumn(titles[10], bounds[10], (Requirement req) -> req.referencesInternal.totalCount, 
				Comparator.comparingInt(c -> c));
		colReferencesInternal.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.referencesInternal.totalCount));
			}
		});
		
		// References External
		TableViewerColumn colReferencesExternal = createTableViewerColumn(titles[11], bounds[11], (Requirement req) -> req.referencesExternal.totalCount, 
				Comparator.comparingInt(c -> c));
		colReferencesExternal.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.referencesExternal.totalCount));
			}
		});

		// Weakness
		TableViewerColumn colWeakness = createTableViewerColumn(titles[12], bounds[12], (Requirement req) -> req.weakness.totalCount, 
				Comparator.comparingInt(c -> c));
		colWeakness.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.weakness.totalCount));
			}
		});

		// Imperatives
		TableViewerColumn colImperatives = createTableViewerColumn(titles[13], bounds[13], (Requirement req) -> req.imperatives.totalCount, 
				Comparator.comparingInt(c -> c));
		colImperatives.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.imperatives.totalCount));
			}
		});

		// Continuances
		TableViewerColumn colContinuances = createTableViewerColumn(titles[14], bounds[14], (Requirement req) -> req.continuances.totalCount, 
				Comparator.comparingInt(c -> c));
		colContinuances.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Requirement r = (Requirement) cell.getElement();
				cell.setText(Integer.toString(r.continuances.totalCount));
			}
		});
	}
	
	static class RequirementTextFilter extends ViewerFilter {
        private final Supplier<String> searchTextSupplier;

        public RequirementTextFilter(Supplier<String> searchTextSupplier) {
            this.searchTextSupplier = searchTextSupplier;
        }

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            Requirement requirement = (Requirement) element;
            String searchText = searchTextSupplier.get().toLowerCase();
            return searchText.isEmpty() || requirement.text.toLowerCase().contains(searchText);
        }
    }
}