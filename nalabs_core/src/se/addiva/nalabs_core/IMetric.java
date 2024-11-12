package se.addiva.nalabs_core;

public interface IMetric {
    String[] getKeywords();
    void setKeywords(String[] keywords);

    AnalyzeResult analyze(String text);

    String[] getDefaultKeywords();
    
    String metricDescription();
    
    SeverityLevel getSeverityLevel();
}