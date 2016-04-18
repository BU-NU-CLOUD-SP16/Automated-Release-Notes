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

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

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
