package nalabs.core;

public class Imperatives2 extends MetricBase {
	
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"must", "should", "could", "would", "can", "will", "may", "should"
        };
    }

	public Imperatives2(String[] keys)
    {
        setKeywords(keys);
    }

    public Imperatives2()
    {
    }
}