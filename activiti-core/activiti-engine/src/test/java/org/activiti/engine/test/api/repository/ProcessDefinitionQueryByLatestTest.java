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
package org.activiti.engine.test.api.repository;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

public class ProcessDefinitionQueryByLatestTest extends PluggableActivitiTestCase {

	private static final String XML_FILE_PATH = "org/activiti/engine/test/repository/latest/";

	  @Override
	  protected void setUp() throws Exception {
	    super.setUp();
	  }

	  @Override
	  protected void tearDown() throws Exception {
	    super.tearDown();
	}

	protected List<String> deploy(List<String> xmlFileNameList) throws Exception {
		var deploymentIdList = new ArrayList<String>();
		for(var xmlFileName : xmlFileNameList){
		    var deploymentId = repositoryService
		  	      .createDeployment()
		  	      .name(XML_FILE_PATH + xmlFileName)
		  	      .addClasspathResource(XML_FILE_PATH + xmlFileName)
		  	      .deploy()
		  	      .getId();
		    deploymentIdList.add(deploymentId);
		}
		return deploymentIdList;
	}

	private void unDeploy(List<String> deploymentIdList) throws Exception {
		for(var deploymentId : deploymentIdList){
			repositoryService.deleteDeployment(deploymentId, true);
		}
	}

	public void testQueryByLatestAndId() throws Exception {
		// Deploy
    var xmlFileNameList = asList("name_testProcess1_one.bpmn20.xml",
				"name_testProcess1_two.bpmn20.xml", "name_testProcess2_one.bpmn20.xml");
    var deploymentIdList = deploy(xmlFileNameList);

    var processDefinitionIdList = new ArrayList<String>();
		for(var deploymentId : deploymentIdList){
      var processDefinitionId = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).list().get(0).getId();
			processDefinitionIdList.add(processDefinitionId);
		}

    var idQuery1 = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionIdList.get(0)).latestVersion();
    var  processDefinitions = idQuery1.list();
		assertThat(processDefinitions).hasSize(0);

    var idQuery2 = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionIdList.get(1)).latestVersion();
		processDefinitions = idQuery2.list();
		assertThat(processDefinitions).hasSize(1);
		assertThat(processDefinitions.get(0).getKey()).isEqualTo("testProcess1");

    var idQuery3 = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionIdList.get(2)).latestVersion();
		processDefinitions = idQuery3.list();
		assertThat(processDefinitions).hasSize(1);
		assertThat(processDefinitions.get(0).getKey()).isEqualTo("testProcess2");

		// Undeploy
		unDeploy(deploymentIdList);
	}

	public void testQueryByLatestAndName() throws Exception {
		// Deploy
    var xmlFileNameList = asList("name_testProcess1_one.bpmn20.xml",
				"name_testProcess1_two.bpmn20.xml", "name_testProcess2_one.bpmn20.xml");
    var deploymentIdList = deploy(xmlFileNameList);

		// name
    var nameQuery = repositoryService.createProcessDefinitionQuery().processDefinitionName("one").latestVersion();
    var processDefinitions = nameQuery.list();
		assertThat(processDefinitions).hasSize(1);
		assertThat(processDefinitions.get(0).getVersion()).isEqualTo(1);
		assertThat(processDefinitions.get(0).getKey()).isEqualTo("testProcess2");

		// nameLike
    var nameLikeQuery = repositoryService.createProcessDefinitionQuery().processDefinitionName("one").latestVersion();
		processDefinitions = nameLikeQuery.list();
		assertThat(processDefinitions).hasSize(1);
		assertThat(processDefinitions.get(0).getVersion()).isEqualTo(1);
		assertThat(processDefinitions.get(0).getKey()).isEqualTo("testProcess2");

		// Undeploy
		unDeploy(deploymentIdList);
	}

	public void testQueryByLatestAndVersion() throws Exception {
		// Deploy
    var xmlFileNameList = asList("version_testProcess1_one.bpmn20.xml",
				"version_testProcess1_two.bpmn20.xml", "version_testProcess2_one.bpmn20.xml");
    var deploymentIdList = deploy(xmlFileNameList);

		// version
    var nameQuery = repositoryService.createProcessDefinitionQuery().processDefinitionVersion(1).latestVersion();
    var processDefinitions = nameQuery.list();
		assertThat(processDefinitions).hasSize(1);
		assertThat(processDefinitions.getFirst().getKey()).isEqualTo("testProcess2");

		// Undeploy
		unDeploy(deploymentIdList);
	}

	public void testQueryByLatestAndDeploymentId() throws Exception {
		// Deploy
    var xmlFileNameList = asList("name_testProcess1_one.bpmn20.xml",
				"name_testProcess1_two.bpmn20.xml", "name_testProcess2_one.bpmn20.xml");
    var deploymentIdList = deploy(xmlFileNameList);

		// deploymentId
    var deploymentQuery1 = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentIdList.get(0)).latestVersion();
    var processDefinitions = deploymentQuery1.list();
		assertThat(processDefinitions).hasSize(0);

		var deploymentQuery2 = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentIdList.get(1)).latestVersion();
		processDefinitions = deploymentQuery2.list();
		assertThat(processDefinitions).hasSize(1);
		assertThat(processDefinitions.get(0).getKey()).isEqualTo("testProcess1");

		// Undeploy
		unDeploy(deploymentIdList);
	}
}
