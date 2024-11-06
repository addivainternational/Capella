package nalabs.core;

public interface IMetric {
    String[] getKeywords();
    void setKeywords(String[] keywords);

    AnalyzeResult analyze(String text);

    String[] getDefaultKeywords();
}