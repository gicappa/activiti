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
package org.activiti.engine.impl.bpmn.listener;

import static org.activiti.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_CLASS;
import static org.activiti.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION;
import static org.activiti.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION;
import static org.activiti.bpmn.model.ImplementationType.IMPLEMENTATION_TYPE_INSTANCE;
import static org.activiti.engine.delegate.BaseTaskListener.EVENTNAME_ALL_EVENTS;
import static org.activiti.engine.delegate.TransactionDependentExecutionListener.ON_TRANSACTION_BEFORE_COMMIT;

import java.util.List;
import java.util.Map;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.HasExecutionListeners;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Task;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.BaseExecutionListener;
import org.activiti.engine.delegate.BaseTaskListener;
import org.activiti.engine.delegate.CustomPropertiesResolver;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.TransactionDependentExecutionListener;
import org.activiti.engine.delegate.TransactionDependentTaskListener;
import org.activiti.engine.impl.bpmn.parser.factory.ListenerFactory;
import org.activiti.engine.impl.cfg.TransactionContext;
import org.activiti.engine.impl.cfg.TransactionListener;
import org.activiti.engine.impl.cfg.TransactionState;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.delegate.invocation.TaskListenerInvocation;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

/**
 *
 */
public class ListenerNotificationHelper {

  public void executeExecutionListeners(HasExecutionListeners elementWithExecutionListeners,
    DelegateExecution execution, String eventType) {
    var listeners = elementWithExecutionListeners.getExecutionListeners();
    if (listeners != null && !listeners.isEmpty()) {
      var listenerFactory = Context.getProcessEngineConfiguration()
        .getListenerFactory();
      for (var activitiListener : listeners) {

        if (eventType.equals(activitiListener.getEvent())) {

          BaseExecutionListener executionListener = null;

          if (IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(
            activitiListener.getImplementationType())) {
            executionListener = listenerFactory.createClassDelegateExecutionListener(
              activitiListener);
          } else if (IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(
            activitiListener.getImplementationType())) {
            executionListener = listenerFactory.createExpressionExecutionListener(activitiListener);
          } else if (IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(
            activitiListener.getImplementationType())) {
            if (activitiListener.getOnTransaction() != null) {
              executionListener = listenerFactory.createTransactionDependentDelegateExpressionExecutionListener(
                activitiListener);
            } else {
              executionListener = listenerFactory.createDelegateExpressionExecutionListener(
                activitiListener);
            }
          } else if (IMPLEMENTATION_TYPE_INSTANCE.equalsIgnoreCase(
            activitiListener.getImplementationType())) {
            executionListener = (ExecutionListener) activitiListener.getInstance();
          }

          if (executionListener != null) {
            if (activitiListener.getOnTransaction() != null) {
              planTransactionDependentExecutionListener(listenerFactory, execution,
                (TransactionDependentExecutionListener) executionListener, activitiListener);
            } else {
              execution.setEventName(
                eventType); // eventName is used to differentiate the event when reusing an execution listener for various events
              execution.setCurrentActivitiListener(activitiListener);
              ((ExecutionListener) executionListener).notify(execution);
              execution.setEventName(null);
              execution.setCurrentActivitiListener(null);
            }
          }
        }
      }
    }
  }

  protected void planTransactionDependentExecutionListener(
    ListenerFactory listenerFactory,
    DelegateExecution execution,
    TransactionDependentExecutionListener executionListener,
    ActivitiListener activitiListener) {

    var executionVariablesToUse = execution.getVariables();
    var customPropertiesResolver = createCustomPropertiesResolver(
      activitiListener);
    var customPropertiesMapToUse = invokeCustomPropertiesResolver(execution,
      customPropertiesResolver);

    var scope = new TransactionDependentExecutionListenerExecutionScope(
      execution.getProcessInstanceId(), execution.getId(), execution.getCurrentFlowElement(),
      executionVariablesToUse, customPropertiesMapToUse);

    addTransactionListener(activitiListener,
      new ExecuteExecutionListenerTransactionListener(executionListener, scope));
  }

  public void executeTaskListeners(TaskEntity taskEntity, String eventType) {
    if (taskEntity.getProcessDefinitionId() != null) {
      var process = ProcessDefinitionUtil.getProcess(
        taskEntity.getProcessDefinitionId());
      var flowElement = process.getFlowElement(taskEntity.getTaskDefinitionKey(), true);

      if (flowElement instanceof UserTask userTask) {
        executeTaskListeners(userTask, taskEntity, eventType);
      }
    }
  }

