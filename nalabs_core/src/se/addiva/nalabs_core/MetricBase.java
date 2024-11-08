package se.addiva.nalabs_core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MetricBase implements IMetric {
    private String[] keywords = null;
    private Pattern pattern;

    public abstract String[] getDefaultKeywords();

    @Override
    public String[] getKeywords() {
        if(keywords != null) return keywords;
        else return getDefaultKeywords();
    }

    @Override
    public void setKeywords(String[] keywords) {
        this.pattern = null;
        this.keywords = Arrays.stream(keywords)
                .map(s -> s.trim().replace("\"", ""))
                .toArray(String[]::new);
    }

    @Override
    public AnalyzeResult analyze(String text) {
    	
        if (pattern == null) {
            // Put word boundaries on all keywords (expressions), then join them
        	String[] tempKeywords = getKeywords();
        	List<String> modKeywords = new ArrayList<String>();
        	for (String tempKeyword : tempKeywords) {
        		modKeywords.add("\\b" + tempKeyword + "\\b");
        	}
            String joinedKeywords = String.join("|", modKeywords);

            // Replace literal . with escaped \. since . means match any character in the input.
            joinedKeywords = joinedKeywords.replace(".", "\\.");

            pattern = Pattern.compile(joinedKeywords, Pattern.CASE_INSENSITIVE);
        }

        // Match the patterns and create hashmap for existing smells
        Matcher matcher = pattern.matcher(text);
        HashMap<String, Integer> smellsMap = new HashMap<>();
        while (matcher.find()) {
        	String matchedString = matcher.group(); 
        	if (!smellsMap.containsKey(matchedString)) {
        		smellsMap.put(matchedString, 1);
        	} else {
        		smellsMap.put(matchedString, smellsMap.get(matchedString) + 1);
        	}
        }
        
        String d = this.metricDescription();

        return new AnalyzeResult() {
        	{
        		totalCount = smellsMap.size();
        		smells = smellsMap;
        		description = d;
        	}
        };
    }
}
