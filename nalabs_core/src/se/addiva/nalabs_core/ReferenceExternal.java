package se.addiva.nalabs_core;

public class ReferenceExternal extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"defined in reference", "defined in the reference", "specified in reference", "specified in the reference",
                "specified by reference", "specified by the reference", "see reference", "see the reference",
                "refer to reference", "refer to the reference", "further reference", "follow reference",
                "follow the reference", "see"
        };
    }
	
	public ReferenceExternal(String[] keys)
    {
    	setKeywords(keys);
    }

    public ReferenceExternal()
    {
    }
    
    @Override
    public String getType() {
    	return "External Reference";
    }
    
    @Override
    public String getTypeDescription() {
    	return "References to external documents or sections";
    }
    
    @Override
	public SeverityLevel getSeverityLevel() {
		return SeverityLevel.High;
	}
}