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
package org.activiti.bpmn.converter.export;

import javax.xml.stream.XMLStreamWriter;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Event;
import org.activiti.bpmn.model.Message;
import org.activiti.bpmn.model.MessageEventDefinition;
import org.activiti.bpmn.model.Signal;
import org.activiti.bpmn.model.SignalEventDefinition;
import org.apache.commons.lang3.StringUtils;

public class SignalAndMessageDefinitionExport implements BpmnXMLConstants {

  public static void writeSignalsAndMessages(BpmnModel model,
    XMLStreamWriter xtw) throws Exception {

    for (var process : model.getProcesses()) {
      for (var flowElement : process.findFlowElementsOfType(Event.class)) {
        var event = flowElement;
        if (!event.getEventDefinitions().isEmpty()) {
          var eventDefinition = event.getEventDefinitions().get(0);
          if (eventDefinition instanceof SignalEventDefinition signalEvent) {
            if (StringUtils.isNotEmpty(signalEvent.getSignalRef())) {
              if (!model.containsSignalId(signalEvent.getSignalRef())) {
                var signal = new Signal(signalEvent.getSignalRef(),
                  signalEvent.getSignalRef());
                model.addSignal(signal);
              }
            }
          } else if (eventDefinition instanceof MessageEventDefinition messageEvent) {
            if (StringUtils.isNotEmpty(messageEvent.getMessageRef())) {
              if (!model.containsMessageId(messageEvent.getMessageRef())) {
                var message = new Message(messageEvent.getMessageRef(),
                  messageEvent.getMessageRef(), null);
                model.addMessage(message);
              }
            }
          }
        }
      }
    }

    for (var signal : model.getSignals()) {
      xtw.writeStartElement(ELEMENT_SIGNAL);
      xtw.writeAttribute(ATTRIBUTE_ID,
        signal.getId());
      xtw.writeAttribute(ATTRIBUTE_NAME,
        signal.getName());
      if (signal.getScope() != null) {
        xtw.writeAttribute(ACTIVITI_EXTENSIONS_NAMESPACE,
          ATTRIBUTE_SCOPE,
          signal.getScope());
      }
      xtw.writeEndElement();
    }

    for (var message : model.getMessages()) {
      xtw.writeStartElement(BPMN2_PREFIX, ELEMENT_MESSAGE, BPMN2_NAMESPACE);
      var messageId = message.getId();
      // remove the namespace from the message id if set
      if (model.getTargetNamespace() != null && messageId.startsWith(model.getTargetNamespace())) {
        messageId = messageId.replace(model.getTargetNamespace(),
          "");
        messageId = messageId.replaceFirst(":",
          "");
      } else {
        for (var prefix : model.getNamespaces().keySet()) {
          var namespace = model.getNamespace(prefix);
          if (messageId.startsWith(namespace)) {
            messageId = messageId.replace(model.getTargetNamespace(),
              "");
            messageId = prefix + messageId;
          }
        }
      }
      xtw.writeAttribute(ATTRIBUTE_ID,
        messageId);
      if (StringUtils.isNotEmpty(message.getName())) {
        xtw.writeAttribute(ATTRIBUTE_NAME,
          message.getName());
      }
      if (StringUtils.isNotEmpty(message.getItemRef())) {
        // replace the namespace by the right prefix
        var itemRef = message.getItemRef();
        for (var prefix : model.getNamespaces().keySet()) {
          var namespace = model.getNamespace(prefix);
          if (itemRef.startsWith(namespace)) {
            if (prefix.isEmpty()) {
              itemRef = itemRef.replace(namespace + ":",
                "");
            } else {
              itemRef = itemRef.replace(namespace,
                prefix);
            }
            break;
          }
        }
        xtw.writeAttribute(ATTRIBUTE_ITEM_REF,
          itemRef);
      }
      xtw.writeEndElement();
    }
  }
}
