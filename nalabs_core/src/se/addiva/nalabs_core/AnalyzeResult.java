package se.addiva.nalabs_core;

import java.util.HashMap;

public class AnalyzeResult {
	public int totalCount;
	public HashMap<String, SmellMatch> smells;
	public String type;
	public String typeDescription;
	public SeverityLevel severityLevel;
}