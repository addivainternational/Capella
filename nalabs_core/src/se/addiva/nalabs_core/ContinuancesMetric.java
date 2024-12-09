package se.addiva.nalabs_core;

public class ContinuancesMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"below", "as follows", "following", "listed", "in particular", "support", "and", ":"
        };
    }
	
	public ContinuancesMetric(String[] keys)
    {
        setKeywords(keys);
    }

    public ContinuancesMetric()
    {
    }
    
    @Override
    public String getType() {
    	return "Continuance";
    }
    
    @Override
    public String getTypeDescription() {
    	return "Phrases that specify further details, often after an imperative";
    }
    
    @Override
	public SeverityLevel getSeverityLevel() {
		return SeverityLevel.Moderate;
	}
}