package se.addiva.nalabs_core;

public class AriCategoryInfo {
	private String description;
	private String feedback;
	private String recommendation;
	
	public AriCategoryInfo(String description, String feedback, String recommendation) {
		this.description = description;
		this.feedback = feedback;
		this.recommendation = recommendation;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getFeedback() {
		return feedback;
	}
	
	public String getRecommendation() {
		return recommendation;
	}
}
