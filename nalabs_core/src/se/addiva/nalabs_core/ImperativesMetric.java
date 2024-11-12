﻿package se.addiva.nalabs_core;

public class ImperativesMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"is required to", "are applicable", "are to", "responsible for", "should", "would"
        };
    }
	
	public ImperativesMetric(String[] keys)
    {
        setKeywords(keys);
    }

    public ImperativesMetric()
    {
    }
	
    @Override
    public String metricDescription() {
    	return "Imperative";
    }
    
    @Override
	public SeverityLevel getSeverityLevel() {
		return SeverityLevel.Moderate;
	}
}