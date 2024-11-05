package nalabs.core;

public class OptionalityMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"can", "may", "optionally"
        };
    }

    public OptionalityMetric(String[] keys)
    {
    	setKeywords(keys);
    }

    public OptionalityMetric()
    {
    }
}