/*
 * Copyright ${project.inceptionYear}-2020 ${project.organization.name}.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.core.common.spring.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.activiti.core.common.project.model.ProjectManifest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ApplicationUpgradeContextService {

  private final String projectManifestFilePath;

  private final Integer enforcedAppVersion;

  private final boolean isRollbackDeployment;

  private final ObjectMapper objectMapper;

  private final ResourcePatternResolver resourceLoader;

  public ApplicationUpgradeContextService(String path,
    Integer enforcedAppVersion,
    Boolean isRollbackDeployment,
    ObjectMapper objectMapper,
    ResourcePatternResolver resourceLoader) {

    this.projectManifestFilePath = path;
    this.enforcedAppVersion = enforcedAppVersion;
    this.isRollbackDeployment = isRollbackDeployment;
    this.objectMapper = objectMapper;
    this.resourceLoader = resourceLoader;
  }

  private Optional<Resource> retrieveResource() {

    var resource = resourceLoader.getResource(projectManifestFilePath);

    if (resource.exists()) {
      return Optional.of(resource);
    } else {
      return Optional.empty();
    }
  }

  private ProjectManifest read(InputStream inputStream) throws IOException {
    return objectMapper.readValue(inputStream, ProjectManifest.class);
  }

  public boolean isRollbackDeployment() {
    return isRollbackDeployment;
  }

  public ProjectManifest loadProjectManifest() throws IOException {
    var resourceOptional = retrieveResource();

    return read(resourceOptional
      .orElseThrow(
        () -> new FileNotFoundException("'" + projectManifestFilePath + "' manifest not found."))
      .getInputStream());
  }

  public boolean hasProjectManifest() {
    return retrieveResource().isPresent();
  }

  public boolean hasEnforcedAppVersion() {
    return this.enforcedAppVersion > 0;
  }

  public Integer getEnforcedAppVersion() {
    return this.enforcedAppVersion;
  }
}
