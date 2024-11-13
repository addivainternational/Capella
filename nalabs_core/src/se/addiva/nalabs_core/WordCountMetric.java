package se.addiva.nalabs_core;

import java.util.Arrays;

public class WordCountMetric {
	
    public WordCountMetric()
    {
    }
    
    public WordCountResult getWordCount(String text) {
    	
    	String[] wordArray = (String[])Arrays.stream(text.trim().split(" |\n"))
                .filter(n -> !n.isEmpty() && n != " ")
                .toArray(String[]::new);
        
        String d = this.metricDescription();
        return new WordCountResult() {
		        	{
		    			totalCount = wordArray.length;
		    			description = d;
		        	}
        		};
    }
    
    public String metricDescription() {
    	return "Word Count";
    }
}