package nalabs.core;

import java.util.Arrays;
import java.util.HashMap;
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
            // Join all keywords for the regex
            String joinedKeywords = String.join("|", getKeywords());

            // Replace literal . with escaped \. since . means match any character in the input.
            joinedKeywords = joinedKeywords.replace(".", "\\.");

            // Replace spaces with \s+ to allow one or more spaces in the text for the phrases
            joinedKeywords = joinedKeywords.replaceAll("\\s+", "\\\\s+");

            pattern = Pattern.compile("\\b" + joinedKeywords + "\\b", Pattern.CASE_INSENSITIVE);
        }

        Matcher matcher = pattern.matcher(text);
        int count = 0;
        HashMap<String, Integer> smellsMap = new HashMap<>();
        while (matcher.find()) {
        	String matchedString = matcher.group(); 
        	if (!smellsMap.containsKey(matchedString)) {
        		smellsMap.put(matchedString, 1);
        	} else {
        		smellsMap.put(matchedString, smellsMap.get(matchedString) + 1);
        	}
        	count++;
        }
        
        int counts = smellsMap.size();

        return new AnalyzeResult() {
        	{
        		totalCount = counts;
        		smells = smellsMap;
        	}
        };
    }
}
