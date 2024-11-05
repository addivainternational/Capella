package nalabs.core;

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
}