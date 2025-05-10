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
package org.activiti.spring.process;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.core.common.spring.security.config.ActivitiSpringSecurityAutoConfiguration;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.spring.process.model.ProcessExtensionModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(ActivitiSpringSecurityAutoConfiguration.class)
public class ProcessVariablesInitiatorIT {

  @Autowired
  private ProcessExtensionResourceReader reader;

  @Autowired
  private ProcessVariablesInitiator processVariablesInitiator;

  @MockitoBean
  private ProcessExtensionService processExtensionService;

  @MockitoBean
  private UserGroupManager userGroupManager;

  @MockitoBean
  private RepositoryService repositoryService;

  @MockitoBean
  private RuntimeService runtimeService;

  @MockitoBean
  private TaskService taskService;

  @MockitoBean
  private ManagementService managementService;

  @SpringBootApplication
  static class Application {

  }

  @Test
  public void calculateVariablesFromExtensionFileShouldReturnVariablesWithDefaultValues()
    throws Exception {
    //given
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
      .getResourceAsStream("processes/default-vars-extensions.json")) {
      ProcessExtensionModel extension = reader.read(inputStream);

      ProcessDefinition processDefinition = mock(ProcessDefinition.class);
      given(processExtensionService.getExtensionsFor(processDefinition)).willReturn(
        extension.getExtensions("Process_DefaultVarsProcess"));
      given(processExtensionService.hasExtensionsFor(processDefinition)).willReturn(true);
      given(processDefinition.getKey()).willReturn("Process_DefaultVarsProcess");

      //when
      Map<String, Object> variables = processVariablesInitiator.calculateVariablesFromExtensionFile(
        processDefinition,
        null);

      //then
      assertThat(variables)
        .containsEntry("name",
          "Nobody")
        .containsEntry("positionInTheQueue",
          10)
        .doesNotContainKeys("age"); // age has no default value, so it won't be created
    }
  }

  @Test
  public void calculateVariablesFromExtensionFileShouldGivePriorityToProvidedValuesOverDefaultValues()
    throws Exception {
    //given
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
      .getResourceAsStream("processes/default-vars-extensions.json")) {
      ProcessExtensionModel extension = reader.read(inputStream);

      ProcessDefinition processDefinition = mock(ProcessDefinition.class);
      given(processExtensionService.getExtensionsFor(processDefinition)).willReturn(
        extension.getExtensions("Process_DefaultVarsProcess"));
      given(processExtensionService.hasExtensionsFor(processDefinition)).willReturn(true);
      given(processDefinition.getKey()).willReturn("Process_DefaultVarsProcess");

      //when
      Map<String, Object> variables = processVariablesInitiator.calculateVariablesFromExtensionFile(
        processDefinition,
        singletonMap("name",
          "Peter"));

      //then
      assertThat(variables)
        .containsEntry("name", // value for variable "name" has been provided,
          "Peter") // so default value should be ignored.
        .containsEntry("positionInTheQueue",
          10);
    }
  }

  @Test
  public void calculateVariablesFromExtensionFileShouldThrowExceptionWhenMandatoryVariableIsMissing()
    throws Exception {
    //given
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
      .getResourceAsStream("processes/initial-vars-extensions.json")) {
      ProcessExtensionModel extension = reader.read(inputStream);

      ProcessDefinition processDefinition = mock(ProcessDefinition.class);
      given(processExtensionService.getExtensionsFor(processDefinition)).willReturn(
        extension.getExtensions("Process_initialVarsProcess"));
      given(processExtensionService.hasExtensionsFor(processDefinition)).willReturn(true);
      given(processDefinition.getKey()).willReturn("Process_initialVarsProcess");

      //when
      Throwable thrownException = catchThrowable(
        () -> processVariablesInitiator.calculateVariablesFromExtensionFile(processDefinition,
          emptyMap())
      );

      //then
      assertThat(thrownException)
        .isInstanceOf(ActivitiException.class)
        .hasMessageContaining("Can't start process")
        .hasMessageContaining("without required variables - age");
    }
  }

  @Test
  public void calculateVariablesFromExtensionFileShouldThrowExceptionWhenProvidedValueHasNotTheSameTypeAsInTheDefinition()
    throws Exception {
    //given
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
      .getResourceAsStream("processes/initial-vars-extensions.json")) {
      ProcessExtensionModel extension = reader.read(inputStream);

      ProcessDefinition processDefinition = mock(ProcessDefinition.class);
      given(processExtensionService.getExtensionsFor(processDefinition)).willReturn(
        extension.getExtensions("Process_initialVarsProcess"));
      given(processExtensionService.hasExtensionsFor(processDefinition)).willReturn(true);
      given(processDefinition.getKey()).willReturn("Process_initialVarsProcess");

      //when
      Throwable thrownException = catchThrowable(
        () -> processVariablesInitiator.calculateVariablesFromExtensionFile(processDefinition,
          singletonMap("age", "invalidNumber"))
      );

      //then
      assertThat(thrownException)
        .isInstanceOf(ActivitiException.class)
        .hasMessageContaining("Can't start process")
        .hasMessageContaining("as variables fail type validation - age");
    }
  }

  @Test
  public void calculateVariablesFromExtensionFileShouldReturnMapWhenVariableIsJson()
    throws Exception {
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
      .getResourceAsStream("processes/variable-mapping-all-extensions.json")) {

      ProcessExtensionModel extension = reader.read(inputStream);

      ProcessDefinition processDefinition = mock(ProcessDefinition.class);
      given(processExtensionService.getExtensionsFor(processDefinition)).willReturn(
        extension.getExtensions("taskVariableMappingAll"));
      given(processExtensionService.hasExtensionsFor(processDefinition)).willReturn(true);
      given(processDefinition.getKey()).willReturn("taskVariableMappingAll");
      LinkedHashMap<String, Object> mappedJson = new LinkedHashMap<>();
      mappedJson.put("field1", "myString");

      //when
      Map<String, Object> variables = processVariablesInitiator.calculateVariablesFromExtensionFile(
        processDefinition,
        null);

      //then
      assertThat(variables)
        .containsOnly(
          entry("process_variable_inputmap_1", "myString"),
          entry("process_variable_json_type_1", mappedJson),
          entry("process_variable_json_type_2", mappedJson)
        );
    }
  }
}
