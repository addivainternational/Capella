package nalabs.core;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TextAnalyzer
{
    private static ConjunctionsMetric ConjunctionsMetric = new ConjunctionsMetric();
    private static NVMetric NVMetric = new NVMetric();
    private static OptionalityMetric OptionalityMetric = new OptionalityMetric();
    private static SubjectivityMetric SubjectivityMetric = new SubjectivityMetric();
    private static ReferencesMetric ReferencesMetric = new ReferencesMetric();
    private static WeaknessMetric WeaknessMetric = new WeaknessMetric();
    private static ImperativesMetric ImperativesMetric = new ImperativesMetric();
    private static ContinuancesMetric ContinuancesMetric = new ContinuancesMetric();
    private static Imperatives2 Imperatives2Metric = new Imperatives2();
    private static References2 References2Metric = new References2();

    public static nalabs.core.TextAnalysis AnalyzeText(String text) {
    	nalabs.core.TextAnalysis analysis = new nalabs.core.TextAnalysis() {
    		{
	            ARI = CalculateARIScore(text);
	            conjunctions = ConjunctionsMetric.analyze(text);
	            vaguePhrases = NVMetric.analyze(text);
	            optionality = OptionalityMetric.analyze(text);
	            subjectivity = SubjectivityMetric.analyze(text);
	            references = ReferencesMetric.analyze(text);
	            weakness = WeaknessMetric.analyze(text);
	            imperatives = ImperativesMetric.analyze(text);
	            continuances = ContinuancesMetric.analyze(text);
	            imperatives2 = Imperatives2Metric.analyze(text);
	            references2 = References2Metric.analyze(text);
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