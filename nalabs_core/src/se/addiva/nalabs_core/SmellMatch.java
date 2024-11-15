package se.addiva.nalabs_core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SmellMatch implements Iterable<SmellMatchPosition> {
	private int count = 0;
	@SuppressWarnings("serial")
	private Collection<SmellMatchPosition> positions = new ArrayList<SmellMatchPosition>() {};
	
	public SmellMatch() {}
	
	public SmellMatch(int startIndex, int endIndex) {
		this.Add(startIndex, endIndex);
	}
	
	public void Add(int startIndex, int endIndex) {
		this.positions.add(new SmellMatchPosition(startIndex, endIndex));
		this.count++;
	}
	
	public int getCount() {
		return this.count;
	}
	
	@Override
	public Iterator<SmellMatchPosition> iterator()
	{
	    return positions.iterator();
	 } 
}