  public void executeTaskListeners(UserTask userTask, TaskEntity taskEntity, String eventType) {
    for (var activitiListener : userTask.getTaskListeners()) {
      var event = activitiListener.getEvent();
      if (event.equals(eventType) || event.equals(EVENTNAME_ALL_EVENTS)) {
        var taskListener = createTaskListener(activitiListener);

        if (activitiListener.getOnTransaction() != null) {
          planTransactionDependentTaskListener(taskEntity.getExecution(),
            (TransactionDependentTaskListener) taskListener, activitiListener);
        } else {
          taskEntity.setEventName(eventType);
          taskEntity.setCurrentActivitiListener(activitiListener);
          try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor()
              .handleInvocation(
                new TaskListenerInvocation((TaskListener) taskListener, taskEntity));
          } catch (Exception e) {
            throw new ActivitiException("Exception while invoking TaskListener: " + e.getMessage(),
              e);
          } finally {
            taskEntity.setEventName(null);
            taskEntity.setCurrentActivitiListener(null);
          }
        }
      }
    }
  }

  protected BaseTaskListener createTaskListener(ActivitiListener activitiListener) {
    BaseTaskListener taskListener = null;

    var listenerFactory = Context.getProcessEngineConfiguration().getListenerFactory();
    if (IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(
      activitiListener.getImplementationType())) {
      taskListener = listenerFactory.createClassDelegateTaskListener(activitiListener);
    } else if (IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(
      activitiListener.getImplementationType())) {
      taskListener = listenerFactory.createExpressionTaskListener(activitiListener);
    } else if (IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(
      activitiListener.getImplementationType())) {
      if (activitiListener.getOnTransaction() != null) {
        taskListener = listenerFactory.createTransactionDependentDelegateExpressionTaskListener(
          activitiListener);
      } else {
        taskListener = listenerFactory.createDelegateExpressionTaskListener(activitiListener);
      }
    } else if (IMPLEMENTATION_TYPE_INSTANCE.equalsIgnoreCase(
      activitiListener.getImplementationType())) {
      taskListener = (TaskListener) activitiListener.getInstance();
    }
    return taskListener;
  }

  protected void planTransactionDependentTaskListener(DelegateExecution execution,
    TransactionDependentTaskListener taskListener, ActivitiListener activitiListener) {
    var executionVariablesToUse = execution.getVariables();
    var customPropertiesResolver = createCustomPropertiesResolver(
      activitiListener);
    var customPropertiesMapToUse = invokeCustomPropertiesResolver(execution,
      customPropertiesResolver);

    var scope = new TransactionDependentTaskListenerExecutionScope(
      execution.getProcessInstanceId(), execution.getId(), (Task) execution.getCurrentFlowElement(),
      executionVariablesToUse, customPropertiesMapToUse);
    addTransactionListener(activitiListener,
      new ExecuteTaskListenerTransactionListener(taskListener, scope));
  }

  protected CustomPropertiesResolver createCustomPropertiesResolver(
    ActivitiListener activitiListener) {
    CustomPropertiesResolver customPropertiesResolver = null;
    var listenerFactory = Context.getProcessEngineConfiguration().getListenerFactory();
    if (IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(
      activitiListener.getCustomPropertiesResolverImplementationType())) {
      customPropertiesResolver = listenerFactory.createClassDelegateCustomPropertiesResolver(
        activitiListener);
    } else if (IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(
      activitiListener.getCustomPropertiesResolverImplementationType())) {
      customPropertiesResolver = listenerFactory.createExpressionCustomPropertiesResolver(
        activitiListener);
    } else if (IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(
      activitiListener.getCustomPropertiesResolverImplementationType())) {
      customPropertiesResolver = listenerFactory.createDelegateExpressionCustomPropertiesResolver(
        activitiListener);
    }
    return customPropertiesResolver;
  }

  protected Map<String, Object> invokeCustomPropertiesResolver(DelegateExecution execution,
    CustomPropertiesResolver customPropertiesResolver) {
    Map<String, Object> customPropertiesMapToUse = null;
    if (customPropertiesResolver != null) {
      customPropertiesMapToUse = customPropertiesResolver.getCustomPropertiesMap(execution);
    }
    return customPropertiesMapToUse;
  }

  protected void addTransactionListener(ActivitiListener activitiListener,
    TransactionListener transactionListener) {
    var transactionContext = Context.getTransactionContext();
    if (ON_TRANSACTION_BEFORE_COMMIT.equals(
      activitiListener.getOnTransaction())) {
      transactionContext.addTransactionListener(TransactionState.COMMITTING, transactionListener);

    } else if (TransactionDependentExecutionListener.ON_TRANSACTION_COMMITTED.equals(
      activitiListener.getOnTransaction())) {
      transactionContext.addTransactionListener(TransactionState.COMMITTED, transactionListener);

    } else if (TransactionDependentExecutionListener.ON_TRANSACTION_ROLLED_BACK.equals(
      activitiListener.getOnTransaction())) {
      transactionContext.addTransactionListener(TransactionState.ROLLED_BACK, transactionListener);

    }
  }

}
