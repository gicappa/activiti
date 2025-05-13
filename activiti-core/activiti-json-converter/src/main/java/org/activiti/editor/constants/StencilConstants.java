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
package org.activiti.editor.constants;

/**

 */
public interface StencilConstants {

  // stencil items
  String STENCIL_EVENT_START_NONE = "StartNoneEvent";
  String STENCIL_EVENT_START_TIMER = "StartTimerEvent";
  String STENCIL_EVENT_START_MESSAGE = "StartMessageEvent";
  String STENCIL_EVENT_START_SIGNAL = "StartSignalEvent";
  String STENCIL_EVENT_START_ERROR = "StartErrorEvent";

  String STENCIL_EVENT_END_NONE = "EndNoneEvent";
  String STENCIL_EVENT_END_ERROR = "EndErrorEvent";
  String STENCIL_EVENT_END_CANCEL = "EndCancelEvent";
  String STENCIL_EVENT_END_TERMINATE = "EndTerminateEvent";

  String STENCIL_SUB_PROCESS = "SubProcess";
  String STENCIL_EVENT_SUB_PROCESS = "EventSubProcess";
  String STENCIL_CALL_ACTIVITY = "CallActivity";

  String STENCIL_POOL = "Pool";
  String STENCIL_LANE = "Lane";

  String STENCIL_TASK_BUSINESS_RULE = "BusinessRule";
  String STENCIL_TASK_MAIL = "MailTask";
  String STENCIL_TASK_MANUAL = "ManualTask";
  String STENCIL_TASK_RECEIVE = "ReceiveTask";
  String STENCIL_TASK_SCRIPT = "ScriptTask";
  String STENCIL_TASK_SEND = "SendTask";
  String STENCIL_TASK_SERVICE = "ServiceTask";
  String STENCIL_TASK_USER = "UserTask";
  String STENCIL_TASK_CAMEL = "CamelTask";
  String STENCIL_TASK_MULE = "MuleTask";
  String STENCIL_TASK_SHELL = "ShellTask";
  String STENCIL_TASK_DECISION = "DecisionTask";

  String STENCIL_GATEWAY_EXCLUSIVE = "ExclusiveGateway";
  String STENCIL_GATEWAY_PARALLEL = "ParallelGateway";
  String STENCIL_GATEWAY_INCLUSIVE = "InclusiveGateway";
  String STENCIL_GATEWAY_EVENT = "EventGateway";

  String STENCIL_EVENT_BOUNDARY_TIMER = "BoundaryTimerEvent";
  String STENCIL_EVENT_BOUNDARY_ERROR = "BoundaryErrorEvent";
  String STENCIL_EVENT_BOUNDARY_SIGNAL = "BoundarySignalEvent";
  String STENCIL_EVENT_BOUNDARY_MESSAGE = "BoundaryMessageEvent";
  String STENCIL_EVENT_BOUNDARY_CANCEL = "BoundaryCancelEvent";
  String STENCIL_EVENT_BOUNDARY_COMPENSATION = "BoundaryCompensationEvent";

  String STENCIL_EVENT_CATCH_SIGNAL = "CatchSignalEvent";
  String STENCIL_EVENT_CATCH_TIMER = "CatchTimerEvent";
  String STENCIL_EVENT_CATCH_MESSAGE = "CatchMessageEvent";

  String STENCIL_EVENT_THROW_SIGNAL = "ThrowSignalEvent";
  String STENCIL_EVENT_THROW_NONE = "ThrowNoneEvent";

  String STENCIL_SEQUENCE_FLOW = "SequenceFlow";
  String STENCIL_MESSAGE_FLOW = "MessageFlow";
  String STENCIL_ASSOCIATION = "Association";
  String STENCIL_DATA_ASSOCIATION = "DataAssociation";

  String STENCIL_TEXT_ANNOTATION = "TextAnnotation";
  String STENCIL_DATA_STORE = "DataStore";

  String PROPERTY_VALUE_YES = "Yes";
  String PROPERTY_VALUE_NO = "No";

  // stencil properties
  String PROPERTY_OVERRIDE_ID = "overrideid";
  String PROPERTY_NAME = "name";
  String PROPERTY_DOCUMENTATION = "documentation";

  String PROPERTY_PROCESS_ID = "process_id";
  String PROPERTY_PROCESS_VERSION = "process_version";
  String PROPERTY_PROCESS_AUTHOR = "process_author";
  String PROPERTY_PROCESS_NAMESPACE = "process_namespace";
  String PROPERTY_PROCESS_EXECUTABLE = "process_executable";

  String PROPERTY_TIMER_DURATON = "timerdurationdefinition";
  String PROPERTY_TIMER_DATE = "timerdatedefinition";
  String PROPERTY_TIMER_CYCLE = "timercycledefinition";
  String PROPERTY_TIMER_CYCLE_END_DATE = "timerenddatedefinition";

