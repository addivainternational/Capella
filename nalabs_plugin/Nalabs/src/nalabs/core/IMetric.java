package nalabs.core;

public interface IMetric {
    String[] getKeywords();
    void setKeywords(String[] keywords);

    int analyze(String text);

    String[] getDefaultKeywords();
}