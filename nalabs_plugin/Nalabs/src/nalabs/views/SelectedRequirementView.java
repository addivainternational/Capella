package nalabs.views;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import nalabs.helpers.Util;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

import se.addiva.nalabs_core.*;

import org.eclipse.swt.layout.FillLayout;
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
	
	private Font labelTitleFont;
	private Font ariScoreTitleFont;
	private Font wordCountTitleFont;
	private Font nSmellsTitleFont;
	
	private HashMap<SeverityLevel, Color> severityLevelColors = new HashMap<SeverityLevel, Color>();
	private Color smellColor;

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
		labelTitleFont = boldDescriptor.createFont(labelTitle.getDisplay());
		labelTitle.setFont(labelTitleFont);
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
		ariScoreTitleFont = boldAriScoreTitleDescriptor.createFont(ariScoreTitle.getDisplay());
		ariScoreTitle.setFont(ariScoreTitleFont);
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
		wordCountTitleFont = boldWordCountTitleDescriptor.createFont(wordCountTitle.getDisplay());
		wordCountTitle.setFont(wordCountTitleFont);
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
		nSmellsTitleFont = boldnSmellsTitleDescriptor.createFont(nSmellsTitle.getDisplay());
		nSmellsTitle.setFont(nSmellsTitleFont);
		nSmellsTitle.setText("Number of Smells");
		nSmellsValue = new Label(basicInfoComposite, SWT.LEFT | SWT.BOTTOM);
		GridData nSmellsValueGridData = new GridData();
		nSmellsValueGridData.widthHint = 100;
		nSmellsValueGridData.heightHint = 30;
		nSmellsValue.setLayoutData(nSmellsValueGridData);

		// Create smells table
		smellsTable = new TableViewer(smellsTableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		ColumnViewerToolTipSupport.enableFor(smellsTable, ToolTip.NO_RECREATE);
		
		Table table = smellsTable.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createSmellsTableColumns();
		smellsTable.setContentProvider(ArrayContentProvider.getInstance());

		GridData smellsTableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayout(new GridLayout());
		parent.setLayoutData(smellsTableData);
		
		addCustomTooltipSupport(smellsTable);
		
		severityLevelColors.put(SeverityLevel.Critical, nalabs.helpers.Util.getSeverityColor(SeverityLevel.Critical));
		severityLevelColors.put(SeverityLevel.High, nalabs.helpers.Util.getSeverityColor(SeverityLevel.High));
		severityLevelColors.put(SeverityLevel.Moderate, nalabs.helpers.Util.getSeverityColor(SeverityLevel.Moderate));
		severityLevelColors.put(SeverityLevel.Low, nalabs.helpers.Util.getSeverityColor(SeverityLevel.Low));
		severityLevelColors.put(SeverityLevel.None, nalabs.helpers.Util.getSeverityColor(SeverityLevel.None));
		
		smellColor = Util.getSmellColor();
	}
	
	public void dispose() {
		if (labelTitleFont != null) {
			labelTitleFont.dispose();
		}
		if (ariScoreTitleFont != null) {
			ariScoreTitleFont.dispose();
		}
		if (wordCountTitleFont != null) {
			wordCountTitleFont.dispose();
		}
		if (nSmellsTitleFont != null) {
			nSmellsTitleFont.dispose();
		}
		if (smellColor != null) {
			smellColor.dispose();
		}
		severityLevelColors.forEach((s, c) -> {
			c.dispose();
		});
	}

	public Composite getComposite() {
		return composite;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
		if (this.requirement == null) {
			return;
		}
		requirementText.setText(this.requirement.text);
		ariScoreValue.setText(String.format("%.2f", requirement.ariScore));
		wordCountValue.setText(Integer.toString(this.requirement.wordCount.totalCount));
		List<SmellEntry> entries = new ArrayList<SmellEntry>();
		for (AnalyzeResult result : this.requirement.getSmellResults()) {
			if (result.totalCount > 0) {
				for (HashMap.Entry<String, SmellMatch> entry : result.smells.entrySet()) {
					entries.add(new SmellEntry() {
						{
							description = entry.getKey();
							smellMatch = entry.getValue();
							type = result.type;
							typeDescription = result.typeDescription;
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
		Color color = on ? smellColor : Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		setSmellHighlightColorInRequirementText(smellMatch, color);
	}
	
	private void setSmellHighlightColorInRequirementText(SmellMatch smellMatch, Color color) {
		smellMatch.forEach(smellMatchPosition -> {
			int startIndex = smellMatchPosition.getStartIndex();
			int endIndex = smellMatchPosition.getEndIndex();
			
			// Set styles for part of the text
	        StyleRange styleRange = new StyleRange();
	        styleRange.start = startIndex; 
	        styleRange.length = endIndex - startIndex;
	        styleRange.background = color;

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
			public void update(ViewerCell cell) {
				SmellEntry e = (SmellEntry) cell.getElement();
				cell.setText(e.description);
			}
		});

		// Count
		TableViewerColumn colCount = createSmellsTableViewerColumn(titles[1], bounds[1]);
		colCount.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SmellEntry e = (SmellEntry) cell.getElement();
				cell.setText(Integer.toString(e.smellMatch.getCount()));
			}
		});
		
		// Type
		TableViewerColumn colType = createSmellsTableViewerColumn(titles[2], bounds[2]);
		colType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SmellEntry e = (SmellEntry) cell.getElement();
				cell.setText(e.type);
			}
		});
		
		// Severity Level
		TableViewerColumn colSeverityLevel = createSmellsTableViewerColumn(titles[3], bounds[3]);
		colSeverityLevel.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				SmellEntry e = (SmellEntry) cell.getElement();
				cell.setText(e.severityLevel.toString());
				cell.setBackground(severityLevelColors.get(e.severityLevel));
				if (e.severityLevel == SeverityLevel.Critical) {
					cell.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				} else {
					cell.setForeground(null);
				}
			}
		});
	}
	
	private static void addCustomTooltipSupport(TableViewer viewer) {
		
        Table table = viewer.getTable();
        final Shell tooltipShell = new Shell(table.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
        tooltipShell.setLayout(new FillLayout());
        final Label tooltipLabel = new Label(tooltipShell, SWT.NONE);

        table.addListener(SWT.MouseHover, event -> {
        	Point point = new Point(event.x, event.y);
            final TableItem item = table.getItem(point);
            int columnIndex = getColumnIndex(table, event.x);
            if (columnIndex == 2) {
                Object data = item.getData();
                if (data instanceof SmellEntry) {
                	SmellEntry entry = (SmellEntry) data;

                	String tooltipText = entry.typeDescription;

                	tooltipLabel.setText(tooltipText);
                    tooltipShell.pack();

                    // Display tooltip near the mouse pointer
                    tooltipShell.setLocation(Display.getCurrent().map(table, null, event.x + 15, event.y + 10));
                    tooltipShell.setVisible(true);
                }
            } else {
                table.setToolTipText(null);
            }
        });
        
        table.addListener(SWT.MouseMove, event -> {
            if (tooltipShell.isVisible()) {
                TableItem item = table.getItem(table.toControl(event.x, event.y));
                if (item == null) {
                    tooltipShell.setVisible(false);
                }
            }
        });

        table.addListener(SWT.MouseExit, event -> tooltipShell.setVisible(false));
    }
	
	private static int getColumnIndex(Table table, int mouseX) {
        int cumulativeWidth = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            cumulativeWidth += table.getColumn(i).getWidth();
            if (mouseX < cumulativeWidth) {
                return i;
            }
        }
        return -1; // No column found
    }
}
