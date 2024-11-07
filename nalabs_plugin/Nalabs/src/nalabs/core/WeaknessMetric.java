package nalabs.core;

public class WeaknessMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"adequate", "as appropriate", "be able to", "be capable of", "capability of", "capability to", "effective",
                "as required", "normal", "provide for", "timely", "easy to"
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