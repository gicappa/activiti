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

package org.activiti.engine.impl;


import static org.activiti.engine.impl.AbstractNativeQuery.ResultType.LIST;
import static org.activiti.engine.impl.AbstractNativeQuery.ResultType.COUNT;
import static org.activiti.engine.impl.AbstractNativeQuery.ResultType.LIST_PAGE;
import static org.activiti.engine.impl.AbstractNativeQuery.ResultType.SINGLE_RESULT;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.query.NativeQuery;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract superclass for all native query types.
 */
public abstract class AbstractNativeQuery<T extends NativeQuery<?, ?>, U> implements
  Command<Object>, NativeQuery<T, U>, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  protected enum ResultType {
    LIST, LIST_PAGE, SINGLE_RESULT, COUNT
  }

  protected transient CommandExecutor commandExecutor;
  protected transient CommandContext commandContext;

  protected int maxResults = Integer.MAX_VALUE;
  protected int firstResult;
  private ResultType resultType;

  private final Map<String, Object> parameters = new HashMap<>();
  private String sqlStatement;

  protected AbstractNativeQuery(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
  }

  public AbstractNativeQuery(CommandContext commandContext) {
    this.commandContext = commandContext;
  }

  public AbstractNativeQuery<T, U> setCommandExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
    return this;
  }

  @SuppressWarnings("unchecked")
  public T sql(String sqlStatement) {
    this.sqlStatement = sqlStatement;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public T parameter(String name, Object value) {
    parameters.put(name, value);
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public U singleResult() {
    this.resultType = SINGLE_RESULT;
    if (commandExecutor != null) {
      return (U) commandExecutor.execute(this);
    }
    return executeSingleResult(Context.getCommandContext());
  }

  @SuppressWarnings("unchecked")
  public List<U> list() {
    this.resultType = LIST;

    if (commandExecutor != null) {
      return (List<U>) commandExecutor.execute(this);
    }

    return executeList(Context.getCommandContext(), getParameterMap(), 0, Integer.MAX_VALUE);
  }

  @SuppressWarnings("unchecked")
  public List<U> listPage(int firstResult, int maxResults) {
    this.firstResult = firstResult;
    this.maxResults = maxResults;
    this.resultType = LIST_PAGE;

    if (commandExecutor != null) {
      return (List<U>) commandExecutor.execute(this);
    }

    return executeList(Context.getCommandContext(), getParameterMap(), firstResult, maxResults);
  }

  public long count() {
    this.resultType = COUNT;
    if (commandExecutor != null) {
      return (Long) commandExecutor.execute(this);
    }
    return executeCount(Context.getCommandContext(), getParameterMap());
  }

  public Object execute(CommandContext commandContext) {
    if (resultType == LIST) {
      return executeList(commandContext, getParameterMap(), 0, Integer.MAX_VALUE);
    }

    if (resultType == LIST_PAGE) {

      Map<String, Object> parameterMap = getParameterMap();
      parameterMap.put("resultType", "LIST_PAGE");
      parameterMap.put("firstResult", firstResult);
      parameterMap.put("maxResults", maxResults);
      parameterMap.put("orderByColumns", extracted(parameterMap));

      int firstRow = firstResult + 1;
      parameterMap.put("firstRow", firstRow);

      int lastRow;
      if (maxResults == Integer.MAX_VALUE) {
        lastRow = maxResults;
      } else {
        lastRow = firstResult + maxResults + 1;
      }

      parameterMap.put("lastRow", lastRow);
      return executeList(commandContext, parameterMap, firstResult, maxResults);
    }

    if (resultType == ResultType.SINGLE_RESULT) {
      return executeSingleResult(commandContext);
    }

    return executeCount(commandContext, getParameterMap());

  }

  private String extracted(Map<String, Object> parameterMap) {
    if (StringUtils.isBlank(Objects.toString(parameterMap.get("orderBy")))) {
      return "RES.ID_ asc";
    }

    return "RES." + parameterMap.get("orderBy");
  }

  public abstract long executeCount(CommandContext commandContext,
    Map<String, Object> parameterMap);

  /**
   * Executes the actual query to retrieve the list of results.
   *
   * @param commandContext the command context
   * @param parameterMap   the map of parameters
   * @param firstResult    the index of the first result
   * @param maxResults     the maximum number of results
   * @return the list of results
   */
  public abstract List<U> executeList(
    CommandContext commandContext,
    Map<String, Object> parameterMap,
    int firstResult,
    int maxResults);

  public U executeSingleResult(CommandContext commandContext) {
    List<U> results = executeList(commandContext, getParameterMap(), 0, Integer.MAX_VALUE);

    if (results.isEmpty()) {
      return null;
    }

    if (results.size() != 1) {
      throw new ActivitiException("Query return " + results.size() + " results instead of max 1");
    }

    return results.getFirst();
  }

  private Map<String, Object> getParameterMap() {
    var parameterMap = new HashMap<String, Object>();
    parameterMap.put("sql", sqlStatement);
    parameterMap.putAll(parameters);
    return parameterMap;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

}
