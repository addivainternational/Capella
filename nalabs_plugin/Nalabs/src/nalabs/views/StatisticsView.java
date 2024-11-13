package nalabs.views;

import se.addiva.nalabs_core.*;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.awt.SWT_AWT;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

public class StatisticsView {
	
	private Composite composite;
	private Composite titleComposite;
	private Composite chartComposite;
	private Composite generalInfoComposite;
	private Label labelTitle;
	private Label nRequirementsCountLabel;
	private Label nSmellsCountLabel;
	private Label mostCommonSmellTypeTextLabel;
	private Label mostCommonSmellTextLabel;
	private JFreeChart chart = null;
	
	public StatisticsView(Composite parent) {
		composite = parent;
		
		GridData statisticsGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout compositeGridLayout = new GridLayout(1, false);
		compositeGridLayout.marginHeight = 10;
		compositeGridLayout.marginWidth = 30;
		composite.setLayout(compositeGridLayout);
		composite.setLayoutData(statisticsGridData);
		
		titleComposite = new Composite(composite, SWT.FILL);
		GridLayout titleLayout = new GridLayout();
		titleComposite.setLayout(titleLayout);
		
		labelTitle = new Label(titleComposite, SWT.NONE | SWT.TOP);
		GridData labelGridData = new GridData();
		labelGridData.heightHint = 40;
		labelTitle.setLayoutData(labelGridData);
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(labelTitle.getFont()).setStyle(SWT.BOLD).setHeight(16);
		labelTitle.setFont(boldDescriptor.createFont(labelTitle.getDisplay()));
		labelTitle.setText("Requirement Smell Detector");
		
		Composite lowerSplitComposite = new Composite(composite, SWT.FILL);
		GridData lowerSplitCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout lowerSplitCompositeLayout = new GridLayout(2, false);
		lowerSplitComposite.setLayoutData(lowerSplitCompositeData);
		lowerSplitComposite.setLayout(lowerSplitCompositeLayout);
		
		generalInfoComposite = new Composite(lowerSplitComposite, SWT.FILL);
		GridData generalInfoCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout generalInfoCompositeLayout = new GridLayout();
		generalInfoComposite.setLayoutData(generalInfoCompositeData);
		generalInfoComposite.setLayout(generalInfoCompositeLayout);
		
		// Number of requirements
		Label nRequirementsTextLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData nRequirementsTextLabelData = new GridData();
		nRequirementsTextLabel.setLayoutData(nRequirementsTextLabelData);
		nRequirementsTextLabel.setText("Number of Requirements");
		FontDescriptor boldnRequirementsTextLabelDescriptor = FontDescriptor.createFrom(nRequirementsTextLabel.getFont()).setStyle(SWT.BOLD);
		nRequirementsTextLabel.setFont(boldnRequirementsTextLabelDescriptor.createFont(nRequirementsTextLabel.getDisplay()));
		nRequirementsCountLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData nRequirementsCountLabelData = new GridData();
		nRequirementsCountLabelData.heightHint = 20;
		nRequirementsCountLabelData.widthHint = 40;
		nRequirementsCountLabel.setLayoutData(nRequirementsCountLabelData);
		
		// Number of smells
		Label nSmellsTextLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData nSmellsTextLabelData = new GridData();
		nSmellsTextLabelData.verticalIndent = 10;
		nSmellsTextLabel.setLayoutData(nSmellsTextLabelData);
		nSmellsTextLabel.setText("Number of Smells");
		FontDescriptor boldnSmellsTextLabelDescriptor = FontDescriptor.createFrom(nSmellsTextLabel.getFont()).setStyle(SWT.BOLD);
		nSmellsTextLabel.setFont(boldnSmellsTextLabelDescriptor.createFont(nSmellsTextLabel.getDisplay()));
		nSmellsCountLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData nSmellsCountLabelData = new GridData();
		nSmellsCountLabelData.heightHint = 20;
		nSmellsCountLabelData.widthHint = 40;
		nSmellsCountLabel.setLayoutData(nSmellsCountLabelData);
		
		// Most common smell type(s)
		Label mostCommonSmellTypeLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData mostCommonSmellTypeLabelData = new GridData();
		mostCommonSmellTypeLabelData.verticalIndent = 10;
		mostCommonSmellTypeLabel.setLayoutData(mostCommonSmellTypeLabelData);
		mostCommonSmellTypeLabel.setText("Most Common Smell Type(s)");
		FontDescriptor boldMostCommonSmellTypeLabelDescriptor = FontDescriptor.createFrom(mostCommonSmellTypeLabel.getFont()).setStyle(SWT.BOLD);
		mostCommonSmellTypeLabel.setFont(boldMostCommonSmellTypeLabelDescriptor.createFont(mostCommonSmellTypeLabel.getDisplay()));
		mostCommonSmellTypeTextLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData mostCommonSmellTypeTextLabelData = new GridData();
		mostCommonSmellTypeTextLabelData.heightHint = 20;
		mostCommonSmellTypeTextLabelData.widthHint = 140;
		mostCommonSmellTypeTextLabelData.verticalAlignment = SWT.CENTER;
		mostCommonSmellTypeTextLabelData.verticalIndent = 5;
		mostCommonSmellTypeTextLabel.setLayoutData(mostCommonSmellTypeTextLabelData);
		
		// Most common smell(s)
		Label mostCommonSmellLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData mostCommonSmellLabelData = new GridData();
		mostCommonSmellLabel.setLayoutData(mostCommonSmellLabelData);
		mostCommonSmellLabelData.verticalIndent = 10;
		mostCommonSmellLabel.setText("Most Common Smell(s)");
		FontDescriptor boldMostCommonSmellLabelDescriptor = FontDescriptor.createFrom(mostCommonSmellLabel.getFont()).setStyle(SWT.BOLD);
		mostCommonSmellLabel.setFont(boldMostCommonSmellLabelDescriptor.createFont(mostCommonSmellLabel.getDisplay()));
		mostCommonSmellTextLabel = new Label(generalInfoComposite, SWT.NONE);
		GridData mostCommonSmellTextLabelData = new GridData();
		mostCommonSmellTextLabelData.heightHint = 20;
		mostCommonSmellTextLabelData.widthHint = 140;
		mostCommonSmellTextLabelData.verticalAlignment = SWT.CENTER;
		mostCommonSmellTextLabelData.verticalIndent = 5;
		mostCommonSmellTextLabel.setLayoutData(mostCommonSmellTextLabelData);
		
		chartComposite = new Composite(lowerSplitComposite, SWT.EMBEDDED | SWT.FILL);
		GridData chartCompositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 10;
		fillLayout.marginWidth = 10;
		chartComposite.setLayout(fillLayout);
		chartComposite.setLayoutData(chartCompositeData);
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Create a chart
        chart = ChartFactory.createBarChart(
                "Smells",      
                "Smell Types", 
                "#Count",
                dataset, 
                PlotOrientation.VERTICAL,
                true, 
                true,
                false
        );

        // Create a ChartPanel for displaying the chart
        ChartPanel chartPanel = new ChartPanel(chart);

        // Use SWT_AWT bridge to integrate with SWT
        java.awt.Frame frame = SWT_AWT.new_Frame(chartComposite);
        frame.add(chartPanel);
        
        // Customize
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.toRadians(30)));
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0, 10);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        parent.addListener(SWT.Resize, arg0 -> {
			resizeListener(parent);
		});
	}
	
	private void resizeListener(Composite parent) {
		org.eclipse.swt.graphics.Point size = parent.getSize();
		GridData generalInfoData = (GridData) generalInfoComposite.getLayoutData();
		GridData chartData = (GridData) chartComposite.getLayoutData();
		generalInfoData.widthHint = (int) (size.x * 0.3);
		chartData.widthHint = size.x - generalInfoData.widthHint;
		generalInfoComposite.setLayoutData(generalInfoData);
		chartComposite.setLayoutData(chartData);
	}
	
	public void setRequirementData(Collection<Requirement> requirements) {

		int nRequirements = requirements.size();
		
        HashMap<String, Integer> smellCountTypeMap = new HashMap<String, Integer>();
        HashMap<String, Integer> smellCountMapAggregated = new HashMap<String, Integer>();
		for (Requirement requirement : requirements) {
			for (AnalyzeResult result : requirement.getSmellResults()) {
				String type = result.description;
				Integer v = smellCountTypeMap.get(type);
				smellCountTypeMap.put(type, v == null ? result.totalCount : v + result.totalCount);
				result.smells.forEach((key, value) -> 
					smellCountMapAggregated.merge(key, value, Integer::sum)
		        );
			}
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
        if (mostCommonSmellTypeValue > 0) {
        	for (Map.Entry<String, Integer> entry : smellCountTypeMap.entrySet()) {
            	int value = entry.getValue();
            	if (value == mostCommonSmellTypeValue) {
            		mostCommonSmellTypes.add(entry.getKey());
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
        } else {
        	mostCommonSmellTypeTextLabel.setText("None");
        }
        
        if (mostCommonSmellValue > 0) {
        	mostCommonSmellTextLabel.setText(String.join(", ", mostCommonSmells));
        } else {
        	mostCommonSmellTextLabel.setText("None");
        }
        
        chart.getCategoryPlot().setDataset(dataset);
        NumberAxis rangeAxis = (NumberAxis) ((CategoryPlot)chart.getPlot()).getRangeAxis();
        rangeAxis.setRange(0, axisMax);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	}
}