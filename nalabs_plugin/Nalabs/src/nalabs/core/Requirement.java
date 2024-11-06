package nalabs.core;

public class Requirement {

    public Requirement(){}

    public Requirement(String id, String text){
        Id = id;
        Text = text;
    }

    public String Id;
    public String Text;
    public double AriScore;
    public int TotalSmells;
    public AnalyzeResult Conjunctions;
    public AnalyzeResult VaguePhrases;
    public AnalyzeResult Optionality;
    public AnalyzeResult Subjectivity;
    public AnalyzeResult References;
    public AnalyzeResult Weakness;
    public AnalyzeResult Imperatives;
    public AnalyzeResult Continuances;
    public AnalyzeResult Imperatives2;
    public AnalyzeResult References2;
}