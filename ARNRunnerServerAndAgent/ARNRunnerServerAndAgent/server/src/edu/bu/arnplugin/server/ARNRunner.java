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

package edu.bu.arnplugin.server;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Karunesh on 4/7/2016.
 */
public class ARNRunner extends RunType{

  private final PluginDescriptor pluginDescriptor;

  public ARNRunner(final RunTypeRegistry registry, final PluginDescriptor pluginDescriptor){
    this.pluginDescriptor = pluginDescriptor;
    registry.registerRunType(this);
  }
  @NotNull
  public String getType() {
    return "ARNRunner";
  }

  @NotNull
  public String getDisplayName() {
    return "ARNRunner";
  }

  @NotNull
  public String getDescription() {
    return "Runner type for Automated Release Notes";
  }

  @Nullable
  public PropertiesProcessor getRunnerPropertiesProcessor() {
    return new PropertiesProcessor() {
      private void checkNotEmpty(@NotNull final Map<String, String> properties,
                                 @NotNull final String key,
                                 @NotNull final String message,
                                 @NotNull final Collection<InvalidProperty> res) {
        if (jetbrains.buildServer.util.StringUtil.isEmptyOrSpaces(properties.get(key))) {
          res.add(new InvalidProperty(key, message));
        }
      }

      public Collection<InvalidProperty> process(Map<String, String> properties) {
        final Collection<InvalidProperty> result = new ArrayList<InvalidProperty>();
        if (properties == null) return result;

        checkNotEmpty(properties, "file_path", "File Path must be specified", result);
        checkNotEmpty(properties, "vsts_url", "VSTS URL must be specified", result);
        checkNotEmpty(properties, "vsts_user_name", "VSTS User Name must be specified", result);
        checkNotEmpty(properties, "vsts_password", "VSTS Password must be specified", result);
        checkNotEmpty(properties, "tc_url", "Teamcity URL must be specified", result);
        checkNotEmpty(properties, "tc_user_name", "Teamcity User Name must be specified", result);
        checkNotEmpty(properties, "tc_password", "Teamcity Password must be specified", result);

        return result;
      }
    };
  }

  @Nullable
  public String getEditRunnerParamsJspFilePath() {
    return pluginDescriptor.getPluginResourcesPath("editRunnerRunParameters.jsp");
  }

  @Nullable
  public String getViewRunnerParamsJspFilePath() {
    return pluginDescriptor.getPluginResourcesPath("viewRunnerRunParameters.jsp");
  }

  @Nullable
  public Map<String, String> getDefaultRunnerProperties() {
    Map<String,String> defaults = new HashMap<String, String>();



    return defaults;
  }

  @Override
  public String describeParameters(@NotNull final Map<String, String> parameters){

    return "File Path: file_path";
  }
}
