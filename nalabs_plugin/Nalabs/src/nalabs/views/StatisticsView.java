package nalabs.views;

import se.addiva.nalabs_core.AnalyzeResult;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;

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
	private JFreeChart chart = null;
	
	public StatisticsView(Composite parent) {
		composite = parent;
		GridData statisticsGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayout(new FillLayout());
		parent.setLayoutData(statisticsGridData);
		
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
        java.awt.Frame frame = SWT_AWT.new_Frame(composite);
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