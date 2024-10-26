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
package org.activiti.editor.language.xml;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.IOParameter;
import org.junit.jupiter.api.Test;

public class CallActivityConverterTest extends AbstractConverterTest {

  @Test
  public void convertXMLToModel() throws Exception {
    var bpmnModel = readXMLFile();
    validateModel(bpmnModel);
  }

  @Test
  public void convertModelToXML() throws Exception {
    var bpmnModel = readXMLFile();
    var parsedModel = exportAndReadXMLFile(bpmnModel);
    validateModel(parsedModel);
  }

  protected String getResource() {
    return "callactivity.bpmn";
  }

  private void validateModel(BpmnModel model) {
    var flowElement = model.getMainProcess().getFlowElement("callactivity");
    assertThat(flowElement).isNotNull();
    assertThat(flowElement).isInstanceOf(CallActivity.class);
    var callActivity = (CallActivity) flowElement;
    assertThat(callActivity.getId()).isEqualTo("callactivity");
    assertThat(callActivity.getName()).isEqualTo("Call activity");

    assertThat(callActivity.getCalledElement()).isEqualTo("processId");

    var parameters = callActivity.getInParameters();
    assertThat(parameters).hasSize(2);
    var parameter = parameters.getFirst();
    assertThat(parameter.getSource()).isEqualTo("test");
    assertThat(parameter.getTarget()).isEqualTo("test");
    parameter = parameters.get(1);
    assertThat(parameter.getSourceExpression()).isEqualTo("${test}");
    assertThat(parameter.getTarget()).isEqualTo("test");

    parameters = callActivity.getOutParameters();
    assertThat(parameters).hasSize(1);
    parameter = parameters.getFirst();
    assertThat(parameter.getSource()).isEqualTo("test");
    assertThat(parameter.getTarget()).isEqualTo("test");
  }
}
