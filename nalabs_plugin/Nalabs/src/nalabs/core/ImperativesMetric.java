package nalabs.core;

public class ImperativesMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"shall", "must", "is required to", "are applicable", "are to", "responsible for", "will", "should"
        };
    }
	
	public ImperativesMetric(String[] keys)
    {
        setKeywords(keys);
    }

    public ImperativesMetric()
    {
    }
	
}