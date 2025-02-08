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
package org.activiti.core.el;

import static java.util.Collections.emptyMap;

import jakarta.el.ValueExpression;
import jakarta.el.VariableMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActivitiVariablesMapper extends VariableMapper {

  Map<String, ValueExpression> map = emptyMap();

  public ActivitiVariablesMapper() {
  }

  @Override
  public ValueExpression resolveVariable(String variable) {
    return map.get(variable);
  }

  @Override
  public ValueExpression setVariable(String variable, ValueExpression expression) {
    if (map.isEmpty()) {
      map = new HashMap<>();
    }
    return map.put(variable, expression);
  }
}
