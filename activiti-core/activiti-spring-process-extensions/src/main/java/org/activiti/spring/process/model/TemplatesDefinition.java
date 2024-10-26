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
package org.activiti.spring.process.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TemplatesDefinition {

    private Map<String, TaskTemplateDefinition> tasks = new HashMap<>();

    @JsonProperty("default")
    private TaskTemplateDefinition defaultTemplate;

    public Map<String, TaskTemplateDefinition> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, TaskTemplateDefinition> tasks) {
        this.tasks = tasks;
    }

    public TaskTemplateDefinition getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(TaskTemplateDefinition defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public Optional<TemplateDefinition> findAssigneeTemplateForTask(String taskUUID) {
        TaskTemplateDefinition taskTemplateDefinition = tasks.get(taskUUID);
        if (taskTemplateDefinition == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(taskTemplateDefinition.getAssignee());
    }

    public Optional<TemplateDefinition> findCandidateTemplateForTask(String taskUUID) {
        TaskTemplateDefinition taskTemplateDefinition = tasks.get(taskUUID);
        if (taskTemplateDefinition == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(taskTemplateDefinition.getCandidate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TemplatesDefinition that = (TemplatesDefinition) o;
        return Objects.equals(tasks, that.tasks) && Objects
            .equals(defaultTemplate, that.defaultTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasks, defaultTemplate);
    }
}
