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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.activiti.bpmn.converter.SubprocessXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.junit.jupiter.api.Test;

public class SubProcessMultiDiagramConverterNoDITest extends AbstractConverterTest {

  @Override
  protected BpmnModel readXMLFile() throws Exception {
    var xmlStream = this.getClass().getClassLoader().getResourceAsStream(getResource());
    var xif = XMLInputFactory.newInstance();
    var in = new InputStreamReader(xmlStream, UTF_8);
    var xtr = xif.createXMLStreamReader(in);
    return new SubprocessXMLConverter().convertToBpmnModel(xtr);
  }

  @Override
  protected BpmnModel exportAndReadXMLFile(BpmnModel bpmnModel) throws Exception {
    byte[] xml = new SubprocessXMLConverter().convertToXML(bpmnModel);
    System.out.println("xml " + new String(xml, UTF_8));
    var xif = XMLInputFactory.newInstance();
    var in = new InputStreamReader(new ByteArrayInputStream(xml), UTF_8);
    var xtr = xif.createXMLStreamReader(in);
    return new SubprocessXMLConverter().convertToBpmnModel(xtr);
  }

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
    deployProcess(parsedModel);
  }

  protected String getResource() {
    return "subprocessmultidiagrammodel-noDI.bpmn";
  }

  private void validateModel(BpmnModel model) {
    var flowElement = model.getMainProcess().getFlowElement("start1");
    assertThat(flowElement).isNotNull();
    assertThat(flowElement).isInstanceOf(StartEvent.class);
    assertThat(flowElement.getId()).isEqualTo("start1");

    flowElement = model.getMainProcess().getFlowElement("userTask1");
    assertThat(flowElement).isNotNull();
    assertThat(flowElement).isInstanceOf(UserTask.class);
    assertThat(flowElement.getId()).isEqualTo("userTask1");
    var userTask = (UserTask) flowElement;
    assertThat(userTask.getCandidateUsers().size() == 1).isTrue();
    assertThat(userTask.getCandidateGroups().size() == 1).isTrue();

    flowElement = model.getMainProcess().getFlowElement("subprocess1");
    assertThat(flowElement).isNotNull();
    assertThat(flowElement).isInstanceOf(SubProcess.class);
    assertThat(flowElement.getId()).isEqualTo("subprocess1");
    var subProcess = (SubProcess) flowElement;
    assertThat(subProcess.getFlowElements().size() == 11).isTrue();
  }
}
