package se.addiva.nalabs;

public class Requirement {

    public Requirement(){}

    public Requirement(String id, String text){
        Id = id;
        Text = text;
    }

    public String Id;
    public String Text;
    public double AriScore;
    public int Conjunctions;
    public int VaguePhrases;
    public int Optionality;
    public int Subjectivity;
    public int References;
    public int Weakness;
    public int Imperatives;
    public int Continuances;
    public int Imperatives2;
    public int References2;
}
