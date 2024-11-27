package nalabs.views;

import nalabs.helpers.*;
import se.addiva.nalabs_core.*;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Font;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.awt.Color;
import java.awt.SystemColor;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatisticsView {
	
	private Composite composite;
	private Composite leftComposite;
	private Composite rightComposite;
	private Composite requirementsChartComposite;
	private Composite smellsChartComposite;
	private Label nRequirementsCountLabel;
	private Label nSmellsCountLabel;
	private Label mostCommonSmellTypeTextLabel;
	private Label mostCommonSmellTextLabel;
	
	private JFreeChart requirementsChart = null;
	private JFreeChart smellsChart = null;
	
	private Font nRequirementsTextLabelFont;
	private Font nSmellsTextLabelFont;
	private Font mostCommonSmellTypeLabelFont;
	private Font mostCommonSmellLabelFont;
	
	public StatisticsView(Composite parent) {
		composite = parent;
		
		GridData statisticsGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout compositeGridLayout = new GridLayout(2, false);
		compositeGridLayout.marginHeight = 10;
		compositeGridLayout.marginWidth = 30;
		composite.setLayout(compositeGridLayout);
		composite.setLayoutData(statisticsGridData);
		
		leftComposite = new Composite(composite, SWT.FILL);
		GridData leftCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout leftLayout = new GridLayout();
		leftComposite.setLayout(leftLayout);
		leftComposite.setLayoutData(leftCompositeData);
		
		Composite upperLeftComposite = new Composite(leftComposite, SWT.FILL);
		GridData upperLeftCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout upperLeftCompositeLayout = new GridLayout();
		upperLeftComposite.setLayout(upperLeftCompositeLayout);
		upperLeftComposite.setLayoutData(upperLeftCompositeData);
		
		Composite lowerLeftComposite = new Composite(leftComposite, SWT.FILL);
		GridData lowerLeftCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		FormLayout lowerLeftCompositeLayout = new FormLayout();
		lowerLeftComposite.setLayout(lowerLeftCompositeLayout);
		lowerLeftComposite.setLayoutData(lowerLeftCompositeData);
		
		rightComposite = new Composite(composite, SWT.FILL);
		GridData rightCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout rightLayout = new GridLayout();
		rightComposite.setLayout(rightLayout);
		rightComposite.setLayoutData(rightCompositeData);
		
		// Number of requirements
		Label nRequirementsTextLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData nRequirementsTextLabelData = new GridData();
		nRequirementsTextLabel.setLayoutData(nRequirementsTextLabelData);
		nRequirementsTextLabel.setText("Number of Requirements");
		FontDescriptor boldnRequirementsTextLabelDescriptor = FontDescriptor.createFrom(nRequirementsTextLabel.getFont()).setStyle(SWT.BOLD);
		nRequirementsTextLabelFont = boldnRequirementsTextLabelDescriptor.createFont(nRequirementsTextLabel.getDisplay());
		nRequirementsTextLabel.setFont(nRequirementsTextLabelFont);
		nRequirementsCountLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData nRequirementsCountLabelData = new GridData();
		nRequirementsCountLabelData.heightHint = 20;
		nRequirementsCountLabelData.widthHint = 40;
		nRequirementsCountLabel.setLayoutData(nRequirementsCountLabelData);
		
		// Number of smells
		Label nSmellsTextLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData nSmellsTextLabelData = new GridData();
		nSmellsTextLabelData.verticalIndent = 10;
		nSmellsTextLabel.setLayoutData(nSmellsTextLabelData);
		nSmellsTextLabel.setText("Number of Smells");
		FontDescriptor boldnSmellsTextLabelDescriptor = FontDescriptor.createFrom(nSmellsTextLabel.getFont()).setStyle(SWT.BOLD);
		nSmellsTextLabelFont = boldnSmellsTextLabelDescriptor.createFont(nSmellsTextLabel.getDisplay());
		nSmellsTextLabel.setFont(nSmellsTextLabelFont);
		nSmellsCountLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData nSmellsCountLabelData = new GridData();
		nSmellsCountLabelData.heightHint = 20;
		nSmellsCountLabelData.widthHint = 40;
		nSmellsCountLabel.setLayoutData(nSmellsCountLabelData);
		
		// Most common smell type(s)
		Label mostCommonSmellTypeLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData mostCommonSmellTypeLabelData = new GridData();
		mostCommonSmellTypeLabelData.verticalIndent = 10;
		mostCommonSmellTypeLabel.setLayoutData(mostCommonSmellTypeLabelData);
		mostCommonSmellTypeLabel.setText("Most Common Smell Type(s)");
		FontDescriptor boldMostCommonSmellTypeLabelDescriptor = FontDescriptor.createFrom(mostCommonSmellTypeLabel.getFont()).setStyle(SWT.BOLD);
		mostCommonSmellTypeLabelFont = boldMostCommonSmellTypeLabelDescriptor.createFont(mostCommonSmellTypeLabel.getDisplay());
		mostCommonSmellTypeLabel.setFont(mostCommonSmellTypeLabelFont);
		mostCommonSmellTypeTextLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData mostCommonSmellTypeTextLabelData = new GridData();
		mostCommonSmellTypeTextLabelData.heightHint = 20;
		mostCommonSmellTypeTextLabelData.widthHint = 140;
		mostCommonSmellTypeTextLabelData.verticalAlignment = SWT.CENTER;
		mostCommonSmellTypeTextLabelData.verticalIndent = 5;
		mostCommonSmellTypeTextLabel.setLayoutData(mostCommonSmellTypeTextLabelData);
		
		// Most common smell(s)
		Label mostCommonSmellLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData mostCommonSmellLabelData = new GridData();
		mostCommonSmellLabel.setLayoutData(mostCommonSmellLabelData);
		mostCommonSmellLabelData.verticalIndent = 10;
		mostCommonSmellLabel.setText("Most Common Smell(s)");
		FontDescriptor boldMostCommonSmellLabelDescriptor = FontDescriptor.createFrom(mostCommonSmellLabel.getFont()).setStyle(SWT.BOLD);
		mostCommonSmellLabelFont = boldMostCommonSmellLabelDescriptor.createFont(mostCommonSmellLabel.getDisplay());
		mostCommonSmellLabel.setFont(mostCommonSmellLabelFont);
		mostCommonSmellTextLabel = new Label(upperLeftComposite, SWT.NONE);
		GridData mostCommonSmellTextLabelData = new GridData();
		mostCommonSmellTextLabelData.heightHint = 20;
		mostCommonSmellTextLabelData.widthHint = 140;
		mostCommonSmellTextLabelData.verticalAlignment = SWT.CENTER;
		mostCommonSmellTextLabelData.verticalIndent = 5;
		mostCommonSmellTextLabel.setLayoutData(mostCommonSmellTextLabelData);
		
		// Smell type info
		Button button = new Button(lowerLeftComposite, SWT.PUSH);
        button.setText("Smell Type Info");
        FormData buttonData = new FormData();
        buttonData.bottom = new FormAttachment(100, 0);	// position relative to bottom left
        button.setLayoutData(buttonData);
        button.addListener(SWT.Selection, e -> {
        	SmellTypeInfoView smellTypeInfo = new SmellTypeInfoView(lowerLeftComposite.getShell());
    		if (smellTypeInfo.open() == SmellTypeInfoView.OK) {}
        });
        
        // Generate report
        Button reportButton = new Button(lowerLeftComposite, SWT.PUSH);
        reportButton.setText("Generate Report");
        FormData reportButtonData = new FormData();
        reportButtonData.left = new FormAttachment(0, 100);
        reportButtonData.bottom = new FormAttachment(100, 0);	// position relative to bottom left
        reportButton.setLayoutData(reportButtonData);
        reportButton.addListener(SWT.Selection, e -> {
        	
        	// Create file dialog
        	FileDialog fileDialog = new FileDialog(lowerLeftComposite.getShell(), SWT.OPEN);
        	String userHome = System.getProperty("user.home");
            fileDialog.setFilterPath(userHome);
            fileDialog.setText("Save Report...");
            fileDialog.setFilterExtensions(new String[] { "*.pdf" });
            fileDialog.setFilterNames(new String[] { "Pdf Files (*.pdf)" });
            
            // Open the dialog and retrieve the selected file path
            String selectedFile = fileDialog.open();
            if (selectedFile != null) {
            	File f = new File(selectedFile);
            	if(f.exists() && !f.isDirectory()) { 
            		// Manually adding message box since setOverwrite seems buggy
            		MessageBox messageBox = new MessageBox(lowerLeftComposite.getShell(), SWT.OK | SWT.CANCEL);
            		messageBox.setText("Overwrite?");
            		messageBox.setMessage("The file'" + selectedFile + "' already exists. Overwrite?");
            		int response = messageBox.open();
                    if (response == SWT.OK) {
                    	Util.generateReport(composite, selectedFile);
                    } 
            	} else {
            		Util.generateReport(composite, selectedFile);
            	}
            }
        });
		
		// Requirements chart composite
		requirementsChartComposite = new Composite(rightComposite, SWT.EMBEDDED | SWT.FILL | SWT.LEFT);
		GridData requirementsChartData = new GridData(SWT.FILL, SWT.FILL, true, true);
		FillLayout requirementsChartLayout = new FillLayout();
		requirementsChartLayout.marginHeight = 10;
		requirementsChartLayout.marginWidth = 10;
		requirementsChartComposite.setLayout(requirementsChartLayout);
		requirementsChartComposite.setLayoutData(requirementsChartData);
		
		// Requirements chart
		requirementsChart = ChartFactory.createPieChart(
                "Requirement Severity", 
                null, 
                true, 
                true,
                false);
		requirementsChart.setBackgroundPaint(Color.WHITE);
		@SuppressWarnings("unchecked")
        PiePlot<String> piePlot = (PiePlot<String>)requirementsChart.getPlot();
		piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator(
            "{0} {2}",
            new DecimalFormat("0"), 
            new DecimalFormat("0.0%")         // Format for percentage
        ));

        // Optional: Customize font, colors, etc.
		piePlot.setLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
	        
		requirementsChart.getPlot().setBackgroundPaint(null);
		requirementsChart.getPlot().setOutlinePaint(new Color(0,0,0,0));
        ChartPanel requirementsChartPanel = new ChartPanel(requirementsChart); 
        java.awt.Frame requirementsChartCompositeFrame = SWT_AWT.new_Frame(requirementsChartComposite);
        requirementsChartCompositeFrame.add(requirementsChartPanel);
		
        // Smells chart composite
		smellsChartComposite = new Composite(rightComposite, SWT.EMBEDDED | SWT.FILL | SWT.LEFT);
		GridData chartCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 10;
		fillLayout.marginWidth = 10;
		smellsChartComposite.setLayout(fillLayout);
		smellsChartComposite.setLayoutData(chartCompositeData);
		
		// Smells chart
        smellsChart = ChartFactory.createBarChart(
                "Smells",      
                "Smell Types", 
                "#Count",
                new DefaultCategoryDataset(), 
                PlotOrientation.VERTICAL,
                false, 
                true,
                false
        );
        smellsChart.setBackgroundPaint(Color.WHITE);
        ChartPanel smellsChartPanel = new ChartPanel(smellsChart);
        java.awt.Frame smellsFrame = SWT_AWT.new_Frame(smellsChartComposite);
        smellsFrame.add(smellsChartPanel);
        
        // Customize
        CategoryPlot plot = (CategoryPlot) smellsChart.getPlot();
        plot.setBackgroundAlpha(0.3f);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.toRadians(30)));
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 10);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        parent.addListener(SWT.Resize, arg0 -> {
			resizeListener(parent);
		});
	}
	
	public void dispose() {
		if (nRequirementsTextLabelFont != null) {
			nRequirementsTextLabelFont.dispose();
		}
		if (nSmellsTextLabelFont != null) {
			nSmellsTextLabelFont.dispose();
		}
		if (mostCommonSmellTypeLabelFont != null) {
			mostCommonSmellTypeLabelFont.dispose();
		}
		if (mostCommonSmellLabelFont != null) {
			mostCommonSmellLabelFont.dispose();
		}
	}
	
	private void resizeListener(Composite parent) {
		org.eclipse.swt.graphics.Point size = parent.getSize();
		GridData chartData = (GridData) smellsChartComposite.getLayoutData();
		GridData leftData = (GridData) leftComposite.getLayoutData();
		GridData rightData = (GridData) rightComposite.getLayoutData();
		leftData.widthHint = (int) (size.x * 0.3);
		chartData.widthHint = size.x - leftData.widthHint;
		rightData.widthHint = size.x - leftData.widthHint;
		leftComposite.setLayoutData(leftData);
		smellsChartComposite.setLayoutData(chartData);
	}
	
	public void setRequirementData(Collection<Requirement> requirements) {

		int nRequirements = requirements.size();
		
        HashMap<String, Integer> smellCountTypeMap = new HashMap<String, Integer>();
        HashMap<String, String> smellTypeDescriptionMap = new HashMap<String, String>();
        HashMap<String, Integer> smellCountMapAggregated = new HashMap<String, Integer>();
        HashMap<String, Integer> requirementMap = new HashMap<String, Integer>();
		for (Requirement requirement : requirements) {
			for (AnalyzeResult result : requirement.getSmellResults()) {
				String type = result.type;
				Integer v = smellCountTypeMap.get(type);
				smellCountTypeMap.put(type, v == null ? result.totalCount : v + result.totalCount);
				result.smells.forEach((key, value) -> 
					smellCountMapAggregated.merge(key, value.getCount(), Integer::sum)
		        );
				smellTypeDescriptionMap.put(type, result.typeDescription);
			}
			Integer v = requirementMap.get(requirement.severityLevel.toString());
			int severityCount = v == null ? 1 : v + 1;
			requirementMap.put(requirement.severityLevel.toString(), severityCount);
		}
		
		DefaultPieDataset<String> requirementDataset = new DefaultPieDataset<String>();
        for (SeverityLevel severityLevel : Util.getOrderedSeverityLevels()) {
        	String severityLevelString = severityLevel.toString();
        	Integer value = requirementMap.get(severityLevelString);
        	int v = value != null ? value : 0;
        	requirementDataset.setValue(severityLevelString, v);
        }
        
		int axisMax = 10;
		int nSmells = 0;
		int mostCommonSmellTypeValue = 0;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : smellCountTypeMap.entrySet()) {
            String label = entry.getKey();
            int totalValue = entry.getValue();
            nSmells += totalValue;
            dataset.addValue(totalValue, "Smell", label);
            if (totalValue > axisMax) {
            	axisMax = (int)Math.round(totalValue/10.0) * 10;
            }
            if (totalValue > mostCommonSmellTypeValue) {
            	mostCommonSmellTypeValue = totalValue;
            }
        }
        
        List<String> mostCommonSmellTypes = new ArrayList<String>();
        List<String> mostCommonSmellTypeDescriptions = new ArrayList<String>();
        if (mostCommonSmellTypeValue > 0) {
        	for (Map.Entry<String, Integer> entry : smellCountTypeMap.entrySet()) {
            	int value = entry.getValue();
            	if (value == mostCommonSmellTypeValue) {
            		String key = entry.getKey();
            		mostCommonSmellTypes.add(key);
            		String description = smellTypeDescriptionMap.get(key);
            		mostCommonSmellTypeDescriptions.add(key + " - " + description);
            	}
            }
        }
        
        Map<String, Integer> smellCountMapAggregatedOrdered = 
        		smellCountMapAggregated.entrySet().stream()
        	    .sorted(Entry.<String, Integer>comparingByValue().reversed())
        	    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
        	                              (e1, e2) -> e1, LinkedHashMap::new));
        List<String> mostCommonSmells = new ArrayList<String>();
        int mostCommonSmellValue = -1;
        for (Map.Entry<String, Integer> e : smellCountMapAggregatedOrdered.entrySet()) {
        	int value = e.getValue();
        	if (mostCommonSmellValue == -1 || value == mostCommonSmellValue) {
        		mostCommonSmellValue = value;
        		mostCommonSmells.add(e.getKey());
        	}
        }
        
        nRequirementsCountLabel.setText(Integer.toString(nRequirements));
        nSmellsCountLabel.setText(Integer.toString(nSmells));
        
        if (mostCommonSmellTypeValue > 0) {
	        mostCommonSmellTypeTextLabel.setText(String.join(", ", mostCommonSmellTypes));
	        mostCommonSmellTypeTextLabel.setToolTipText(String.join("\n", mostCommonSmellTypeDescriptions));
        } else {
        	mostCommonSmellTypeTextLabel.setText("None");
        }
        
        if (mostCommonSmellValue > 0) {
        	mostCommonSmellTextLabel.setText(String.join(", ", mostCommonSmells));
        } else {
        	mostCommonSmellTextLabel.setText("None");
        }
        
        @SuppressWarnings("unchecked")
        PiePlot<String> piePlot = (PiePlot<String>)requirementsChart.getPlot();
        piePlot.setDataset(requirementDataset);
        setAwtColorFromSeverity(piePlot, SeverityLevel.Critical);
        setAwtColorFromSeverity(piePlot, SeverityLevel.High);
        setAwtColorFromSeverity(piePlot, SeverityLevel.Moderate);
        setAwtColorFromSeverity(piePlot, SeverityLevel.Low);
        setAwtColorFromSeverity(piePlot, SeverityLevel.None);
        
        CategoryPlot categoryPlot = smellsChart.getCategoryPlot();
        categoryPlot.setDataset(dataset);
        categoryPlot.setBackgroundPaint(SystemColor.inactiveCaption);
        ((BarRenderer)categoryPlot.getRenderer()).setBarPainter(new StandardBarPainter());
        BarRenderer r = (BarRenderer)smellsChart.getCategoryPlot().getRenderer();
        org.eclipse.swt.graphics.Color smellColor = nalabs.helpers.Util.getSmellColor();
        Color awtColor = Util.convertToAwtColor(smellColor);
        smellColor.dispose();
        r.setSeriesPaint(0, awtColor);
        
        NumberAxis rangeAxis = (NumberAxis) ((CategoryPlot)smellsChart.getPlot()).getRangeAxis();
        rangeAxis.setRange(0, axisMax);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	}
	
	private void setAwtColorFromSeverity(PiePlot<String> piePlot, SeverityLevel severityLevel) {
        org.eclipse.swt.graphics.Color color = Util.getSeverityColor(severityLevel);
        piePlot.setSectionPaint(severityLevel.toString(), Util.convertToAwtColor(color));
        color.dispose();
	}
}