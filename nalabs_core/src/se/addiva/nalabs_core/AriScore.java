package se.addiva.nalabs_core;

import java.util.HashMap;

public class AriScore {
	
	private double value;
	private AriCategory category;
	
	public AriScore(double value) {
		this.value = value;
		if (value < 7) {
			this.category = AriCategory.Basic;
		} else if (value >= 7 && value < 11) {
			this.category = AriCategory.General;
		} else if (value >= 11 && value < 16) {
			this.category = AriCategory.Technical;
		} else if (value >= 16) {
			this.category = AriCategory.Advanced;
		}
	}
	
	public double getValue() {
		return value;
	}
	
	public AriCategory getCategory() {
		return category;
	}
	
	@SuppressWarnings("serial")
	private static final HashMap<AriCategory, AriCategoryInfo> categoryInfoMap = new HashMap<AriCategory, AriCategoryInfo>() {{
				put(AriCategory.Basic, new AriCategoryInfo(
						"Text is very simple, suitable for non-technical stakeholders or foundational requirements.", 
						"Your text is very simple and clear but may lack sufficient technical depth for engineering purposes.", 
						"Add technical terms or additional details to ensure the requirement is precise and unambiguous."));
				put(AriCategory.General, new AriCategoryInfo(
						"Text is clear and moderately detailed, suitable for general stakeholders with limited technical knowledge.", 
						"Your text is clear and appropriate for a broad audience but may need slight adjustments for technical stakeholders.", 
						"Maintain simplicity but ensure that critical technical terms and details are not omitted. Optimize for clarity while introducing minimal technical complexity."));
				put(AriCategory.Technical, new AriCategoryInfo(
						"Text is moderately complex, suitable for technical stakeholders or professionals familiar within the domain.", 
						"Your text is moderately complex and well-suited for technical audiences, but ensure it's not overly complicated.", 
						"Ensure that technical details are presented clearly, avoiding unnecessary jargon or excessive sentence length. Split long sentences for better readability."));
				put(AriCategory.Advanced, new AriCategoryInfo(
						"Text is highly technical and detailed, suitable for experts or advanced users with deep technical knowledge.", 
						"Your text is highly detailed and suitable for experts, but it may be too complex for general understanding.", 
						"Simplify overly long sentences and provide definitions or examples for highly technical terms to improve accessibility without losing precision."));
	}};
	
	public static HashMap<AriCategory, AriCategoryInfo> getCategoryInfoMap() {
		return categoryInfoMap;
	}
}
