package se.addiva.nalabs_core;

import java.util.function.Consumer;

public class TextAnalysis {
    public AriScore ARI;
    
    public WordCountResult wordCount;

    public AnalyzeResult conjunctions;

    public AnalyzeResult vaguePhrases;

    public AnalyzeResult optionality;

    public AnalyzeResult subjectivity;

    public AnalyzeResult referenceInternal;

    public AnalyzeResult weakness;

    public AnalyzeResult imperatives;

    public AnalyzeResult continuances;

    public AnalyzeResult referenceExternal;
    
    public void forEachSmellTypeResult(Consumer<AnalyzeResult> method) {
    	method.accept(conjunctions);
    	method.accept(vaguePhrases);
    	method.accept(optionality);
    	method.accept(subjectivity);
    	method.accept(referenceInternal);
    	method.accept(weakness);
    	method.accept(imperatives);
    	method.accept(continuances);
    	method.accept(referenceExternal);
    }
}