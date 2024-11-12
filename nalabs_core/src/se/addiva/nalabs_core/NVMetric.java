package se.addiva.nalabs_core;

public class NVMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"may", "could", "has to", "have to", "might", "will", "should have", "must have", "all the other",
                "all other", "based on", "some", "appropriate", "as a", "as an",
                "a minimum", "up to", "adequate", "as applicable", "be able to", "be capable of", "but not limited to",
                "capability of", "capability to", "effective", "normal"
        };
    }

    public NVMetric(String[] keys)
    {
    	setKeywords(keys);
    }

    public NVMetric()
    {
    }
    
    @Override
    public String metricDescription() {
    	return "Vague Phrase";
    }
}