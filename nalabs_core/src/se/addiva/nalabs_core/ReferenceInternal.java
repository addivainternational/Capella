package se.addiva.nalabs_core;

public class ReferenceInternal extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"e.g.", "i.e.", "for example", "figure", "table", "note"
        };
    }
	
	public ReferenceInternal(String[] keys)
    {
    	setKeywords(keys);
    }

    public ReferenceInternal()
    {
    }
    
    @Override
    public String metricDescription() {
    	return "Internal Reference";
    }
    
    @Override
	public SeverityLevel getSeverityLevel() {
		return SeverityLevel.High;
	}
}