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
package org.activiti.bpmn.model.helper;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.IntermediateCatchEvent;
import org.activiti.bpmn.model.LinkEventDefinition;
import org.activiti.bpmn.model.ThrowEvent;

import java.util.Collection;

public class LinkThrowEventFlowNodeHelper {
    public static FlowNode findRelatedIntermediateCatchEventForLinkEvent(ThrowEvent throwEvent) {
        String linkEventTarget = ((LinkEventDefinition) throwEvent.getEventDefinitions().get(0)).getTarget();
        Collection<FlowElement> allFlowElements = throwEvent.getParentContainer().getFlowElements();
        for (FlowElement flowElement : allFlowElements) {
            if (flowElement instanceof IntermediateCatchEvent intermediateCatchEvent) {
                if (intermediateCatchEvent.isLinkCatchEvent()) {
                    LinkEventDefinition destinationEvent = (LinkEventDefinition) intermediateCatchEvent.getEventDefinitions().get(0);
                    if (destinationEvent.getId().equals(linkEventTarget)) {
                        return intermediateCatchEvent;
                    }
                }
            }
        }
        return null;
    }
}
