package edu.bu.ec500.bean;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class TCResponse {

	@JsonProperty("count") 
	private int count;
	@JsonProperty("change") 
	private ArrayList<Change> changes;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<Change> getChanges() {
		
		return changes;
	}

	public void setChanges(ArrayList<Change> changes) {
		this.changes = changes;
	}
}
