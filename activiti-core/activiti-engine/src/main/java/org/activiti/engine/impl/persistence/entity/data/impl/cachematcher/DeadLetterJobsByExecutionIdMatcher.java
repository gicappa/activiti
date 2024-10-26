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
package org.activiti.engine.impl.persistence.entity.data.impl.cachematcher;

import org.activiti.engine.impl.persistence.CachedEntityMatcherAdapter;
import org.activiti.engine.impl.persistence.entity.DeadLetterJobEntity;

/**

 */
public class DeadLetterJobsByExecutionIdMatcher extends CachedEntityMatcherAdapter<DeadLetterJobEntity> {

  @Override
  public boolean isRetained(DeadLetterJobEntity jobEntity, Object param) {
    return jobEntity.getExecutionId() != null && jobEntity.getExecutionId().equals(param); // param = executionId
  }


}