  String PROPERTY_MESSAGES = "messages";
  String PROPERTY_MESSAGE_ID = "message_id";
  String PROPERTY_MESSAGE_NAME = "message_name";
  String PROPERTY_MESSAGE_ITEM_REF = "message_item_ref";

  String PROPERTY_MESSAGEREF = "messageref";

  String PROPERTY_SIGNALREF = "signalref";

  String PROPERTY_ERRORREF = "errorref";

  String PROPERTY_CANCEL_ACTIVITY = "cancelactivity";

  String PROPERTY_NONE_STARTEVENT_INITIATOR = "initiator";

  String PROPERTY_ASYNCHRONOUS = "asynchronousdefinition";
  String PROPERTY_EXCLUSIVE = "exclusivedefinition";

  String PROPERTY_MULTIINSTANCE_TYPE = "multiinstance_type";
  String PROPERTY_MULTIINSTANCE_CARDINALITY = "multiinstance_cardinality";
  String PROPERTY_MULTIINSTANCE_COLLECTION = "multiinstance_collection";
  String PROPERTY_MULTIINSTANCE_VARIABLE = "multiinstance_variable";
  String PROPERTY_MULTIINSTANCE_CONDITION = "multiinstance_condition";

  String PROPERTY_TASK_LISTENERS = "tasklisteners";
  String PROPERTY_EXECUTION_LISTENERS = "executionlisteners";
  String PROPERTY_LISTENER_EVENT = "event";
  String PROPERTY_LISTENER_CLASS_NAME = "className";
  String PROPERTY_LISTENER_EXPRESSION = "expression";
  String PROPERTY_LISTENER_DELEGATE_EXPRESSION = "delegateExpression";
  String PROPERTY_LISTENER_FIELDS = "fields";

  String PROPERTY_EVENT_LISTENERS = "eventlisteners";
  String PROPERTY_EVENTLISTENER_VALUE = "eventListeners";
  String PROPERTY_EVENTLISTENER_EVENTS = "events";
  String PROPERTY_EVENTLISTENER_EVENT = "event";
  String PROPERTY_EVENTLISTENER_IMPLEMENTATION = "implementation";
  String PROPERTY_EVENTLISTENER_RETHROW_EVENT = "rethrowEvent";
  String PROPERTY_EVENTLISTENER_RETHROW_TYPE = "rethrowType";
  String PROPERTY_EVENTLISTENER_CLASS_NAME = "className";
  String PROPERTY_EVENTLISTENER_DELEGATE_EXPRESSION = "delegateExpression";
  String PROPERTY_EVENTLISTENER_ENTITY_TYPE = "entityType";
  String PROPERTY_EVENTLISTENER_ERROR_CODE = "errorcode";
  String PROPERTY_EVENTLISTENER_SIGNAL_NAME = "signalname";
  String PROPERTY_EVENTLISTENER_MESSAGE_NAME = "messagename";

  String PROPERTY_FIELD_NAME = "name";
  String PROPERTY_FIELD_STRING_VALUE = "stringValue";
  String PROPERTY_FIELD_EXPRESSION = "expression";
  String PROPERTY_FIELD_STRING = "string";

  String PROPERTY_FORMKEY = "formkeydefinition";

  String PROPERTY_USERTASK_ASSIGNMENT = "usertaskassignment";
  String PROPERTY_USERTASK_PRIORITY = "prioritydefinition";
  String PROPERTY_USERTASK_DUEDATE = "duedatedefinition";
  String PROPERTY_USERTASK_ASSIGNEE = "assignee";
  String PROPERTY_USERTASK_OWNER = "owner";
  String PROPERTY_USERTASK_CANDIDATE_USERS = "candidateUsers";
  String PROPERTY_USERTASK_CANDIDATE_GROUPS = "candidateGroups";
  String PROPERTY_USERTASK_CATEGORY = "categorydefinition";

  String PROPERTY_SERVICETASK_CLASS = "servicetaskclass";
  String PROPERTY_SERVICETASK_EXPRESSION = "servicetaskexpression";
  String PROPERTY_SERVICETASK_DELEGATE_EXPRESSION = "servicetaskdelegateexpression";
  String PROPERTY_SERVICETASK_RESULT_VARIABLE = "servicetaskresultvariable";
  String PROPERTY_SERVICETASK_FIELDS = "servicetaskfields";
  String PROPERTY_SERVICETASK_FIELD_NAME = "name";
  String PROPERTY_SERVICETASK_FIELD_STRING_VALUE = "stringValue";
  String PROPERTY_SERVICETASK_FIELD_STRING = "string";
  String PROPERTY_SERVICETASK_FIELD_EXPRESSION = "expression";

