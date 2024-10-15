using Mdu.Nalabs.Metrics;

namespace Addiva.Nalabs.Interop;

public class TextAnalyzer
{
    private static readonly ConjunctionsMetric ConjunctionsMetric = new();
    private static readonly NVMetric NVMetric = new();
    private static readonly OptionalityMetric OptionalityMetric = new();
    private static readonly SubjectivityMetric SubjectivityMetric = new();
    private static readonly ReferencesMetric ReferencesMetric = new();
    private static readonly WeaknessMetric WeaknessMetric = new();
    private static readonly ImperativesMetric ImperativesMetric = new();
    private static readonly ContinuancesMetric ContinuancesMetric = new();
    private static readonly Imperatives2 Imperatives2Metric = new();
    private static readonly References2 References2Metric = new();

    public static Addiva.Nalabs.Interop.TextAnalysis AnalyzeText(string text)
    {
        var analysis = new Addiva.Nalabs.Interop.TextAnalysis
        {
            ARI = CalculateARIScore(text),
            Conjunctions = ConjunctionsMetric.Analyze(text),
            VaguePhrases = NVMetric.Analyze(text),
            Optionality = OptionalityMetric.Analyze(text),
            Subjectivity = SubjectivityMetric.Analyze(text),
            References = ReferencesMetric.Analyze(text),
            Weakness = WeaknessMetric.Analyze(text),
            Imperatives = ImperativesMetric.Analyze(text),
            Continuances = ContinuancesMetric.Analyze(text),
            Imperatives2 = Imperatives2Metric.Analyze(text),
            References2 = References2Metric.Analyze(text)
        };

        return analysis;
    }

    private static double CalculateARIScore(string text)
    {
        var sentences = text
            .Trim()
            .Split(['.'])
            .Where(n => !string.IsNullOrEmpty(n) && n != " " && n.Length > 1)
            .ToArray();

        var words = text
            .Trim()
            .Split([' ', '\n'])
            .Where(n => !string.IsNullOrEmpty(n) && n != " ")
            .ToArray();

        double sum = 0;

        foreach (var s in sentences)
        {
            sum += s.Split(' ').Length;
        }

        double wordSum = 0;

        foreach (var s in words)
        {
            wordSum += s.Length;
        }

        return sum / sentences.Length + 9 * (wordSum / words.Count());
    }
}