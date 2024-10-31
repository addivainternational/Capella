package nalabs.handlers;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class RequirementsTableView extends ViewPart {

    public static final String ID = "nqdin29hbfwpifgpnpw09fgew30"; // Unique ID for your view
    
    private Collection<se.addiva.nalabs.Requirement> nalabRequirements;
    private TableViewer tableViewer;

    @Override
    public void createPartControl(Composite parent) {
        createTableViewer(parent);
    }
    
    private void createTableViewer(Composite parent) {
        // Initialize TableViewer
        tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        
        // Get the underlying table
        Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        createColumns();
        
        // Set content provider
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
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
    	String[] titles = { "Id", "Text", "Ari Score", "Conjunctions", "Vague Phrases", 
        		"Optionality", "Subjectivity", "References", "Weakness", "Imperatives",
        		"Continuances", "Imperatives2", "References2" };
        int[] bounds = { 50, 400, 80, 100, 100, 80, 80, 80, 80, 80, 100, 80, 80 };

        // Id
        TableViewerColumn colId = createTableViewerColumn(titles[0], bounds[0]);
        colId.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return r.Id;
            }
        });

        // Text
        TableViewerColumn colText = createTableViewerColumn(titles[1], bounds[1]);
        colText.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return r.Text;
            }
        });

        // AriScore
        TableViewerColumn colAriScore = createTableViewerColumn(titles[2], bounds[2]);
        colAriScore.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.AriScore);
            }
        });
        
        // Conjunctions
        TableViewerColumn colConjunctions = createTableViewerColumn(titles[3], bounds[3]);
        colConjunctions.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.Conjunctions);
            }
        });
        
        // VaguePhrases
        TableViewerColumn colVaguePhrases = createTableViewerColumn(titles[4], bounds[4]);
        colVaguePhrases.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.VaguePhrases);
            }
        });
        
        // Optionality
        TableViewerColumn colOptionality = createTableViewerColumn(titles[5], bounds[5]);
        colOptionality.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.Optionality);
            }
        });
        
        // Subjectivity
        TableViewerColumn colSubjectivity = createTableViewerColumn(titles[6], bounds[6]);
        colSubjectivity.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.Subjectivity);
            }
        });
        
        // References
        TableViewerColumn colReferences = createTableViewerColumn(titles[7], bounds[7]);
        colReferences.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.References);
            }
        });
        
        // Weakness
        TableViewerColumn colWeakness = createTableViewerColumn(titles[8], bounds[8]);
        colWeakness.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.Weakness);
            }
        });
        
        // Imperatives
        TableViewerColumn colImperatives = createTableViewerColumn(titles[9], bounds[9]);
        colImperatives.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.Imperatives);
            }
        });	
        
        // Continuances
        TableViewerColumn colContinuances = createTableViewerColumn(titles[10], bounds[10]);
        colContinuances.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.Continuances);
            }
        });	
        
        // Imperatives2
        TableViewerColumn colImperatives2 = createTableViewerColumn(titles[11], bounds[11]);
        colImperatives2.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.Imperatives2);
            }
        });	
        
        // References2
        TableViewerColumn colReferences2 = createTableViewerColumn(titles[12], bounds[12]);
        colReferences2.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	se.addiva.nalabs.Requirement r = (se.addiva.nalabs.Requirement) element;
                return Double.toString(r.References2);
            }
        });	
    }


    @Override
    public void setFocus() {
    	tableViewer.getControl().setFocus();
    }
    
    public void setRequirementData(Collection<se.addiva.nalabs.Requirement> requirements) {
    	nalabRequirements = requirements;
    	tableViewer.setInput(nalabRequirements);
    }
}