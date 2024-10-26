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
package org.activiti.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;

public class ApplicationService {

  private final ApplicationDiscovery applicationDiscovery;
  private final ApplicationReader applicationReader;

  public ApplicationService(ApplicationDiscovery applicationDiscovery,
    ApplicationReader applicationReader) {
    this.applicationDiscovery = applicationDiscovery;
    this.applicationReader = applicationReader;
  }

  public List<ApplicationContent> loadApplications() {
    try {

      var applications = new ArrayList<ApplicationContent>();
      var applicationResources = applicationDiscovery.discoverApplications();

      for (var applicationResource : applicationResources) {
        try (var inputStream = applicationResource.getInputStream()) {
          applications.add(applicationReader.read(inputStream));
        }
      }

      return applications;

    } catch (IOException e) {
      throw new ApplicationLoadException("Unable to load application resource", e);
    }
  }
}
