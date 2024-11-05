package nalabs.core;

public class References2 extends MetricBase {
	@Override
    public String[] getDefaultKeywords() {
        return new String[] {
        		"defined in reference", "defined in the reference", "specified in reference", "specified in the reference",
                "specified by reference", "specified by the reference", "see reference", "see the reference",
                "refer to reference", "refer to the reference", "further reference", "follow reference",
                "follow the reference", "see"
        };
    }
	
	public References2(String[] keys)
    {
    	setKeywords(keys);
    }

    public References2()
    {
    }
}