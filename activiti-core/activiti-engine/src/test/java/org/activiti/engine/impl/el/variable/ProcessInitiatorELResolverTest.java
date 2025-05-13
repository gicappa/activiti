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
package org.activiti.engine.impl.el.variable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.junit.Test;

public class ProcessInitiatorELResolverTest {

  private static final String INITIATOR = "initiator";

  private final ProcessInitiatorELResolver resolver = new ProcessInitiatorELResolver();

  @Test
  public void canResolve_should_returnTrueWhenItsExecutionEntityAndPropertyIsInitiator() {
    //when
    boolean canResolve = resolver.canResolve(INITIATOR, new ExecutionEntityImpl());
    //then
    assertThat(canResolve).isTrue();
  }

  @Test
  public void canResolve_should_returnFalseWhenItsExecutionEntityAndPropertyIsNotInitiator() {
    //when
    boolean canResolve = resolver.canResolve("anyOtherProperty", new ExecutionEntityImpl());
    //then
    assertThat(canResolve).isFalse();
  }

  @Test
  public void canResolve_should_returnFalseWhenItsNotExecutionEntityAndPropertyIsInitiator() {
    //when
    boolean canResolve = resolver.canResolve(INITIATOR, mock(VariableScope.class));
    //then
    assertThat(canResolve).isFalse();
  }

  @Test
  public void resolve_should_returnProcessInitiator() {
    //given
    var processInstance = mock(ExecutionEntity.class);
    given(processInstance.getStartUserId()).willReturn("peter");
    var variableScope = buildVariableScope(processInstance);

    //when
    var result = resolver.resolve(INITIATOR, variableScope);

    //then
    assertThat(result).isEqualTo("peter");
  }

  @Test
  public void resolve_should_returnNullWhenVariableScopeDontHaveProcessInstance() {
    //given
    var variableScope = buildVariableScope(null);

    //when
    var result = resolver.resolve(INITIATOR, variableScope);

    //then
    assertThat(result).isNull();
  }

  private ExecutionEntity buildVariableScope(ExecutionEntity processInstance) {
    var variableScope = mock(ExecutionEntity.class);
    given(variableScope.getProcessInstance()).willReturn(processInstance);
    return variableScope;
  }
}
