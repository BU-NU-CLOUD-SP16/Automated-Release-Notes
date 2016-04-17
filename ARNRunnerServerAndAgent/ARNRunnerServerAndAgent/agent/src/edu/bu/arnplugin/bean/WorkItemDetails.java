/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.bu.arnplugin.bean;

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

  @JsonProperty("System.AssignedTo")
  private String assignedTo;

  @JsonProperty("Microsoft.VSTS.Scheduling.StoryPoints")
  private String storyPoints;

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

  public String getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo = assignedTo;
  }

  public String getStoryPoints() {
    return storyPoints;
  }

  public void setStoryPoints(String storyPoints) {
    this.storyPoints = storyPoints;
  }
}
