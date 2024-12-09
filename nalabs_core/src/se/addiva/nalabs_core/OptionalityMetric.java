package se.addiva.nalabs_core;

public class OptionalityMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"can", "optionally"
        };
    }

    public OptionalityMetric(String[] keys)
    {
    	setKeywords(keys);
    }

    public OptionalityMetric()
    {
    }
    
    @Override
    public String getType() {
    	return "Optionality";
    }
    
    @Override
    public String getTypeDescription() {
    	return "Terms suggesting optional implementation";
    }
    
    @Override
	public SeverityLevel getSeverityLevel() {
		return SeverityLevel.Critical;
	}
}