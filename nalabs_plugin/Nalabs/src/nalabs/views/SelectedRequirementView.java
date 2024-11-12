package nalabs.views;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import se.addiva.nalabs_core.*;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class SelectedRequirementView {

	private se.addiva.nalabs_core.Requirement requirement;
	private Composite composite;
	private Label labelTitle;
	private Label requirementText;
	private TableViewer smellsTable;

	public SelectedRequirementView(Composite parent) {

		composite = parent;
		composite.setLayout(new GridLayout(2, false));

		Composite basicInfoComposite = new Composite(composite, SWT.FILL);
		GridData basicInfoCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout basicInfoCompositeLayout = new GridLayout();
		basicInfoCompositeLayout.marginLeft = 15;
		basicInfoComposite.setLayout(basicInfoCompositeLayout);
		basicInfoComposite.setLayoutData(basicInfoCompositeData);

		Composite smellsTableComposite = new Composite(composite, SWT.FILL);
		GridData smellsTableCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		smellsTableComposite.setLayout(new GridLayout());
		smellsTableComposite.setLayoutData(smellsTableCompositeData);

		parent.addListener(SWT.Resize, arg0 -> {
			org.eclipse.swt.graphics.Point size = parent.getSize();
			basicInfoCompositeData.heightHint = size.y;
			smellsTableCompositeData.heightHint = size.y;
			smellsTable.getTable().setLayoutData(smellsTableCompositeData);
		});

		labelTitle = new Label(basicInfoComposite, SWT.TITLE | SWT.BOLD);
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 220;
		labelGridData.heightHint = 30;
		labelTitle.setLayoutData(labelGridData);
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(labelTitle.getFont()).setStyle(SWT.BOLD).setHeight(12);
		labelTitle.setFont(boldDescriptor.createFont(labelTitle.getDisplay()));
		labelTitle.setText("Selected Requirement");
		requirementText = new Label(basicInfoComposite, SWT.LEFT | SWT.TOP);
		GridData textGridData = new GridData();
		textGridData.widthHint = 500;
		textGridData.heightHint = 40;
		requirementText.setLayoutData(textGridData);

		// Create smells table
		smellsTable = new TableViewer(smellsTableComposite);
		Table table = smellsTable.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createSmellsTableColumns();
		smellsTable.setContentProvider(ArrayContentProvider.getInstance());

		GridData smellsTableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayout(new GridLayout());
		parent.setLayoutData(smellsTableData);
	}

	public Composite getComposite() {
		return composite;
	}

	public void setRequirement(se.addiva.nalabs_core.Requirement requirement) {
		this.requirement = requirement;
		requirementText.setText(this.requirement.Text);
		List<SmellEntry> entries = new ArrayList<SmellEntry>();
		for (AnalyzeResult result : this.requirement.getResults()) {
			if (result.totalCount > 0) {
				for (HashMap.Entry<String, Integer> entry : result.smells.entrySet()) {
					entries.add(new SmellEntry() {
						{
							description = entry.getKey();
							count = entry.getValue();
							type = result.description;
							severityLevel = result.severityLevel.toString();
						}
					});
				}
			}
		}
		smellsTable.setInput(entries);
	}

	private TableViewerColumn createSmellsTableViewerColumn(String title, int bound) {
		TableViewerColumn viewerColumn = new TableViewerColumn(smellsTable, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private void createSmellsTableColumns() {
		// Define column names and widths
		String[] titles = { "Smell", "#Count", "Type", "Severity Level" };
		int[] bounds = { 200, 20, 100, 100 };

		// Description
		TableViewerColumn colDescription = createSmellsTableViewerColumn(titles[0], bounds[0]);
		colDescription.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SmellEntry e = (SmellEntry) element;
				return e.description;
			}
		});

		// Count
		TableViewerColumn colCount = createSmellsTableViewerColumn(titles[1], bounds[1]);
		colCount.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SmellEntry e = (SmellEntry) element;
				return Integer.toString(e.count);
			}
		});
		
		// Type
		TableViewerColumn colType = createSmellsTableViewerColumn(titles[2], bounds[2]);
		colType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SmellEntry e = (SmellEntry) element;
				return e.type;
			}
		});
		
		// Severity Level
		TableViewerColumn colSeverityLevel = createSmellsTableViewerColumn(titles[3], bounds[3]);
		colSeverityLevel.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				SmellEntry e = (SmellEntry) element;
				return e.severityLevel;
			}
		});
	}
}
