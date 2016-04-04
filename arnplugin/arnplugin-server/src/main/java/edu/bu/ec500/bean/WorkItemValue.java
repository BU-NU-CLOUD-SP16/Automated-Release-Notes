package edu.bu.ec500.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemValue {
	
	@JsonProperty("id") 
	private String id;
	
	@JsonProperty("rev") 
	private String rev;
	
	@JsonProperty("fields") 
	private WorkItemDetails fields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public WorkItemDetails getFields() {
		return fields;
	}

	public void setFields(WorkItemDetails fields) {
		this.fields = fields;
	}
	

}
