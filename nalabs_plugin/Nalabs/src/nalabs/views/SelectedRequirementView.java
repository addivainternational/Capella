package nalabs.views;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import se.addiva.nalabs_core.*;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class SelectedRequirementView {

	private Requirement requirement;
	private Composite composite;
	private StyledText requirementText;
	private Label ariScoreValue;
	private Label wordCountValue;
	private Label nSmellsValue;
	private TableViewer smellsTable;

	public SelectedRequirementView(Composite parent) {

		composite = parent;
		composite.setLayout(new GridLayout(2, false));

		Composite basicInfoComposite = new Composite(composite, SWT.FILL);
		GridData basicInfoCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout basicInfoCompositeLayout = new GridLayout(2, false);
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

		// Requirement text
		Label labelTitle = new Label(basicInfoComposite, SWT.BOTTOM);
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 220;
		labelGridData.heightHint = 30;
		labelTitle.setLayoutData(labelGridData);
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(labelTitle.getFont()).setStyle(SWT.BOLD);
		labelTitle.setFont(boldDescriptor.createFont(labelTitle.getDisplay()));
		labelTitle.setText("Selected Requirement");
		requirementText = new StyledText(basicInfoComposite, SWT.LEFT | SWT.BOTTOM);
		GridData textGridData = new GridData();
		textGridData.widthHint = 500;
		textGridData.heightHint = 30;
		requirementText.setLayoutData(textGridData);
		
		// ARI Score
		Label ariScoreTitle = new Label(basicInfoComposite, SWT.BOTTOM);
		GridData ariScoreTitleData = new GridData();
		ariScoreTitleData.widthHint = 220;
		ariScoreTitleData.heightHint = 30;
		ariScoreTitle.setLayoutData(ariScoreTitleData);
		FontDescriptor boldAriScoreTitleDescriptor = FontDescriptor.createFrom(ariScoreTitle.getFont()).setStyle(SWT.BOLD);
		ariScoreTitle.setFont(boldAriScoreTitleDescriptor.createFont(ariScoreTitle.getDisplay()));
		ariScoreTitle.setText("ARI Score");
		ariScoreValue = new Label(basicInfoComposite, SWT.LEFT | SWT.BOTTOM);
		GridData ariScoreValueGridData = new GridData();
		ariScoreValueGridData.widthHint = 100;
		ariScoreValueGridData.heightHint = 30;
		ariScoreValue.setLayoutData(ariScoreValueGridData);
		
		// Word Count
		Label wordCountTitle = new Label(basicInfoComposite, SWT.BOTTOM);
		GridData wordCountTitleData = new GridData();
		wordCountTitleData.widthHint = 220;
		wordCountTitleData.heightHint = 30;
		wordCountTitle.setLayoutData(wordCountTitleData);
		FontDescriptor boldWordCountTitleDescriptor = FontDescriptor.createFrom(wordCountTitle.getFont()).setStyle(SWT.BOLD);
		wordCountTitle.setFont(boldWordCountTitleDescriptor.createFont(wordCountTitle.getDisplay()));
		wordCountTitle.setText("Word Count");
		wordCountValue = new Label(basicInfoComposite, SWT.LEFT | SWT.BOTTOM);
		GridData wordCountValueGridData = new GridData();
		wordCountValueGridData.widthHint = 100;
		wordCountValueGridData.heightHint = 30;
		wordCountValue.setLayoutData(wordCountValueGridData);
		
		// Number of Smells
		Label nSmellsTitle = new Label(basicInfoComposite, SWT.BOTTOM);
		GridData nSmellsTitleData = new GridData();
		nSmellsTitleData.widthHint = 220;
		nSmellsTitleData.heightHint = 30;
		nSmellsTitle.setLayoutData(nSmellsTitleData);
		FontDescriptor boldnSmellsTitleDescriptor = FontDescriptor.createFrom(nSmellsTitle.getFont()).setStyle(SWT.BOLD);
		nSmellsTitle.setFont(boldnSmellsTitleDescriptor.createFont(nSmellsTitle.getDisplay()));
		nSmellsTitle.setText("Number of Smells");
		nSmellsValue = new Label(basicInfoComposite, SWT.LEFT | SWT.BOTTOM);
		GridData nSmellsValueGridData = new GridData();
		nSmellsValueGridData.widthHint = 100;
		nSmellsValueGridData.heightHint = 30;
		nSmellsValue.setLayoutData(nSmellsValueGridData);

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

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
		if (this.requirement == null) {
			return;
		}
		requirementText.setText(this.requirement.Text);
		ariScoreValue.setText(String.format("%.2f", requirement.AriScore));
		wordCountValue.setText(Integer.toString(this.requirement.WordCount.totalCount));
		List<SmellEntry> entries = new ArrayList<SmellEntry>();
		for (AnalyzeResult result : this.requirement.getSmellResults()) {
			if (result.totalCount > 0) {
				for (HashMap.Entry<String, SmellMatch> entry : result.smells.entrySet()) {
					entries.add(new SmellEntry() {
						{
							description = entry.getKey();
							smellMatch = entry.getValue();
							type = result.description;
							severityLevel = result.severityLevel;
						}
					});
				}
			}
		}
		nSmellsValue.setText(Integer.toString(entries.size()));
		smellsTable.setInput(entries);
		setAllSmellMatchHighlightStates(true);
	}
	
	private void setAllSmellMatchHighlightStates(boolean on) {
		for (AnalyzeResult result : this.requirement.getSmellResults()) {
			if (result.totalCount > 0) {
				for (HashMap.Entry<String, SmellMatch> entry : result.smells.entrySet()) {
					setSmellMatchHighlightState(entry.getValue(), on);
				}
			}
		}
	}
	
	private void setSmellMatchHighlightState(SmellMatch smellMatch, boolean on) {
		Color color = on ? new Color(Display.getCurrent(), 7, 246, 242) : Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		setSmellBackgroundInRequirementText(smellMatch, color);
	}
	
	private void setSmellBackgroundInRequirementText(SmellMatch smellMatch, Color backgroundColor) {
		smellMatch.forEach(smellMatchPosition -> {
			int startIndex = smellMatchPosition.getStartIndex();
			int endIndex = smellMatchPosition.getEndIndex();
			
			// Set styles for part of the text
	        StyleRange styleRange = new StyleRange();
	        styleRange.start = startIndex; 
	        styleRange.length = endIndex - startIndex;
	        styleRange.background = backgroundColor;

	        // Apply the style to the StyledText widget
	        requirementText.setStyleRange(styleRange);
		});
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
				return Integer.toString(e.smellMatch.getCount());
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
				return e.severityLevel.toString();
			}
			@Override
		    public Color getBackground(Object element) {
				SmellEntry e = (SmellEntry) element;
				return nalabs.helpers.Util.getSeverityColor(e.severityLevel);
		    }
		});
	}
}
