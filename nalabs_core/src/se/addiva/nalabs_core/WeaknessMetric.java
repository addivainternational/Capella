package se.addiva.nalabs_core;

public class WeaknessMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"as appropriate", "as required", "provide for", "timely", "easy to"
        };
    }
	
	public WeaknessMetric(String[] keys)
    {
    	setKeywords(keys);
    }

    public WeaknessMetric()
    {
    }
    
    @Override
    public String metricDescription() {
    	return "Weakness";
    }
}