  String PROPERTY_FORM_PROPERTIES = "formproperties";
  String PROPERTY_FORM_ID = "id";
  String PROPERTY_FORM_NAME = "name";
  String PROPERTY_FORM_TYPE = "type";
  String PROPERTY_FORM_EXPRESSION = "expression";
  String PROPERTY_FORM_VARIABLE = "variable";
  String PROPERTY_FORM_DATE_PATTERN = "datePattern";
  String PROPERTY_FORM_REQUIRED = "required";
  String PROPERTY_FORM_READABLE = "readable";
  String PROPERTY_FORM_WRITABLE = "writable";
  String PROPERTY_FORM_ENUM_VALUES = "enumValues";
  String PROPERTY_FORM_ENUM_VALUES_NAME = "name";
  String PROPERTY_FORM_ENUM_VALUES_ID = "id";

  String PROPERTY_DATA_PROPERTIES = "dataproperties";
  String PROPERTY_DATA_ID = "dataproperty_id";
  String PROPERTY_DATA_NAME = "dataproperty_name";
  String PROPERTY_DATA_TYPE = "dataproperty_type";
  String PROPERTY_DATA_VALUE = "dataproperty_value";

  String PROPERTY_SCRIPT_FORMAT = "scriptformat";
  String PROPERTY_SCRIPT_TEXT = "scripttext";

  String PROPERTY_RULETASK_CLASS = "ruletask_class";
  String PROPERTY_RULETASK_VARIABLES_INPUT = "ruletask_variables_input";
  String PROPERTY_RULETASK_RESULT = "ruletask_result";
  String PROPERTY_RULETASK_RULES = "ruletask_rules";
  String PROPERTY_RULETASK_EXCLUDE = "ruletask_exclude";

  String PROPERTY_MAILTASK_TO = "mailtaskto";
  String PROPERTY_MAILTASK_FROM = "mailtaskfrom";
  String PROPERTY_MAILTASK_SUBJECT = "mailtasksubject";
  String PROPERTY_MAILTASK_CC = "mailtaskcc";
  String PROPERTY_MAILTASK_BCC = "mailtaskbcc";
  String PROPERTY_MAILTASK_TEXT = "mailtasktext";
  String PROPERTY_MAILTASK_HTML = "mailtaskhtml";
  String PROPERTY_MAILTASK_CHARSET = "mailtaskcharset";

  String PROPERTY_CALLACTIVITY_CALLEDELEMENT = "callactivitycalledelement";
  String PROPERTY_CALLACTIVITY_IN = "callactivityinparameters";
  String PROPERTY_CALLACTIVITY_OUT = "callactivityoutparameters";
  String PROPERTY_IOPARAMETER_SOURCE = "source";
  String PROPERTY_IOPARAMETER_SOURCE_EXPRESSION = "sourceExpression";
  String PROPERTY_IOPARAMETER_TARGET = "target";

  String PROPERTY_CAMELTASK_CAMELCONTEXT = "cameltaskcamelcontext";

  String PROPERTY_MULETASK_ENDPOINT_URL = "muletaskendpointurl";
  String PROPERTY_MULETASK_LANGUAGE = "muletasklanguage";
  String PROPERTY_MULETASK_PAYLOAD_EXPRESSION = "muletaskpayloadexpression";
  String PROPERTY_MULETASK_RESULT_VARIABLE = "muletaskresultvariable";

  String PROPERTY_SEQUENCEFLOW_DEFAULT = "defaultflow";
  String PROPERTY_SEQUENCEFLOW_CONDITION = "conditionsequenceflow";
  String PROPERTY_SEQUENCEFLOW_ORDER = "sequencefloworder";
  String PROPERTY_FORM_REFERENCE = "formreference";

  String PROPERTY_MESSAGE_DEFINITIONS = "messagedefinitions";
  String PROPERTY_MESSAGE_DEFINITION_ID = "id";
  String PROPERTY_MESSAGE_DEFINITION_NAME = "name";
  String PROPERTY_MESSAGE_DEFINITION_ITEM_REF = "message_item_ref";

  String PROPERTY_SIGNAL_DEFINITIONS = "signaldefinitions";
  String PROPERTY_SIGNAL_DEFINITION_ID = "id";
  String PROPERTY_SIGNAL_DEFINITION_NAME = "name";
  String PROPERTY_SIGNAL_DEFINITION_SCOPE = "scope";

  String PROPERTY_TERMINATE_ALL = "terminateall";
  String PROPERTY_TERMINATE_MULTI_INSTANCE = "terminateMultiInstance";

  String PROPERTY_DECISIONTABLE_REFERENCE = "decisiontaskdecisiontablereference";
  String PROPERTY_DECISIONTABLE_REFERENCE_ID = "decisiontablereferenceid";
  String PROPERTY_DECISIONTABLE_REFERENCE_NAME = "decisiontablereferencename";
  String PROPERTY_DECISIONTABLE_REFERENCE_KEY = "decisionTableReferenceKey";
}
