package se.addiva.nalabs_core;

public class Requirement {

    public Requirement(){}

    public Requirement(String id, String text){
        this.id = id;
        this.text = text;
    }

    public String id;
    public String text;
    public AriScore ariScore;
    public WordCountResult wordCount;
    public int totalSmells = 0;
    public SeverityLevel severityLevel = SeverityLevel.None;
    public AnalyzeResult conjunctions;
    public AnalyzeResult vaguePhrases;
    public AnalyzeResult optionality;
    public AnalyzeResult subjectivity;
    public AnalyzeResult referencesInternal;
    public AnalyzeResult weakness;
    public AnalyzeResult imperatives;
    public AnalyzeResult continuances;
    public AnalyzeResult referencesExternal;
    
    public AnalyzeResult[] getSmellResults() {
    	return new AnalyzeResult[] { conjunctions, vaguePhrases, optionality, subjectivity, referencesInternal, 
    			weakness, imperatives, continuances, referencesExternal };
    }
}