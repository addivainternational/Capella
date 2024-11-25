package se.addiva.nalabs_core;

public enum SeverityLevel {
	None,
	Low,
	Moderate,
	High,
	Critical;
	
	@Override
	public String toString() {
	    switch (this.ordinal()) {
	        case 0:
	            return "No Smell";
	        case 1:
	            return "Low";
	        case 2:
	            return "Moderate";
	        case 3:
	            return "High";
	        case 4:
	        	return "Critical";
	        default:
	            return null;
	    }
	}
}