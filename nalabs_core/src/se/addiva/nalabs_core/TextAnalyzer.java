package se.addiva.nalabs_core;

import java.util.Arrays;

public class TextAnalyzer
{
	private static WordCountMetric WordCountMetric = new WordCountMetric();
    private static ConjunctionsMetric ConjunctionsMetric = new ConjunctionsMetric();
    private static NVMetric NVMetric = new NVMetric();
    private static OptionalityMetric OptionalityMetric = new OptionalityMetric();
    private static SubjectivityMetric SubjectivityMetric = new SubjectivityMetric();
    private static ReferenceInternal ReferenceInternal = new ReferenceInternal();
    private static WeaknessMetric WeaknessMetric = new WeaknessMetric();
    private static ImperativesMetric ImperativesMetric = new ImperativesMetric();
    private static ContinuancesMetric ContinuancesMetric = new ContinuancesMetric();
    private static ReferenceExternal ReferenceExternal = new ReferenceExternal();

    public static se.addiva.nalabs_core.TextAnalysis AnalyzeText(String text) {
    	
    	se.addiva.nalabs_core.TextAnalysis analysis = new se.addiva.nalabs_core.TextAnalysis() {
    		{
	            ARI = CalculateARIScore(text);
	            wordCount = WordCountMetric.getWordCount(text);
	            conjunctions = ConjunctionsMetric.analyze(text);
	        	vaguePhrases = NVMetric.analyze(text);
	        	optionality = OptionalityMetric.analyze(text);
	        	subjectivity = SubjectivityMetric.analyze(text);
	        	referenceInternal = ReferenceInternal.analyze(text);
	        	weakness = WeaknessMetric.analyze(text);
	        	imperatives = ImperativesMetric.analyze(text);
	        	continuances = ContinuancesMetric.analyze(text);
	        	referenceExternal = ReferenceExternal.analyze(text);
    		}
        };

        return analysis;
    }

    private static double CalculateARIScore(String text)
    {
        String[] sentences = (String[])Arrays.stream(text.trim().split("\\."))
        		.filter(n -> !n.isEmpty() && n != " " && n.length() > 1)
        		.toArray(String[]::new);     

        String[] words = (String[])Arrays.stream(text.trim().split(" |\n"))
            .filter(n -> !n.isEmpty() && n != " ")
            .toArray(String[]::new);

        double sum = 0;

        for (String s : sentences)
        {
            sum += s.split(" ").length;
        }

        double wordSum = 0;

        for (String s : words)
        {
            wordSum += s.length();
        }

        return sum / sentences.length + 9 * (wordSum / words.length);
    }
}