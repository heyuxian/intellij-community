/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package com.intellij.codeInsight;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author peter
 */
@State(
  name = "ProjectCodeInsightSettings",
  storages = {
    @Storage(file = StoragePathMacros.PROJECT_FILE),
    @Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/codeInsightSettings.xml", scheme = StorageScheme.DIRECTORY_BASED)
  }
)

public class ProjectCodeInsightSettings implements PersistentStateComponent<ProjectCodeInsightSettings> {
  public List<String> excludedNames = ContainerUtil.newArrayList();

  public static ProjectCodeInsightSettings getSettings(@NotNull Project project) {
    return ServiceManager.getService(project, ProjectCodeInsightSettings.class);
  }

  public boolean isExcluded(@NotNull String name) {
    for (String excluded : excludedNames) {
      if (nameMatches(name, excluded)) {
        return true;
      }
    }
    for (String excluded : CodeInsightSettings.getInstance().EXCLUDED_PACKAGES) {
      if (nameMatches(name, excluded)) {
        return true;
      }
    }

    return false;
  }

  private static boolean nameMatches(@NotNull String name, String excluded) {
    return name.equals(excluded) || name.startsWith(excluded + ".");
  }

  @Nullable
  @Override
  public ProjectCodeInsightSettings getState() {
    return this;
  }

  @Override
  public void loadState(ProjectCodeInsightSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
