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
package org.activiti.runtime.api.impl;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.interceptor.DelegateInterceptor;

public class CompositeVariableExpressionEvaluator implements ExpressionEvaluator {

    private SimpleMapExpressionEvaluator simpleMapExpressionEvaluator;
    private VariableScopeExpressionEvaluator variableScopeExpressionEvaluator;

    public CompositeVariableExpressionEvaluator(SimpleMapExpressionEvaluator simpleMapExpressionEvaluator,
                                                VariableScopeExpressionEvaluator variableScopeExpressionEvaluator) {
        this.simpleMapExpressionEvaluator = simpleMapExpressionEvaluator;
        this.variableScopeExpressionEvaluator = variableScopeExpressionEvaluator;
    }

    @Override
    public Object evaluate(Expression expression,
        ExpressionManager expressionManager,
        DelegateInterceptor delegateInterceptor) {
        try {
            return simpleMapExpressionEvaluator.evaluate(expression, expressionManager, delegateInterceptor);
        } catch (ActivitiException activitiException) {
            return variableScopeExpressionEvaluator.evaluate(expression, expressionManager, delegateInterceptor);
        }
    }

}
