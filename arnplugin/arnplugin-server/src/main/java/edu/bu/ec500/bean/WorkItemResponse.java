package edu.bu.ec500.bean;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemResponse {
	
	@JsonProperty("count") 
	private int count;
	
	@JsonProperty("value") 
	private ArrayList<WorkItemValue> values;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<WorkItemValue> getValues() {
		return values;
	}

	public void setValues(ArrayList<WorkItemValue> values) {
		this.values = values;
	}

}
