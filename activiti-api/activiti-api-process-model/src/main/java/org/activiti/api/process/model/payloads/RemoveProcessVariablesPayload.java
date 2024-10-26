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
package org.activiti.api.process.model.payloads;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.activiti.api.model.shared.Payload;

public class RemoveProcessVariablesPayload implements Payload {

    private String id;
    private String processInstanceId;
    private List<String> variableNames = new ArrayList<>();

    public RemoveProcessVariablesPayload() {
        this.id = UUID.randomUUID().toString();
    }

    public RemoveProcessVariablesPayload(String processInstanceId,
                                         List<String> variableNames) {
        this();
        this.processInstanceId = processInstanceId;
        this.variableNames = variableNames;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    public void setVariableNames(List<String> variableNames) {
        this.variableNames = variableNames;
    }
}
