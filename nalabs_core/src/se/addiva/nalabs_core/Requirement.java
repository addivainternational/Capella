package se.addiva.nalabs_core;

public class Requirement {

    public Requirement(){}

    public Requirement(String id, String text){
        Id = id;
        Text = text;
    }

    public String Id;
    public String Text;
    public double AriScore;
    public WordCountResult WordCount;
    public int TotalSmells;
    public AnalyzeResult Conjunctions;
    public AnalyzeResult VaguePhrases;
    public AnalyzeResult Optionality;
    public AnalyzeResult Subjectivity;
    public AnalyzeResult ReferencesInternal;
    public AnalyzeResult Weakness;
    public AnalyzeResult Imperatives;
    public AnalyzeResult Continuances;
    public AnalyzeResult ReferencesExternal;
    
    public AnalyzeResult[] getSmellResults() {
    	return new AnalyzeResult[] { Conjunctions, VaguePhrases, Optionality, Subjectivity, ReferencesInternal, 
    			Weakness, Imperatives, Continuances, ReferencesExternal };
    }
}