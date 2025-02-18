/*
 * Copyright 2010-2020 Alfresco Software, Ltd.
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

package org.activiti.engine.impl.cmd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.Serializable;
import java.util.Optional;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionInfoCacheObject;
import org.activiti.engine.repository.ProcessDefinition;


/**

 */
public class GetProcessDefinitionInfoCmd implements Command<ObjectNode>, Serializable {

  private static final long serialVersionUID = 1L;

  protected String processDefinitionId;
  protected ProcessDefinition processDefinition;

  public GetProcessDefinitionInfoCmd(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
  }

  public GetProcessDefinitionInfoCmd(ProcessDefinition processDefinition) {
    this(processDefinition.getId());

    this.processDefinition = processDefinition;
  }

  public ObjectNode execute(CommandContext commandContext) {
    if (processDefinitionId == null) {
      throw new ActivitiIllegalArgumentException("process definition id is null");
    }

    DeploymentManager deploymentManager = commandContext.getProcessEngineConfiguration().getDeploymentManager();
    // make sure the process definition is in the cache
    var processDefinition = resolveProcessDefinition(deploymentManager);

    return executeInternal(deploymentManager,commandContext, processDefinition);
  }

    protected ProcessDefinition resolveProcessDefinition(DeploymentManager deploymentManager) {
      return  Optional.ofNullable(processDefinition).orElseGet(() ->  deploymentManager.findDeployedProcessDefinitionById(processDefinitionId));
    }

    protected ObjectNode executeInternal(DeploymentManager deploymentManager,CommandContext commandContext,ProcessDefinition processDefinition){
      ObjectNode resultNode = null;
      ProcessDefinitionInfoCacheObject definitionInfoCacheObject = deploymentManager.getProcessDefinitionInfoCache().get(processDefinitionId);
      if (definitionInfoCacheObject != null) {
          resultNode = definitionInfoCacheObject.getInfoNode();
      }
      return resultNode;
  }

}
