package edu.bu.ec500.arn.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemDetails {
	
	@JsonProperty("System.TeamProject") 
	private String teamProject;
	
	@JsonProperty("System.WorkItemType") 
	private String type;
	
	@JsonProperty("System.State") 
	private String state;
	
	@JsonProperty("System.CreatedBy") 
	private String createdBy;
	
	@JsonProperty("System.Title") 
	private String title;
	
	@JsonProperty("System.Description") 
	private String description;

	public String getTeamProject() {
		return teamProject;
	}

	public void setTeamProject(String teamProject) {
		this.teamProject = teamProject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
