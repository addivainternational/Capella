package nalabs.views;

import se.addiva.nalabs_core.AnalyzeResult;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.graphics.Font;
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
import org.jfree.chart.axis.NumberAxis;

import java.util.Map;
import java.util.HashMap;

public class StatisticsView {
	
	private Composite composite;
	private Composite generalInfoComposite;
	private Composite chartComposite;
	private Label labelTitle;
	private JFreeChart chart = null;
	
	public StatisticsView(Composite parent) {
		composite = parent;
		composite.setLayout(new GridLayout(1, false));
		
		GridData statisticsGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout compositeGridLayout = new GridLayout();
		compositeGridLayout.marginHeight = 10;
		compositeGridLayout.marginWidth = 30;
		composite.setLayout(compositeGridLayout);
		composite.setLayoutData(statisticsGridData);
		
		generalInfoComposite = new Composite(composite, SWT.FILL);
		GridLayout generalInfoCompositeLayout = new GridLayout();
		generalInfoComposite.setLayout(generalInfoCompositeLayout);
		
		labelTitle = new Label(generalInfoComposite, SWT.NONE);
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 300;
		labelGridData.heightHint = 30;
		labelTitle.setLayoutData(labelGridData);
		FontDescriptor boldDescriptor = FontDescriptor.createFrom(labelTitle.getFont()).setStyle(SWT.BOLD).setHeight(16);
		labelTitle.setFont(boldDescriptor.createFont(labelTitle.getDisplay()));
		labelTitle.setText("Requirement Smell Detector");
		
		chartComposite = new Composite(composite, SWT.EMBEDDED | SWT.FILL);
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
        
        NumberAxis rangeAxis = (NumberAxis) ((CategoryPlot)chart.getPlot()).getRangeAxis();
        rangeAxis.setRange(0, 10);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	}
	
	public void setRequirementData(Collection<se.addiva.nalabs_core.Requirement> requirements) {

        HashMap<String, Integer> smellCountMap = new HashMap<String, Integer>();
		for (se.addiva.nalabs_core.Requirement requirement : requirements) {
			for (AnalyzeResult result : requirement.getResults()) {
				String type = result.description;
				Integer v = smellCountMap.get(type);
				smellCountMap.put(type, v == null ? result.totalCount : v + result.totalCount);
			}
		}
        
		int axisMax = 10;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : smellCountMap.entrySet()) {
            String label = entry.getKey();
            int totalValue = entry.getValue();
            dataset.addValue(totalValue, "Smell", label);
            if (totalValue > axisMax) {
            	axisMax = (int)Math.round(totalValue/10.0) * 10;
            }
        }
        chart.getCategoryPlot().setDataset(dataset);
        NumberAxis rangeAxis = (NumberAxis) ((CategoryPlot)chart.getPlot()).getRangeAxis();
        rangeAxis.setRange(0, axisMax);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	}
}