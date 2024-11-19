package se.addiva.nalabs_core;

public class ConjunctionsMetric extends MetricBase {
    @Override
    public String[] getDefaultKeywords() {
        return new String[] {
            "after", "although", "as long as", "before", "but", "else", "if", "in order", "in case", "nor", "or",
            "otherwise", "once", "since", "then", "though", "till", "unless", "until", "when", "whenever", "where",
            "whereas", "wherever", "while", "yet"
        };
    }

    public ConjunctionsMetric(String[] keys)
    {
        setKeywords(keys);
    }

    public ConjunctionsMetric()
    {
    }
    
    @Override
    public String getType() {
    	return "Conjunction";
    }
    
    @Override
    public String getTypeDescription() {
    	return "Complexity metrics based on sentence structure";
    }

	@Override
	public SeverityLevel getSeverityLevel() {
		return SeverityLevel.Low;
	}
}
