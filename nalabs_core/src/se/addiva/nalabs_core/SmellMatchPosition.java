package se.addiva.nalabs_core;

public class SmellMatchPosition {
	
	private int startIndex;
	private int endIndex;
	
	public SmellMatchPosition(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public int getStartIndex() {
		return this.startIndex;
	}
	
	public int getEndIndex() {
		return this.endIndex;
	}
}