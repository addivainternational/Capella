package se.addiva.nalabs_core;

public class SubjectivityMetric extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"similar", "better", "similarly", "worse", "having in mind", "take into account", "take into consideration",
                "as [adjective] as possible"
        };
    }

    public SubjectivityMetric(String[] keys)
    {
    	setKeywords(keys);
    }

    public SubjectivityMetric()
    {
    }
    
    @Override
    public String getType() {
    	return "Subjectivity";
    }
    
    @Override
    public String getTypeDescription() {
    	return "Personal opinions or subjective judgments";
    }
    
    @Override
	public SeverityLevel getSeverityLevel() {
		return SeverityLevel.Moderate;
	}
}