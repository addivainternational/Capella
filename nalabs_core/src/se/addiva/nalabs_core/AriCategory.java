package se.addiva.nalabs_core;

public enum AriCategory {
	Basic, 
	General,
	Technical,
	Advanced;
	
	@Override
	public String toString() {
	    switch (this.ordinal()) {
	        case 0:
	            return "Basic Requirements";
	        case 1:
	            return "General Audience";
	        case 2:
	            return "Technical Documentation";
	        case 3:
	            return "Advanced Engineering Requirements";
	        default:
	            return null;
	    }
	}
}
