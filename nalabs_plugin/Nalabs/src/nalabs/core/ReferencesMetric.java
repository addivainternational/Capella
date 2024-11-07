package nalabs.core;

public class ReferencesMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"e.g.", "i.e.", "for example", "figure", "table", "note"
        };
    }
	
	public ReferencesMetric(String[] keys)
    {
    	setKeywords(keys);
    }

    public ReferencesMetric()
    {
    }
    
    @Override
    public String metricDescription() {
    	return "Reference #1";
    }
}