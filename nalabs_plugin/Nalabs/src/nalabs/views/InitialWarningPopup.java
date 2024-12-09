package nalabs.views;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.polarsys.kitalpha.vp.requirements.Requirements.Requirement;

public class InitialWarningPopup extends Dialog {
	
	private Collection<Requirement> faultyRequirements;
	private boolean useDefaultTextField;

    public InitialWarningPopup(Shell parentShell, Collection<Requirement> faultyRequirements, boolean useDefaultTextField) {
        super(parentShell);
        this.faultyRequirements = faultyRequirements;
        this.useDefaultTextField = useDefaultTextField;
    }
    
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Warning!");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = 20;
        gridLayout.marginWidth = 30;
        container.setLayout(gridLayout);

        Label warningLabel = new Label(container, SWT.NONE);
        warningLabel.setText("At least one requirement is not on the correct format.");
        
        Label theFollowingLabel = new Label(container, SWT.NONE);
        GridData followingLabelData = new GridData();
        followingLabelData.verticalIndent = 10;
        theFollowingLabel.setText("The following requirements have incorrect format:");
        theFollowingLabel.setLayoutData(followingLabelData);
        
        TableViewer tableViewer = new TableViewer(container);
        Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createColumns(tableViewer, useDefaultTextField);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		tableViewer.setInput(faultyRequirements);
		
		Text noteText = new Text(container, SWT.WRAP);
		noteText.setEditable(false);
		GridData noteTextData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		noteTextData.widthHint = 520; 
		noteTextData.heightHint = 40;
		noteText.setLayoutData(noteTextData);
		noteText.setText("In general, this means that 'ReqIFIdentifier' is empty. Also, if 'Text field' is selected as requirement text, then it might be that the above text is not wrapped in a <p>-tag, as it should.");
		
		Label currentModeTextInfo = new Label(container, SWT.NONE);
		GridData currentModeTextInfoData = new GridData();
		currentModeTextInfo.setText("Requirement text source: ".concat(useDefaultTextField ? "'Text field'" : "'Long Name'"));
		currentModeTextInfo.setLayoutData(currentModeTextInfoData);
		
		Label continueLabel = new Label(container, SWT.NONE);
		GridData continueLabelData = new GridData();
		continueLabelData.verticalIndent = 10;
		continueLabel.setText("Do you want to continue? (requirements with incorrect format will be ignored)");
		continueLabel.setLayoutData(continueLabelData);

        return container;
    }
    
    private TableViewerColumn createSmellsTableViewerColumn(TableViewer tableViewer, String title, int bound) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
    
    private void createColumns(TableViewer tableViewer, boolean useDefaultTextField) {
    	
		// Define column names and widths
		String[] titles = { "ReqIFIdentifier", "Text" };
		int[] bounds = { 100, 400 };
		
		// Id
		TableViewerColumn colId = createSmellsTableViewerColumn(tableViewer, titles[0], bounds[0]);
		colId.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Requirement r = (Requirement) element;
				return r.getReqIFIdentifier();
			}
		});

		// Text
		TableViewerColumn colText = createSmellsTableViewerColumn(tableViewer, titles[1], bounds[1]);
		colText.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Requirement r = (Requirement) element;
				return useDefaultTextField ? r.getReqIFText() : r.getReqIFLongName();
			}
		});
	}

}