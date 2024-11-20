package nalabs.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import se.addiva.nalabs_core.ContinuancesMetric;
import se.addiva.nalabs_core.IMetric;
import se.addiva.nalabs_core.ImperativesMetric;
import se.addiva.nalabs_core.NVMetric;
import se.addiva.nalabs_core.OptionalityMetric;
import se.addiva.nalabs_core.ReferenceExternal;
import se.addiva.nalabs_core.ReferenceInternal;
import se.addiva.nalabs_core.SubjectivityMetric;
import se.addiva.nalabs_core.WeaknessMetric;

import org.eclipse.swt.widgets.Button;

public class SmellTypeInfoView extends Window {
	
	protected SmellTypeInfoView(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Smell Type Info");
		newShell.setSize(1200, 800);
		newShell.setLayout(new GridLayout(1, false));

		// Center the window on the screen
		Display display = newShell.getDisplay();
		Monitor primaryMonitor = display.getPrimaryMonitor();
		Rectangle monitorBounds = primaryMonitor.getBounds();
		Rectangle shellBounds = newShell.getBounds();

		int x = monitorBounds.x + (monitorBounds.width - shellBounds.width) / 2;
		int y = monitorBounds.y + (monitorBounds.height - shellBounds.height) / 2;

		newShell.setLocation(x, y);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		GridData containerData = new GridData(SWT.FILL, SWT.FILL, true, true); 
		container.setLayoutData(containerData);

		TableViewer smellsTypeInfoViewer = new TableViewer(container, SWT.BORDER | SWT.HORIZONTAL | SWT.VERTICAL | SWT.WRAP);
		Table table = smellsTypeInfoViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createSmellsTypeInfoTableColumns(smellsTypeInfoViewer);
		smellsTypeInfoViewer.setContentProvider(ArrayContentProvider.getInstance());

		setData(smellsTypeInfoViewer);
		
		table.addListener(SWT.MeasureItem, event -> {
			event.height = 65;
        });
		
		container.addListener(SWT.Resize, arg0 -> {
			resizeListener(container, smellsTypeInfoViewer);
		});
		
		Button closeButton = new Button(container, SWT.PUSH);
		closeButton.setText("Close");
		closeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		closeButton.addListener(SWT.Selection, event -> close());

		return container;
	}

	@Override
	public int open() {
		// Set the window to modal
		setShellStyle(SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CENTER);
		return super.open();
	}
	
	private void setData(TableViewer smellsTypeInfoViewer) {
		IMetric[] metricDataArray = {
			new NVMetric(), 
		    new OptionalityMetric(),
		    new SubjectivityMetric(),
		    new ReferenceInternal(),
		    new WeaknessMetric(),
		    new ImperativesMetric(),
		    new ContinuancesMetric(),
		    new ReferenceExternal() 
	    };
		smellsTypeInfoViewer.setInput(metricDataArray);
	}
	
	private static void resizeListener(Composite parent, TableViewer smellsTypeInfoViewer) {
		org.eclipse.swt.graphics.Point size = parent.getSize();
		GridData smellsTypeInfoViewerData = (GridData) parent.getLayoutData();
		smellsTypeInfoViewerData.widthHint = (int) (size.x * 0.95);
		smellsTypeInfoViewerData.heightHint = (int) (size.y * 0.95);
		smellsTypeInfoViewer.getTable().setLayoutData(smellsTypeInfoViewerData);
	}

	private static TableViewerColumn createSmellsTableViewerColumn(TableViewer smellsTypeInfoViewer, String title, int bound) {
		TableViewerColumn viewerColumn = new TableViewerColumn(smellsTypeInfoViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private static void createSmellsTypeInfoTableColumns(TableViewer smellsTypeInfoViewer) {
		// Define column names and widths
		String[] titles = { "Smell Type", "Description", "Keywords", "Severity Level" };
		int[] bounds = { 120, 480, 450, 100 };

		// Smell Type
		TableViewerColumn colSmellType = createSmellsTableViewerColumn(smellsTypeInfoViewer, titles[0], bounds[0]);
		colSmellType.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMetric e = (IMetric) element;
				return e.getType();
			}
		});

		// Description
		TableViewerColumn colDescription = createSmellsTableViewerColumn(smellsTypeInfoViewer, titles[1], bounds[1]);
		colDescription.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMetric e = (IMetric) element;
				return e.getTypeDescription();
			}
		});

		// Keywords
		TableViewerColumn colKeywords = createSmellsTableViewerColumn(smellsTypeInfoViewer, titles[2], bounds[2]);
		colKeywords.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMetric e = (IMetric) element;
				return String.join(", ", e.getKeywords());
			}
		});

		// Severity Level
		TableViewerColumn colSeverityLevel = createSmellsTableViewerColumn(smellsTypeInfoViewer, titles[3], bounds[3]);
		colSeverityLevel.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMetric e = (IMetric) element;
				return e.getSeverityLevel().toString();
			}
		});
	}

}