package se.addiva.nalabs_core;

public class ReferencesInternal extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"e.g.", "i.e.", "for example", "figure", "table", "note"
        };
    }
	
	public ReferencesInternal(String[] keys)
    {
    	setKeywords(keys);
    }

    public ReferencesInternal()
    {
    }
    
    @Override
    public String metricDescription() {
    	return "Internal Reference";
    }
}